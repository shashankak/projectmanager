package com.fsd.shashank.fsdcbaapi.service;

import com.fsd.shashank.fsdcbaapi.dto.ProjectDto;
import com.fsd.shashank.fsdcbaapi.dto.TaskDto;
import com.fsd.shashank.fsdcbaapi.dto.UserDto;
import com.fsd.shashank.fsdcbaapi.entity.ParentTask;
import com.fsd.shashank.fsdcbaapi.entity.Project;
import com.fsd.shashank.fsdcbaapi.entity.Task;
import com.fsd.shashank.fsdcbaapi.entity.User;
import com.fsd.shashank.fsdcbaapi.repository.ParentTaskRepo;
import com.fsd.shashank.fsdcbaapi.repository.ProjectRepo;
import com.fsd.shashank.fsdcbaapi.repository.TaskRepo;
import com.fsd.shashank.fsdcbaapi.repository.UserRepo;
import com.fsd.shashank.fsdcbaapi.utils.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FsdServiceImpl implements FsdService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto saveUser(UserDto userDto) {
        try {
            User user = userRepo.save(userDtoToEntity(userDto));
            userDto.setUserId(user.getUserId());
        } catch (Exception e) {
            throw e;
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = new ArrayList<>();

        try {
            List<User> users = (List<User>) userRepo.findAll();
            for (User user : users) {
                userDtos.add(userEntityToDto(user));
            }
        } catch (Exception e) {
            throw e;
        }

        return userDtos;
    }

    @Override
    public Boolean deleteUser(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new RuntimeException("Invalid UserID");
            }
            userRepo.deleteById(userId);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    private User userDtoToEntity(UserDto userDto) {
        User user = new User();

        user.setUserId(userDto.getUserId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmployeeId(userDto.getEmployeeId());

        return user;
    }

    private UserDto userEntityToDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmployeeId(user.getEmployeeId());
        return userDto;
    }
}
