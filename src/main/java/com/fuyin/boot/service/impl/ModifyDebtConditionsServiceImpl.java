package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.ModifyDebtConditionsDO;
import com.fuyin.boot.mgb.mapper.ModifyDebtConditionsMapper;
import com.fuyin.boot.service.ModifyDebtConditionsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: ModifyDebtConditionsServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 21:53
 * @Version: 1.0
 */
@Service
public class ModifyDebtConditionsServiceImpl extends ServiceImpl<ModifyDebtConditionsMapper,ModifyDebtConditionsDO>
        implements ModifyDebtConditionsService {
}
