package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.AvailabilityIndicatorsDO;
import com.fuyin.boot.mgb.mapper.AvailabilityIndicatorsMapper;
import com.fuyin.boot.service.AvailabilityIndicatorsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: AvailabilityIndicatorsServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 18:03
 * @Version: 1.0
 */
@Service
public class AvailabilityIndicatorsServiceImpl extends ServiceImpl<AvailabilityIndicatorsMapper,AvailabilityIndicatorsDO> implements AvailabilityIndicatorsService {
}
