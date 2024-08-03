import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class ExcelExporter {
    public void exportExcel(String filePath) {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建Sheet
        Sheet sheet = workbook.createSheet("Sheet 1");

        // 创建行
        Row headerRow = sheet.createRow(0);

        // 创建一个字体样式
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        // 创建表头单元格
        String[] columns = {"第一列", "第二列", "第三列", "第四列"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(cellStyle); // 设置样式
        }

        // 为第二行创建随机数据
        Row secondRow = sheet.createRow(1);
        Random random = new Random();
        for (int i = 0; i < columns.length; i++) {
            secondRow.createCell(i).setCellValue(String.valueOf(random.nextInt(100)));
        }

        // 将输出写入文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExcelExporter excelExporter = new ExcelExporter();
        excelExporter.exportExcel("D:\\file.xlsx");
    }
}