package com.ecommerce.service;

// for image Save purpose and serve

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    //method1 for save image or upload image

     String uploadFile(MultipartFile file, String path) throws IOException; // in input 2 parameter passes
                                                                // (1 which file, 2 which location you want to save)

    // method 2 for serve the image

    InputStream getResource(String path, String name) throws FileNotFoundException;



}
