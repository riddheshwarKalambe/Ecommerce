package com.ecommerce.helper;

import com.ecommerce.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){  // U & V both are GENERIC TYPE  // SUPPOSE U(entity) & V(dto)
        List<U> entity = page.getContent();
        //List<UserDto> dtoList = entity.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());


        //steps increase for pageableformat view and extra class create in dto layer i.e pagabaleResponce
        PageableResponse<V> response = new PageableResponse<>(); // all value set in pageableResponce class for show pagable format
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }
}
