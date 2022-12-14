package com.fuyin.boot.spider.executor;

import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.spider.pipeline.DebtPipeline;
import com.fuyin.boot.spider.processor.DebtSeleniumProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @Description 执行通过selenium自动下载渲染完成的页面的processor
 * @author 何义祈安
 * @Date 2022/10/23 17:31
 * @Version 1.0
 */
@Component
public class DebtSeleniumExecutor {

    @Value("${indexUrl.DebtDetails}")
    private String indexUrl;

    @Autowired
    private DebtSeleniumProcessor debtSeleniumProcessor;

    @Autowired
    private DebtPipeline pipeline;

    @Autowired
    private DebtDetailsDownloader debtDetailsDownloader;

//    @Scheduled(fixedRate = 3600*24)
    public void doCrawler(){
        // 添加代理
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("117.70.40.50",33542)));

        // 设置布隆过滤器去重策略
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));

        Request request = new Request(indexUrl);
//        request.putExtra("level","list");
//        request.putExtra("pageNum","1");

        Spider.create(debtSeleniumProcessor)
                //TODO 自定义保存还没写
                .addPipeline(pipeline)
                .setDownloader(debtDetailsDownloader)
                .setScheduler(scheduler)
                .addRequest(request)
                .thread(1)
                .run();
    }

}
