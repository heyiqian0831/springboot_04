package com.fuyin.boot.spider.pipeline;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuyin.boot.mgb.entity.DetailUrlDO;
import com.fuyin.boot.mgb.mapper.DetailUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @ClassName: GetDetailPagePipeline
 * @Author: 何义祈安
 * @Description: GetDetailPageProcessor的自定义存储
 * @Date: 2022/11/7 23:54
 * @Version: 1.0
 */
@Component
public class GetDetailPagePipeline implements Pipeline {
    @Autowired
    private DetailUrlMapper detailUrlMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取resultItems中的url
        Map<String,Object> map = resultItems.get("result");
        if(map!= null) {
            String name = String.valueOf(map.get("enterpriseName"));
            String url = String.valueOf(map.get("targetUrl"));
            String type = String.valueOf(map.get("stockType"));
            //判断url，如果不是详情页
            String pattern = ".*eastmoney.com/.*";
            if (!Pattern.matches(pattern, url)) {
                throw new RuntimeException("爬取url错误");
            }
            putInDatabases(name,url,type);
        }else {
            //TODO 换成日志
            System.out.println("没有map");
        }
    }
    /**
    *@Description 存到数据库
    *
    *@Param [name, url]
    *@return void
    **/
    private void putInDatabases(String name, String url,String type) {
        DetailUrlDO detailUrlDO = new DetailUrlDO();
        detailUrlDO.setName(name);
        detailUrlDO.setUrl(url);
        detailUrlDO.setType(type);

        //存储到数据库
        LambdaQueryWrapper<DetailUrlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetailUrlDO::getName,name);
        DetailUrlDO detailUrl = detailUrlMapper.selectOne(wrapper);
        if(Objects.isNull(detailUrl)){
            int insert = detailUrlMapper.insert(detailUrlDO);
            System.out.println("url是否存入数据库:" + insert);
        }else{
            detailUrlMapper.update(detailUrlDO,wrapper);
        }

    }
}
