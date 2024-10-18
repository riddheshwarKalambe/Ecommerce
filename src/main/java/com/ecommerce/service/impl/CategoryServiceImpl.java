package com.ecommerce.service.impl;

import com.ecommerce.dtos.CategoryDto;
import com.ecommerce.dtos.PageableResponse;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.helper.Helper;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

        // create
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        // creating category id randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);// pass above categoey id generated value in dto

        Category category = mapper.map(categoryDto, Category.class);   // Dto -> Entity Convert
        Category saveCategory = categoryRepository.save(category);  // Data save in Entity
        CategoryDto categoryDto1 = mapper.map(saveCategory, CategoryDto.class);  // Entity -> Dto convert
        return categoryDto1;
    }


        // update
    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        // get Category of given Id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found with given id !!"));

        // update category Details
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);

        // Entity To Dto Convert
        CategoryDto categoryDto1 = mapper.map(updatedCategory, CategoryDto.class);
        return categoryDto1;
    }

        // delete
    @Override
    public void delete(String categoryId) {
        // get Category of given Id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found with given id !!"));

        String fullPath = imagePath + category.getCoverImage(); //i.e image/user/ + abc.jpg = image/user/abc.jpg
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
            logger.info("Category image not found in folder");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);//
        }

        categoryRepository.delete(category);
    }


    // getAll
    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort); // object create of pagebale and value set

        Page<Category> page = categoryRepository.findAll(pageable); // object pass and get data in the form of page beacuse pagable use

        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);// Helper->Class getPagableResonce method call
                                                // and pass page and get outpot as dto fomat of data in the orm of pageabkeResponse

        return pageableResponse;
    }


    //get single category by Id
    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found with given id !!"));

        return mapper.map(category, CategoryDto.class);
    }
}
