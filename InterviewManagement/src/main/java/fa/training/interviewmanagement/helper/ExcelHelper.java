package fa.training.interviewmanagement.helper;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UploadHistoryEntity;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.job.StatusJobEnum;
import fa.training.interviewmanagement.model.job.StatusUploadHistoryEnum;
import fa.training.interviewmanagement.model.user.UploadHistoryRepository;
import fa.training.interviewmanagement.service.processor.JobValidateProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
@Component
public class ExcelHelper {
    @Autowired
    JobValidateProcessor jobValidateProcessor;
    @Autowired
    UploadHistoryRepository uploadHistoryRepository;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Id", "Title", "Description", "Published"};
    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }


    public List<Job> excelToTutorials(InputStream is, UserEntity userEntity) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Job> jobList = new ArrayList<>();
            int totalRow = 0;
            int countFail = 0;
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                boolean hasError = false; // Cờ để theo dõi lỗi
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                totalRow++;
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Job job = new Job();
                job.setStatus(StatusJobEnum.DRAFT);

                int cellIdx = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    try {
                        switch (cellIdx) {
                            case 1:
                                if (jobValidateProcessor.checkDuplicateTitle(currentCell.getStringCellValue())) {
                                    throw new RuntimeException("Trùng Title");
                                }
                                job.setTitle(currentCell.getStringCellValue());
                                break;

                            case 2:
                                job.setSkill(currentCell.getStringCellValue());
                                break;

                            case 3:
                                job.setStartWork(currentCell.getLocalDateTimeCellValue().toLocalDate());
                                break;

                            case 4:
                                job.setEndWork(currentCell.getLocalDateTimeCellValue().toLocalDate());
                                break;

                            case 5:
                                job.setFr(String.valueOf(currentCell.getNumericCellValue()));
                                break;

                            case 6:
                                job.setT(String.valueOf(currentCell.getNumericCellValue()));
                                break;

                            case 7:
                                job.setBenefits(currentCell.getStringCellValue());
                                break;

                            case 8:
                                job.setAddress(currentCell.getStringCellValue());
                                break;

                            case 9:
                                job.setLevel(currentCell.getStringCellValue());
                                break;

                            case 10:
                                job.setDescription(currentCell.getStringCellValue());
                                break;

                            default:
                                break;
                        }
                    } catch (Exception e) {
                        countFail++;
                        hasError = true;
                    }
                    cellIdx++;
                }
                if (!hasError) {
                    jobList.add(job);
                }
            }

            workbook.close();
            Integer countSuccess = calculation(totalRow, countFail);
            log.info("Total row: " + totalRow);
            log.info("Count fail: " + countFail);
            log.info("Count success: " + countSuccess);
            // Write log information to an Excel file using the new class
            ExcelLogWriter.writeLogToExcel(totalRow, countFail, countSuccess);
            // Ghi nội dung lỗi ra file edited_template.xlsx
            UploadHistoryEntity uploadHistoryEntity = new UploadHistoryEntity();
            uploadHistoryEntity.setUploadDate(LocalDateTime.now());
            uploadHistoryEntity.setStatusUploadHistoryEnum(countFail > 0 ? StatusUploadHistoryEnum.FAILURE : StatusUploadHistoryEnum.SUCCESS);
            uploadHistoryEntity.setUploadBy(userEntity);
            uploadHistoryRepository.save(uploadHistoryEntity);
            return jobList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }


    private static Integer calculation(Integer totalRow, Integer countFail) {
        return totalRow - countFail;
    }
}
