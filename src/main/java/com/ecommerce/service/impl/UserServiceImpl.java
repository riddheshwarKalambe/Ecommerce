package com.ecommerce.service.impl;


import com.ecommerce.dtos.PageableResponse;
import com.ecommerce.dtos.UserDto;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.helper.Helper;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
//import jakarta.persistence.PrePersist;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.PrePersist;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);



    @Override
    @PrePersist  //
    public UserDto createUser(UserDto userDto) {

        // generate unique id in string formate automatic

        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        // IN repository layer data transfer in the form of entity  but present in dto format .i.e converted data dto to entity
        User user= dtoToEntity(userDto);
        User savedUser = userRepository.save(user); // jpa method

        // but return in data in the form of dto and above step is created data in the form Entity .i.e data is convered Entity into dto
        UserDto newDto= entityToDto(savedUser);
        return newDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        
        // user data retrive by id if data is not retrive from database then error will be shown
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

        //  user data update (i.e.Setdata) but my input data in userdto format and i want to set data into user (i.e userDto-->User)
        user.setUserName(userDto.getUserName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        //user.setEmail(userDto.getEmail())


        // save data
        User updatedUser = userRepository.save(user); // rightnow updatedUser data is present in User form
                                                    // but this method return type is UserDto

        // i.e User --> UserDto
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId)   {

        // user data retrive by id if data is not retrive from database then error will be shown
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

        // If user deleted then its profie photo also deleted, i want image path were to store image(i.e full path = get image form user +  path of application .properties)
        //delete user frofile image
        //i.e image/user/ +   abc.jpg
        String fullPath = imagePath + user.getImageName();//i.e image/user/ + abc.jpg = image/user/abc.jpg
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
            logger.info("User image not found in folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);//
        }

        //delete user
        userRepository.delete(user);

    }

    @Override
    //public List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
     //PageNumber defaults starts from 0                                       // pageRequest is method of pagable interface

       // Sort sort = Sort.by(sortBy);     // sort object created by using FACTORY METHOD Sort.bY
         Sort sort = (sortDir.equalsIgnoreCase("dssc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;
         //Ternary operator use for i give condition and it return responce true/false (i.e Ascending/Decending)

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);    //pagable it is interface thats way object not create
                                                                    //pageRequestmethod is collect and store data in pageable
            // sort object pass


//        List<User> users =  userRepository.findAll();     // use in without pageination all data shows

        Page<User> page=  userRepository.findAll(pageable); // return page object

/*          // one global method create for pageintion and sorting in Helper class

        List<User> users = page.getContent(); // list return by pageination

        // List<User> --> List<UserDto> (it is converted many way for each loop, stream Api)
        // we used stream Api
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        //return dtoList;


        //steps increase for pageableformat view and extra class create in dto layer i.e pagabaleResponse
        PageableResponse<UserDto> response = new PageableResponse<>(); // all value set in pageableResponce class for show pagable format
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

*/

        // Above comment step will call as a helper

        PageableResponse<UserDto> response =Helper.getPageableResponse(page, UserDto.class);


        return response;
    }

    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

        return entityToDto(user); // user to dto convert and return
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found with this given emailId !!"));
        return entityToDto(user);
    }

   @Override
    public List<UserDto> searchUser(String keyword) {

        List<User> users = userRepository.findByUserNameContaining(keyword);// entity users list find by searching keyword
        //List<UserDto> dtoList = users.stream().map(this::entityToDto).collect(Collectors.toList());//list of user converted into list of userdto (i.e entity-->userDto)
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }


    private User dtoToEntity(UserDto userDto) {

//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .userName(userDto.getUserName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();

        return mapper.map(userDto, User.class); // for singal userdto to entityuser convert
    }

    private UserDto entityToDto(User savedUser) {

//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .userName(savedUser.getUserName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .about(savedUser.getPassword())
//                .imageName(savedUser.getImageName()).build();

        return mapper.map(savedUser, UserDto.class);
    }


}