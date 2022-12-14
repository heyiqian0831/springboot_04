package com.fuyin.boot.spider.executor;

import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.spider.pipeline.DebtAllPipeline;
import com.fuyin.boot.spider.processor.DebtCapacityProcessor;
import com.fuyin.boot.spider.processor.DebtCashFlowProcessor;
import com.fuyin.boot.spider.processor.DebtProfitabilityProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.net.URL;

/**
 * @ClassName: DebtCapacityCrawlExecutor
 * @Author: 何义祈安
 * @Description: 负债能力指标分析表爬取操作类
 * @Date: 2022/11/7 15:59
 * @Version: 1.0
 */
@Component
public class DebtAllExecutor {
    @Autowired
    private DebtCapacityProcessor capacityProcessor;
    @Autowired
    private DebtProfitabilityProcessor profitabilityProcessor;
    @Autowired
    private DebtCashFlowProcessor cashFlowProcessor;
    @Autowired
    private DebtDetailsDownloader debtDetailsDownloader;
    @Autowired
    private DebtAllPipeline debtAllPipeline;

    /**
    *@Description 调用spider连接执行webmagic四大组件
    *@Param url,：详情页url
    * @Param stockType：股票类型，取自DetailsUrl表，用在downloader
    * @Param processorType：Processor类型，判断是爬取债务重组中的哪个表
    *@return void
    **/
    public void doCrawl(String url,String stockType,String processorType){
        QueueScheduler scheduler = new QueueScheduler();
        scheduler.setDuplicateRemover(new BloomFilterDuplicateRemover(1000000));
        if ("capacity".equals(processorType)){
            Request request = new Request(url);
            request.putExtra("stockType",stockType);
            Spider.create(capacityProcessor)
                    .addPipeline(debtAllPipeline)
                    .setDownloader(debtDetailsDownloader)
                    .setScheduler(scheduler)
                    .addRequest(request)
                    .thread(1)
                    .run();
        }
        if ("profitability".equals(processorType)){
            Request request = new Request(url);
            request.putExtra("stockType",stockType);
            Spider.create(profitabilityProcessor)
                    .addPipeline(debtAllPipeline)
                    .setDownloader(debtDetailsDownloader)
                    .setScheduler(scheduler)
                    .addRequest(request)
                    .thread(1)
                    .run();
        }
        if ("cashFlow".equals(processorType)){
            Request request = new Request(url);
            request.putExtra("stockType",stockType);
            Spider.create(cashFlowProcessor)
                    .addPipeline(debtAllPipeline)
                    .setDownloader(debtDetailsDownloader)
                    .setScheduler(scheduler)
                    .addRequest(request)
                    .thread(1)
                    .run();

        }
    }


}
