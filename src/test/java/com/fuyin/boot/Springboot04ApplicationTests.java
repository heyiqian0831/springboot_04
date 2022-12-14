package com.fuyin.boot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuyin.boot.controller.RoleController;
import com.fuyin.boot.domain.LoginAdmin;
import com.fuyin.boot.enums.Roles;
import com.fuyin.boot.mgb.entity.*;
import com.fuyin.boot.mgb.mapper.*;
import com.fuyin.boot.model.domain.RoleResource;
import com.fuyin.boot.model.domain.UserAndRoles;
import com.fuyin.boot.model.param.*;
import com.fuyin.boot.model.vo.CapacityResultVO;
import com.fuyin.boot.model.vo.CashFlowResultVO;
import com.fuyin.boot.model.vo.DebtResultDescVO;
import com.fuyin.boot.model.vo.ProfitabilityResultVO;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.*;
import com.fuyin.boot.spider.executor.*;
import com.fuyin.boot.spider.pipeline.DebtPipeline;
import com.fuyin.boot.spider.processor.DebtProcessor;
import com.fuyin.boot.utils.DebtConvertorUtils;
import com.fuyin.boot.utils.DebtReversalUtils;
import com.fuyin.boot.utils.JwtTokenUtil;
import com.fuyin.boot.utils.NumberUtils;
import com.google.common.base.Function;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class Springboot04ApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private DebtDiagnosisMapper debtDiagnosisMapper;
    @Autowired
    private DebtProcessor debtProcessor;
    @Autowired
    private DebtPipeline debtPipeline;
    @Autowired
    private DebtSeleniumExecutor debtSeleniumExecutor;
    @Autowired
    private NumberUtils numberUtils;
    @Autowired
    private DebtAllExecutor debtCapacityCrawlExecutor;
    @Autowired
    private GetDetailPageExecutor getDetailPageCrawlExecutor;
    @Value("${indexUrl.SearchHome}")
    private String indexUrl;
    @Autowired
    private DetailUrlMapper detailUrlMapper;
    @Autowired
    private BusinRestructureExecutor restructureExecutor;
    @Autowired
    private DebtService debtService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private BusinessGrowService growService;
    @Autowired
    private BusinessGrowthMapper growthMapper;
    @Autowired
    private RoleController roleController;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleResourceRelationService roleResource;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DebtCapacityMapper debtCapacityMapper;
    @Autowired
    private DebtConvertorUtils debtConvertorUtils;
    @Autowired
    private DebtProfitabilityMapper profitabilityMapper;
    @Autowired
    private DebtCashFlowMapper cashFlowMapper;


    //??????mapper????????????
    @Test
    void MapperTest() {
        //???????????????????????????BaseMapper?????????
//        List<UserDO> users = userMapper.selectList(null);
//        users.forEach(userDO -> System.out.println(userDO));
//        System.out.println("--------------------------");
//        //??????mbg????????????
//        UserDOExample userDOExample = new UserDOExample();
//        userDOExample.createCriteria().andPhoneNumberEqualTo("123456");
//        List<UserDO> userDOS = userMapper.selectByExample(userDOExample);
//        System.out.println(userDOS.get(0));
//        System.out.println("--------------------------");
//        List<String> list = new ArrayList<>();
//        list.add("hello");
//        System.out.println(list);
//        System.out.println("-----------------------------");
//        List<String> list2 = new ArrayList<>(Arrays.asList("test"));
//        System.out.println(list2);
        List<UserDO> userDOS = userMapper.selectList(null);
        userDOS.forEach(System.out::println);
    }

    //??????mybatis???mybatisPlus???????????????????????????????????????
    @Test
    public void testMybatisAndMybatisPlusRepeat(){
        AdminDO adminDO = new AdminDO();
        adminDO.setUsername("admin4");
        adminDO.setPassword("xxx");
        adminDO.setNickname("???????????????");
        adminDO.setStatus(1);
        adminMapper.insert(adminDO);
    }

    //????????????????????????
    @Test
    public void testByPhoneNumber(){
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("phone_Number","110");
        List<UserDO> userDOS = userMapper.selectList(wrapper);
        userDOS.forEach(System.out::println);
    }

    //????????????????????????
    @Test
    public void testSelectAuthorise(){
//        List<String> list = resourceMapper.selectNameByUserPrimaryKey(1L);
//        list.forEach(System.out::println);
        List<RoleResource> resourceByRoleId = resourceMapper.getResourceByRoleId(101L);
        System.out.println("----");
        System.out.println(resourceByRoleId);
        for (RoleResource resource : resourceByRoleId) {
            System.out.println(resource.getRoleDelFlag()+resource.getResourceDesc());
        }
        System.out.println(resourceByRoleId.get(0));
    }

    //??????????????????????????????
    @Test
    public void testInsertCommonUserRoleRelation(){
        int i = userRoleRelationMapper.insertCommonUserRoleRelation(4L);
        System.out.println(i);
    }

    //???????????????????????????
    @Test
    public void testSelectAdminResourceName(){
        List<String> list = resourceMapper.selectNameByAdminPrimaryKey(1L);
        list.forEach(System.out::println);
    }

    @Test
    public void testselectPermsByUserId(){
//        List<String> list = menuMapper.selectPermsByUserId(1L);
//        System.out.println(list);
//        ???????????????????????????????????????????????????????????????????????????????????????
//        List<AdminDO> adminDOS = adminMapper.selectList(null);
//        adminDOS.forEach(adminDO -> System.out.println(adminDO));
//
//        ???????????????????????????
//        UserRoleRelationDOExample userRoleRelationDOExample = new UserRoleRelationDOExample();
//        userRoleRelationDOExample.createCriteria().andUserIdEqualTo(1L);
//        List<UserRoleRelationDO> userRoleRelationDOS = userRoleRelationMapper.selectByExample(userRoleRelationDOExample);
//        userRoleRelationDOS.forEach(System.out::println);
//
//        ????????????????????????????????????????????????
//        List<String> lists = resourceMapper.selectNameByPrimaryKey(1L);
//        lists.forEach(System.out::println);
    }

    //????????????????????????passwordEncode??????????????????
    //TODO ????????????????????????????????????BCryptPasswordEncoder!!
    @Test
    public void testBCryptPasswordEncoder(){
        String s1 = passwordEncoder.encode("333");
        System.out.println(s1);
    }

    //??????updateNicknamePhoneNumber??????
    @Test
    public void testUpdateNicknamePhoneNumber(){
        UserDO userDO = new UserDO();
        userDO.setId(8L);
//        userDO.setNickname("????????????");
        userDO.setPhoneNumber("130");
        userService.updateUsernamePhoneNumber(userDO);
    }

    //??????updateNicknamePhoneNumberSet??????
    @Test
    public void testUpdateNicknamePhoneNumberSet(){
        UserDO userDO = new UserDO();
        userDO.setId(9L);
        userDO.setUsername("????????????");
        userDO.setPhoneNumber("140");
        userService.updateUsernamePhoneNumberSet(userDO);
    }

    //??????selectList????????????????????????????????????
//    ==>  Preparing: SELECT id,username,password,nickname,phone_number,email,status,last_ip,last_login_time,gmt_create,gmt_modified,version,del_flag
//                    FROM user
//                    WHERE del_flag=0
//    ==> Parameters:
    @Test
    public void testSelectListWrapper(){
        List<UserDO> userDOS = userMapper.selectList(null);
        userDOS.forEach(System.out::println);
    }

    //??????wealthem???token?????????
//    @Test
//    public void testJWTFromWealthem(){
//        LoginUser loginUser = new LoginUser();
//        String token = jwtTokenUtil.generateToken(loginUser, true, 1L, new Date());
//        System.out.println(token);
//    }

    //??????sgjwt,???????
//    @Test
//    public void testsgJWT(){
//        LoginUser loginUser = new LoginUser();
//        Map<String,Object> map = new HashMap<>();
//        map.put("CLAIM_KEY_USERID",1);
//        Boolean isAdmin = true;
//        map.put("CLAIM_KEY_IS_ADMIN",isAdmin);
//        map.put("CLAIM_KEY_USERNAME",loginUser);
//        System.out.println("-----------------");
//        try {
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //??????????????????????????????????????????????????????????????????????????????????????????????????????
    //==>  Preparing: UPDATE user SET last_login_time=?, gmt_modified=? WHERE del_flag=0 AND (id = ?)
    //==> Parameters: 2022-09-18 16:10:26.667(Timestamp), 2022-09-18 16:10:26.734(Timestamp), 9(Long)
    //       <==    Updates: 1
    @Test
    public void testUpdateLastLoginTime(){
        UserDO userDO = userMapper.selectById(9);
        int i = loginService.setUserLastLoginTime(userDO);
        System.out.println(i);

    }

    //?????????????????????????????????????????????????????????????????????????????????????????????????????????
//    ==>  Preparing: UPDATE admin SET last_login_time=?, gmt_modified=? WHERE del_flag=0 AND (id = ?)
//    ==> Parameters: 2022-09-18 16:24:14.524(Timestamp), 2022-09-18 16:24:14.599(Timestamp), 4(Long)
//    <==    Updates: 1
    @Test
    public void testUpdateLastLoginTimeAdmin(){
        AdminDO adminDO = new AdminDO();
        adminDO.setId(4L);
        adminService.setAdminLastLoginTime(adminDO);

    }

    //??????????????????(??????????????????)
    @Test
    public void testMetaObjectHandler(){
        UserDO userDO = new UserDO();
        userDO.setId(10L);
        userDO.setPhoneNumber("sddg");
        userDO.setPassword("sdfsga");
        int insert = userMapper.insert(userDO);
        userMapper.updateById(userDO);
    }
    //??????????????????(??????????????????),
    //???????????????Could not set property 'gmtModified' of 'class com.fuyin.boot.mgb.entity.UserDO' with value '2022-09-18T15:29:34.169' Cause: java.lang.IllegalArgumentException: argument type mismatch
    //gmtModified???????????????????????????????????????????????????????????????????????????????????????LocalDateTime????????????????????????????????????Date??????????????????????????????????????????????????????LocalDateTime????????????????????????????????????
    @Test
    public void testMetaObjectHandler2(){
        AdminDO adminDO = new AdminDO();
        adminDO.setUsername("zing22");
        adminDO.setNickname("zin22g");
        adminDO.setPassword("zi22ng");
        adminDO.setStatus(1);
        int insert = adminMapper.insert(adminDO);
        adminMapper.updateById(adminDO);
    }

    //?????????????????????????????????
    @Test
    public void testgetOneByUsernameAndPhoneNumber(){
        UsernameAndPhoneNumberParam usernameAndPhoneNumberParam = new UsernameAndPhoneNumberParam();
//        nicknameAndPhoneNumber.setNickname("????????????");
        usernameAndPhoneNumberParam.setPhoneNumber("1400");
        ResponseResult result = adminService.getOneByUsernameAndPhoneNumber(usernameAndPhoneNumberParam);
        Map<String,Object> map = new HashMap<>();
        map = (Map<String, Object>) result.getData();
        UserDO user = (UserDO) map.get("user");
        System.out.println(user);
    }

    //??????JWT,ok
    @Test
    public void testJwt(){
        LambdaQueryWrapper<AdminDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminDO::getPhoneNumber,"g110");
        List<AdminDO> adminDOS = adminMapper.selectList(wrapper);
        AdminDO adminDO = adminDOS.get(0);
        List<String> list = resourceMapper.selectNameByAdminPrimaryKey(adminDO.getId());
        LoginAdmin loginAdmin = new LoginAdmin(adminDO,list);
        String token = jwtTokenUtil.generateToken(
                loginAdmin,true,adminDO.getId(),adminDO.getLastLoginTime());
        System.out.println(token);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        Boolean isAdmin = jwtTokenUtil.getIsAdminFromToken(token);
        System.out.println(userId);
        System.out.println(isAdmin);
    }

    //??????LoginService.Login
    @Test
    public void testLoginService(){
        UserLoginParam userDO = new UserLoginParam();
        userDO.setPhoneNumber("110");
        userDO.setPassword("111");
//        ResponseResult login = userService.login(userDO, );
//        Map<String,Object> map = (Map<String, Object>) login.getData();
//        System.out.println(map.get("??"));
    }
    @Test
    public void testLoginServiceTest(){
//        String test = loginService.test();
//        System.out.println(test);
    }

    //??????????????????
//    ==>  Preparing: UPDATE user SET del_flag=1 WHERE id=? AND del_flag=0
//    ==> Parameters: 7(Long)
//    <==    Updates: 1
    @Test
    public void deleteLuoji(){
//        int i = userMapper.deleteById(7L);
//        System.out.println(i);
//        adminService.deleteUser("7");
        UserDO userDO = new UserDO();
        userDO.setUsername("userTestFordelflag2");
        userDO.setPhoneNumber("userTestFordelflag2");
        userDO.setPassword("test2");
        userMapper.insert(userDO);

        RoleDO role = new RoleDO();
        role.setName("testFordelflag2");
        role.setDescription("testFordelflag2");
        roleMapper.insert(role);
    }

    //??????DebtDiagnosisMapper
    @Test
    public void testDebtDiagnosisMapper(){
        DebtDiagnosisDO debtDiagnosisDO = new DebtDiagnosisDO();
        debtDiagnosisDO.setName("efgjfgj");
        debtDiagnosisMapper.insert(debtDiagnosisDO);
    }

    //??????DebtProcessor
    @Test
    public void testDebtProcessor(){
        Request request = new Request("https://emweb.securities.eastmoney.com/PC_HSF10/NewFinanceAnalysis/ZYZBAjaxNew?type=1&code=SZ003023");
//        setGetHeaders(request);
        Spider.create(debtProcessor)
                .addRequest(request)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(
                                new BloomFilterDuplicateRemover(100000000)))
                .addPipeline(debtPipeline)
//                .addUrl("https://data.eastmoney.com/stockdata/003023.html")
                .thread(5)
                .run();

    }

    @Test
    public void testDebtSeleniumExecutor(){
        debtSeleniumExecutor.doCrawler();
    }

    //??????debtCapacityCrawlExecutor???  11.14??????
    //?????????????????????????????????
    @Value("${indexUrl.DebtDetails}")
    private String caihongjituan;
    //?????????????????????????????????
    private String haitiantianxian = "https://emweb.securities.eastmoney.com/PC_HKF10/FinancialAnalysis/index?type=web&code=08227&color=b";
    //?????????????????????????????????
    private String alibaba = "http://emweb.eastmoney.com/pc_usf10/FinancialAnalysis/index?color=web&code=BABA";
    private String AB="ABStock";
    private String HK="HKStock";
    private String US="USStock";
    //AB
    private String a = "https://emweb.securities.eastmoney.com/PC_HSF10/NewFinanceAnalysis/Index?type=web&code=sz000721";
    private String aa = "https://emweb.securities.eastmoney.com/PC_HSF10/IndustryAnalysis/Index?type=web&code=SZ002317#";
    private String aaa = "https://emweb.securities.eastmoney.com/PC_HSF10/IndustryAnalysis/Index?type=web&code=SZ003023#";
    //HK
    private String b = "https://emweb.securities.eastmoney.com/PC_HKF10/pages/home/index.html?code=01996&type=web&color=w#/newfinancialanalysis";
    //US
    private String c = "http://emweb.eastmoney.com/pc_usf10/FinancialAnalysis/index?color=web&code=XOM";
    @Test
    public void testDebtCapacitycrawl(){
        debtCapacityCrawlExecutor.doCrawl(a,AB,"capacity");
        debtCapacityCrawlExecutor.doCrawl(b,HK,"capacity");
        debtCapacityCrawlExecutor.doCrawl(c,US,"capacity");

        debtCapacityCrawlExecutor.doCrawl(a,AB,"profitability");
        debtCapacityCrawlExecutor.doCrawl(b,HK,"profitability");
        debtCapacityCrawlExecutor.doCrawl(c,US,"profitability");

        debtCapacityCrawlExecutor.doCrawl(a,AB,"cashFlow");
        debtCapacityCrawlExecutor.doCrawl(b,HK,"cashFlow");
        debtCapacityCrawlExecutor.doCrawl(c,US,"cashFlow");

//        debtProfitabilityCrawlExecutor.doCrawl();
//        debtCashFlowCrawlExecutor.doCrawl();
    }
    //??????BusinRestructureExecutor
    @Test
    public void testBusinRestructure(){
        restructureExecutor.doCrawl(aaa,AB);
        restructureExecutor.doCrawl(a,AB);
    }
    //?????????????????????????????????????????????url ????????????
    @Test
    public void testDetailUrlMapper(){
        LambdaQueryWrapper<DetailUrlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetailUrlDO::getName,"????????????");
        DetailUrlDO detailUrlDO = detailUrlMapper.selectOne(wrapper);
        System.out.println(detailUrlDO);
        System.out.println("?????????????????????"+Objects.isNull(detailUrlDO));
    }

    //??????Roles?????????formString??????
    @Test
    public void testRolesenum(){
        String name = "???????????????";
        Roles roles = Roles.fromString(name);
        System.out.println(roles.getName());
    }

    //??????AdminServiceImpl.insertOne??????????????????????????????
    @Test
    public void testInsertOne(){
        InsertUserParam insertUserParam = new InsertUserParam();
        insertUserParam.setUsername("userTestInsertOne1");
        insertUserParam.setPhoneNumber("1");
        insertUserParam.setRole("?????????????????????????????????");
        adminService.insertOne(insertUserParam);
    }

    //??????RoleController??????getAll
    @Test
    public void testgetRoleAll(){
//        ResponseResult allRole = roleController.getAllRole();
//        System.out.println(allRole.getData());
        List<RoleDO> list = roleService.list();
        Map<String,Object> map = new HashMap<>();
        map.put("date",list);
        System.out.println(map);
    }

    //??????RoleService????????????
    @Test
    public void testRoleService(){
        ResponseResult role = roleService.getRoleOne("???????????????");
        System.out.println(role+"  "+role.getData());

        ResponseResult roleAll = roleService.getRoleAll();
        Map<String,Object> map = (Map<String, Object>) roleAll.getData();
        Set<String> strings = map.keySet();
        for (String string : strings) {
            System.out.println(map.get(string));
        }
        System.out.println("----");
        System.out.println(roleAll.getData());

    }
    //??????mybatis-plus???insert???????????????
    @Test
    public void testInsert(){
        RoleDO role = new RoleDO();
        role.setName("juese");
        System.out.println("?????????role "+role.getId());
        int insert = roleMapper.insert(role);
        System.out.println("????????????role "+role.getId());

    }

    //??????RoleService???insert??????
    @Test
    public void testRoleServiceInsert(){
        RoleNewInsertParam roleNewInsertParam = new RoleNewInsertParam();
        roleNewInsertParam.setRoleName("test_role2");
        roleNewInsertParam.setRoleDesc("????????????2");
        List<String> list = new ArrayList<>();
        list.add("????????????");
        roleNewInsertParam.setResource(list);
        ResponseResult responseResult = roleService.insertRole(roleNewInsertParam);
        System.out.println(responseResult.getData());
    }

    //11.18 ?????????DebtServiceImpl????????? AB???ok, HK???ok, US:ok
    @Test
    public void testDebtServiceImpl(){
        InputEnterpriceParam enterpriceParam = new InputEnterpriceParam();
        enterpriceParam.setName("????????????");
        ResponseResult debtDetails = debtService.getDebtData(enterpriceParam);
        System.out.println("??????????????????");
        Map<String,Object> map = (Map<String, Object>) debtDetails.getData();
        Set<String> strings = map.keySet();
        for (String string : strings) {
            System.out.println(map.get(string));
        }
    }

    //11.18 ?????????BusinessServiceImpl???????????????????????????AB???ok, HK???ok, US:???????????????
    @Test
    public void testBusinessService(){
        InputEnterpriceParam enterpriceParam = new InputEnterpriceParam();
        enterpriceParam.setName("?????????");
        ResponseResult businData = businessService.getBusinData(enterpriceParam);
        System.out.println("??????????????????"+businData.getData());
    }

    //11.19 ??????RoleService???removel??????
    @Test
    public void testRemovelById(){
        boolean b = roleService.removeById(117);
        System.out.println(b);
    }

    //11.19 ??????RRRService??????updateRoleResource
    @Test
    public void testupdateRoleResource(){
        UpdateRoleWithResource roleWithResource = new UpdateRoleWithResource();
        roleWithResource.setRoleId(118L);
        List<String> list = new ArrayList<>();
        list.add("????????????");
        list.add("????????????");
        roleWithResource.setResource(list);
        roleResource.updateRoleResource(roleWithResource);
    }

    ///11.19 ??????AdminController ????????????????????????????????????
    //      ?????????????????????userid,username,userPhoneNumber,roleName
    @Test
    public void testupdateUserByAdmin(){
        InsertUserParam insertUserParam = new InsertUserParam();
        insertUserParam.setId(7L);
        insertUserParam.setUsername("userTest444");
        insertUserParam.setPhoneNumber("userTest4444");
        insertUserParam.setRole("????????????");
        adminService.update(insertUserParam);
    }

    //11.20 ??????????????????
    @Test
    public void testPageSelect(){
        IPage<ResourceDO> page = new Page<>();
        //??????????????????
        page.setSize(3);
        //?????????????????????
        page.setCurrent(2);
        IPage<ResourceDO> resourceDOIPage = resourceMapper.selectPage(page, null);
        System.out.println(resourceDOIPage);
        System.out.println(resourceDOIPage.getRecords());
        System.out.println(resourceDOIPage.getCurrent()+" of "+resourceDOIPage.getTotal());
        for (ResourceDO record : resourceDOIPage.getRecords()) {
            System.out.println(record);
        }
//        resourceMapper.selectMapsPage()
    }
    //11.20 ??????roleService.getRolePage?????????????????????
    @Test
    public void testGetRolePage(){
        RoleSelectPage rolePage = new RoleSelectPage();
        rolePage.setCurrent(2);
        ResponseResult rolePage1 = roleService.getRolePage(rolePage);
        System.out.println("------");
        System.out.println(rolePage1.getData());
        List<RoleDO> data = (List<RoleDO>) rolePage1.getData();
        for (RoleDO datum : data) {
            System.out.println(datum.getDescription());
        }
    }
    @Autowired
    private BusinessSolutionContentMapper businessSolutionContentMapper;
    @Autowired
    private CombinationThreeDebtMapper combinationThreeDebtMapper;
    //11.21 ??????????????????????????????
    @Test
    public void testSchemeManagement(){
//        BusinessSolutionContentDO businessSolutionContentDO = businessSolutionContentMapper.selectById(1);
//        System.out.println(businessSolutionContentDO);
        CombinationThreeDebtDO combinationThreeDebtDO = combinationThreeDebtMapper.selectById(1);
        System.out.println(combinationThreeDebtDO);
    }

    //11.25 ??????????????????????????? ?????????ok, ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????map
    @SneakyThrows
    @Test
    public void testLambda() throws IllegalAccessException {
        RoleDO roleDO = new RoleDO();
        roleDO.setName("ceshi");
        roleDO.setDescription("");
        roleDO.setGmtCreate(new Date());
        Class roleClass = roleDO.getClass();
        Field[] declaredFields = roleClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            System.out.println("?????????????????????"+declaredField);
            System.out.println("?????????????????? "+declaredField.getName());
            System.out.println("?????????????????? "+declaredField.get(roleDO));
        }
        System.out.println("-------------------------");


            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(roleDO);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    System.out.println("????????????????????? "+name+":"+ propertyUtilsBean.getNestedProperty(roleDO, name));
                }
            }
    }

    //11.26,??????DebtConvertor??????????????????????????????????????????????????????
    @Test
    public void testDebtConvertor(){
        DebtCapacityDO debtCapacityDO = debtCapacityMapper.selectById(2);
        System.out.println(debtCapacityDO);
        List<CapacityResultVO> capacityResultVOList = debtConvertorUtils.capaConvertor(debtCapacityDO);
        for (CapacityResultVO capacityResultVO : capacityResultVOList) {
            System.out.println(capacityResultVO);
        }
        System.out.println("----------Profitability");
        DebtProfitabilityDO debtProfitabilityDO = profitabilityMapper.selectById(1);
        System.out.println(debtProfitabilityDO);
        List<ProfitabilityResultVO> profitabilityResultVOList = debtConvertorUtils.profitConvertor(debtProfitabilityDO);
        profitabilityResultVOList.forEach(System.out::println);

        System.out.println("------------CashFlow");
        DebtCashFlowDO debtCashFlowDO = cashFlowMapper.selectById(1);
        List<CashFlowResultVO> cashFlowResultVOS = debtConvertorUtils.cashConvertor(debtCashFlowDO);
        cashFlowResultVOS.forEach(System.out::println);
    }

    //11.26 ?????????????????????????????????????????????????????????total
    @Test
    public void testyoucangouzaozhong(){
        DebtResultDescVO debtResultDescVO = new DebtResultDescVO();
        String s = debtResultDescVO.debtResult("????????????",2, 3, 5);
        System.out.println(s);
    }

    @Autowired
    private DebtReversalUtils debtReversalUtils;
    //11.26 ??????List<VO>???DO???????????????
    @Test
    public void testDebtReversalUtils(){
        List<CapacityResultVO> capacityResults = new ArrayList<>();
        CapacityResultVO c1 = new CapacityResultVO();
        c1.setIndex("quickRatio");
        c1.setValue(0.84);
        c1.setCDO_id(20L);
        CapacityResultVO c2 = new CapacityResultVO();
        c2.setIndex("debtAssetRatio");
        c2.setValue(67.1);
        c2.setCDO_id(20L);
        CapacityResultVO c3 = new CapacityResultVO();
        c3.setIndex("currentRatio");
        c3.setValue(1.31);
        c3.setCDO_id(20L);

        capacityResults.add(c1);
        capacityResults.add(c2);
        capacityResults.add(c3);
        DebtCapacityDO debtCapacityDO = debtReversalUtils.capaReversal(capacityResults);
        System.out.println(debtCapacityDO);

        List<ProfitabilityResultVO> profitabilitys = new ArrayList<>();
        ProfitabilityResultVO p1 = new ProfitabilityResultVO();
        p1.setIndex("grossProfitRatio");
        p1.setValue(20.9);
        p1.setPDO_id(18L);
        ProfitabilityResultVO p2 = new ProfitabilityResultVO();
        p2.setIndex("returnOnInvestedCapital");
        p2.setValue(0.28);
        p2.setPDO_id(18L);
        ProfitabilityResultVO p3 = new ProfitabilityResultVO();
        p3.setIndex("netInterestRate");
        p3.setValue(3.84);
        p3.setPDO_id(18L);
        ProfitabilityResultVO p4 = new ProfitabilityResultVO();
        p4.setIndex("returnOnTotalAssets");
        p4.setValue(2.27);
        p4.setPDO_id(18L);
        profitabilitys.add(p1);
        profitabilitys.add(p2);
        profitabilitys.add(p3);
        profitabilitys.add(p4);
        DebtProfitabilityDO debtProfitabilityDO = debtReversalUtils.profitReversal(profitabilitys);
        System.out.println(debtProfitabilityDO);
    }

    @Autowired
    private OperationAbilityService operationAbilityService;
    //11.26 ??????operationAbilityServiceImpl?????????????????????
    @Test
    public void testOperationAbility(){
        OperationAbilityDO operationAbility = new OperationAbilityDO();
        operationAbility.setRequirementAnalysis("????????????");
        operationAbility.setSupplyAnalysis("?????????");
        operationAbility.setTeamStabilityAnalysis("???");
        OperationAbilityDO operationAbilityDO = operationAbilityService.completeScore(operationAbility);
        System.out.println(operationAbilityDO);
    }

    //????????????????????????????????????????????????????????????
    @Test
    public void testcontinue(){
//        for(int i = 0;i<10;i++){
//            System.out.println(i);
//            if(i == 5){
//                System.out.println(i);
//                break;
//            }
//        }
        String typeNamePattern = ".*???.*";
        if(Pattern.matches(typeNamePattern, "???")) {
            System.out.println(Pattern.matches(typeNamePattern, "???"));
        }
        System.out.println(Pattern.matches(typeNamePattern, "???"));

    }

    //??????BusinessGrowService???IService???getOne??????
    @Test
    public void testBusinessGrowServicesGetOne(){
        LambdaQueryWrapper<BusinessGrowthDO> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(BusinessGrowthDO::getName,"????????????");
//        BusinessGrowthDO growthDO = growService.getOne(wrapper);
//        System.out.println(growthDO);
//        BusinessGrowthDO growthDO = growthMapper.selectOne(wrapper);
//        System.out.println(growthDO);
//        BusinessGrowthDO byId = growService.getById(2);
//        System.out.println(byId);
        BusinessGrowthDO one = growService.getOne(wrapper);
        System.out.println(one);
    }



    //??????getDetailPageCrawlExecutor--????????????????????????
    @Test
    public void testGetDetailsPageExecutor(){
        String name = "?????????";
        LambdaQueryWrapper<DetailUrlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetailUrlDO::getName,name);

        System.out.println("?????????????????????????????????????????????????????????????????????url");
        //????????????????????????
        List<DetailUrlDO> detailUrlDO = detailUrlMapper.selectList(wrapper);
        System.out.println(detailUrlDO);
        getDetailPageCrawlExecutor.doCrawl(name);
        System.out.println("?????????????????????????????????????????????????????????????????????rul");
        List<DetailUrlDO> detailUrlDO1 = detailUrlMapper.selectList(wrapper);
        System.out.println(detailUrlDO1);
    }

    @Test
    public void testzhegnze(){
        String url = "";
        String pattern = ".*http://quote.eastmoney.com/.*";
        boolean isMatch = Pattern.matches(pattern, url);
        System.out.println("??????????????????????????? 'url' ????????????? " + isMatch);
        String code = "08227";
        String code1 = "003023";
        System.out.println(code.length());
        System.out.println(code1.length());
        switch (code1.length()){
            case 5:{
                //??????
                System.out.println("hhhh");
                break;
            } case 6:{
                //AB???
                System.out.println("gggggggggggggggg");
                break;
            } default:{
                break;
            }
        }

    }

    @Test
    public void testnumberconvertabale(){
        String abc = "64.30???";
        Pattern compile = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = compile.matcher(abc);
        matcher.find();
        String string = matcher.group();//????????????????????????
        System.out.println(string);//0.00
    }

    @Test
    public void testzhongwen(){
        String r_name3 = "6340???";
//        Pattern pattern = Pattern.compile("(\\d+\\.\\d+)|(\\d+)");
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(r_name3);
        System.out.println(matcher.replaceAll("").trim());

    }

    //?????????????????????
    @Test
    public void testconvertable(){
        String str = "4.302???";
//        String string = str.replaceAll("[\u4e00-\u9fa5]", "");
//        System.out.println(string);
        Double conversion = numberUtils.conversion(str);
        System.out.println(conversion);
//        String str1 = "61ff2872-6320-4f53-9b51-1e5e9e174198" ;
//        System.out.println(str.replace("-",""));
    }

    //???????????????????????????true??????????????????false
    @Test
    public void pipeline(){
//        DebtDiagnosisDO debtDiagnosisDO = new DebtDiagnosisDO();
//        debtDiagnosisDO.setName("???");
//        debtPipeline.processtest(debtDiagnosisDO);
        String str = "--";
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(str);
        if(m.matches()){
            flag = true;
        }
        System.out.println(flag);
    }
    /**
    *@Description ??????????????????????????????
    **/
    @Test
    public void testoutbaifenhao(){
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        String format = decimalFormat.format(Double.valueOf("36.17".replaceAll("%", "")));
        System.out.println(format);
    }

    @Test
    public void teststring2double(){
        String price = "-4.302???";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String s = price.replaceAll("[\u4e00-\u9fa5]", "");
        System.out.println(s);
        String format = decimalFormat.format(Double.valueOf(s));
        System.out.println(format);
        System.out.println(decimalFormat.format(Double.parseDouble(format)*10000));
//        System.out.println(Double.valueOf(decimalFormat.format(s)));
        System.out.println("-----------");
        String aa = "32.556";
        Double aDouble = Double.valueOf(decimalFormat.format(Double.valueOf(aa)));
        System.out.println(aDouble*10000.00);

    }


    //?????????????????????????????????????????????getAllUserAllRole
    @Test
    public void testgetAllUserAllRole(){
//        LambdaQueryWrapper<UserAndRoles> wrapper = new LambdaQueryWrapper<>();
//        wrapper.select(UserAndRoles::getId,UserAndRoles::getNickname,UserAndRoles::getPhoneNumber,
//                       UserAndRoles::getGmtCreate,UserAndRoles::getGmtModified,UserAndRoles::getRoleDOList);
        QueryWrapper<UserAndRoles> wrapper = new QueryWrapper<>();
//        u.id uid,u.nickname unickname,u.phone_number uphonenumber,u.gmt_create ugmtcreate,
//        u.gmt_modified ugmtmodified,r.id rid,r.`name` rname,r.description rdescription
        wrapper.select("u.id uid","u.nickname unickname","u.phone_number uphonenumber", "u.gmt_create ugmtcreate",
                       "u.gmt_modified ugmtmodified","r.id rid","r.`name` rname","r.description rdescription");
        List<UserAndRoles> allUserAllRole = userMapper.getAllUserAllRole(wrapper);
        allUserAllRole.forEach(System.out::println);
    }
    @Test
    public void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        String s1 = bcpe.encode("111");
        String s2 = bcpe.encode("222");
        System.out.println(s1);
        //$2a$10$fptv7/1BseA13/USsfhIKu9Q5CSyGotWEAjUmTgNxoIKUcrT18GRe
        System.out.println(s2);
        //$2a$10$RD.jjgFPuAmoEnAIysS/TepfyJYqp/MG1QTFklG6WtCbNILbT4RzG
        System.out.println("????????????????????????" + bcpe.matches(
                "333",
                "$2a$10$Ez8D0OyGYi7/JdGyr/kNNOFg8FcC4sMekFeML8nwncOJ/m.UXxr.y"));
        System.out.println("????????????????????????" + bcpe.matches(
                "2222",
                "$2a$10$xopmYdSLxUYOBOOC0oMWIeOYNlym4rWBI/YR14pRTj8bKpOIqiIGu"));
    }
}
