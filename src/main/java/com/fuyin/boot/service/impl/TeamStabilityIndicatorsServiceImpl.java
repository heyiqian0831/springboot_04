package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.TeamStabilityIndicatorsDO;
import com.fuyin.boot.mgb.mapper.TeamStabilityIndicatorsMapper;
import com.fuyin.boot.service.TeamStabilityIndicatorsService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * @ClassName: TeamStabilityIndicatorsServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 18:05
 * @Version: 1.0
 */
@Service
public class TeamStabilityIndicatorsServiceImpl extends ServiceImpl<TeamStabilityIndicatorsMapper,TeamStabilityIndicatorsDO>
        implements TeamStabilityIndicatorsService {
}
