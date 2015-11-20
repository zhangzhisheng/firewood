package org.cn.zszhang.common.excel4testng.sysutil.text;

import org.cn.zszhang.common.excel4testng.ExcelDataProvider;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by zszhang on 2015/5/30.
 */
public class CharacterUtilTest {

    @DataProvider(name = "regularStringData")
    public static Object[][] regularStringData() {
        return new Object[][]{
                new Object[]{null, null},
                new Object[]{"", ""},
                new Object[]{"㈠㈡㈢㈣㈤㈥㈦㈧㈨㈩", "12345678910"},
                new Object[]{"❶❷❸❹❺❻❼❽❾❿", "12345678910"},
                new Object[]{"➀➁➂➃➄➅➆➇➈➉", "12345678910"},
                new Object[]{"➊➋➌➍➎➏➐➑➒➓", "12345678910"},
                new Object[]{"ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ", "123456789101112"},
                new Object[]{"ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻ", "123456789101112"},
                new Object[]{"①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳", "1234567891011121314151617181920"},
                new Object[]{"⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛", "1234567891011121314151617181920"},
                new Object[]{"⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇", "1234567891011121314151617181920"},
                new Object[]{"⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵", "abcdefghijklmnopqrstuvwxyz"},
                new Object[]{"ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ", "abcdefghijklmnopqrstuvwxyz"},
                new Object[]{"ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ", "abcdefghijklmnopqrstuvwxyz"},
                new Object[]{"⓪⓫⓬⓭⓮⓯⓰⓱⓲⓳⓴⓵⓶⓷⓸⓹⓺⓻⓼⓽⓾", "01112131415161718192012345678910"},
                new Object[]{"我爱北京天安门㈠㈡㈢㈣➀➁➂➃", "我爱北京天安门12341234"}
        };
    }

    @Test(dataProvider = "regularStringData")
    public void regularString(String input, String expected) {
        String actual = CharacterUtil.regularString(input);
        Assert.assertEquals(actual, expected, input);
    }

    @Test(dataProvider = "ExcelDataProvider", dataProviderClass = ExcelDataProvider.class)
    public void testRegularString(String input, String expected) {
        System.out.println("input ="+input + ",expected="+expected);
        String actual = CharacterUtil.regularString(input);
        Assert.assertEquals(actual, expected, input);
    }
}