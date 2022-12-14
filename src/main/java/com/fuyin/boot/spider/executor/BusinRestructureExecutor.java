package com.fuyin.boot.spider.executor;

import com.fuyin.boot.spider.Downloader.BusRestructureDownloader;
import com.fuyin.boot.spider.pipeline.BusinessAllPipeline;
import com.fuyin.boot.spider.processor.BusinRestructureProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @ClassName: BusinRestructureExecutor
 * @Author: 何义祈安
 * @Description: 业务重组数据爬取执行类
 * @Date: 2022/11/15 16:45
 * @Version: 1.0
 */
@Component
public class BusinRestructureExecutor {
    @Autowired
    private BusinRestructureProcessor businRestructureProcessor;
    @Autowired
    private BusRestructureDownloader busRestructureDownloader;
    @Autowired
    private BusinessAllPipeline businessAllPipeline;

    public void doCrawl(String url,String stockType) {
        Request request = new Request(url);
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));

        request.putExtra("stockType",stockType);
        Spider.create(businRestructureProcessor)
                .setDownloader(busRestructureDownloader)
                .addPipeline(businessAllPipeline)
                .addRequest(request)
                .setScheduler(scheduler)
                .thread(5)
                .run();

    }
}
