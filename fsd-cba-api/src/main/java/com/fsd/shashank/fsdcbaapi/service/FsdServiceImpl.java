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

    @Autowired
    private ProjectRepo projectRepo;

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

    @Override
    public ProjectDto saveProject(ProjectDto projectDto) {
        try {
            Project project = projectDtoToEntity(projectDto);
            project = projectRepo.save(project);
            projectDto.setProjectId(project.getProjectId());
            User user = userRepo.findById(projectDto.getManager().getUserId()).get();
            user.setProject(project);
            user = userRepo.save(user);
        } catch (Exception e) {
            throw e;
        }
        return projectDto;
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projectDtos = new ArrayList<>();
        try {
            List<Project> projects = (List<Project>) projectRepo.findAll();
            for (Project project : projects) {
                projectDtos.add(projectEntityToDto(project));
            }
        } catch (Exception e) {
            throw e;
        }
        return projectDtos;
    }

    @Override
    public Boolean deleteProject(Integer projectId) {
        try {
            if (projectId == null || projectId <= 0) {
                throw new RuntimeException("Invalid UserID");
            }
            Project project = projectRepo.findById(projectId).get();
            User user = userRepo.findByProject(project);
            if (user != null) {
                user.setProject(null);
                user = userRepo.save(user);
            }
            projectRepo.delete(project);
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

    private ProjectDto projectEntityToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();

        projectDto.setProjectId(project.getProjectId());
        projectDto.setProject(project.getProject());
        projectDto.setPriority(project.getPriority());
        projectDto.setStartDate(DateConverter.convert(project.getStartDate()));
        projectDto.setEndDate(DateConverter.convert(project.getEndDate()));

        projectDto.setManager(userEntityToDto(userRepo.findByProject(project)));

        projectDto.setCompleted(project.getEndDate() != null ? (project.getEndDate().compareTo(new Date()) == -1) : false);

        int completedTasks = 0;
        for (Task task : project.getTasks()) {
            if (task.getEndDate() != null ? (task.getEndDate().compareTo(new Date()) == -1) : false) {
                completedTasks = completedTasks + 1;
            }
        }
        projectDto.setNoOfCompletedTasks(completedTasks);
        projectDto.setNoOfTasks(project.getTasks().size());
        projectDto.setStartDateIsEndDate(project.getEndDate() != null ? (project.getEndDate().compareTo(new Date()) == 0) : false);

        return projectDto;
    }

    private Project projectDtoToEntity(ProjectDto projectDto) {

        Project project = new Project();
        project.setProjectId(projectDto.getProjectId());
        project.setProject(projectDto.getProject());
        project.setPriority(projectDto.getPriority());
        project.setStartDate(DateConverter.convert(projectDto.getStartDate()));
        if (projectDto.getStartDateIsEndDate()) {
            project.setEndDate(DateConverter.convert(projectDto.getStartDate()));
        } else {
            project.setEndDate(DateConverter.convert(projectDto.getEndDate()));
        }

        return project;
    }
}
