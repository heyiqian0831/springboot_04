package com.fuyin.boot.mgb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * @author 何义祈安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("operation_ability")
public class OperationAbilityDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String requirementAnalysis;

    private Integer raScore;

    private String supplyAnalysis;

    private Integer saScore;

    private String teamStabilityAnalysis;

    private Integer tsaScore;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirementAnalysis() {
        return requirementAnalysis;
    }

    public void setRequirementAnalysis(String requirementAnalysis) {
        this.requirementAnalysis = requirementAnalysis;
    }

    public Integer getRaScore() {
        return raScore;
    }

    public void setRaScore(Integer raScore) {
        this.raScore = raScore;
    }

    public String getSupplyAnalysis() {
        return supplyAnalysis;
    }

    public void setSupplyAnalysis(String supplyAnalysis) {
        this.supplyAnalysis = supplyAnalysis;
    }

    public Integer getSaScore() {
        return saScore;
    }

    public void setSaScore(Integer saScore) {
        this.saScore = saScore;
    }

    public String getTeamStabilityAnalysis() {
        return teamStabilityAnalysis;
    }

    public void setTeamStabilityAnalysis(String teamStabilityAnalysis) {
        this.teamStabilityAnalysis = teamStabilityAnalysis;
    }

    public Integer getTsaScore() {
        return tsaScore;
    }

    public void setTsaScore(Integer tsaScore) {
        this.tsaScore = tsaScore;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", requirementAnalysis=").append(requirementAnalysis);
        sb.append(", raScore=").append(raScore);
        sb.append(", supplyAnalysis=").append(supplyAnalysis);
        sb.append(", saScore=").append(saScore);
        sb.append(", teamStabilityAnalysis=").append(teamStabilityAnalysis);
        sb.append(", tsaScore=").append(tsaScore);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public Integer operationAbilityToal(Integer raScore, Integer saScore, Integer tsaScore) {
        this.raScore = raScore;
        this.saScore = saScore;
        this.tsaScore = tsaScore;
        return raScore+saScore+tsaScore;
    }
}
