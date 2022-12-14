package com.fuyin.boot.spider.processor;

import com.fuyin.boot.enums.StockType;
import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtCashFlowDO;
import com.fuyin.boot.mgb.entity.DebtDiagnosisDO;
import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.utils.NumberUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: DebtCashFlowProcessor
 * @Author: 何义祈安
 * @Description: 解析现金诊断指标，获取数据
 * @Date: 2022/11/6 17:55
 * @Version: 1.0
 */
@Component
public class DebtCashFlowProcessor implements PageProcessor {
    NumberUtils numberUtils = new NumberUtils();

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void process(Page page) {
        String stockType = String.valueOf(page.getRequest().getExtra("stockType"));
        if(StockType.AB_STOCK.getCode().equals(stockType)){
            praseABDetails(page);
        }else if(StockType.HK_STOCK.getCode().equals(stockType)){
            praseHKDetails(page);
        }else if(StockType.US_STOCK.getCode().equals(stockType)){
            praseUSDetails(page);
        }
    }

    /**
     *@Description 解析AB股页面
     *
     *@Param [page]
     **/
    private void praseABDetails(Page page) {
        //TODO 考虑空url到股票名称要不要封装成一个单独的类
        Html html = page.getHtml();
        //System.out.println(html);
        DebtCashFlowDO debtCashFlowDO = new DebtCashFlowDO();
        //获取数据url
        debtCashFlowDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期
        String da = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[1]/th[3]/span/text()").toString();
        System.out.println("信息日期："+da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtCashFlowDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        String code = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[1]/a[@target='_blank']/text()").toString();
        System.out.println("股票代码："+code);
        debtCashFlowDO.setCode(code);
        //获取股票名称
        Selectable name = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[@class='key']/b/a[1]/span/text()");
        System.out.println("股票名称："+name);
        debtCashFlowDO.setName(name.toString());

        //从现金流量表获取数据
        List<Selectable> nodes_xjllb = html.xpath("//div[@id='report_xjllb']/table[@id='report_xjllb_table']/tbody/tr").nodes();
        Double cashflow = 0.00;
        Double texRevenue = 0.00;
        for (Selectable node : nodes_xjllb) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                switch(s){
                    case "经营活动产生的现金流量净额":{
                        //经营活动产生的现金流量净额cashflow
                        String noValue = "--";
                        if (!noValue.equals(node.xpath("//tr/td[2]/span/text()").toString())){
                            cashflow = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                            System.out.println("经营活动产生的现金流量净额："+cashflow);
                        }
                        break;
                    } case "支付的各项税费":{
                        //获取支付的各项税费 tax revenue
                        texRevenue = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("支付的各项税费："+ texRevenue);
                        break;
                    } default:{
                        break;
                    }
                }
            }
        }

        //从利润表获取数据
        List<Selectable> nodes_lrb = html.xpath("//div[@id='report_lrb']/table[@id='report_lrb_table']/tbody/tr").nodes();
        Double netProfit = 0.00;
        Double totalIncome = 0.00;
        for (Selectable node : nodes_lrb) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                switch(s){
                    case "净利润":{
                        //获取净利润netProfit
                        netProfit = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("净利润："+netProfit);
                        break;
                    } case "营业总收入":{
                        //获取销售额（营业总收入）totalIncome
                        totalIncome = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("营业总收入："+ totalIncome);
                        break;
                    } default:{
                        break;
                    }
                }
            }
        }

        //从资产负债表获取数据
        List<Selectable> nodes_zcfzb = html.xpath("//div[@id=report_zcfzb]/table[@id=report_zcfzb_table]/tbody/tr").nodes();
        Double totalCurrentLiab = 0.00;
        Double totalEquity = 0.00;
        Double totalAssets = 0.00;
        for (Selectable node : nodes_zcfzb) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                switch(s){
                    case "流动负债合计":{
                        //获取（流动负债合计）totalCurrentLiab
                        totalCurrentLiab = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("流动负债合计："+totalCurrentLiab);
                        break;
                    } case "股东权益合计":{
                        //获取（股东权益合计）totalEquity
                        totalEquity = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("股东权益合计："+ totalEquity);
                        break;
                    } case "资产总计":{
                        //获取（资产总计）totalAssets
                        totalAssets = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("资产总计："+totalAssets);
                        break;
                    } default:{
                        break;
                    }
                }
            }
        }
        if(cashflow!=0){
            if(totalIncome!=0) {
                //获取销售现金比率(经营活动现金净流量（有 -现金流量表）/销售额（有 营业总收入--利润表）)
                debtCashFlowDO.setCashSalesRatio(decimalHandle(cashflow/totalIncome));
            }if(totalCurrentLiab!=0 && totalEquity!=0) {
                //获取现金到期债务比（经营活动现金净流量（有-现金流量表）/本期到期的债务（有？流动负债合计-股东权益合计 ->资产负债表））
                debtCashFlowDO.setCashToMaturityRatio(decimalHandle(cashflow / (totalCurrentLiab - totalEquity)));
            }if(totalAssets!=0 && totalEquity!=0) {
                //获取现金流动负债比（经营活动现金净流量（有-现金流量表）/期末负债总额（有？资产总计-所有者权益合计 ->资产负债表））
                debtCashFlowDO.setCashFlowLiabilityRatio(decimalHandle(cashflow / (totalAssets - totalEquity)));
            }if(netProfit!=0) {
                //获取净利润现金流比率（净利润（有->利润表）÷经营活动现金流净额（有 经营活动产生的现金流量净额 ->现金流量表））
                debtCashFlowDO.setNetProfitCashFlowRatio(decimalHandle(netProfit / cashflow));
            }
        }
        //将获取到的指标放到page对象里
        page.putField("debtCashFlowDO",debtCashFlowDO);
    }

    /**
     *@Description 解析港股详情页获取信息（东方财富不做港股谢谢）
     *@Param [page]
     *@return void
     **/
    private void praseHKDetails(Page page) {

    }

    /**
     *@Description 解析美股详情页获取信息
     *@Param [page]
     *@return void
     **/
    private void praseUSDetails(Page page) {
        Html html = page.getHtml();
        //System.out.println(html);
        DebtCashFlowDO debtCashFlowDO = new DebtCashFlowDO();
        //获取数据url
        debtCashFlowDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期
        String da = html.css("div#report_zyzb table tbody tr:nth-child(1) th:nth-child(2) span", "text").toString();
        System.out.println("信息日期："+da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtCashFlowDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        Selectable codename = html.xpath("//div[@class='head_top']/h1/span");
        String code = codename.xpath("//span/i/text()").toString();
        System.out.println("股票代码："+code);
        debtCashFlowDO.setCode(code);
        //获取股票名称
        String name = codename.xpath("//span[@id='js_title']/text()").toString();
        System.out.println("股票名称："+name);
        debtCashFlowDO.setName(name);

        //从综合损益表获取数据
        List<Selectable> nodes_lrb = html.xpath("//div[@id='report_lrb']/table/tbody/tr").nodes();
        Double netProfit = 0.00;
        Double totalIncome = 0.00;
        Double texRevenue = 0.00;
        for (Selectable node : nodes_lrb) {
            String s = node.xpath("//tr/th[@class='tips-colname-Left']/span/text()").toString();
            if(!Objects.isNull(s)){
                switch(s){
                    case "净利润":{
                        //获取净利润netProfit 470.8亿
                        netProfit = numberUtils.conversion(node.css("tr th.tips-fieldname-Right span", "text").toString());
                        System.out.println("净利润："+netProfit);
                        break;
                    } case "营业收入":{
                        System.out.println(s+" ?????????");
                        System.out.println(node);
                        //获取销售额（营业总收入）totalIncome 8531亿
                        totalIncome = numberUtils.conversion(node.css("tr th.tips-fieldname-Right span", "text").toString());
                        System.out.println("营业总收入："+ totalIncome);
                        break;
                    } default:{
                        break;
                    }
                }
            }
        }
        for (Selectable node : nodes_lrb) {
            String s = node.xpath("//tr/td[1]/text()").toString();
            if(!Objects.isNull(s)){
                switch(s){
                    case "所得税":{
                        //获取支付的各项税费 tax revenue 268.1亿
                        texRevenue = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                        System.out.println("所得税："+ texRevenue);
                        break;
                    } default:{
                        break;
                    }
                }
            }
        }

        //从资产负债表获取数据
        List<Selectable> nodes_zcfzb = html.xpath("//div[@id=report_zcfzb]/table/tbody/tr").nodes();
        Double totalCurrentLiab = 0.00;
        Double totalEquity = 0.00;
        Double totalAssets = 0.00;
        for (Selectable node : nodes_zcfzb) {
            String s = node.xpath("//tr/th[@class='tips-colname-Left']/span/text()").toString();
            if(!Objects.isNull(s)){
                switch(s){
                    case "流动负债合计":{
                        //获取（流动负债合计）totalCurrentLiab 3838亿
                        totalCurrentLiab = numberUtils.conversion(node.css("tr th.tips-fieldname-Right span", "text").toString());
                        System.out.println("流动负债合计："+totalCurrentLiab);
                        break;
                    } case "股东权益合计":{
                        //获取（股东权益合计）totalEquity 1.082万亿
                        totalEquity = numberUtils.conversion(node.css("tr th.tips-fieldname-Right span", "text").toString());
                        System.out.println("股东权益合计："+ totalEquity);
                        break;
                    } case "总资产":{
                        //获取（资产总计）totalAssets
                        totalAssets = numberUtils.conversion(node.css("tr th.tips-fieldname-Right span", "text").toString());
                        System.out.println("资产总计："+totalAssets);
                        break;
                    } default:{
                        break;
                    }
                }
            }
        }
        //从现金流量表获取数据
        List<Selectable> nodes_xjllb = html.css("div#report_xjllb table tbody tr").nodes();
        Double cashflow = 0.00;
        for (Selectable node : nodes_xjllb) {
            String ss = node.xpath("//tr/th[@class='tips-colname-Left']/span/text()").toString();
            if(!Objects.isNull(ss) && "经营活动产生的现金流量净额".equals(ss)){
                //经营活动产生的现金流量净额cashflow 1428亿
                String s1 = node.css("tr th.tips-fieldname-Right span", "text").toString();
                cashflow = numberUtils.conversion(s1);
                System.out.println("经营活动产生的现金流量净额："+cashflow);
            }
        }
        if(cashflow!=0){
            if(totalIncome!=0) {
                //获取销售现金比率(经营活动现金净流量（有 -现金流量表）/销售额（有 营业总收入--利润表）)
                debtCashFlowDO.setCashSalesRatio(decimalHandle(cashflow/totalIncome));
            }if(totalCurrentLiab!=0 && totalEquity!=0) {
                //获取现金到期债务比（经营活动现金净流量（有-现金流量表）/本期到期的债务（有？流动负债合计-股东权益合计 ->资产负债表））
                debtCashFlowDO.setCashToMaturityRatio(decimalHandle(cashflow / (totalCurrentLiab - totalEquity)));
            }if(totalAssets!=0 && totalEquity!=0) {
                //获取现金流动负债比（经营活动现金净流量（有-现金流量表）/期末负债总额（有？资产总计-所有者权益合计 ->资产负债表））
                debtCashFlowDO.setCashFlowLiabilityRatio(decimalHandle(cashflow / (totalAssets - totalEquity)));
            }if(netProfit!=0) {
                //获取净利润现金流比率（净利润（有->利润表）÷经营活动现金流净额（有 经营活动产生的现金流量净额 ->现金流量表））
                debtCashFlowDO.setNetProfitCashFlowRatio(decimalHandle(netProfit / cashflow));
            }
        }
        //将获取到的指标放到page对象里
        System.out.println(debtCashFlowDO);
        page.putField("debtCashFlowDO",debtCashFlowDO);

    }

    /**
    *@Description 将数据转成后两位
    **/
    private Double decimalHandle(Double currentRatio) {
        if("--".equals(currentRatio.toString())) {
            return 0.00;
        }
        return Double.valueOf(decimalFormat.format(Double.valueOf(currentRatio.toString())));
    }

    private Site site = Site.me()
            //编码
            .setCharset("UTF-8")
            //抓取时间间隔
            .setSleepTime(1)
            //超时时间
            .setTimeOut(1000*10)
            //重试时间
            .setRetrySleepTime(3000)
            //重试次数
            .setRetryTimes(3);
    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

//        QueueScheduler scheduler = new QueueScheduler();
//        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
//
////        DebtPipeline debtPipeline = new DebtPipeline();
//        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/Index?type=web&code=SZ003023#bfbbb-0");
//
//        Spider.create(new DebtCashFlowProcessor())
//                //TODO 自定义保存还没写
////                .addPipeline(debtPipeline)
//                .setDownloader(new DebtDetailsDownloader())
//                .setScheduler(scheduler)
//                .addRequest(request)
//                .thread(1)
//                .run();
    }
}
