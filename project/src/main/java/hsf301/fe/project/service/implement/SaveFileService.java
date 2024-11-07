package hsf301.fe.project.service.implement;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import hsf301.fe.project.config.FireBaseConfig;
import hsf301.fe.project.service.defines.ISaveFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@Service
public class SaveFileService implements ISaveFileService {

    @Autowired
    FireBaseConfig firebaseConfiguration;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String storageBucket = "koi-auction-backend.appspot.com";
        Storage storage = StorageClient.getInstance().bucket().getStorage();
        String fileName = "file/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(storageBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        Blob blob = storage.create(blobInfo, file.getBytes());

        String publicUrl = "https://firebasestorage.googleapis.com/v0/b/" + storageBucket + "/o/" + URLEncoder.encode(fileName, "UTF-8") + "?alt=media";

        return publicUrl;  // Trả về Public URL
    }

}