package com.fuyin.boot.spider.pipeline;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuyin.boot.mgb.entity.BusinessDupontDO;
import com.fuyin.boot.mgb.entity.BusinessGrowthDO;
import com.fuyin.boot.mgb.entity.BusinessValuationDO;
import com.fuyin.boot.mgb.mapper.BusinessDupontMapper;
import com.fuyin.boot.mgb.mapper.BusinessGrowthMapper;
import com.fuyin.boot.mgb.mapper.BusinessValuationMapper;
import com.fuyin.boot.service.BusinessDupontService;
import com.fuyin.boot.service.BusinessGrowService;
import com.fuyin.boot.service.BusinessValuationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: BusinessAllPipeline
 * @Author: 何义祈安
 * @Description: 业务重组指标表存储
 * @Date: 2022/11/14 21:35
 * @Version: 1.0
 */
@Component
public class BusinessAllPipeline implements Pipeline {
    @Autowired
    private BusinessGrowthMapper growthComMapper;
    @Autowired
    private BusinessValuationMapper valuaComMapper;
    @Autowired
    private BusinessDupontMapper dupontMapper;
    @Autowired
    private BusinessGrowService growService;
    @Autowired
    private BusinessValuationService valuationService;
    @Autowired
    private BusinessDupontService dupontService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> all = resultItems.getAll();
        BusinessGrowthDO growComparison = (BusinessGrowthDO) all.get("growComparison");
        BusinessGrowthDO growthAverage = (BusinessGrowthDO) all.get("growthAverage");

        BusinessValuationDO valuaComparison = (BusinessValuationDO) all.get("valuaComparison");
        BusinessValuationDO valuationAverage = (BusinessValuationDO) all.get("valuationAverage");

        BusinessDupontDO dupont = (BusinessDupontDO) all.get("dupont");
        BusinessDupontDO dupontAverage = (BusinessDupontDO) all.get("dupontAverage");


        if(!Objects.isNull(growComparison)){
            //查是否存在,通过股票代码来判断而不是名字，因为股票代码有唯一性
            LambdaQueryWrapper<BusinessGrowthDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BusinessGrowthDO::getName,growComparison.getName());
            List<BusinessGrowthDO> businessGrowths = growthComMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(businessGrowths)){
                //存储成长性指标数据
                growthComMapper.insert(growComparison);
            }else{
                growthComMapper.update(growComparison,wrapper);
            }
            LambdaQueryWrapper<BusinessGrowthDO> wrapperAve = new LambdaQueryWrapper<>();
            wrapperAve.eq(BusinessGrowthDO::getName,growthAverage.getName());
            growService.saveOrUpdate(growthAverage,wrapperAve);
        }
        if(!Objects.isNull(valuaComparison)){
            //存储成长性指标数据
            LambdaQueryWrapper<BusinessValuationDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BusinessValuationDO::getName,valuaComparison.getName());
            List<BusinessValuationDO> valueComs = valuaComMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(valueComs)){
                //存储成长性指标数据
                valuaComMapper.insert(valuaComparison);
            }else{
                valuaComMapper.update(valuaComparison,wrapper);
            }
            LambdaQueryWrapper<BusinessValuationDO> wrapperAve = new LambdaQueryWrapper<>();
            wrapperAve.eq(BusinessValuationDO::getName,valuationAverage.getName());
            valuationService.saveOrUpdate(valuationAverage,wrapperAve);
        }
        if(!Objects.isNull(dupont)){
            //存储成长性指标数据
            LambdaQueryWrapper<BusinessDupontDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BusinessDupontDO::getName,dupont.getName());
            List<BusinessDupontDO> duponts = dupontMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(duponts)){
                //存储成长性指标数据
                dupontMapper.insert(dupont);
            }else{
                dupontMapper.update(dupont,wrapper);
            }
            LambdaQueryWrapper<BusinessDupontDO> wrapperAve = new LambdaQueryWrapper<>();
            wrapperAve.eq(BusinessDupontDO::getName,dupontAverage.getName());
            dupontService.saveOrUpdate(dupontAverage,wrapperAve);
        }
    }
}
