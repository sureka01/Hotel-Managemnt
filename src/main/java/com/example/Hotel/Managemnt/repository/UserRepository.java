package com.example.Hotel.Managemnt.repository;

import com.example.Hotel.Managemnt.entity.User;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);


    List<User> findByUserRoleIgnoreCase(String userRole);

    void deleteById(Integer id);

    Optional<User> findByEmail(String email);

}
