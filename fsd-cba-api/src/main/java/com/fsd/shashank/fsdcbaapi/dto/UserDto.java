package com.fsd.shashank.fsdcbaapi.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String employeeId;

    public UserDto() {

    }

    public UserDto(Integer userId) {
        this.userId = userId;
    }

    public UserDto(Integer userId, String firstName, String lastName, String employeeId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }
}
