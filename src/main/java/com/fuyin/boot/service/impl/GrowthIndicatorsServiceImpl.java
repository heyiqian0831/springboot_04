package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.GrowthIndicatorsDO;
import com.fuyin.boot.mgb.mapper.GrowthIndicatorsMapper;
import com.fuyin.boot.service.GrowthIndicatorsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: GrowthIndicatorsServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 18:07
 * @Version: 1.0
 */
@Service
public class GrowthIndicatorsServiceImpl extends ServiceImpl<GrowthIndicatorsMapper,GrowthIndicatorsDO>
        implements GrowthIndicatorsService {
}
