package org.cn.zszhang.common.excel4testng;

import org.testng.Assert;

/**
 * Created by zszhang on 2015/11/19.
 */
public class ConvertUtilTest {

    //@Test(dataProvider = "ExcelDataProvider", dataProviderClass = ExcelDataProvider.class)
    public void testConvert(Class type, String svalue, Object expected) throws Exception {
        System.out.println("type="+type+",svalue=["+svalue+"],expected="+expected);
        Object actual = ConvertUtil.convert(type, svalue);
        Assert.assertEquals(actual, expected);
    }

}