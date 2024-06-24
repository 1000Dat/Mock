package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ExcelService {
    void save(MultipartFile file, UserEntity userEntity) throws IOException;
    void readAndEditExcel(int countSuccess, int countFail, int totalRow) throws IOException;
    File getEditedFile();
}
