package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.DebtToCapitalDO;
import com.fuyin.boot.mgb.mapper.DebtToCapitalMapper;
import com.fuyin.boot.service.DebtToCapitalService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: DebtToCapitalServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 21:52
 * @Version: 1.0
 */
@Service
public class DebtToCapitalServiceImpl extends ServiceImpl<DebtToCapitalMapper,DebtToCapitalDO>
        implements DebtToCapitalService {
}
