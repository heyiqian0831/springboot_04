package com.fuyin.boot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 何义祈安
 * @Description 价格转换工具类
 */
@Component
public class NumberUtils {

    private static final Double MILLION = 10000.00;
    private static final Double MILLIONS = 1000000.00;
    private static final Double BILLION = 100000000.00;
    private static final Double MILLION_BILLION = 1000000000000.00;
    private static final String MILLION_UNIT = "万";
    private static final String BILLION_UNIT = "亿";
    private static final String MILLION_BILLION_UNIT = "万亿";

    private static Logger log = LoggerFactory.getLogger(NumberUtils.class);

    private static final Pattern NUMBER_POINT_COMPILE = Pattern.compile("(\\d+\\.\\d+)|(\\d+)|(\\d+\\-\\d+)");
    private static final String CHINESE_COMPILE = "[\u4e00-\u9fa5]";

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /**
    *@Description 将字符串数据去除单位获取Double值
    *@Date 21:42 2022/11/2
    *@return java.lang.Double
    **/
    public Double conversion(String price){
        //获取去除单位后的值
        Double result = getDigital(price);

        //取出中文单位判断万亿
        String chineseCompile = getChineseCompile(price);

        if (chineseCompile.equals(MILLION_UNIT)){
            //万为单位，乘上0000
            BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(result));
            BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(MILLION));
            result = bigDecimal1.multiply(bigDecimal2).doubleValue();
            System.out.println("万: "+result);
        }else if(chineseCompile.equals(BILLION_UNIT)){
            //亿为单位，乘上00000000
            BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(result));
            BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(BILLION));
            result = bigDecimal1.multiply(bigDecimal2).doubleValue();
            System.out.println("亿: "+result);
        }else if(chineseCompile.equals(MILLION_BILLION_UNIT)){
            BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(result));
            BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(MILLION_BILLION));
            result = bigDecimal1.multiply(bigDecimal2).doubleValue();
            System.out.println("万亿: "+result);
        }
        return result;
    }

    /**
    *@Description 获取去除单位的值
    **/
    private static Double getDigital(String price) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        String format = decimalFormat.format(Double.valueOf(price.replaceAll(CHINESE_COMPILE, "")));
        return Double.valueOf(format);
    }

    /**
    *@Description 取出中文单位
    **/
    private static String getChineseCompile(String price) {
        String replace = price.replace("-", "");
        Matcher matcher = NUMBER_POINT_COMPILE.matcher(replace);
        return matcher.replaceAll("").trim();
        //        System.out.println(company);
    }

    /**
     * 将数字转换成以万为单位或者以亿为单位，因为在前端数字太大显示有问题
     *
     * @param amount 报销金额
     */
    public String amountConversion(double amount) {
        //最终返回的结果值
        String result = String.valueOf(amount);
        //四舍五入后的值
        double value = 0;
        //转换后的值
        double tempValue = 0;
        //余数
        double remainder = 0;

        //金额大于1百万小于1亿
        if (amount > MILLIONS && amount < BILLION) {
            tempValue = amount / MILLION;
            remainder = amount % MILLION;
            log.info("tempValue=" + tempValue + ", remainder=" + remainder);

            //余数小于5000则不进行四舍五入
            if (remainder < (MILLION / 2)) {
                value = formatNumber(tempValue, 2, false);
            } else {
                value = formatNumber(tempValue, 2, true);
            }
            //如果值刚好是10000万，则要变成1亿
            if (value == MILLION) {
                result = zeroFill(value / MILLION) + BILLION_UNIT;
            } else {
                result = zeroFill(value) + MILLION_UNIT;
            }
        }
        //金额大于1亿
        else if (amount > BILLION) {
            tempValue = amount / BILLION;
            remainder = amount % BILLION;
            log.info("tempValue=" + tempValue + ", remainder=" + remainder);

            //余数小于50000000则不进行四舍五入
            if (remainder < (BILLION / 2)) {
                value = formatNumber(tempValue, 2, false);
            } else {
                value = formatNumber(tempValue, 2, true);
            }
            result = zeroFill(value) + BILLION_UNIT;
        } else {
            result = zeroFill(amount);
        }
        log.info("result=" + result);
        return result;
    }


    /**
     * 对数字进行四舍五入，保留2位小数
     *
     * @param number   要四舍五入的数字
     * @param decimal  保留的小数点数
     * @param rounding 是否四舍五入
     */
    public static Double formatNumber(double number, int decimal, boolean rounding) {
        BigDecimal bigDecimal = new BigDecimal(number);

        if (rounding) {
            return bigDecimal.setScale(decimal, RoundingMode.HALF_UP).doubleValue();
        } else {
            return bigDecimal.setScale(decimal, RoundingMode.DOWN).doubleValue();
        }
    }

    /**
     * 对四舍五入的数据进行补0显示，即显示.00
     *
     */
    public static String zeroFill(double number) {
        String value = String.valueOf(number);

        if (value.indexOf(".") < 0) {
            value = value + ".00";
        } else {
            String decimalValue = value.substring(value.indexOf(".") + 1);

            if (decimalValue.length() < 2) {
                value = value + "0";
            }
        }
        return value;
    }

    /**
     * 测试方法入口
     * @param args
     */
//    public static void main(String[] args) throws Exception {
////        amountConversion(120);
////        amountConversion(18166.35);
////        amountConversion(1222188.35);
////        amountConversion(129887783.5);
////        conversion("120亿");
////        conversion("18166.35万");
////        conversion("1222188.35万");
////        conversion("129887783.5亿");
//
//    }


}
