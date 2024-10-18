package com.ecommerce.dtos;


import lombok.*;

import java.util.List;

// for pageable purpose we can create a separate class in our project and real time project we can create a global class same this class type
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {

   // private List<UserDto> content;  // if we write this <UserDto> then this pageableResponse class is  only specific use for UserDto

    // but I want to create a global/ general type then we can use or convert into generic type <T> thats why we also write in class name <T>
    private List<T> content;  //<T> when we create an object of class then we pass which type of object create then same list also that type object create
                                // be change that list to same type (i.e <T>)

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
}
