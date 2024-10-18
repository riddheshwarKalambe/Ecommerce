package com.ecommerce.repository;

import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
// persistance layer
public interface UserRepository extends JpaRepository <User, String> {

    Optional<User> findByEmail(String email);   // find means in sql query will be create on where condition and optional represents in user value present or not

   // User findByEmailAndPassword(String email, String password); // find means in sql query will be create on where condition

   List<User> findByUserNameContaining(String keyword);      // search in matching keywords and return list of word were used
                                                           // and we have used in sql query for searching purpose (like condition) like for keyword (i.e containg/search )


}
