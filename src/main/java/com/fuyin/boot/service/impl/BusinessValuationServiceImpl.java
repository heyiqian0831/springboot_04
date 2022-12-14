package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.BusinessValuationDO;
import com.fuyin.boot.mgb.mapper.BusinessValuationMapper;
import com.fuyin.boot.service.BusinessValuationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: BusinessValuationServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/16 21:36
 * @Version: 1.0
 */
@Service
public class BusinessValuationServiceImpl extends ServiceImpl<BusinessValuationMapper,BusinessValuationDO>
        implements BusinessValuationService {

}
