package cn.w2cl.webMagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * 进行页面解析的PageProcessor的实现类
 * @Authror 卫骏
 * @Date 2020/1/21 15:40
 */
public class JobProcessor implements PageProcessor {
    public void process(Page page) {
        //使用选择器获取到._22B8YPpa0q_CmMi1q6bhTY这个class的所有元素
        //三种方式：
        //1、css选择器
        List<String> all = page.getHtml().css(".hotnews ul li").all();
        page.putField("div",all);
        //2、XPath
        page.putField("div2",page.getHtml().xpath("//div[@id=pane-news]/div/ul/li[@class=hdline0]/strong/a"));
        //3、正则表达式
        page.putField("div3",page.getHtml().$(".hotnews ul li").regex(".*习近平.*").all());

        //获取一条数据
        page.putField("div4",page.getHtml().xpath("//div[@id=pane-news]/div/ul/li").get());
        page.putField("div5",page.getHtml().xpath("//div[@id=pane-news]/div/ul/li").toString());

        //抽取链接继续爬取
        page.addTargetRequests(page.getHtml().css(".hotnews ul li").links().all());
        page.putField("url",page.getHtml().xpath("//div[@class=cnt_bd]/h1"));
    }

    //site是对爬虫进行的一些配置
    private Site site = Site.me()
            .setCharset("utf8") //设置字符集编码
            .setTimeOut(10000) //设置超时时间 10秒 单位是毫秒
            .setRetrySleepTime(3000) //设置重试时间为3秒，默认值是1000毫秒
            .setSleepTime(3) //设置重试次数
            ;
    public Site getSite() {
        return this.site;
    }

    /**
     * 爬取京东快报页面的类型数据
     * @param args
     */
    public static void main(String[] args) {
        Spider.create(new JobProcessor())
                .addUrl("https://news.baidu.com/") //添加要解析的页面地址
                .addPipeline(new FilePipeline("C:\\Users\\admin123\\Desktop\\新建文件夹")) //pipeline默认输出是consolePipeline，而FilePipeline是输出到文件中
                .thread(4)// 开启4个线程运行爬虫
                .run(); //运行爬虫
    }
}
