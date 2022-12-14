package com.fuyin.boot.spider.Downloader;

import com.fuyin.boot.enums.StockType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.util.List;

/**
 * @ClassName: BusRestructureDownloader
 * @Author: 何义祈安
 * @Description: 企业详情页行业对比（业务重组）页渲染下载
 * @Date: 2022/11/14 20:51
 * @Version: 1.0
 */
@Component
public class BusRestructureDownloader implements Downloader {
    private RemoteWebDriver driver;

    public BusRestructureDownloader(){
        // 加载chromedriver 是使用chorme的必要条件
        System.setProperty("webdriver.chrome.driver","E:\\RJ_chromedriver\\home\\chrome\\chromedriver_106.0.5249\\chromedriver.exe");
        // 添加chrome的配置信息
        ChromeOptions chromeOptions = new ChromeOptions();
        // 设置为无头模式
        chromeOptions.addArguments("--headless");
        // 设置打开的窗口大小 非必要属性
        chromeOptions.addArguments("--window-size=1920,1080");
        // 使用配置信息 创建driver对象
        driver = new ChromeDriver(chromeOptions);
    }
    @Override
    public Page download(Request request, Task task) {
        String stockType = String.valueOf(request.getExtra("stockType"));
        //设置点击事件
        Actions actions = new Actions(driver);
        driver.get(request.getUrl());
        //AB
        if(StockType.AB_STOCK.getCode().equals(stockType)) {
            try {
                Thread.sleep(1000);
                WebElement element = driver.findElement(By.cssSelector("div.weftnbox ul:nth-child(1) li#IndustryAnalysis a"));
                actions.click(element).perform();

                Thread.sleep(3000);
                String html = driver.getPageSource();
                return createPage(html, driver.getCurrentUrl(),stockType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //港股
        if(StockType.US_STOCK.getCode().equals(stockType)) {
            try {
                Thread.sleep(1000);
                List<WebElement> elements = driver.findElements(By.xpath("//div[@id='title']/ul[@class='topMenu']/li"));
                for (WebElement element : elements) {
                    if ("行业对比".equals(element.getText())){
                        actions.click(element).perform();
                    }
                }
                Thread.sleep(3000);
                String html = driver.getPageSource();
                return createPage(html, driver.getCurrentUrl(),stockType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        if(AB_STOCK.equals(stockType)) {
//        }
            return null;
    }
    /**
     *@Description 通用的创建page对象的方法
     *@Param [html, currentUrl]
     *@return us.codecraft.webmagic.Page
     **/
    private Page createPage(String html, String currentUrl, String stockType) {
        Page page = new Page();
        // 设置网页源码 + url
        page.setRawText(html);
        page.setUrl(new PlainText(currentUrl));
        page.isDownloadSuccess();
        // 设置request对象
        Request request = new Request(currentUrl);
        request.putExtra("stockType",stockType);
        page.setRequest(request);
        return page;
    }

    @Override
    public void setThread(int i) {

    }
}
