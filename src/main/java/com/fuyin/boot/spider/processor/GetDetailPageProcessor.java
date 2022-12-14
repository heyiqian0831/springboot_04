package com.fuyin.boot.spider.processor;

import com.baomidou.mybatisplus.annotation.*;
import com.fuyin.boot.enums.PageType;
import com.fuyin.boot.spider.Downloader.DebtDetailsDownloader;
import com.fuyin.boot.spider.Downloader.EastMoneySearchDownloader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: GetDetailPageProcessor
 * @Author: 何义祈安
 * @Description: 通过用户输入名称查询detail页url
 * @Date: 2022/11/7 18:06
 * @Version: 1.0
 */
@Component
public class GetDetailPageProcessor implements PageProcessor {
    Pattern pattern = Pattern.compile("[0-9]+");
    private static final Pattern P = Pattern.compile(".*\\d+.*");

    @Override
    public void process(Page page) {
        String whichPage = page.getRequest().getExtra("whichPage").toString();
        //返回的是搜索结果页
        if(PageType.SEARCH.getCode().equals(whichPage)){
            praseQuotation(page);
        }else if (PageType.QUOTATION.getCode().equals(whichPage)){
            //返回的是详情页
            System.out.println("测试成功");
            Map<String,Object> map = new HashMap<>();
            map.put("enterpriseName",page.getRequest().getExtra("enterpriseName").toString());
            map.put("targetUrl",page.getUrl().toString());
            map.put("stockType",page.getRequest().getExtra("stockType").toString());
            page.putField("result",map);
        }
    }

        /**
    *@Description downloader返回的是企业行情页页面，获取该页面url继续下载数据页面
     *            返回页面种类：1）详情页；
     *                       2）行情页
    *@Param [page]
    *@return void
    **/
    private void praseQuotation(Page page) {
        Request request = new Request();
        Html html = page.getHtml();
        //获取用户输入的企业名称
        String enterpriseName = page.getRequest().getExtra("enterpriseName").toString();
        //判断url是否是搜索页
        String currentUrl = page.getRequest().getUrl();
        //搜索页(quotation) https://so.eastmoney.com/quotation/s?isAssociation=false&keyword=%E7%BE%8E%E5%88%A9%E4%BA%91
        String searchQuaPattern = ".*eastmoney.com/quotation.*";
        //搜索页（web）https://so.eastmoney.com/web/s?keyword=%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4
        String searchWebPattern = ".*eastmoney.com/web.*";
        //行情页
        String detailPattern = ".*quote.eastmoney.com.*";
        if(Pattern.matches(searchWebPattern, currentUrl)){
            //是行情搜索(web)结果页
            //获取搜索结果页中企业股票名称
//            #stock_type_AB_STOCK > ul > li > a > span > em
            String title = html.css("div.c_l div.exstock div.exstock_t a", "title").toString();
            boolean ifHasUserInput = false;
            if (title.equals(enterpriseName)){
                ifHasUserInput = true;
            }
            //搜索结果有用户要的股票
            if(ifHasUserInput){
                //下载页用搜索页操作
                request.putExtra("whichPage",PageType.SEARCH.getCode());
                request.putExtra("enterpriseName",enterpriseName);
                //加不加url没关系，回到search必定是搜索页
                request.setUrl("https://www.eastmoney.com");
                // 添加到队列
                page.addTargetRequest(request);
            }else{
                //没有用户要的股票，只能是名字错误
                //TODO 用日志
                throw new RuntimeException("名字错了谢谢");
            }

        }
        if(Pattern.matches(searchQuaPattern, currentUrl)){
            //是行情搜索(quotation)结果页
            String typeNamePattern = ".*股.*";
            List<Selectable> nodes = html.css("div.main.container div.c_l div.search_stocks div.stock_type_item").nodes();
            for (Selectable node : nodes) {
                String typeName = node.css("div.sti_h div.sti_t", "text").toString();
                if(Pattern.matches(typeNamePattern, typeName)){
                    String name = html.css("ul.sti_list li:nth-child(1) a span em", "text").toString();
                    //搜索结果有用户要的股票
                    if(name.equals(enterpriseName)){
                        //下载页用搜索页操作
                        request.putExtra("whichPage",PageType.SEARCH.getCode());
                        request.putExtra("enterpriseName",enterpriseName);
                        //加不加url没关系，回到search必定是搜索页
                        request.setUrl("https://www.eastmoney.com");
                        // 添加到队列
                        page.addTargetRequest(request);
                    }else{
                        //没有用户要的股票，只能是名字错误
                        //TODO 用日志
                        throw new RuntimeException("名字错了谢谢");
                    }
                    break;
                }
            }

        }
        if(Pattern.matches(detailPattern, currentUrl)){
            //是企业行情页
            //获取详情页股票名称，判断是否和用户要查的一致，因为已退市的股票也能进到这一步
//            String getName = html.xpath("//div[@class='quote_title_l']/span[1]/text()").toString();
            String getName = html.css("div.quote_title_l span:nth-child(1)", "title").toString();
            if(getName.equals(enterpriseName)){
                //获取股票代码判断是哪种股票
                String getCode = html.xpath("//div[@class='quote_title_l']/span[2]/text()").toString();
                boolean matches = getCode.matches("[0-9]+");
                if(!matches){
                    //美股
                    String stockPrice = html.css("div.zxj span span","text").toString();
                    String quotation = PageType.US_QUOTATION.getCode();
                    //加入队列
                    whichPage(stockPrice,quotation,enterpriseName,page);
                }else{
                    //港股或者AB
                    int length = getCode.length();
                    switch (length){
                        case 5:{
                            //港股
                            String stockPrice = html.xpath("//div[@class='zxj']/span/span/text()").toString();
                            String HKQuotation = PageType.HK_QUOTATION.getCode();
                            whichPage(stockPrice,HKQuotation,enterpriseName,page);
                            break;
                        }
                        case 6:{
                            //AB股 退市：errorstate 没退市：zxj
                            String ifTui = html.css("div.quote3l_l div.quote_quotenums div:nth-child(1)", "class").toString();
                            if ("zxj".equals(ifTui)){
                                //该公司没退市
                                request.putExtra("whichPage",PageType.AB_QUOTATION.getCode());
                                request.putExtra("enterpriseName",enterpriseName);
                                request.setUrl(page.getUrl().toString());
                                page.addTargetRequest(request);
                            }else{
                                throw new RuntimeException("该公司已退市");
                            }
                            break;
                        }
                        default:{
                            break;
                        }
                    }
                }
            }else{
                throw new RuntimeException("极大概率是退市了，不做判断");
            }
        }
    }

    private void whichPage(String stockPrice, String quotation, String enterpriseName, Page page) {
        Request request = new Request();
        Matcher m = P.matcher(stockPrice);
        if (m.matches()){
            //该公司没退市
            request.putExtra("whichPage",quotation);
            request.putExtra("enterpriseName",enterpriseName);
            request.setUrl(page.getUrl().toString());
            page.addTargetRequest(request);
        }else{
            throw new RuntimeException("该公司已退市");
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

//        DebtPipeline debtPipeline = new DebtPipeline();
        Request request = new Request("https://so.eastmoney.com/quotation/s?isAssociation=false&keyword=%E7%89%B9%E6%96%AF%E6%8B%89");
        request.putExtra("enterpriseName","特斯拉");
        request.putExtra("whichPage","search");
        Spider.create(new GetDetailPageProcessor())
                //TODO 自定义保存还没写
//                .addPipeline(debtPipeline)
//                .setDownloader(new EastMoneySearchDownloader())
                .setScheduler(scheduler)
                .addRequest(request)
                .thread(1)
                .run();
    }
}
