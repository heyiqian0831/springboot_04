package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.BusinessSolutionContentDO;
import com.fuyin.boot.mgb.mapper.BusinessSolutionContentMapper;
import com.fuyin.boot.service.BusinessSolutionContentService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: BusinessSolutionContentServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 22:22
 * @Version: 1.0
 */
@Service
public class BusinessSolutionContentServiceImpl extends ServiceImpl<BusinessSolutionContentMapper,BusinessSolutionContentDO>
        implements BusinessSolutionContentService {
}
