package com.bh.proprietor.core.tools.util;

import com.bh.proprietor.core.tools.http.HttpUtil;
import org.junit.Test;

/**
 * Created by chandler on 2017/10/25 0025.
 */
public class HttpUtilTest {
    @Test
    public void URLdecode(){
        System.out.println(HttpUtil.decode("opt%2Fdev_work%2Fcommon%2Fupload%2F2017-10-25%2FgN8S0EfEWJ%2F%E7%A7%9F%E8%B5%81%E5%90%88%E5%90%8C.docx","utf-8"));
    }
}
