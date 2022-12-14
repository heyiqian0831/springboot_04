package com.fuyin.boot.spider.pipeline;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtCashFlowDO;
import com.fuyin.boot.mgb.entity.DebtProfitabilityDO;
import com.fuyin.boot.mgb.mapper.DebtCapacityMapper;
import com.fuyin.boot.mgb.mapper.DebtCashFlowMapper;
import com.fuyin.boot.mgb.mapper.DebtProfitabilityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.*;

/**
 * @ClassName: DebtCapacityPipeline
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/7 15:25
 * @Version: 1.0
 */
@Component
public class DebtAllPipeline implements Pipeline {
    @Autowired
    private DebtCapacityMapper debtCapacityMapper;
    @Autowired
    private DebtProfitabilityMapper debtProfitabilityMapper;
    @Autowired
    private DebtCashFlowMapper debtCashFlowMapper;

    DebtCapacityDO debtCapacityDO = new DebtCapacityDO();
    DebtProfitabilityDO debtProfitabilityDO = new DebtProfitabilityDO();
    DebtCashFlowDO debtCashFlowDO = new DebtCashFlowDO();

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> resultItemsAll = resultItems.getAll();
        Set<String> keys = resultItemsAll.keySet();
        for (String key : keys) {
            if("debtCapacityDO".equals(key)){
                debtCapacityDO = resultItems.get("debtCapacityDO");
                //获取股票名称和股票时间，
                String name = debtCapacityDO.getName();
                Date date = debtCapacityDO.getDate();
                //在数据库中查询该时间的股票信息是否已存在
                LambdaQueryWrapper<DebtCapacityDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DebtCapacityDO::getName,name);
                wrapper.eq(DebtCapacityDO::getDate,date);
                List<DebtCapacityDO> debtCapacityDOS = debtCapacityMapper.selectList(wrapper);
                if (debtCapacityDOS.size() > 1) {
                    throw new RuntimeException("数据未找到||数据错误");
                }
                //如果不存在，就新增数据
                if(CollectionUtils.isEmpty(debtCapacityDOS)){
                    int insert = debtCapacityMapper.insert(debtCapacityDO);
                    System.out.println(insert);
                }else{
                    //如果存在，就通过id更新，不能用条件更新，避免刚好爬取到的更新条件和数据库中的不符合
                    Long id = debtCapacityDOS.get(0).getId();
                    UpdateWrapper<DebtCapacityDO> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("id",id);
                    int update = debtCapacityMapper.update(debtCapacityDO, updateWrapper);
                    System.out.println(update);
                }
            }if("debtProfitabilityDO".equals(key)){
                debtProfitabilityDO = resultItems.get("debtProfitabilityDO");
                //获取股票名称和股票时间，
                String name = debtProfitabilityDO.getName();
                Date date = debtProfitabilityDO.getDate();
                //在数据库中查询该时间的股票信息是否已存在
                LambdaQueryWrapper<DebtProfitabilityDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DebtProfitabilityDO::getName,name);
                wrapper.eq(DebtProfitabilityDO::getDate,date);
                List<DebtProfitabilityDO> debtProfitabilityDOS = debtProfitabilityMapper.selectList(wrapper);
                if (debtProfitabilityDOS.size() > 1) {
                    throw new RuntimeException("数据未找到||数据错误");
                }
                //如果不存在，就新增数据
                if(CollectionUtils.isEmpty(debtProfitabilityDOS)){
                    int insert = debtProfitabilityMapper.insert(debtProfitabilityDO);
                    System.out.println(insert);
                }else{
                    //如果存在，就通过id更新，不能用条件更新，避免刚好爬取到的更新条件和数据库中的不符合
                    Long id = debtProfitabilityDOS.get(0).getId();
                    UpdateWrapper<DebtProfitabilityDO> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("id",id);
                    int update = debtProfitabilityMapper.update(debtProfitabilityDO, updateWrapper);
                    System.out.println(update);
                }
            }else if("debtCashFlowDO".equals(key)){
                debtCashFlowDO = resultItems.get("debtCashFlowDO");
                //获取股票名称和股票时间，
                String name = debtCashFlowDO.getName();
                Date date = debtCashFlowDO.getDate();
                //在数据库中查询该时间的股票信息是否已存在
                LambdaQueryWrapper<DebtCashFlowDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DebtCashFlowDO::getName,name);
                wrapper.eq(DebtCashFlowDO::getDate,date);
                List<DebtCashFlowDO> debtCashFlowDOS = debtCashFlowMapper.selectList(wrapper);
                if (debtCashFlowDOS.size() > 1) {
                    throw new RuntimeException("数据未找到||数据错误");
                }
                //如果不存在，就新增数据
                if(CollectionUtils.isEmpty(debtCashFlowDOS)){
                    int insert = debtCashFlowMapper.insert(debtCashFlowDO);
                    System.out.println(insert);
                }else{
                    //如果存在，就通过id更新，不能用条件更新，避免刚好爬取到的更新条件和数据库中的不符合
                    Long id = debtCashFlowDOS.get(0).getId();
                    UpdateWrapper<DebtCashFlowDO> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("id",id);
                    int update = debtCashFlowMapper.update(debtCashFlowDO, updateWrapper);
                    System.out.println(update);
                }
            }
        }
    }

}
