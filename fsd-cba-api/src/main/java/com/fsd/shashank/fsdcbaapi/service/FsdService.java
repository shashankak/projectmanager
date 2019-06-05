package com.fsd.shashank.fsdcbaapi.service;

import com.fsd.shashank.fsdcbaapi.dto.ProjectDto;
import com.fsd.shashank.fsdcbaapi.dto.TaskDto;
import com.fsd.shashank.fsdcbaapi.dto.UserDto;
import com.fsd.shashank.fsdcbaapi.entity.Task;
import com.fsd.shashank.fsdcbaapi.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FsdService {
    UserDto saveUser(UserDto userDto);

    List<UserDto> getAllUsers();

    Boolean deleteUser(Integer userId);

    ProjectDto saveProject(ProjectDto projectDto);

    List<ProjectDto> getAllProjects();

    Boolean deleteProject(Integer projectId);

    TaskDto saveTask(TaskDto taskDto);

    List<TaskDto> getAllTasks();

    List<UserDto> getAvailableManagers();

    List<UserDto> getAvailableUsersForTask();

    TaskDto getTaskById(Integer taskId);

}
