package pl.telephoners.controllers;

import com.google.cloud.storage.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping(path = "posts")
public class PostController {

    Storage storage = StorageOptions.getDefaultInstance().getService();

    @PostMapping("upload-images")
    public void uploadImages(@RequestParam("files") MultipartFile[] multipartFiles){
        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            BlobId blobId = BlobId.of("telephoners","folder/" + multipartFile.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            try {
                storage.create(blobInfo,multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
