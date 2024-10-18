package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity                 // data store
@Table(name = "categories")
public class Category {

     @Id
     @Column(name = "id")
    private  String categoryId;

     @Column(name = "category_title", length = 60,nullable = false)
    private String title;

     @Column(name = "category_desc", length = 500)
    private String description;

     @Column(name = "category_cover_image")
    private String coverImage;

    // other attributes if you have...

}
