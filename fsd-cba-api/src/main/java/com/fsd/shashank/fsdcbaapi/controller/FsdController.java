package com.fsd.shashank.fsdcbaapi.controller;

import com.fsd.shashank.fsdcbaapi.dto.ProjectDto;
import com.fsd.shashank.fsdcbaapi.dto.ResponseDto;
import com.fsd.shashank.fsdcbaapi.dto.TaskDto;
import com.fsd.shashank.fsdcbaapi.dto.UserDto;
import com.fsd.shashank.fsdcbaapi.service.FsdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class FsdController {
    public static final String FAILURE = "failure";
    public static final String SUCCESS = "success";

    Logger logger = LoggerFactory.getLogger(FsdController.class);

    @Autowired
    private FsdService fsdService;

    private ResponseDto createSuccessResponse(Object data) {
        ResponseDto response = new ResponseDto();
        response.setResult(SUCCESS);
        if (data instanceof List<?>) {
            response.setObjectList(data);
        } else {
            response.setObject(data);
        }
        return response;
    }

    private ResponseDto createFailureResponse() {
        ResponseDto response = new ResponseDto();
        response.setResult(FAILURE);
        return response;
    }

    @RequestMapping(path = "saveUser", method = RequestMethod.POST)
    public ResponseDto saveUser(@RequestBody UserDto userDto) {
        ResponseDto response = new ResponseDto();
        response = createFailureResponse();
        try {
            userDto = fsdService.saveUser(userDto);
            response = createSuccessResponse(userDto);
        } catch (Exception e) {
            // Nothing to throw.
            logger.error("There was error while saving user: " + userDto.getFirstName() + " Cause: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(path = "getAllUsers", method = RequestMethod.GET)
    public ResponseDto getAllUsers() {
        try {
            return createSuccessResponse(fsdService.getAllUsers());
        } catch (Exception e) {
            logger.error("There was error while retriving all users. Cause: " + e.getMessage());
            return createFailureResponse();
        }
    }

    @RequestMapping(path = "getAvailableManagers", method = RequestMethod.GET)
    public ResponseDto getAvailableManagers() {
        try {
            return createSuccessResponse(fsdService.getAvailableManagers());
        } catch (Exception e) {
            logger.error("There was error while retriving all managers. Cause: " + e.getMessage());
            return createFailureResponse();
        }
    }

    @RequestMapping(path = "getAvailableUsersForTask", method = RequestMethod.GET)
    public ResponseDto getAvailableUsersForTask() {
        try {
            return createSuccessResponse(fsdService.getAvailableUsersForTask());
        } catch (Exception e) {
            logger.error("There was error while retriving all users for task. Cause: " + e.getMessage());
            return createFailureResponse();
        }
    }

    @RequestMapping(path = "deleteUserById/{userId}", method = RequestMethod.DELETE)
    public ResponseDto deleteUserById(@PathVariable("userId") Integer userId) {
        ResponseDto response = new ResponseDto();
        response = createFailureResponse();
        try {
            response = createSuccessResponse(fsdService.deleteUser(userId));
        } catch (Exception e) {
            logger.error("There was error while deleting user: " + userId + " Cause: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(path = "saveProject", method = RequestMethod.POST)
    public ResponseDto saveProject(@RequestBody ProjectDto projectDto) {
        ResponseDto response = new ResponseDto();
        response = createFailureResponse();
        try {
            projectDto = fsdService.saveProject(projectDto);
            response = createSuccessResponse(projectDto);
        } catch (Exception e) {
            // Nothing to throw.
            logger.error("There was error while saving project: " + projectDto.getProject() + " Cause: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(path = "getAllProjects", method = RequestMethod.GET)
    public ResponseDto getAllProjects() {
        try {
            return createSuccessResponse(fsdService.getAllProjects());
        } catch (Exception e) {
            logger.error("There was error while retriving all projects. Cause: " + e.getMessage());
            return createFailureResponse();
        }
    }

    @RequestMapping(path = "deleteProjectById/{projectId}", method = RequestMethod.DELETE)
    public ResponseDto deleteProjectById(@PathVariable("projectId") Integer projectId) {
        ResponseDto response = new ResponseDto();
        response = createFailureResponse();
        try {
            response = createSuccessResponse(fsdService.deleteProject(projectId));
        } catch (Exception e) {
            logger.error("There was error while deleting project: " + projectId + " Cause: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(path = "saveTask", method = RequestMethod.POST)
    public ResponseDto saveTask(@RequestBody TaskDto taskDto) {
        ResponseDto response = new ResponseDto();
        response = createFailureResponse();
        try {
            taskDto = fsdService.saveTask(taskDto);
            response = createSuccessResponse(taskDto);
        } catch (Exception e) {
            // Nothing to throw.
            logger.error("There was error while saving task: " + taskDto.getTask() + " Cause: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(path = "getAllTasks", method = RequestMethod.GET)
    public ResponseDto getAllTasks() {
        try {
            return createSuccessResponse(fsdService.getAllTasks());
        } catch (Exception e) {
            logger.error("There was error while retrieving all tasks. Cause: " + e.getMessage());
            return createFailureResponse();
        }
    }



    @RequestMapping(path = "getTaskById/{taskId}", method = RequestMethod.GET)
    public ResponseDto getTaskById(@PathVariable("taskId") Integer taskId) {
        ResponseDto response = new ResponseDto();
        response = createFailureResponse();
        try {
            response = createSuccessResponse(fsdService.getTaskById(taskId));
        } catch (Exception e) {
            logger.error("There was error while loading task: " + taskId + " Cause: " + e.getMessage());
        }
        return response;
    }
}
