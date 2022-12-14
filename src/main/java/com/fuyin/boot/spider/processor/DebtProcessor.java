package com.fuyin.boot.spider.processor;

import com.fuyin.boot.mgb.entity.DebtDiagnosisDO;
import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.mgb.mapper.DebtDiagnosisMapper;
import com.fuyin.boot.mgb.mapper.UserMapper;
import com.fuyin.boot.spider.pipeline.DebtPipeline;
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
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description TODO
 * @author 何义祈安
 * @Date 2022/10/7 20:55
 * @Version 1.0
 */
@Component
public class DebtProcessor implements PageProcessor {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        //数据实例
        DebtDiagnosisDO debtDiagnosisDO = new DebtDiagnosisDO();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");


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

    private static void setGetHeaders(Request request) {
//        request.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        request.addHeader("Accept-Encoding","gzip, deflate, br");
//        request.addHeader("Accept-Language","zh-CN,zh;q=0.9");
//        request.addHeader("Cache-Control","max-age=0");
//        request.addHeader("Connection","keep-alive");
//        request.addHeader("Host","https://data.eastmoney.com");
//        request.addHeader("Referer","https://search.51job.com/");
//        request.addHeader("Upgrade-Insecure-Requests","1");
        request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//        request.addHeader("");
    }

    public static void main(String[] args) {
        DebtPipeline debtPipeline = new DebtPipeline();
        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HSF10/NewFinanceAnalysis/ZYZBAjaxNew?type=1&code=SZ003023");
//        setGetHeaders(request);
        Spider.create(new DebtProcessor())
                .addRequest(request)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(
                                new BloomFilterDuplicateRemover(100000000)))
                .addPipeline(debtPipeline)
//                .addUrl("https://data.eastmoney.com/stockdata/003023.html")
                .thread(5)
                .run();
    }
}
