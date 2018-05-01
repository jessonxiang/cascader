package com.bh.core.tools.lang;

import java.math.BigDecimal;

/**
 * Created by jesson on 2017/5/19 0019.
 */
public class NumberFormat {

    /**
     * 四舍五入保留length长度小数
     * @param number
     * @param length
     * @return
     */
    public static Double formartDouble(double number, int length){
        BigDecimal bigDecimal = new BigDecimal(number);
        BigDecimal bd = bigDecimal.setScale(length, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 四舍五入保留length长度小数
     * @param number
     * @param length
     * @return
     */
    public static Double formartDouble(String number, int length){
        BigDecimal bigDecimal = new BigDecimal(number);
        BigDecimal bd = bigDecimal.setScale(length, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 四舍五入保留length长度小数
     * @param number
     * @return
     */
    public static Double formartDouble(Double number,int length){
        BigDecimal bigDecimal = new BigDecimal(number);
        BigDecimal bd = bigDecimal.setScale(length, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

}
