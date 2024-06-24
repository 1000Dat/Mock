package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.helper.ExcelHelper;
import fa.training.interviewmanagement.repository.JobRepository;
import fa.training.interviewmanagement.service.ExcelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    private static final String TEMP_FILE_PATH = "src/main/resources/edited_template.xlsx";
    @Autowired
    JobRepository repository;

    @Autowired
    ExcelHelper excelHelper;
    public void save(MultipartFile file, UserEntity userEntity) {
        try {
            List<Job> jobs = excelHelper.excelToTutorials(file.getInputStream(), userEntity);
            if (!CollectionUtils.isEmpty(jobs)) {
                repository.saveAll(jobs);
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }



    @Override
    public void readAndEditExcel(int countSuccess, int countFail, int totalRow) throws IOException {
        // Đọc file Excel từ thư mục resources
        ClassPathResource resource = new ClassPathResource("template.xlsx");
        try (InputStream inputStream = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Lấy sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);

            // Ghi giá trị vào cột A, B, và C cho các hàng bên dưới

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Total Of Job");
            headerRow.createCell(1).setCellValue("Number Of Success");
            headerRow.createCell(2).setCellValue("Number Of Fail");

            // Create data row
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(totalRow);
            dataRow.createCell(1).setCellValue(countFail);
            dataRow.createCell(2).setCellValue(countSuccess);

            // Lưu lại file Excel sau khi chỉnh sửa vào vị trí tạm thời
            try (FileOutputStream outputStream = new FileOutputStream(TEMP_FILE_PATH)) {
                workbook.write(outputStream);
            }
        }
    }

    @Override
    public File getEditedFile() {
        return new File(TEMP_FILE_PATH);
    }
}
