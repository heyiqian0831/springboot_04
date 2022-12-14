package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.CapacityIndicatorsDO;
import com.fuyin.boot.mgb.mapper.CapacityIndicatorsMapper;
import com.fuyin.boot.service.CapacityIndicatorsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: CapacityIndicatorsServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 15:51
 * @Version: 1.0
 */
@Service
public class CapacityIndicatorsServiceImpl extends ServiceImpl<CapacityIndicatorsMapper,CapacityIndicatorsDO>
        implements CapacityIndicatorsService {

}
