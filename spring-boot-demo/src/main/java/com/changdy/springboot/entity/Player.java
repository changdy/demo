package com.changdy.springboot.entity;

import lombok.Data;

import java.util.List;

@Data
public class Player {
    private String id;
    private String username;
    private String password;
    private String address;
    private String email;
    private Role role;
    private List<Sword> swords;
}