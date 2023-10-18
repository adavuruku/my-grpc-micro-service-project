package com.example.serviceclient.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.serviceclient.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileService {
    private final Cloudinary cloudinaryConfig;

    public FileService(Cloudinary cloudinaryConfig){
        this.cloudinaryConfig = cloudinaryConfig;
    }
    public FileResponse uploadFile(MultipartFile fileToUpload) {
        try {
            File uploadedFile = convertMultiPartToFile(fileToUpload);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            boolean isDeleted = uploadedFile.delete();

            if (isDeleted){
                System.out.println("File successfully deleted");
            }else
                System.out.println("File doesn't exist");
            return  FileResponse.build(uploadResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
