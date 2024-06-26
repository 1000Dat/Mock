package fa.training.interviewmanagement.helper;

import fa.training.interviewmanagement.config.WebConfig;
import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UploadHistoryEntity;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.job.StatusJobEnum;
import fa.training.interviewmanagement.model.job.StatusUploadHistoryEnum;
import fa.training.interviewmanagement.repository.UploadHistoryRepository;
import fa.training.interviewmanagement.service.processor.JobValidateProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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

    @Autowired
    WebConfig DATE_FORMATTER;
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
            List<String> errors = new ArrayList<>();
            StringBuilder errorMessageBuilder = new StringBuilder();

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
                errorMessageBuilder.append("Row " + rowNumber + ": ");
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    try {
                        switch (cellIdx) {
                            case 1:
                                if (currentCell == null || currentCell.getStringCellValue().isEmpty()) {
                                    errorMessageBuilder.append("Title không được để trống. ");
                                } else if (jobValidateProcessor.checkDuplicateTitle(currentCell.getStringCellValue())) {
                                    errorMessageBuilder.append("Trùng Title. ");
                                    throw new RuntimeException();
                                } else {
                                    job.setTitle(currentCell.getStringCellValue());
                                }
                                break;

                            case 2:
                                if (currentCell == null || currentCell.getStringCellValue().isEmpty()) {
                                    errorMessageBuilder.append("Skill không được để trống. ");
                                } else {
                                    job.setSkill(currentCell.getStringCellValue());
                                }
                                break;

                            case 3:
                                if (currentCell == null || !DateUtil.isCellDateFormatted(currentCell)) {
                                    errorMessageBuilder.append("Định dạng ngày bắt đầu không hợp lệ hoặc trống. ");
                                } else {
                                    LocalDate startDate = currentCell.getLocalDateTimeCellValue().toLocalDate();
                                    if (startDate.isBefore(LocalDate.now())) {
                                        errorMessageBuilder.append("Ngày bắt đầu không được là quá khứ. ");
                                    }
                                    job.setStartWork(startDate);
                                }
                                break;

                            case 4:
                                if (currentCell == null || !DateUtil.isCellDateFormatted(currentCell)) {
                                    errorMessageBuilder.append("Định dạng ngày kết thúc không hợp lệ hoặc trống. ");
                                } else {
                                    LocalDate endDate = currentCell.getLocalDateTimeCellValue().toLocalDate();
                                    if (endDate.isBefore(job.getStartWork())) {
                                        errorMessageBuilder.append("Ngày kết thúc không được trước ngày bắt đầu. ");
                                    }
                                    job.setEndWork(endDate);
                                }
                                break;

                            case 5:
                                if (currentCell == null || currentCell.getCellType() != CellType.NUMERIC) {
                                    errorMessageBuilder.append("Giá trị FR không hợp lệ hoặc trống. ");
                                } else {
                                    double frValue = currentCell.getNumericCellValue();
                                    if (frValue < 0) {
                                        errorMessageBuilder.append("Giá trị FR không được âm. ");
                                    }
                                    job.setFr(String.valueOf(frValue));
                                }
                                break;

                            case 6:
                                if (currentCell == null || currentCell.getCellType() != CellType.NUMERIC) {
                                    errorMessageBuilder.append("Giá trị T không hợp lệ hoặc trống. ");
                                } else {
                                    double tValue = currentCell.getNumericCellValue();
                                    if (tValue < 0) {
                                        errorMessageBuilder.append("Giá trị T không được âm. ");
                                    }
                                    job.setT(String.valueOf(tValue));
                                }
                                break;

                            case 7:
                                if (currentCell == null || currentCell.getStringCellValue().isEmpty()) {
                                    errorMessageBuilder.append("Benefits không được để trống. ");
                                } else {
                                    job.setBenefits(currentCell.getStringCellValue());
                                }
                                break;

                            case 8:
                                if (currentCell == null || currentCell.getStringCellValue().isEmpty()) {
                                    errorMessageBuilder.append("Address không được để trống. ");
                                } else {
                                    job.setAddress(currentCell.getStringCellValue());
                                }
                                break;

                            case 9:
                                if (currentCell == null || currentCell.getStringCellValue().isEmpty()) {
                                    errorMessageBuilder.append("Level không được để trống. ");
                                } else {
                                    job.setLevel(currentCell.getStringCellValue());
                                }
                                break;

                            case 10:
                                if (currentCell == null || currentCell.getStringCellValue().isEmpty()) {
                                    errorMessageBuilder.append("Description không được để trống. ");
                                } else {
                                    job.setDescription(currentCell.getStringCellValue());
                                }
                                break;

                            default:
                                // Handle default case if needed
                                break;
                        }
                    } catch (Exception e) {
                        countFail++;
                        hasError = true;
                    }
                    cellIdx++;
                }

                if (!hasError) {
                    try {
                        double frValue = Double.parseDouble(job.getFr());
                        double tValue = Double.parseDouble(job.getT());
                        if (frValue >= tValue) {
                            errorMessageBuilder.append("Giá trị FR phải nhỏ hơn giá trị T. ");
                            hasError = true;
                            countFail++;
                        }
                    } catch (NumberFormatException e) {
                        errorMessageBuilder.append("Giá trị FR hoặc T không hợp lệ. ");
                        hasError = true;
                        countFail++;
                    }
                }

                // Thêm thông điệp lỗi tích lũy vào danh sách errorMessages
                if (hasError) {
                    errors.add(errorMessageBuilder.toString());
                    errorMessageBuilder.setLength(0);
                } else {
                    jobList.add(job);
                }
                rowNumber++; // Tăng biến rowNumber sau mỗi lần lặp qua hàng
            }

            workbook.close();
            Integer countSuccess = calculation(totalRow, countFail);
            log.info("Total row: " + totalRow);
            log.info("Count fail: " + countFail);
            log.info("Count success: " + countSuccess);
            log.info("Errors: " + errors);
            // Write log information to an Excel file using the new class
            ExcelLogWriter.writeLogToExcel(totalRow, countFail, countSuccess, errors);
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