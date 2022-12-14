package com.fuyin.boot.spider.pipeline;

import com.fuyin.boot.mgb.entity.DebtDiagnosisDO;
import com.fuyin.boot.mgb.mapper.DebtDiagnosisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @ClassName DebtPipeline
 * @Description TODO
 * @author 何义祈安
 * @Date 2022/10/12 21:57
 * @Version 1.0
 */
@Component
public class DebtPipeline implements Pipeline {

    @Autowired
    private DebtDiagnosisMapper debtDiagnosisMapper;

    /**
    *@Param [resultItems, task]
    **/
    @Override
    public void process(ResultItems resultItems, Task task) {

        //从ResultItems中取出数据
        DebtDiagnosisDO debtDiagnosisDO = resultItems.get("debtDiagnosisDO");

        if(debtDiagnosisDO != null){
            int insert = this.debtDiagnosisMapper.insert(debtDiagnosisDO);
            System.out.println(insert);

        }
    }

    public void processtest(DebtDiagnosisDO debtDiagnosisDO){
        if(debtDiagnosisDO != null){
            int insert = this.debtDiagnosisMapper.insert(debtDiagnosisDO);
            System.out.println(insert);

        }
    }
}
