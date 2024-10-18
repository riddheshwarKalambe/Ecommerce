package com.ecommerce.model;

//import jakarta.persistence.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {

    @Id
    // @GeneratedValue(Secondary = GenerationType.IDENTITY) for integer dataya type only
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name="user_email", unique = true)
    private String email;

    @Column(name="user_password", length=10)
    private String password;

    @Column(name = "user_gender")
    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;


}
