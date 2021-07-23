/*
 * chenhx
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package wiki.primo.reptile.infoq;

import com.alibaba.fastjson.JSON;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import wiki.primo.reptile.util.ChromeOptionsUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenhx
 * @version 0.0.1
 * @className InfoQList.java
 * @date 2021-07-22 8:11 下午
 * @description
 */
public class InfoQList {

    /**
     * 获取InfoQ文章列表数据
     * @param args
     */
    public static void main(String[] args) {
        String url = "https://xie.infoq.cn/recommend";

        System.getProperties().setProperty("webdriver.chrome.driver", "/Users/chenhx/Desktop/config/chromedriver");
        System.getProperties().setProperty("selenuim_config", "/Users/chenhx/Desktop/config/config.ini");

        ChromeOptions chromeOptions = ChromeOptionsUtils.getChrome();
        //InfoQ有检测 user-agent，必须有这句
        chromeOptions.addArguments("user-agent=\"Mozilla/5.0 (iPod; U; CPU iPhone OS 2_1 like Mac OS X; ja-jp) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5F137 Safari/525.20\"");
        ChromeDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.get(url);
        //  设置隐性等待时间 - 需要配合使用的
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        try {
            //等待一定时间再进行加载
            WebDriverWait wait = new WebDriverWait(webDriver, 20);
            //等待元素的加载。最大的时间30秒
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(InfoQBlog.URLS.substring(0, InfoQBlog.URLS.lastIndexOf("/")))));
        } catch (org.openqa.selenium.TimeoutException e) {
            //实际项目中，请打印日志。务必不要使用：e.printStackTrace()
            e.printStackTrace();
        }
        String pageSource = webDriver.getPageSource();
        JXDocument jxDocument = JXDocument.create(pageSource);
        List<String> titles = jxDocument.selN(InfoQBlog.TITLES).stream().map(JXNode::asString).collect(Collectors.toList());
        List<String> blogUrls = jxDocument.selN(InfoQBlog.URLS).stream().map(JXNode::asString).collect(Collectors.toList());
        webDriver.close();
        webDriver.quit();
        System.out.println("titles:"+JSON.toJSONString(titles));
        System.out.println("blogUrls:"+JSON.toJSONString(blogUrls));
    }

}
