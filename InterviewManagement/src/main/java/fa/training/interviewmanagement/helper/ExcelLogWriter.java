package fa.training.interviewmanagement.helper;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Log4j2
public class ExcelLogWriter {
    private static final String TEMP_FILE_PATH = "src/main/resources/edited_template.xlsx";

    public static void writeLogToExcel(int totalRow, int countFail, int countSuccess) throws IOException {
        // Đọc file Excel từ thư mục resources
        ClassPathResource resource = new ClassPathResource("template.xlsx");
        try (InputStream inputStream = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Lấy sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return;
            }

            // Ghi giá trị vào cột A, B, và C cho các hàng bên dưới

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Total Of Job");
            headerRow.createCell(1).setCellValue("Number Of Success");
            headerRow.createCell(2).setCellValue("Number Of Fail");

            // Create data row
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(totalRow);
            dataRow.createCell(1).setCellValue(countSuccess);
            dataRow.createCell(2).setCellValue(countFail);

            // Lưu lại file Excel sau khi chỉnh sửa vào vị trí tạm thời
            try (FileOutputStream outputStream = new FileOutputStream(TEMP_FILE_PATH)) {
                workbook.write(outputStream);
            }
        }
    }
}

