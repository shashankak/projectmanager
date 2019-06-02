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
        response.setObject(data);
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
}
