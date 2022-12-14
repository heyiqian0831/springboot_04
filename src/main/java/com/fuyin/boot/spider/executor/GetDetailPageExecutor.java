package com.fuyin.boot.spider.executor;

import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.spider.Downloader.EastMoneySearchDownloader;
import com.fuyin.boot.spider.pipeline.DebtAllPipeline;
import com.fuyin.boot.spider.pipeline.GetDetailPagePipeline;
import com.fuyin.boot.spider.processor.DebtCapacityProcessor;
import com.fuyin.boot.spider.processor.GetDetailPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @ClassName: GetDetailPageCrawlExecutor
 * @Author: 何义祈安
 * @Description: 获取企业的最终详情页操作类
 * @Date: 2022/11/7 23:36
 * @Version: 1.0
 */
@Component
public class GetDetailPageExecutor {

    @Autowired
    private GetDetailPageProcessor getDetailPageProcessor;
    @Autowired
    private EastMoneySearchDownloader eastMoneySearchDownloader;
    @Autowired
    private GetDetailPagePipeline getDetailPagePipeline;

    @Value("${indexUrl.SearchHome}")
    private String indexUrl;

    //@Scheduled(fixedRate = 3600*24)
    public void doCrawl(String enterpriseName){
        // 添加代理
        //HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        //httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("117.70.40.50",33542)));

        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));

        Request request = new Request(indexUrl);
        request.putExtra("enterpriseName",enterpriseName);
        request.putExtra("whichPage","search");
        Spider.create(getDetailPageProcessor)
                //TODO 自定义保存还没写
                .addPipeline(getDetailPagePipeline)
                .setDownloader(eastMoneySearchDownloader)
                .setScheduler(scheduler)
                .addRequest(request)
                .thread(1)
                .run();

        //TODO 怎么设置返回值咧

    }
}
