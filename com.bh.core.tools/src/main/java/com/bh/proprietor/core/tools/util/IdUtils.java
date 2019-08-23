/**
 * Project Name:com.wstro.wplus.core.service.impl
 * File Name:IdUtils.java
 * Package Name:com.wstro.wplus.core.utils
 * Date:2016年1月12日下午2:16:18
 * Copyright (c) 2016, winstrong All Rights Reserved.
 */

package com.bh.proprietor.core.tools.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * ClassName:IdUtils <br/>
 * Function: 产生12为不重复的随机数. <br/>
 * Reason: 作为租户主键KEY的生产策略,实际使用不保存一定不重复,所以还是需要查询一次. <br/>
 * Date: 2016年1月12日 下午2:16:18 <br/>
 *
 * @author songs@wstro
 * @version
 * @since
 * @see
 */
public final class IdUtils {

    /**
     *
     * createId:(创建一个12位不重复的KEY). <br/>
     * (产生的KEY包含字母和数字).<br/>
     *
     * @author songs@wstro
     * @return
     * @since
     */
    public static String createId() {
        String id = UUID.randomUUID().toString();

        id = DEKHash(id) + "";

        int diff = 12 - id.length();
        String randStr = RandomStringUtils.randomAlphabetic(12);
        for (int i = 0 ; i < diff ; i++) {
            int randIndex = (int) (Math.random() * randStr.length());
            int index = (int) (Math.random() * id.length());
            id = id.substring(0, index) + randStr.charAt(randIndex) + id.substring(index, id.length());
        }
        return id;
    }

    /**
     *
     * DEKHash:(对KEY进行HASH操作). <br/>
     *
     * @author songs@wstro
     * @param str
     * @return
     * @since
     */
    private static int DEKHash(String str) {
        int hash = str.length();

        for (int i = 0 ; i < str.length() ; i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }

        return (hash & 0x7FFFFFFF);
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag
     *            是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0 ; i < length ; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }
}
