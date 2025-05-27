package com.example.Hotel.Managemnt.dto;


import lombok.Data;

import java.util.Date;

@Data
public class userDTO {

private Integer id;


    private String name;


    private String username;


    private String email;


    private String password;


    private Date createdDateTime;



    private Date lastUpdatedDateTime;


    private String createdUser;

    private String userRole;
}


