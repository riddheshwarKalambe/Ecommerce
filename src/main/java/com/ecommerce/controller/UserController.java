package com.ecommerce.controller;

import com.ecommerce.dtos.ApiResponseMessage;
import com.ecommerce.dtos.ImageResponse;
import com.ecommerce.dtos.PageableResponse;
import com.ecommerce.dtos.UserDto;
import com.ecommerce.service.FileService;
import com.ecommerce.service.UserService;
//import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;  // dynamic set value imageuploadedpath from application.propertie
                                    // and its configuration take it in application.properties file

    private Logger logger = LoggerFactory.getLogger(UserController.class);





                                 // create

            //@ResponseEntity is a powerful and flexible way to return HTTP responses
            // with detailed control over status codes, headers, and the body of the response.
    @PostMapping(value="/createUser")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }





    //update                                            update input -> id + Data(json) i.e UserDto
    @PutMapping("/{userId}")                     //(" ") this is a path uri variable i.e dynamic value set through url api
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto)
    //@PathVariable annotation is used to extract the value from the URI(i.euserId)
    // and this value gives to String userId (i.e. putmapping/url -> pathvariable and pathvariable-> String UserId i.e input)

    {
        UserDto UpdateduserDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(UpdateduserDto1, HttpStatus.OK);
    }


    // delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId)  //if pathvariable and java variable both are same
                                                                                    // then we don't write is also ok
    // id we can send general msg then we use ApiResponseMessage object in responseentity because it is properly converted general msg into json format
    {
        userService.deleteUser(userId);
        ApiResponseMessage message
                = ApiResponseMessage
                .builder()
                .message("User is deleted Successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    // get all
    @GetMapping
   // public ResponseEntity<List<UserDto>> getAllUsers(
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(   // whenever you want to use pagaination in your project then you can use PagebleResponse
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "userName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    )               // @RequestParam values came from the url if client/user gives it is not mendatory
    {
        // List<UserDto> allUsers = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    // search user
    @GetMapping("/search/{Keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("Keyword") String keyword){
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }


    //======================= For Imageupload(i.e user information Store) and file serve

    // upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);// upload file

        UserDto user= userService.getUserById(userId);// input pass user id that user's database image update
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build(); //response msg only
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    // serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        //
        UserDto user = userService.getUserById(userId);
        logger.info("user image name : {} ", user.getImageName());  // i get a image name
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream()); // copy resource data into response
    }






}
