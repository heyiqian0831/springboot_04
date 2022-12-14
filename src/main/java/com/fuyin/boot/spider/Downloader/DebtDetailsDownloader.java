package com.fuyin.boot.spider.Downloader;

import com.fuyin.boot.enums.StockType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.sql.Driver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*@author 何义祈安
*@Description 下载组件定义
*@Date 17:22 2022/10/23
**/
@Component
public class DebtDetailsDownloader implements Downloader {

    private RemoteWebDriver driver;

    public DebtDetailsDownloader(){
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
    *@Description 自定义下载
    *@Date 17:23 2022/10/23
    *@Param [request, task]
    *@return us.codecraft.webmagic.Page
    **/
    @Override
    public Page download(Request request, Task task) {
        //获取用户输入的企业名称
        String enterpriseName = String.valueOf(request.getExtra("enterpriseName"));
        String stockType = String.valueOf(request.getExtra("stockType"));
        //设置点击事件
        Actions actions = new Actions(driver);
        //如果是国内股票的详情页
        driver.get(request.getUrl());
        if(StockType.AB_STOCK.getCode().equals(stockType)) {
            try {
                Thread.sleep(1000);
                //找到主要指标的年度并点击
                WebElement element = driver.findElement(By.cssSelector("div.section.first div.content div.tab ul#zyzbTab"));
                WebElement element1 = element.findElement(By.cssSelector("li[data-type='1']"));
                actions.click(element1).perform();

                //点击资产负债的年度部分
                WebElement element2 = driver.findElement(By.cssSelector("ul#zcfzb_ul"));
                WebElement element3 = element2.findElement(By.cssSelector("li[reportdatetype='1'][reporttype='1']"));
                actions.click(element3).perform();

                //点击利润表的年度部分
                WebElement element4 = driver.findElement(By.cssSelector("ul#lrb_ul"));
                WebElement element5 = element4.findElement(By.cssSelector("li[reportdatetype='1'][reporttype='1']"));
                actions.click(element5).perform();

                //点击现金流量表年度部分xjllb_ul
                WebElement element6 = driver.findElement(By.cssSelector("ul#xjllb_ul"));
                WebElement element7 = element6.findElement(By.cssSelector("li[reportdatetype='1'][reporttype='1']"));
                actions.click(element7).perform();

                // 睡一段时间 让浏览器渲染成功
                driver.executeScript("window.scrollTo(0,document.body.scrollHeight-300)");
                driver.executeScript("window.scrollTo(-300,document.body.scrollHeight0)");
                Thread.sleep(3000);
                String html = driver.getPageSource();
                return createPage(html, driver.getCurrentUrl(),stockType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //如果是香港股票的详情页
        if(StockType.HK_STOCK.getCode().equals(stockType)){
            try {
                Thread.sleep(1000);
                Map<String,WebElement> map = new HashMap<>();
                //点击主要指标的年度
                WebElement ele_zyzb= driver.findElement(By.cssSelector("div.content_zyzb div.scrollTab ul li:nth-child(2)"));
                map.put("zyzb",ele_zyzb);
                actions.click(ele_zyzb).perform();
                Thread.sleep(1000);

                //点击资产负债年度 content_zcfzb
                WebElement ele_zcfz = driver.findElement(By.cssSelector("div.content_zcfzb div.scrollTab ul li:nth-child(2)"));
                map.put("zcfzb",ele_zcfz);
                actions.click(ele_zcfz).perform();
                Thread.sleep(1000);

                //点击利润表年度
                WebElement ele_lrb = driver.findElement(By.cssSelector("div.content_lrb ul li:nth-child(2)"));
                map.put("lrb",ele_lrb);
                actions.click(ele_lrb).perform();
                Thread.sleep(1000);

                //点击现金流量表年度 content_xjllb
                WebElement ele_xjllb = driver.findElement(By.cssSelector("div.content_xjllb div.scrollTab ul li:nth-child(2)"));
                map.put("xjllb",ele_xjllb);
                actions.click(ele_xjllb).perform();
                Thread.sleep(1000);
//                click(map,actions);

                // 睡一段时间 让浏览器渲染成功
                driver.executeScript("window.scrollTo(0,document.body.scrollHeight-300)");
                driver.executeScript("window.scrollTo(-300,document.body.scrollHeight0)");
                Thread.sleep(3000);
                String html = driver.getPageSource();
                return createPage(html, driver.getCurrentUrl(),stockType);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //如果是美国股票的详情页
        if(StockType.US_STOCK.getCode().equals(stockType)){
            try {
                Thread.sleep(1000);
                Map<String,WebElement> map = new HashMap<>();
                //点击主要指标的年度
                WebElement ele_zyzb= driver.findElement(By.cssSelector("ul#ulzyzb li:nth-child(2) a"));
                map.put("zyzb",ele_zyzb);
                actions.click(ele_zyzb).perform();
                Thread.sleep(2000);


                //点击资产负债年度
                WebElement ele_zcfz = driver.findElement(By.cssSelector("ul#ulzcfzb li:nth-child(2) a"));
                map.put("zcfzb",ele_zcfz);
                //点死你，shift
                for(int i=0;i<10;i++){
                    actions.doubleClick(ele_zcfz).perform();
                }
                Thread.sleep(1000);

                //点击综合损益表年度
                WebElement ele_lrb = driver.findElement(By.cssSelector("ul#ullrb li:nth-child(2) a"));
                map.put("lrb",ele_lrb);
                actions.click(ele_lrb).perform();
                Thread.sleep(1000);

                //点击现金流量表年度
                WebElement ele_xjllb = driver.findElement(By.cssSelector("ul#ulxjllb li:nth-child(2) a"));
                map.put("xjllb",ele_xjllb);
                actions.click(ele_xjllb).perform();
                Thread.sleep(1000);
//                click(map,actions);

                // 睡一段时间 让浏览器渲染成功
                driver.executeScript("window.scrollTo(0,document.body.scrollHeight-300)");
                driver.executeScript("window.scrollTo(-300,document.body.scrollHeight0)");
                Thread.sleep(3000);
                String html = driver.getPageSource();
                return createPage(html, driver.getCurrentUrl(),stockType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void click(Map<String, WebElement> map,Actions actions) {
        for (WebElement value : map.values()) {
            try {
                actions.click(value).perform();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    /**
     * 设置线程方法 与我们无关 放在这里即可
     * @param i
     */
    @Override
    public void setThread(int i) {
    }
}
