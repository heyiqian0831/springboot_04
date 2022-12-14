package com.fuyin.boot.spider.processor;

import com.fuyin.boot.mgb.entity.DebtDiagnosisDO;
import com.fuyin.boot.mgb.mapper.DebtDiagnosisMapper;
import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.spider.pipeline.DebtPipeline;
import com.fuyin.boot.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 何义祈安
 * @create: 2021/07/17 10:02
 * @description: 解析页面
 */
@Component
public class DebtSeleniumProcessor implements PageProcessor {

    @Autowired
    private DebtDiagnosisMapper debtDiagnosisMapper;

    NumberUtils numberUtils = new NumberUtils();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void process(Page page) {
//        String level = page.getRequest().getExtra("level").toString();
//                               switch (level){
//            case "list":
//                parseList(page);
//                break;
//            case "detail":
//                praseDetail(page);
//                break;
//        }
        //获取财务报表信息
        praseDebtDetails(page);

    }

    /**
    *@Description 通过动态下载后的页面中获取数据
    *@Date 21:09 2022/10/27
    **/
    private void praseDebtDetails(Page page) {
        Html html = page.getHtml();
//        System.out.println(html);
        //创建DebtDiagnosisDO数据实例
        DebtDiagnosisDO debtDiagnosisDO = new DebtDiagnosisDO();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Selectable tbody = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody");
        //获取数据url
        debtDiagnosisDO.setDetailUrl(page.getUrl().toString());
        //获取信息日期

        String da = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[1]/th[3]/span/text()").toString();
        System.out.println(da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtDiagnosisDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        String s1 = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[1]/a[@target='_blank']/text()").toString();
        System.out.println(s1);
        debtDiagnosisDO.setCode(html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[1]/a[@target='_blank']/text()").toString());
        //获取股票名称
        Selectable name = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[@class='key']/b/a[1]/span/text()");
        System.out.println(name);
        debtDiagnosisDO.setName(html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[@class='key']/b/a[1]/span/text()").toString());

        List<Selectable> nodes = html.css("div#F10MainTargetDiv div#report_zyzb table.needScroll tbody tr").nodes();
        System.out.println(nodes);
        System.out.println(nodes.get(1));
        System.out.println(nodes.get(3));
        System.out.println("for--------------------%%%%%%%%");
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            System.out.println(s);
            if(!Objects.isNull(s)){
                switch (s){
                    case "毛利率(%)":{
                        System.out.println(node.xpath("//tr/td[2]/span/text()").toString());
                        break;
                    }
                    default:
                    {
                        System.out.println("不是工作日");
                        break;
                    }
                }

            }
            System.out.println("------------"+s+" done ----------------");
        }


        //获取流动比率currentRatio
        Selectable currentRatio1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[26]");
        Selectable currentRatio2 = currentRatio1.xpath("//tr/td[2]/span/text()");
        System.out.println("cr2+ "+currentRatio2);
        debtDiagnosisDO.setCurrentRatio(Double.valueOf(decimalFormat.format(Double.valueOf(currentRatio2.toString()))));
        //获取速动比率
        Selectable quickRatio1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[27]");
        Selectable quickRatio2 = quickRatio1.xpath("//tr/td[2]/span/text()");
        System.out.println(quickRatio1.xpath("//tr/td[1]/span/text()")+" 速动比率");
        debtDiagnosisDO.setQuickRatio(Double.valueOf(decimalFormat.format(Double.valueOf(quickRatio2.toString()))));
        //获取资产负债率
        Selectable debtAssetRatio1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[29]");
        Selectable debtAssetRatio2 = debtAssetRatio1.xpath("//tr/td[2]/span/text()");
        debtDiagnosisDO.setDebtAssetRatio(Double.valueOf(decimalFormat.format(Double.valueOf(debtAssetRatio2.toString()))));

        //获取毛利率
        Selectable grossProfitRatio1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[23]");
        Selectable grossProfitRatio2 = grossProfitRatio1.xpath("//tr/td[2]/span/text()");
        debtDiagnosisDO.setGrossProfitRatio(Double.valueOf(decimalFormat.format(Double.valueOf(grossProfitRatio2.toString()))));
        //获取净利率
        Selectable netInterestRate1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[24]");
        Selectable netInterestRate2 = netInterestRate1.xpath("//tr/td[2]/span/text()");
        debtDiagnosisDO.setNetInterestRate(Double.valueOf(decimalFormat.format(Double.valueOf(netInterestRate2.toString()))));
        //获取净资产收益率
        Selectable roe1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[20]");
        Selectable roe2 = roe1.xpath("//tr/td[2]/span/text()");
        debtDiagnosisDO.setRoe(Double.valueOf(decimalFormat.format(Double.valueOf(roe2.toString()))));
        //获取总资产收益率
        Selectable returnOnTotalAssets1 = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[22]");
        Selectable returnOnTotalAssets2 = returnOnTotalAssets1.xpath("//tr/td[2]/span/text()");
        debtDiagnosisDO.setReturnOnTotalAssets(Double.valueOf(decimalFormat.format(Double.valueOf(returnOnTotalAssets2.toString()))));

        //------------------------
        //经营活动产生的现金流量净额cashflow
        Selectable cf = html.xpath("//div[@id='report_xjllb']/table[@id='report_xjllb_table']/tbody/tr[34]");
        System.out.println(cf);
        System.out.println(cf.xpath("//tr/td[2]/span/text()").toString());
        Double cashflow = numberUtils.conversion(cf.xpath("//tr/td[2]/span/text()").toString());
        System.out.println("cashflow ====="+cashflow);
        //获取支付的各项税费 tax revenue
        Selectable tr = html.xpath("//div[@id='report_xjllb']/table[@id='report_xjllb_table']/tbody/tr[27]");
        System.out.println(tr.xpath("//tr/td[1]/span/text()")+" 支付的各项税费");
        Double texRevenue = numberUtils.conversion(tr.xpath("//tr/td[2]/span/text()").toString());

        //获取净利润netProfit52
        Selectable np = html.xpath("//div[@id='report_lrb']/table[@id='report_lrb_table']/tbody/tr[52]");
        System.out.println(np.xpath("//tr/td[1]/span/text()")+" 净利润");
        Double netProfit = numberUtils.conversion(np.xpath("//tr/td[2]/span/text()").toString());
        //获取财务费用financialExpenses
        Selectable fe = html.xpath("//div[@id='report_lrb']/table[@id='report_lrb_table']/tbody/tr[23]");
        System.out.println(fe.xpath("//tr/td[1]/span/text()")+" 财务费用");
        Double financialExpenses = numberUtils.conversion(fe.xpath("//tr/td[2]/span/text()").toString());
        //获取利润总额totalProfit
        Selectable tp = html.xpath("//div[@id='report_lrb']/table[@id='report_lrb_table']/tbody/tr[48]");
        System.out.println(tp.xpath("//tr/td[1]/span/text()")+" 利润总额");
        Double totalProfit = numberUtils.conversion(tp.xpath("//tr/td[2]/span/text()").toString());
        //获取所得税费用incomeTax
        Selectable it = html.xpath("//div[@id='report_lrb']/table[@id='rreport_lrb_table']/tbody/tr[49]");
        System.out.println(it.xpath("//tr/td[1]/span/text()")+" 所得税费用");
        Double incomeTax = numberUtils.conversion(it.xpath("//tr/td[2]/span/text()").toString());
        //获取销售额（营业总收入）totalIncome
        Selectable ti = html.xpath("//div[@id='report_lrb']/table[@id='report_lrb_table']/tbody/tr[2]");
        System.out.println(ti.xpath("//tr/td[1]/span/text()")+" 营业总收入");
        Double totalIncome = numberUtils.conversion(ti.xpath("//tr/td[2]/span/text()").toString());


        //获取（流动负债合计）totalCurrentLiab
        Selectable tc = html.xpath("//div[@id=report_zcfzb]/table[@id=report_zcfzb_table]/tbody/tr[106]");
        System.out.println(tc.xpath("//tr/td[1]/span/text()")+" 流动负债合计");
        Double totalCurrentLiab = numberUtils.conversion(tc.xpath("//tr/td[2]/span/text()").toString());
        //获取（所有者/股东权益合计）totalEquity
        Selectable te = html.xpath("//div[@id=report_zcfzb]/table[@id=report_zcfzb_table]/tbody/tr[149]");
        System.out.println(te.xpath("//tr/td[1]/span/text()")+" 股东权益合计");
        Double totalEquity = numberUtils.conversion(te.xpath("//tr/td[2]/span/text()").toString());
        //获取（资产总计）totalAssets
        Selectable ta = html.xpath("//div[@id=report_zcfzb]/table[@id=report_zcfzb_table]/tbody/tr[69]");
        System.out.println(ta.xpath("//tr/td[1]/span/text()")+" 资产总计");
        Double totalAssets = numberUtils.conversion(ta.xpath("//tr/td[2]/span/text()").toString());

        //获取资本回报率((净收入（营业总收入）-税收（支付的各项税费）/投资资本（资本总计）)
        debtDiagnosisDO.setReturnOnInvestedCapital(Double.valueOf(decimalFormat.format((totalIncome-texRevenue)/totalAssets)));
        //获取销售现金比率(经营活动现金净流量（有 -现金流量表）/销售额（有 营业总收入--利润表）)
        debtDiagnosisDO.setCashSalesRatio(Double.valueOf(decimalFormat.format(cashflow/totalIncome)));
        //获取现金到期债务比（经营活动现金净流量（有-现金流量表）/本期到期的债务（有？流动负债合计-股东权益合计 ->资产负债表））
        debtDiagnosisDO.setCashToMaturityRatio(Double.valueOf(decimalFormat.format(cashflow/(totalCurrentLiab-totalEquity))));
        //获取现金流动负债比（经营活动现金净流量（有-现金流量表）/期末负债总额（有？资产总计-所有者权益合计 ->资产负债表））
        debtDiagnosisDO.setCashFlowLiabilityRatio(Double.valueOf(decimalFormat.format(cashflow/(totalAssets-totalEquity))));
        //获取净利润现金流比率（净利润（有->利润表）÷经营活动现金流净额（有 经营活动产生的现金流量净额 ->现金流量表））
        debtDiagnosisDO.setNetProfitCashFlowRatio(Double.valueOf(decimalFormat.format(netProfit/cashflow)));
        System.out.println(debtDiagnosisDO);
        debtDiagnosisDO.setId(3L);
//        debtDiagnosisMapper.insert(debtDiagnosisDO);
        page.putField("debtDiagnosisDO",debtDiagnosisDO);

    }

    /**
     * 解析详情页
     *
     * @param page
     */
    private void praseDetail(Page page) {
        Html html = page.getHtml();
        System.out.println(html);
        String title = html.$("div.master .p-name").xpath("///allText()").get();
        String priceStr = html.$("div.summary-price-wrap .p-price span.price").xpath("///allText()").get();
        String pic = "https:"+html.$("#spec-img").xpath("///@src").get();
        String url = "https:"+html.$("div.master .p-name a").xpath("///@href").get();
        String sku = html.$("a.notice.J-notify-sale").xpath("///@data-sku").get();

//        Item item = new Item();
//        item.setTitle(title);
//        item.setPic(pic);
//        item.setPrice(Float.valueOf(priceStr));
//        item.setUrl(url);
//        item.setUpdated(new Date());
//        item.setSku(StringUtils.isNotBlank(sku)?Long.valueOf(sku) : null);

//        System.out.println(item);
        // 单条数据塞入
//        page.putField("item", item);
    }

    /**
     * 解析列表页
     * @param page
     */
    private void parseList(Page page) {
        Html html = page.getHtml();
        // 这里拿到sku 和 spu 并交给pipeline
        List<Selectable> nodes = html.$("ul.gl-warp.clearfix > li").nodes();
//        List<Item> itemList = new ArrayList<>();
        for (Selectable node : nodes) {
            // 拿到sku和spu
            String sku = node.$("li").xpath("///@data-sku").get();
            String spu = node.$("li").xpath("///@data-spu").get();
            String href = "https:" + node.$("div.p-img a").xpath("///@href").get();

//            Item item = new Item();
//            item.setSku(Long.valueOf(sku));
//            item.setSpu(StringUtils.isNotBlank(spu) ? Long.valueOf(spu) : 0);
//            item.setCreated(new Date());
//            itemList.add(item);

            // 同时还需要把链接加到详情页 加到队列
            Request request = new Request(href);
            request.putExtra("level", "detail");
            request.putExtra("pageNum", page.getRequest().getExtra("pageNum"));
            request.putExtra("detailUrl", href);
            page.addTargetRequest(request);
        }

        // 以集合的方式存入
//        page.putField("itemList", itemList);

        // 同时还要去做分页
        String pageNum = page.getRequest().getExtra("pageNum").toString();
        if ("1".equals(pageNum)){
            Request request = new Request("https://nextpage.com");
            request.putExtra("level", "page"); // 标识去分页
            request.putExtra("pageNum", (Integer.valueOf(pageNum) + 1) + "");// 页码要+1 接下来要的是第二页
            // 添加到队列
            page.addTargetRequest(request);
        }
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

        DebtPipeline debtPipeline = new DebtPipeline();
        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HSF10/FinanceAnalysis/Index?type=web&code=SZ003023#bfbbb-0");

        Spider.create(new DebtSeleniumProcessor())
                //TODO 自定义保存还没写
                .addPipeline(debtPipeline)
                .setDownloader(new DebtDetailsDownloader())
                .setScheduler(scheduler)
                .addRequest(request)
                .thread(1)
                .run();
    }
}
