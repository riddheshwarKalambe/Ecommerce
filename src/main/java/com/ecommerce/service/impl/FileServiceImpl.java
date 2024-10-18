package com.ecommerce.service.impl;

import com.ecommerce.exception.BadApiRequestException;
import com.ecommerce.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename(); // i have recieved file in input and after that
             //if orignal file name is(ex. abc.png)           // we can get which file uploaded this file name get
        logger.info("Filename : {}", originalFilename);
        // But i want to change name of file which is uploaded (i.e originalFileName) because some times if same file name generated then what
        // that's why we can generated file name randomly
        //and after that we can add randomly generated string + originalFilename = extension(i.e filenamewithextension)

        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); //(ex. .png)

                String fileNameWithExtension = filename+extension;  // we will used as name of file
        String fullPathWithFileName= path + fileNameWithExtension; //  we will used as path of file
                                                                // File.separator(i.e /)(i.e "folder" + File.separator + "subfolder")
        logger.info("full image path: {} ", fullPathWithFileName);
        if(extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg")) {
            // file save
            logger.info("file extension is {}", extension);
            File folder = new File(path); // if i dont have a path then create a folder of that "new" path

            if (!folder.exists()) {
                //create the folder
                folder.mkdirs();  //mkdir -> create a singal folder and mkdirs -> create a folder and its sub folder also
            }
                // upload
            // save file on location   input file data        where to save file path
                Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName)); //save file
            return fileNameWithExtension;
        } else {
                    throw new BadApiRequestException("File with this "+ extension+ " not allowed !!");
            }

    }



    // method 2 for serve the image (i.e.retrieve a image from userId)
    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path+File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
