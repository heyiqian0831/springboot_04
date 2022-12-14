package com.fuyin.boot.spider.Downloader;

import com.fuyin.boot.enums.PageType;
import com.fuyin.boot.enums.StockType;
import javafx.util.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.File;
import java.util.*;

/**
 * @ClassName: EastMoneySearchDownloader
 * @Author: 何义祈安
 * @Description: 接收用户发送要查找的公司名称，
 * @Description 1）操作输入、操作点击查询，获取行情Details页并下载页面返回
 * @Description  2）操作行情页，返回最后详情页
 * @Date: 2022/11/1 16:46
 * @Version: 1.0
 */
@Component
public class EastMoneySearchDownloader implements Downloader {

    @Value("${indexUrl.SearchHome}")
    private String SearchHome;

    ArrayList<String> list = new ArrayList<String>();

    private RemoteWebDriver driver;

    public EastMoneySearchDownloader(){
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

    /**
    *@Description 通过东方财务搜索并点击财务报表,
     *            返回页面种类：1）详情页；
     *                       2）行情页
    *@Param [request, task]
    *@return us.codecraft.webmagic.Page
    **/
    @Override
    public Page download(Request request, Task task) {
        //获取用户输入的企业名称
        String enterpriseName = String.valueOf(request.getExtra("enterpriseName"));
        //设置点击事件
        Actions actions = new Actions(driver);
        //如果要操作的初始页面是搜索页：PageType.SEARCH.getCode()
        if(PageType.SEARCH.getCode().equals(request.getExtra("whichPage"))){
            try {
                driver.get(SearchHome);
                //TODO 这里是等待网页加载渲染，是否需要时间长点
                Thread.sleep(1000);
                //找到搜索输入框，并输入企业名称
                driver.findElement(By.cssSelector("input#code_suggest")).sendKeys(enterpriseName);
                Thread.sleep(1000);
                //点击搜索按钮
                WebElement searchButton = driver.findElement(By.cssSelector("a#search_view_btn1"));
                actions.click(searchButton).perform();
                Thread.sleep(1000);
                forGetCurrentWindow(driver);
                Thread.sleep(1000);

                //返回page
                String html = driver.getPageSource();
                return createPage(html,driver.getCurrentUrl(),PageType.SEARCH.getCode(),enterpriseName,null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //如果操作初始页是行情页：（AB股）
        if(PageType.AB_QUOTATION.getCode().equals(request.getExtra("whichPage"))){
            try {
                driver.get(request.getUrl());
                //TODO 这里是等待网页加载渲染，是否需要时间长点
                Thread.sleep(1000);
                //点击财务分析
                driver.findElements(By.xpath("//div[@class = 'hqrls']/div[1]/a"));
                List<WebElement> choose = driver.findElements(By.xpath("//div[@class = 'hqrls']/div[1]/a"));
                for (WebElement webElement : choose) {
                    if("财务分析".equals(webElement.getText())){
                        actions.click(webElement).perform();
                    }
                }
                Thread.sleep(2000);
                forGetCurrentWindow(driver);

                //相等就返回page
                String html = driver.getPageSource();
                return createPage(html,driver.getCurrentUrl(),PageType.QUOTATION.getCode(),enterpriseName, StockType.AB_STOCK.getCode());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //港股
        if(PageType.HK_QUOTATION.getCode().equals(request.getExtra("whichPage"))) {
            try {
                driver.get(request.getUrl());
                Thread.sleep(1000);
                //点击财务分析
                WebElement element = driver.findElement(By.cssSelector("div.hknoticef10 div.r div.hkf10 span.links a:nth-child(2)"));
                actions.click(element).perform();
                Thread.sleep(2000);
                forGetCurrentWindow(driver);

                //相等就返回page
                String html = driver.getPageSource();
                return createPage(html,driver.getCurrentUrl(),PageType.QUOTATION.getCode(),enterpriseName, StockType.HK_STOCK.getCode());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //美股
        if(PageType.US_QUOTATION.getCode().equals(request.getExtra("whichPage"))) {
            try {
                driver.get(request.getUrl());
                Thread.sleep(1000);
                //点击财务分析
                WebElement element = driver.findElement(By.cssSelector("div#us_f10 span:nth-child(4)"));
                actions.click(element).perform();
                Thread.sleep(2000);
                forGetCurrentWindow(driver);

                //相等就返回page
                String html = driver.getPageSource();
                return createPage(html,driver.getCurrentUrl(),PageType.QUOTATION.getCode(),enterpriseName,StockType.US_STOCK.getCode());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
    *@Description 获取url最后变动的页面为主页
    *@Param [driver]
    *@return void
    **/
    private void forGetCurrentWindow(RemoteWebDriver driver) {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if(!list.contains(handle)){
                java.time.Duration time = java.time.Duration.ofDays(3);
                WebDriverWait wait = new WebDriverWait(driver, time);
                wait.until(new ExceptWindow(handle));
                System.out.println(handle);
                list.add(handle);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
    *@Description
    *
     * @Param html: 渲染后最终网页源代码
     * @Param currentUrl: 渲染后最终网页URL
     * @Param flag: 判断返回的是搜索结果行情页还是详情页
     * @Param name: 企业名称
     * @Param type: 股票类型
    *@return us.codecraft.webmagic.Page
    **/
    private Page createPage(String html, String currentUrl,String flag,String name,String type) {
        Page page = new Page();
        // 设置网页源码 + url
        page.setRawText(html);
        page.setUrl(new PlainText(currentUrl));
        page.isDownloadSuccess();

        // 设置request对象
        Request request = new Request(currentUrl);
        request.putExtra("whichPage",flag);
        request.putExtra("enterpriseName",name);
        request.putExtra("stockType",type);
        page.setRequest(request);

        return page;
    }

    @Override
    public void setThread(int i) {

    }

    static class ExceptWindow implements ExpectedCondition<WebDriver> {
        private String id;
        public ExceptWindow(String id) {
            this.id = id;
        }
        @Override
        public WebDriver apply(WebDriver d) {
            return d.switchTo().window(id);
        }
    }

}
