package org.cn.zszhang.common.excel4testng;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zszhang on 2015/11/18.
 */
public class ExcelDataUtil {
    public static String[][] getSheetData(InputStream inputStream, String sheetName, int colCount)
            throws FileNotFoundException, IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(inputStream);
        Sheet sheet = wb.getSheet(sheetName);
        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();
        String[][] data = new String[lastRow-firstRow+1][colCount];

        Row row;
        Cell cell;
        for(int i=0; firstRow <= lastRow; i++, firstRow++) {
            row = sheet.getRow(firstRow);
            if(null == row) continue;
            for(int j=0;j<colCount; j++) {
                cell = row.getCell(j);
                data[i][j] = getCellValue(cell);
            }
        }

        return data;
    }

    private static String getCellValue(Cell cell) {
        if(null == cell)    return null;
        Object v = null;
        int type = cell.getCellType() == Cell.CELL_TYPE_FORMULA ?  cell.getCachedFormulaResultType() : cell.getCellType();
        switch (type) {
            case Cell.CELL_TYPE_STRING:
                v = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    v = cell.getDateCellValue();
                } else {
                    v = cell.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                v = cell.getBooleanCellValue();
                break;
            default:
                // do nothing.
                //logger.debug("cell " + cell.getColumnIndex() + "convert fail...");
        }
        return v.toString();
    }

}
