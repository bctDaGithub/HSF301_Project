package hsf301.fe.project.service.defines;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ISaveFileService {
    String uploadImage(MultipartFile file) throws IOException;
}
