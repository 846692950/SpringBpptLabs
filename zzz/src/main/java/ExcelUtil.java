import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static void main(String[] args) {
        String filePath = "D:\\sss.xlsx";
        List<List<String>> excelData = readExcelContent(filePath);
        for (List<String> row : excelData) {
            for (String s : row) {
                System.out.println(s);
            }
        }
    }

    /**
     * 读取Excel文件内容并以List形式返回
     *
     * @param path excel路径
     * @return List<List < String>> excel中的数据集合
     */
    public static List<List<String>> readExcelContent(String path) {
        List<List<String>> contentList = new ArrayList<>();
        final int MAX_ROW_COUNT = 1000; // 最大读取行数
        InputStream is = null;
        Workbook wb = null;
        try {
            is = new FileInputStream(new File(path));
            wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);//获取第一个工作表
            int rowNum = sheet.getPhysicalNumberOfRows(); //获取行数
            int rowCount = Math.min(rowNum, MAX_ROW_COUNT); //限制最多读取1000行
            for (int r = 0; r < rowCount; r++) {
                Row row = sheet.getRow(r); // 获取当前行
                if (row != null) {
                    int cellCount = row.getPhysicalNumberOfCells(); // 获取单元格数
                    List<String> cellList = new ArrayList<>(); // 存储单元格数据的列表
                    for (int c = 0; c < cellCount; c++) {
                        Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); // 获取单元格
                        if (cell != null) {
                            CellType type = cell.getCellTypeEnum(); // 获取单元格类型
                            switch (type) {
                                case STRING:
                                    cellList.add(cell.getStringCellValue()); // 字符串类型
                                    break;
                                case NUMERIC:
                                    cellList.add(String.valueOf(cell.getNumericCellValue())); // 数字类型
                                    break;
                                case BOOLEAN:
                                    cellList.add(String.valueOf(cell.getBooleanCellValue())); // 布尔类型
                                    break;
                                case FORMULA:
                                    cellList.add(cell.getCellFormula()); // 公式类型
                                    break;
                                default:
                                    cellList.add(""); // 其他类型处理为空字符串
                            }
                        } else {
                            cellList.add(""); // 处理空单元格为空字符串
                        }
                    }
                    contentList.add(cellList); // 将一行数据添加到结果列表
                }
            }
        } catch (RuntimeException | IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return contentList; // 返回读取到的所有内容
    }

}
