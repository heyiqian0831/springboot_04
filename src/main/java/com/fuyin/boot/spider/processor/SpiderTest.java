package com.fuyin.boot.spider.processor;

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

import java.text.SimpleDateFormat;

/**
 * @Description 爬虫测试入口
 * @author 何义祈安
 * @Date 2022/10/7 20:55
 * @Version 1.0
 */
@Component
public class SpiderTest implements PageProcessor {

    @Override
    public void process(Page page) {
        //解析公司信息并存储
        try {
            paraseDetails(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *@author 何义祈安
    *@Description 解析财务报告详细信息
    **/
    private void paraseDetails(Page page) {
        Html html = page.getHtml();
        System.out.println(html);
//        String s = html.css("div.footer-qrlast div.t a").toString();
//        System.out.println(s);
//        String s2 = html.css("div.footer-qrlast div.t a","text").toString();
//        System.out.println("s2"+s2);
//        String s3 = html.css("div.footer-qrlast div.t a","text").toString();
//        System.out.println("s3"+s3);
//        String s4 = html.css("div.footer-qrlast div.t a","href").toString();
//        System.out.println("s4"+s4);
//
//        String s1 = html.xpath("//div[@class='footer-qrlast']/div[@class='t']/a/text()").toString();
//        System.out.println(s1);
//        System.out.println("加了text()"+html.xpath("//div[@class='footer-qrlast']/div[@class='t']/a/text()"));
//
//        String text2 = html.css("div.footerlinks a:nth-child(5)", "text").toString();
//        System.out.println(text2);
    }

    private Site site = Site.me()
            .setCharset("UTF-8") //编码
            .setSleepTime(1) //抓取时间间隔
            .setTimeOut(1000*10) //超时时间
            .setRetrySleepTime(3000) //重试时间
            .setRetryTimes(3); //重试次数
    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HKF10/pages/home/index.html?code=01996&type=web&color=w#/NewFinancialAnalysis");
        request.putExtra("stockType","HKStock");
        Spider.create(new SpiderTest())
                .setDownloader(new BusRestructureDownloader())
                .addRequest(request)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(
                                new BloomFilterDuplicateRemover(100000000)))
                .thread(5)
                .run();
    }
}
