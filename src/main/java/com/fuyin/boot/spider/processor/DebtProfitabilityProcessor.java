package com.fuyin.boot.spider.processor;

import com.fuyin.boot.enums.StockType;
import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtDiagnosisDO;
import com.fuyin.boot.mgb.entity.DebtProfitabilityDO;
import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.regex.Pattern;

/**
 * @ClassName: DebtProfitabilityProcessor
 * @Author: 何义祈安
 * @Description: 解析盈利能力诊断指标，获取数据
 * @Date: 2022/11/6 17:54
 * @Version: 1.0
 */
@Component
public class DebtProfitabilityProcessor implements PageProcessor {
    NumberUtils numberUtils = new NumberUtils();
    private Pattern pattern = Pattern.compile("[^0-9]");


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
     *@Description 通过动态下载的页面中解析数据
     *
     *@Param [page]
     **/
    private void praseABDetails(Page page) {
        Html html = page.getHtml();
        DebtProfitabilityDO debtProfitabilityDO = new DebtProfitabilityDO();
        //获取数据url
        debtProfitabilityDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期
        String da = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[1]/th[3]/span/text()").toString();
        System.out.println("信息日期："+da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtProfitabilityDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        String code = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[1]/a[@target='_blank']/text()").toString();
        System.out.println("股票代码："+code);
        debtProfitabilityDO.setCode(code);
        //获取股票名称
        Selectable name = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[@class='key']/b/a[1]/span/text()");
        System.out.println("股票名称："+name);
        debtProfitabilityDO.setName(name.toString());

        //获取主要指标表中的所有数据对象，tr
        List<Selectable> nodes = html.css("div#report_zyzb table.needScroll tbody tr").nodes();
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                switch (s){
                    case "毛利率(%)":{
                        //获取毛利率
                        Selectable grossProfitRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("毛利率的值："+grossProfitRatio);
                        debtProfitabilityDO.setGrossProfitRatio(decimalHandle(grossProfitRatio));
                        break;
                    } case "净利率(%)":{
                        //获取净利率
                        Selectable netInterestRate = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("净利率的值：："+netInterestRate);
                        debtProfitabilityDO.setNetInterestRate(decimalHandle(netInterestRate));
                        break;
                    } case "净利率(加权)(%)":{
                        //净资产收益率
                        Selectable roe = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("净资产收益率的值："+roe);
                        debtProfitabilityDO.setRoe(decimalHandle(roe));
                        break;
                    } case "总资产收益率(加权)(%)":{
                        //总资产收益率
                        Selectable returnOnTotalAssets = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("总资产收益率的值：："+returnOnTotalAssets);
                        debtProfitabilityDO.setReturnOnTotalAssets(decimalHandle(returnOnTotalAssets));
                        break;
                    } default: {
                        break;
                    }
                }
            }
        }
        List<Selectable> nodes_lrb = html.xpath("//div[@id='report_lrb']/table[@id='report_lrb_table']/tbody/tr").nodes();
        Double totalIncome = 0.00;
        for (Selectable node : nodes_lrb) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                if("营业总收入".equals(s)){
                    //获取销售额（营业总收入）totalIncome
                    totalIncome = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                    System.out.println("营业总收入的值：："+totalIncome);
                }
            }
        }
        List<Selectable> nodes_xjllb = html.xpath("//div[@id='report_xjllb']/table[@id='report_xjllb_table']/tbody/tr").nodes();
        Double texRevenue = 0.00;
        for (Selectable node : nodes_xjllb) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                if("支付的各项税费".equals(s)){
                    //获取支付的各项税费 tax revenue
                    texRevenue = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                    System.out.println("支付的各项税费的值：："+texRevenue);
                }
            }
        }
        List<Selectable> nodes_zcfzb = html.xpath("//div[@id=report_zcfzb]/table[@id=report_zcfzb_table]/tbody/tr").nodes();
        Double totalAssets = 0.00;
        for (Selectable node : nodes_zcfzb) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                if("资产总计".equals(s)){
                    //获取（资产总计）totalAssets
                    totalAssets = numberUtils.conversion(node.xpath("//tr/td[2]/span/text()").toString());
                    System.out.println("资产总计的值：："+totalAssets);
                }
            }
        }
        //如果都能爬取到，则放入实体对象
        if(totalIncome != 0 && texRevenue != 0 && totalAssets != 0){
            //获取资本回报率((净收入（营业总收入）-税收（支付的各项税费）/投资资本（资本总计）)
            debtProfitabilityDO.setReturnOnInvestedCapital(Double.valueOf(decimalFormat.format((totalIncome-texRevenue)/totalAssets)));
        }
        page.putField("debtProfitabilityDO",debtProfitabilityDO);
    }

    /**
     *@Description 解析港股详情页获取信息
     *@Param [page]
     *@return void
     **/
    private void praseHKDetails(Page page) {
        Html html = page.getHtml();
        DebtProfitabilityDO debtProfitabilityDO = new DebtProfitabilityDO();
        //获取数据url
        debtProfitabilityDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期
        String da = html.css("div.content_zyzb table.commonTable tbody tr:nth-child(1) th:nth-child(2)", "text").toString();
        System.out.println("信息日期："+da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtProfitabilityDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        Selectable codename = html.xpath("//div[@id='title']/div[@class='head_top']/h1");
        String code = pattern.matcher(codename.xpath("//h1/span[1]/text()").toString()).replaceAll("").trim();
        System.out.println("股票代码："+code);
        debtProfitabilityDO.setCode(code);
        //获取股票名称
        String name = codename.xpath("//h1/span[@class='stockName']/text()").toString();
        System.out.println("股票名称："+name);
        debtProfitabilityDO.setName(name);

        List<Selectable> nodes = html.css("div.content_zyzb table.commonTable tbody tr").nodes();
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/text()").toString();
            if(!Objects.isNull(s)){
                switch (s){
                    case "毛利率(%)":{
                        //获取毛利率
                        Selectable grossProfitRatio = node.xpath("//tr/td[2]/text()");
                        System.out.println("毛利率的值："+grossProfitRatio);
                        debtProfitabilityDO.setGrossProfitRatio(decimalHandle(grossProfitRatio));
                        break;
                    } case "净利率(%)":{
                        //获取净利率
                        Selectable netInterestRate = node.xpath("//tr/td[2]/text()");
                        System.out.println("净利率的值：："+netInterestRate);
                        debtProfitabilityDO.setNetInterestRate(decimalHandle(netInterestRate));
                        break;
                    } case "平均净资产收益率(%)":{
                        //净资产收益率
                        Selectable roe = node.xpath("//tr/td[2]/text()");
                        System.out.println("净资产收益率的值："+roe);
                        debtProfitabilityDO.setRoe(decimalHandle(roe));
                        break;
                    } case "总资产收益率(加权)(%)":{
                        //总资产收益率 利润表的净利润（净亏损以括号填列）/资产负债表的资产总计
                        Selectable returnOnTotalAssets = node.xpath("//tr/td[2]/text()");
                        System.out.println("总资产收益率的值：："+returnOnTotalAssets);
                        debtProfitabilityDO.setReturnOnTotalAssets(decimalHandle(returnOnTotalAssets));
                        break;
                    } default: {
                        break;
                    }
                }
            }
        }
        page.putField("debtProfitabilityDO",debtProfitabilityDO);

    }

    /**
     *@Description 解析美股详情页获取信息
     *@Param [page]
     *@return void
     **/
    private void praseUSDetails(Page page) {
        Html html = page.getHtml();
        //System.out.println(html);
        DebtProfitabilityDO debtProfitabilityDO = new DebtProfitabilityDO();
        //获取数据url
        debtProfitabilityDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期(已经加上span)
        String da = html.css("div#report_zyzb table tbody tr:nth-child(1) th:nth-child(2) span", "text").toString();
        System.out.println("信息日期："+da);
        try {
            //选择器出错
            Date date = simpleDateFormat.parse(da);
            debtProfitabilityDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        Selectable codename = html.xpath("//div[@class='head_top']/h1/span");
        String code = codename.xpath("//span/i/text()").toString();
        System.out.println("股票代码："+code);
        debtProfitabilityDO.setCode(code);
        //获取股票名称
        String name = codename.xpath("//span[@id='js_title']/text()").toString();
        System.out.println("股票名称："+name);
        debtProfitabilityDO.setName(name);

        List<Selectable> nodes = html.css("div#report_zyzb table tbody tr").nodes();
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/text()").toString();
            if(!Objects.isNull(s)){
                switch (s){
                    case "销售毛利率":{
                        //获取毛利率
                        Selectable grossProfitRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("毛利率的值："+grossProfitRatio);
                        debtProfitabilityDO.setGrossProfitRatio(decimalHandle(grossProfitRatio));
                        break;
                    } case "销售净利率":{
                        //获取净利率
                        Selectable netInterestRate = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("净利率的值：："+netInterestRate);
                        debtProfitabilityDO.setNetInterestRate(decimalHandle(netInterestRate));
                        break;
                    } case "净资产收益率":{
                        //净资产收益率 综合损益表的 净资产/资产负债表的 总资产
                        Selectable roe = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("净资产收益率的值："+roe);
                        debtProfitabilityDO.setRoe(decimalHandle(roe));
                        break;
                    } case "总资产收益率(加权)(%)":{
                        //总资产收益率 利润表的净利润（净亏损以括号填列）/资产负债表的资产总计
                        Selectable returnOnTotalAssets = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("总资产收益率的值：："+returnOnTotalAssets);
                        debtProfitabilityDO.setReturnOnTotalAssets(decimalHandle(returnOnTotalAssets));
                        break;
                    } default: {
                        break;
                    }
                }
            }
        }
        // (综合损益的营业收入-综合损益的所得税）/资产负债的总资产
        page.putField("debtProfitabilityDO",debtProfitabilityDO);

    }


    private Double decimalHandle(Selectable currentRatio) {
        String format = decimalFormat.format(Double.valueOf(currentRatio.toString().replaceAll("%", "")));
        return Double.valueOf(format);
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
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));

//        DebtPipeline debtPipeline = new DebtPipeline();
        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/Index?type=web&code=SZ003023#bfbbb-0");

        Spider.create(new DebtProfitabilityProcessor())
                //TODO 自定义保存还没写
//                .addPipeline(debtPipeline)
                .setDownloader(new DebtDetailsDownloader())
                .setScheduler(scheduler)
                .addRequest(request)
                .thread(1)
                .run();
    }

}
