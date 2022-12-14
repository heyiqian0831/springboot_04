package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.DebtSolutionContentDO;
import com.fuyin.boot.mgb.mapper.DebtSolutionContentMapper;
import com.fuyin.boot.service.DebtSolutionContentService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: DebtSolutionContentServiceImpl
 * @Author: 何义祈安
 * @Description: 债务重组内容管理
 * @Date: 2022/11/29 12:22
 * @Version: 1.0
 */
@Service
public class DebtSolutionContentServiceImpl extends ServiceImpl<DebtSolutionContentMapper,DebtSolutionContentDO>
        implements DebtSolutionContentService {

}
