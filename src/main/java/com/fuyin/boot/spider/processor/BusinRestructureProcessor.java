package com.fuyin.boot.spider.processor;

import com.fuyin.boot.enums.StockType;
import com.fuyin.boot.mgb.entity.BusinessDupontDO;
import com.fuyin.boot.mgb.entity.BusinessGrowthDO;
import com.fuyin.boot.mgb.entity.BusinessValuationDO;
import com.fuyin.boot.spider.Downloader.BusRestructureDownloader;
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
import java.util.List;
import java.util.regex.Pattern;

/**
 * @ClassName: BusGrowComProcessor
 * @Author: 何义祈安
 * @Description: 成长性比较表解析
 * @Date: 2022/11/14 21:27
 * @Version: 1.0
 */
@Component
public class BusinRestructureProcessor implements PageProcessor {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final Pattern p = Pattern.compile(".*\\d+.*");
    private Pattern pattern = Pattern.compile("[^0-9]");

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
    *@Description 创建三表对象及三表行业平均对象
    **/
    BusinessGrowthDO growComparison = new BusinessGrowthDO();
    BusinessGrowthDO growthAverage = new BusinessGrowthDO();
    BusinessValuationDO valuaComparison = new BusinessValuationDO();
    BusinessValuationDO valuationAverage = new BusinessValuationDO();
    BusinessDupontDO dupont = new BusinessDupontDO();
    BusinessDupontDO dupontAverage = new BusinessDupontDO();

    /**
     *@Description 解析AB股票
     **/
    private void praseABDetails(Page page) {
        Html html = page.getHtml();
        //*[@id="hq_1"]
        String enterpiceName = html.css("span#hq_1", "text").toString();
        //获取url
        growComparison.setDetailUrl(page.getUrl().toString());
        growthAverage.setDetailUrl(page.getUrl().toString());
        List<Selectable> divs = html.xpath("//div[@id='templateDiv']/div").nodes();
        for (Selectable div : divs) {
            String idValue = div.css("div div.name", "id").toString();
            switch (idValue){
                //成长性指标表
                case "czxbj":{
                    if(enterpiceName.equals(div.xpath("//tbody/tr[3]/td[3]/text()").toString())) {
                        growComparison.setCode(div.xpath("//tbody/tr[3]/td[2]/text()").toString());
                        growComparison.setName(div.xpath("//tbody/tr[3]/td[3]/text()").toString());
                        growComparison.setBasicEpsYear3(decimalHandle(div.xpath("//tbody/tr[3]/td[4]/text()")));
                        growComparison.setBasicEpsA21(decimalHandle(div.xpath("//tbody/tr[3]/td[5]/text()")));
                        growComparison.setBasicEpsTtm(decimalHandle(div.xpath("//tbody/tr[3]/td[6]/text()")));
                        growComparison.setBasicEpsE22(decimalHandle(div.xpath("//tbody/tr[3]/td[7]/text()")));
                        growComparison.setBasicEpsE23(decimalHandle(div.xpath("//tbody/tr[3]/td[8]/text()")));
                        growComparison.setBasicEpsE24(decimalHandle(div.xpath("//tbody/tr[3]/td[9]/text()")));
                        growComparison.setOperatingIncomeYear3(decimalHandle(div.xpath("//tbody/tr[3]/td[10]/text()")));
                        growComparison.setOperatingIncomeA21(decimalHandle(div.xpath("//tbody/tr[3]/td[11]/text()")));
                        growComparison.setOperatingIncomeTtm(decimalHandle(div.xpath("//tbody/tr[3]/td[12]/text()")));
                        growComparison.setOperatingIncomeE22(decimalHandle(div.xpath("//tbody/tr[3]/td[13]/text()")));
                        growComparison.setOperatingIncomeE23(decimalHandle(div.xpath("//tbody/tr[3]/td[14]/text()")));
                        growComparison.setOperatingIncomeE24(decimalHandle(div.xpath("//tbody/tr[3]/td[15]/text()")));
                        page.putField("growComparison",growComparison);

                        growthAverage.setCode(div.xpath("//tbody/tr[3]/td[2]/text()").toString());
                        growthAverage.setName(div.xpath("//tbody/tr[3]/td[3]/text()").toString()+"行业平均");
                        growthAverage.setBasicEpsYear3(decimalHandle(div.xpath("//tbody/tr[4]/td[2]/text()")));
                        growthAverage.setBasicEpsA21(decimalHandle(div.xpath("//tbody/tr[4]/td[3]/text()")));
                        growthAverage.setBasicEpsTtm(decimalHandle(div.xpath("//tbody/tr[4]/td[4]/text()")));
                        growthAverage.setBasicEpsE22(decimalHandle(div.xpath("//tbody/tr[4]/td[5]/text()")));
                        growthAverage.setBasicEpsE23(decimalHandle(div.xpath("//tbody/tr[4]/td[6]/text()")));
                        growthAverage.setBasicEpsE24(decimalHandle(div.xpath("//tbody/tr[4]/td[7]/text()")));
                        growthAverage.setOperatingIncomeYear3(decimalHandle(div.xpath("//tbody/tr[4]/td[8]/text()")));
                        growthAverage.setOperatingIncomeA21(decimalHandle(div.xpath("//tbody/tr[4]/td[9]/text()")));
                        growthAverage.setOperatingIncomeTtm(decimalHandle(div.xpath("//tbody/tr[4]/td[10]/text()")));
                        growthAverage.setOperatingIncomeE22(decimalHandle(div.xpath("//tbody/tr[4]/td[11]/text()")));
                        growthAverage.setOperatingIncomeE23(decimalHandle(div.xpath("//tbody/tr[4]/td[12]/text()")));
                        growthAverage.setOperatingIncomeE24(decimalHandle(div.xpath("//tbody/tr[4]/td[13]/text()")));
                        page.putField("growthAverage",growthAverage);
                    }
                    break;
                }
                //估值比较
                case "gzsp":{
                    if(enterpiceName.equals(div.xpath("//tbody/tr[3]/td[3]/text()").toString())){
                        valuaComparison.setCode(div.xpath("//tbody/tr[3]/td[2]/text()").toString());
                        valuaComparison.setName(div.xpath("//tbody/tr[3]/td[3]/text()").toString());
                        valuaComparison.setPeg(decimalHandle(div.xpath("//tbody/tr[3]/td[4]/text()")));
                        valuaComparison.setPriceEarningsA21(decimalHandle(div.xpath("//tbody/tr[3]/td[5]/text()")));
                        valuaComparison.setPriceEarningsTtm(decimalHandle(div.xpath("//tbody/tr[3]/td[6]/text()")));
                        valuaComparison.setPriceEarningsE22(decimalHandle(div.xpath("//tbody/tr[3]/td[7]/text()")));
                        valuaComparison.setPriceEarningsE23(decimalHandle(div.xpath("//tbody/tr[3]/td[8]/text()")));
                        valuaComparison.setPriceEarningsE24(decimalHandle(div.xpath("//tbody/tr[3]/td[9]/text()")));
                        valuaComparison.setMarketSalesA21(decimalHandle(div.xpath("//tbody/tr[3]/td[10]/text()")));
                        valuaComparison.setMarketSalesTtm(decimalHandle(div.xpath("//tbody/tr[3]/td[11]/text()")));
                        valuaComparison.setMarketSalesE22(decimalHandle(div.xpath("//tbody/tr[3]/td[12]/text()")));
                        valuaComparison.setMarketSalesE23(decimalHandle(div.xpath("//tbody/tr[3]/td[13]/text()")));
                        valuaComparison.setMarketSalesE24(decimalHandle(div.xpath("//tbody/tr[3]/td[14]/text()")));
                        page.putField("valuaComparison",valuaComparison);

                        valuationAverage.setCode(div.xpath("//tbody/tr[3]/td[2]/text()").toString());
                        valuationAverage.setName(div.xpath("//tbody/tr[3]/td[3]/text()").toString()+"行业平均");
                        valuationAverage.setPeg(decimalHandle(div.xpath("//tbody/tr[4]/td[2]/text()")));
                        valuationAverage.setPriceEarningsA21(decimalHandle(div.xpath("//tbody/tr[4]/td[3]/text()")));
                        valuationAverage.setPriceEarningsTtm(decimalHandle(div.xpath("//tbody/tr[4]/td[4]/text()")));
                        valuationAverage.setPriceEarningsE22(decimalHandle(div.xpath("//tbody/tr[4]/td[5]/text()")));
                        valuationAverage.setPriceEarningsE23(decimalHandle(div.xpath("//tbody/tr[4]/td[6]/text()")));
                        valuationAverage.setPriceEarningsE24(decimalHandle(div.xpath("//tbody/tr[4]/td[7]/text()")));
                        valuationAverage.setMarketSalesA21(decimalHandle(div.xpath("//tbody/tr[4]/td[8]/text()")));
                        valuationAverage.setMarketSalesTtm(decimalHandle(div.xpath("//tbody/tr[4]/td[9]/text()")));
                        valuationAverage.setMarketSalesE22(decimalHandle(div.xpath("//tbody/tr[4]/td[10]/text()")));
                        valuationAverage.setMarketSalesE23(decimalHandle(div.xpath("//tbody/tr[4]/td[11]/text()")));
                        valuationAverage.setMarketSalesE24(decimalHandle(div.xpath("//tbody/tr[4]/td[12]/text()")));
                        page.putField("valuationAverage",valuationAverage);
                    }
                    break;
                }
                //杜邦分析比较
                case "dbfxbj":{
                    if(enterpiceName.equals(div.xpath("//tbody/tr[3]/td[3]/text()").toString())){
                        dupont.setCode(div.xpath("//tbody/tr[3]/td[2]/text()").toString());
                        dupont.setName(div.xpath("//tbody/tr[3]/td[3]/text()").toString());
                        dupont.setRoeYear3(decimalHandle(div.xpath("//tbody/tr[3]/td[4]/text()")));
                        dupont.setRoeA19(decimalHandle(div.xpath("//tbody/tr[3]/td[5]/text()")));
                        dupont.setRoeE20(decimalHandle(div.xpath("//tbody/tr[3]/td[6]/text()")));
                        dupont.setRoeE21(decimalHandle(div.xpath("//tbody/tr[3]/td[7]/text()")));
                        dupont.setNetInterestRateYear3(decimalHandle(div.xpath("//tbody/tr[3]/td[8]/text()")));
                        dupont.setNetInterestRateA19(decimalHandle(div.xpath("//tbody/tr[3]/td[9]/text()")));
                        dupont.setNetInterestRateE20(decimalHandle(div.xpath("//tbody/tr[3]/td[10]/text()")));
                        dupont.setNetInterestRateE21(decimalHandle(div.xpath("//tbody/tr[3]/td[11]/text()")));
                        page.putField("dupont",dupont);

                        dupontAverage.setCode(div.xpath("//tbody/tr[3]/td[2]/text()").toString());
                        dupontAverage.setName(div.xpath("//tbody/tr[3]/td[3]/text()").toString()+"行业平均");
                        dupontAverage.setRoeYear3(decimalHandle(div.xpath("//tbody/tr[4]/td[2]/text()")));
                        dupontAverage.setRoeA19(decimalHandle(div.xpath("//tbody/tr[4]/td[3]/text()")));
                        dupontAverage.setRoeE20(decimalHandle(div.xpath("//tbody/tr[4]/td[4]/text()")));
                        dupontAverage.setRoeE21(decimalHandle(div.xpath("//tbody/tr[4]/td[5]/text()")));
                        dupontAverage.setNetInterestRateYear3(decimalHandle(div.xpath("//tbody/tr[4]/td[6]/text()")));
                        dupontAverage.setNetInterestRateA19(decimalHandle(div.xpath("//tbody/tr[4]/td[7]/text()")));
                        dupontAverage.setNetInterestRateE20(decimalHandle(div.xpath("//tbody/tr[4]/td[8]/text()")));
                        dupontAverage.setNetInterestRateE21(decimalHandle(div.xpath("//tbody/tr[4]/td[9]/text()")));
                        page.putField("dupontAverage",dupontAverage);
                    }
                    break;
                } default:{
                    break;
                }
            }
        }
        System.out.println(growComparison);
        System.out.println(valuaComparison);
        System.out.println(dupont);
        System.out.println(growthAverage);
        System.out.println(valuationAverage);
        System.out.println(dupontAverage);
    }

    /**
    *@Description 解析港股
    *@Param [page]
    *@return void
    **/
    private void praseHKDetails(Page page) {
        Html html = page.getHtml();
        //*[@id="hq_1"]
        String code = pattern.matcher(html.css("div.head_top h1 span.stockCode", "text").toString()).replaceAll("").trim();
        //获取url
        String enterpiceName = html.xpath("//div[@class='head_top']/h1/span[@class='stockName']/text()").toString();
        Selectable grow = html.css("div.content.content_czxdb table.commonTable.txtCenter tbody tr:nth-child(1)");
        if(grow.xpath("//tr/td[3]/text()").toString().equals(enterpiceName)){
            growComparison.setCode(code);
            growComparison.setDetailUrl(page.getUrl().toString());
            growComparison.setName(grow.xpath("//tr/td[3]/text()").toString());
            growComparison.setBasicEpsYear3(decimalHandle(grow.xpath("//tr/td[4]/text()")));
            growComparison.setOperatingIncomeYear3(decimalHandle(grow.xpath("//tr/td[5]/text()")));
            page.putField("growComparison",growComparison);

            Selectable average = html.css("div.content.content_czxdb table.commonTable.txtCenter tbody tr:nth-child(2)");
            growthAverage.setCode(code);
            growthAverage.setDetailUrl(page.getUrl().toString());
            growthAverage.setName(enterpiceName+"行业平均");
            growthAverage.setBasicEpsYear3(decimalHandle(average.xpath("//tr/td[2]/text()")));
            growthAverage.setOperatingIncomeYear3(decimalHandle(average.xpath("//tr/td[3]/text()")));
            page.putField("growthAverage",growthAverage);

        }
        //Selectable v = html.xpath("//div[contains(@class,'content_gzdb')]");

        Selectable valuaCom = html.css("div.content.content_gzdb table.commonTable.txtCenter tbody tr:nth-child(1)");
        if(valuaCom.xpath("//tr/td[3]/text()").toString().equals(enterpiceName)){
            valuaComparison.setCode(code);
            valuaComparison.setDetailUrl(page.getUrl().toString());
            valuaComparison.setName(valuaCom.xpath("//tr/td[3]/text()").toString());
            valuaComparison.setPriceEarningsTtm(decimalHandle(valuaCom.xpath("//tr/td[4]/text()")));
            valuaComparison.setMarketSalesTtm(decimalHandle(valuaCom.xpath("//tr/td[8]/text()")));
            page.putField("valuaComparison",valuaComparison);

            Selectable average = html.css("div.content.content_gzdb table.commonTable.txtCenter tbody tr:nth-child(1)");
            valuationAverage.setCode(code);
            valuationAverage.setDetailUrl(page.getUrl().toString());
            valuationAverage.setName(enterpiceName+"行业平均");
            valuationAverage.setPriceEarningsTtm(decimalHandle(average.xpath("//tr/td[2]/text()")));
            valuationAverage.setMarketSalesTtm(decimalHandle(average.xpath("//tr/td[6]/text()")));
            page.putField("valuationAverage",valuationAverage);

        }
        System.out.println(growComparison);
        System.out.println(valuaComparison);

    }

    /**
    *@Description 解析美股
    *@Param [page]
    *@return void
    **/
    private void praseUSDetails(Page page) {
    }

    /**
     *@Description 换成精确到后两位的Double对象类型
     **/
    private Double decimalHandle(Selectable currentRatio) {
        //判断取出的数据是否包含数字，有可能是--，--的话取反返回true
        if(!p.matcher(currentRatio.toString()).matches()){
            return 0.00;
        }
        String format = decimalFormat.format(Double.valueOf(currentRatio.toString().replaceAll("%", "")));
        return Double.valueOf(format);
    }

    @Override
    public Site getSite() {
        return Site.me()
                .setCharset("UTF-8")
                .setSleepTime(1)
                .setTimeOut(1000*10)
                .setRetrySleepTime(3000)
                .setRetryTimes(3);
    }

    public static void main(String[] args) {
        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HKF10/pages/home/index.html?code=01996&type=web&color=w#/IndustryComparison");
        request.putExtra("stockType","HKStock");
        Spider.create(new BusinRestructureProcessor())
                .setDownloader(new BusRestructureDownloader())
                .addRequest(request)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
                .thread(5)
                .run();
    }
}
