package com.fuyin.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuyin.boot.mgb.entity.DebtSolutionContentDO;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.model.vo.*;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.BusinessService;
import com.fuyin.boot.service.DebtService;
import com.fuyin.boot.service.DebtSolutionContentService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: JasperController
 * @Author: 何义祈安
 * @Description: Jasper生成pdf接口
 * @Date: 2022/11/27 17:18
 * @Version: 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/jasper")
public class JasperController {
    @Autowired
    private DebtService debtService;
    @Autowired
    private DebtSolutionContentService dscService;
    @Autowired
    private BusinessService businessService;

    @GetMapping("/test")
    public void createDebtPdf(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        String datetime = str.replace("-", "").replace(" ", "").replace(":","");
        String filename = "pdf_"+datetime+"_content.pdf";

        //引入jasper文件
        Resource resource = new ClassPathResource("templates/DebtPdf/debt222.jasper");
        //文件输入流信息
//        FileInputStream fis = new FileInputStream(resource.getFile());
        InputStream jasperStream = resource.getInputStream();
        //创建JasperPrint，向jasper文件中填充数据
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperStream);
//        response.setCharacterEncoding("GBK");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream os = response.getOutputStream();
        try {
            //获取用户输入的信息
            InputEnterpriceParam enterpriceParam = new InputEnterpriceParam();
            enterpriceParam.setName("广州发展");
            enterpriceParam.setEnterpriseCode("440x8823419980620ff7213");
            enterpriceParam.setReason("该企业的经营能力较差，团队稳定性差，供需不平衡，风险程度高");
            enterpriceParam.setType("以资产清偿债务");
            //从数据库获取更新后的企业指标信息
            ResponseResult debtData = debtService.getDebtData(enterpriceParam);
            Map<String,Object> dataMap = (Map<String, Object>) debtData.getData();
            //获取三表数据
            List<CapacityResultVO> capacity = (List<CapacityResultVO>) dataMap.get("capacity");
            List<ProfitabilityResultVO> profitability = (List<ProfitabilityResultVO>) dataMap.get("profitability");
            List<CashFlowResultVO> cashFlow = (List<CashFlowResultVO>) dataMap.get("cashFlow");
            //获取描述数据
            String debtDesc = (String) dataMap.get("debtDesc");

            Map<String,Object> map = new HashMap();
            map.put("enterpriceName",enterpriceParam.getName());
            //法人代表
            map.put("Legal_representative","龙炫");
            //注册资本
            map.put("registered_capital_left","4,044,113.182万(元)");
            //注册资金
            map.put("registered_capital_right","2039224万");
            //所属行业
            map.put("Industry","新型复合材料");
            //员工人数
            map.put("number_employees","500万");
            //注册地址
            map.put("registered_address","深圳港湾大道其道贸易大厦A座");
            //公司简介
            map.put("profile","创立于1987年，是全球领先的ICT（信息与通信）基础设施和智能终端提供商。目前华为约有19.5万员工，业务遍及170多个国家和地区，服务全球30多亿人口。 华为致力于把数字世界带入每个人、每个家庭、每个组织，构建万物互联的智能世界：让无处不在的联接，成为人人平等的权利，成为智能世界的前提和基础；为世界提供多样性算力，让云无处不在，让智能无所不及；所有的行业和组织，因强大的数字平台而变得敏捷、高效、生机勃勃；通过AI重新定义体验，让消费者在家居、出行、办公、影音娱乐、运动健康等全场景获得极致的个性化智慧体验。");
            //经营范围
            map.put("nature","互联网、钢铁、汽车、食品、教育、石油、外贸");
            //债务重组原因
            map.put("reason",enterpriceParam.getReason());
            //债务重组结果
            map.put("desc",debtDesc);
            //债务重组方式
            map.put("method",enterpriceParam.getType());
            //债务重组实施说明
            LambdaQueryWrapper<DebtSolutionContentDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DebtSolutionContentDO::getMethod,enterpriceParam.getType());
            DebtSolutionContentDO sc = dscService.getOne(wrapper);
            map.put("implementation",sc.getImplementation());
            //债务重组结果预测
            //获取父报表要传递给子报表的list
            map.put("sublist2capa",capacity);
            map.put("sublist2probili",profitability);
            map.put("sublist2cash",cashFlow);
            //获取子报表路径
            Resource subResourceCapa = new ClassPathResource("templates/DebtPdf/capacity2.jasper");
            map.put("subpath2capa",subResourceCapa.getFile().getPath());

            Resource subResourcePro = new ClassPathResource("templates/DebtPdf/profitability2.jasper");
            map.put("subpath2probili",subResourcePro.getFile().getPath());

            Resource subResourceCash = new ClassPathResource("templates/DebtPdf/cash2.jasper");
            map.put("subpath2cash",subResourceCash.getFile().getPath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport,map,new JREmptyDataSource());
            //将JasperPrint以pdf的形式输出
            JasperExportManager.exportReportToPdfStream(print,os);
        } catch (JRException e) {
            e.printStackTrace();
        }
        finally {
            os.flush();
        }
    }

    @GetMapping("/test2")
    public void createBusinessPdf(HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
        //获取文件名
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        String datetime = str.replace("2022-", "").replace("_", ":");
        String filename = "pdf_"+datetime+"_content.pdf";

        //引入jasper文件
        Resource resource = new ClassPathResource("templates/BusinessPdf/main/business2.jasper");
        //文件输入流信息
//        FileInputStream fis = new FileInputStream(resource.getFile());
        InputStream jasperStream = resource.getInputStream();
        //创建JasperPrint，向jasper文件中填充数据
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperStream);
//        response.setCharacterEncoding("GBK");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream os = response.getOutputStream();
        try {
            //获取用户输入的信息
            InputEnterpriceParam enterpriceParam = new InputEnterpriceParam();
            enterpriceParam.setName("众生药业");
            enterpriceParam.setEnterpriseCode("440x8823419980620ff7213");
            enterpriceParam.setReason("该企业的经营能力较差，团队稳定性差，供需不平衡，风险程度高");
            enterpriceParam.setType("以资产清偿债务");
            //从数据库获取更新后的企业指标信息
            ResponseResult businData = businessService.getBusinData(enterpriceParam);
            Map<String,Object> dataMap = (Map<String, Object>) businData.getData();
            //获取三表数据
            List<GrowthResultVO> growth = (List<GrowthResultVO>) dataMap.get("growth");
            List<ValuationResultVO> valuation = (List<ValuationResultVO>) dataMap.get("valuation");
            List<DupontResultVO> dupont = (List<DupontResultVO>) dataMap.get("dupont");

//            Map[] re = new Map[100];
//            JRMapArrayDataSource jrMapArrayDataSource = new JRMapArrayDataSource();
//            List<Map<String,Object>> mapList = new ArrayList<>();

            //子表title中的数据
            Map<String,Object> titleMap = new HashMap();
            titleMap.put("enterpriceName",enterpriceParam.getName());
            //法人代表
            titleMap.put("Legal_representative","龙炫");
            //注册资本
            titleMap.put("registered_capital_left","4,044,113.182万(元)");
            //注册资金
            titleMap.put("registered_capital_right","2039224万");
            //所属行业
            titleMap.put("Industry","新型复合材料");
            //员工人数
            titleMap.put("number_employees","500万");
            //注册地址
            titleMap.put("registered_address","深圳港湾大道其道贸易大厦A座");
            //公司简介
            titleMap.put("profile","创立于1987年，是全球领先的ICT（信息与通信）基础设施和智能终端提供商。目前华为约有19.5万员工，业务遍及170多个国家和地区，服务全球30多亿人口。 华为致力于把数字世界带入每个人、每个家庭、每个组织，构建万物互联的智能世界：让无处不在的联接，成为人人平等的权利，成为智能世界的前提和基础；为世界提供多样性算力，让云无处不在，让智能无所不及；所有的行业和组织，因强大的数字平台而变得敏捷、高效、生机勃勃；通过AI重新定义体验，让消费者在家居、出行、办公、影音娱乐、运动健康等全场景获得极致的个性化智慧体验。");
            //经营范围
            titleMap.put("nature","互联网、钢铁、汽车、食品、教育、石油、外贸");
            //债务重组原因
            titleMap.put("reason",enterpriceParam.getReason());
            //债务重组方式
            titleMap.put("method",enterpriceParam.getType());
            Collection<Map<String, ?>> mapList = new ArrayList<>();
//            JRMapCollectionDataSource jrMapCollectionDataSource = new JRMapCollectionDataSource(mapList);
            mapList.add(titleMap);

            //子表经营能力表数据
            Map<String,Object> oabilityMap = new HashMap();
            oabilityMap.put("requirement","供不应求");
            oabilityMap.put("supply","稳定");
            oabilityMap.put("team_stability","高");
            Collection<Map<String, ?>> mapAbility = new ArrayList<>();
            mapAbility.add(oabilityMap);

            //父表map
            Map<String,Object> businessMap = new HashMap();
            //债务重组实施说明
            LambdaQueryWrapper<DebtSolutionContentDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DebtSolutionContentDO::getMethod,enterpriceParam.getType());
            DebtSolutionContentDO sc = dscService.getOne(wrapper);
//            map.put("implementation",sc.getImplementation());
            //债务重组结果预测
            //获取父报表要传递给子报表的list
            System.out.println(growth);
            System.out.println(valuation);
            System.out.println(dupont);
            businessMap.put("submap2title",titleMap);
            businessMap.put("submap2oability",oabilityMap);
            businessMap.put("sublist2growth",growth);
            businessMap.put("sublist2valuation",valuation);
            businessMap.put("sublist2dupont",dupont);
            businessMap.put("requirement","ceshi");
            //获取子报表路径
            Resource subResourceTitle = new ClassPathResource("templates/BusinessPdf/sub/title2.jasper");
            businessMap.put("subpath2title",subResourceTitle.getFile().getPath());

            Resource subResourceOability = new ClassPathResource("templates/BusinessPdf/sub/oability2.jasper");
            businessMap.put("subpath2oability",subResourceOability.getFile().getPath());

            Resource subResourceGrowth = new ClassPathResource("templates/BusinessPdf/sub/growth/growth2.jasper");
            businessMap.put("subpath2growth",subResourceGrowth.getFile().getPath());

            ClassPathResource growthSub1 = new ClassPathResource("templates/BusinessPdf/sub/growth/SubGrowth/growthSub1.jasper");
            businessMap.put("growthsubpath1",growthSub1.getFile().getPath());

            ClassPathResource growthSub2 = new ClassPathResource("templates/BusinessPdf/sub/growth/SubGrowth/growthSub2.jasper");
            businessMap.put("growthsubpath2",growthSub2.getFile().getPath());

            Resource subResourceValuation = new ClassPathResource("templates/BusinessPdf/sub/valuation2.jasper");
            businessMap.put("subpath2valuation",subResourceValuation.getFile().getPath());

            Resource subResourceDupont = new ClassPathResource("templates/BusinessPdf/sub/dupont2.jasper");
            businessMap.put("subpath2dupont",subResourceDupont.getFile().getPath());
//            JRMapCollectionDataSource jrMapCollectionDataSource = new JRMapCollectionDataSource();
//            Map<Object, Object> objectObjectMap = new Map<>();
            JasperPrint print = JasperFillManager.fillReport(jasperReport,businessMap,new JREmptyDataSource());
            //将JasperPrint以pdf的形式输出
            JasperExportManager.exportReportToPdfStream(print,os);
        } catch (JRException e) {
            e.printStackTrace();
        }
        finally {
            os.flush();
        }
    }



}
