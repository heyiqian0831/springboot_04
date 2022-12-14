package com.fuyin.boot.spider.processor;

import com.fuyin.boot.enums.StockType;
import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtDiagnosisDO;
import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.spider.pipeline.DebtPipeline;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
 * @ClassName: DebtCapacityProcessor
 * @Author: 何义祈安
 * @Description: 解析负债能力指标页面，获取数据
 * @Date: 2022/11/6 17:53
 * @Version: 1.0
 */
@Component
public class DebtCapacityProcessor implements PageProcessor {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Pattern pattern = Pattern.compile("[^0-9]");
    private final Pattern p = Pattern.compile(".*\\d+.*");

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
    *@Description 解析国内AB股获取数据
    *
    *@Param [page]
    **/
    private void praseABDetails(Page page) {
        Html html = page.getHtml();
        //System.out.println(html);
        DebtCapacityDO debtCapacityDO = new DebtCapacityDO();
        //获取数据url
        debtCapacityDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期
        String da = html.xpath("//div[@id='F10MainTargetDiv']/div[@id='report_zyzb']/table[@class='needScroll']/tbody/tr[1]/th[3]/span/text()").toString();
        System.out.println("信息日期："+da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtCapacityDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        String code = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[1]/a[@target='_blank']/text()").toString();
        System.out.println("股票代码："+code);
        debtCapacityDO.setCode(code);
        //获取股票名称
        Selectable name = html.xpath("//div[@class='qphox']/div[@class='sckifbox']/div[@class='scklox']/div[@class='cnt']/p[@class='key']/b/a[1]/span/text()");
        System.out.println("股票名称："+name);
        debtCapacityDO.setName(name.toString());

        List<Selectable> nodes = html.css("div#report_zyzb table.needScroll tbody tr").nodes();
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/span/text()").toString();
            if(!Objects.isNull(s)){
                switch (s){
                    case "流动比率":{
                        //获取流动比率currentRatio
                        Selectable currentRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("流动比率的值："+currentRatio);
                        debtCapacityDO.setCurrentRatio(decimalHandle(currentRatio));
                        break;
                    } case "速动比率":{
                        //获取速动比率quickRatio
                        Selectable quickRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("流动比率的值：："+quickRatio);
                        debtCapacityDO.setQuickRatio(decimalHandle(quickRatio));
                        break;
                    } case "资产负债率(%)":{
                        //获取资产负债率
                        Selectable debtAssetRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("资产负债率的值："+debtAssetRatio);
                        debtCapacityDO.setDebtAssetRatio(decimalHandle(debtAssetRatio));
                        break;
                    } default: {
                        break;
                    }
                }
            }
        }
        //循环结束将获取到的指标放到page对象里
        page.putField("debtCapacityDO",debtCapacityDO);
    }

    /**
    *@Description 解析港股详情页获取信息
    *@Param [page]
    *@return void
    **/
    private void praseHKDetails(Page page) {
        Html html = page.getHtml();
//        System.out.println(html);
        DebtCapacityDO debtCapacityDO = new DebtCapacityDO();
        //获取数据url
        debtCapacityDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期
        String da = html.css("div.content_zyzb table.commonTable tbody tr:nth-child(1) th:nth-child(2)", "text").toString();
        System.out.println("信息日期："+da);
        try {
            Date date = simpleDateFormat.parse(da);
            debtCapacityDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        Selectable codename = html.xpath("//div[@id='title']/div[@class='head_top']/h1");
        String code = pattern.matcher(codename.xpath("//h1/span[1]/text()").toString()).replaceAll("").trim();
        System.out.println("股票代码："+code);
        debtCapacityDO.setCode(code);
        //获取股票名称
        String name = codename.xpath("//h1/span[@class='stockName']/text()").toString();
        System.out.println("股票名称："+name);
        debtCapacityDO.setName(name);

        List<Selectable> nodes = html.css("div.content_zyzb table.commonTable tbody tr").nodes();
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/text()").toString();
            if (!Objects.isNull(s)) {
                switch (s){
                    case "流动比率":{
                        //获取流动比率currentRatio
                        Selectable currentRatio = node.xpath("//tr/td[2]/text()");
                        System.out.println("流动比率的值："+currentRatio);
                        debtCapacityDO.setCurrentRatio(decimalHandle(currentRatio));
                        break;
                    } case "速动比率":{
                        //获取速动比率quickRatio
                        Selectable quickRatio = node.xpath("//tr/td[2]/text()");
                        System.out.println("流动比率的值：："+quickRatio);
                        if(!Objects.isNull(quickRatio)){
                            debtCapacityDO.setQuickRatio(decimalHandle(quickRatio));
                        }
                        break;
                    } case "资产负债率(%)":{
                        //获取资产负债率
                        Selectable debtAssetRatio = node.xpath("//tr/td[2]/text()");
                        System.out.println("资产负债率的值："+debtAssetRatio);
                        debtCapacityDO.setDebtAssetRatio(decimalHandle(debtAssetRatio));
                        break;
                    } default: {
                        break;
                    }
                }
            }
        }
        //循环结束将获取到的指标放到page对象里
        System.out.println(debtCapacityDO);
        page.putField("debtCapacityDO",debtCapacityDO);

    }

    /**
    *@Description 解析美股详情页获取信息
    *@Param [page]
    *@return void
    **/
    private void praseUSDetails(Page page) {
        Html html = page.getHtml();
        //System.out.println(html);
        DebtCapacityDO debtCapacityDO = new DebtCapacityDO();
        //获取数据url
        debtCapacityDO.setDetailUrl(page.getUrl().toString());
        System.out.println("数据url："+page.getUrl().toString());
        //获取信息日期(已经加上span)
        String da = html.css("div#report_zyzb table tbody tr:nth-child(1) th:nth-child(2) span", "text").toString();
        System.out.println("信息日期："+da);
        try {
            //选择器出错
            Date date = simpleDateFormat.parse(da);
            debtCapacityDO.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取股票代码
        Selectable codename = html.xpath("//div[@class='head_top']/h1/span");
        String code = codename.xpath("//span/i/text()").toString();
        System.out.println("股票代码："+code);
        debtCapacityDO.setCode(code);
        //获取股票名称
        String name = codename.xpath("//span[@id='js_title']/text()").toString();
        System.out.println("股票名称："+name);
        debtCapacityDO.setName(name);

        List<Selectable> nodes = html.css("div#report_zyzb table tbody tr").nodes();
        for (Selectable node : nodes) {
            String s = node.xpath("//tr/td[1]/text()").toString();
            if (!Objects.isNull(s)) {
                switch (s){
                    case "流动比率(倍)":{
                        //获取流动比率currentRatio
                        Selectable currentRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("流动比率的值："+currentRatio);
                        debtCapacityDO.setCurrentRatio(decimalHandle(currentRatio));
                        break;
                    } case "速动比率(倍)":{
                        //获取速动比率quickRatio
                        Selectable quickRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("流动比率的值：："+quickRatio.toString());
                        String noValue = "--";
                        if (noValue.equals(quickRatio)){
                            debtCapacityDO.setQuickRatio(decimalHandle(quickRatio));
                        }
                        break;
                    } case "资产负债率":{
                        //获取资产负债率
                        Selectable debtAssetRatio = node.xpath("//tr/td[2]/span/text()");
                        System.out.println("资产负债率的值："+debtAssetRatio);
                        debtCapacityDO.setDebtAssetRatio(decimalHandle(debtAssetRatio));
                        break;
                    } default: {
                        break;
                    }
                }
            }
        }
        //循环结束将获取到的指标放到page对象里
        page.putField("debtCapacityDO",debtCapacityDO);


    }

    /**
    *@Description 换成精确到后两位的Double对象类型
    **/
    private Double decimalHandle(Selectable currentRatio) {
        //判断是否包含数字，筛选“--”，也可以“--”.equals(xxx)
        if(!p.matcher(currentRatio.toString()).matches()){
            return 0.00;
        }
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

        Spider.create(new DebtCapacityProcessor())
                //TODO 自定义保存还没写
//                .addPipeline(debtPipeline)
                .setDownloader(new DebtDetailsDownloader())
                .setScheduler(scheduler)
                .addRequest(request)
                .thread(1)
                .run();
    }
}
