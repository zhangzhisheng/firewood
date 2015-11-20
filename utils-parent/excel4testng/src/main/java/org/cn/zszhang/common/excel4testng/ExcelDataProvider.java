package org.cn.zszhang.common.excel4testng;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.annotations.DataProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zszhang on 2015/11/18.
 */
public class ExcelDataProvider {
    @DataProvider(name="ExcelDataProvider")
    public static Object[][] getTestDataFromExcel(Method method)
            throws FileNotFoundException, IOException, InvalidFormatException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getTestDataFromExcelFile(method.getDeclaringClass(), method);
    }

    private static Object[][] getTestDataFromExcelFile(Class clazz, Method method)
            throws FileNotFoundException, IOException, InvalidFormatException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String clazzName = clazz.getName();
        String filePath = clazzName.replace('.', '/') + ".xlsx";
        String sheetName = method.getName();

        return getTestDataFromExcelSheet(clazz, method, filePath, sheetName);
    }

    private static Object[][] getTestDataFromExcelSheet(Class clazz, Method method, String filePath, String sheetName)
            throws FileNotFoundException, IOException, InvalidFormatException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class[] paraTypes = method.getParameterTypes();
        int paraCnt = method.getParameterCount();

        InputStream is = clazz.getClassLoader().getResourceAsStream(filePath);
        String[][] data = ExcelDataUtil.getSheetData(is, sheetName, paraCnt);
        int testCnt = data.length;
        Object[][] result = new Object[testCnt][paraCnt];

        for(int i=0; i<testCnt; i++) {
            for(int j=0; j<paraCnt; j++) {
                result[i][j] = ConvertUtil.convert(paraTypes[j], data[i][j]);
            }
        }

        return result;
    }
}
