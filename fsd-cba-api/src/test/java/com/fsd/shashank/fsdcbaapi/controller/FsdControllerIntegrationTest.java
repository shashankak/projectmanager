package com.fsd.shashank.fsdcbaapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsd.shashank.fsdcbaapi.FsdCbaApiApplication;
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
import com.fsd.shashank.fsdcbaapi.service.FsdService;
import com.fsd.shashank.fsdcbaapi.utils.DateConverter;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FsdCbaApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsdControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private FsdService fsdService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ParentTaskRepo parentTaskRepo;
    @Autowired
    private TaskRepo taskRepo;

    User userToDelete;
    User manager;
    Project project;
    Project projectToDelete;
    ParentTask parentTask;
    Task taskToDelete;
    Task taskWithParent;
    Task task;


    @Before
    public void setup() {
        userToDelete = userRepo.save(getMockUser(null, "Shashank", "Test", "10"));
        manager = userRepo.save(getMockUser(null, "Sachin", "Tendulkar", "11"));
        project = projectRepo.save(getMockProject(null, "Test Project",
                DateConverter.convert("2019-01-01"), null, false, 1, null));
        projectRepo.save(getMockProject(null, "Test Project Two",
                DateConverter.convert("2019-01-01"), DateConverter.convert("2019-01-01"), false, 1, null));
        projectRepo.save(getMockProject(null, "Test Project Two",
                DateConverter.convert("2019-01-01"), DateConverter.convert("2019-02-01"), false, 1, null));
        projectRepo.save(getMockProject(null, "Test Project Two",
                DateConverter.convert("2019-01-01"), DateConverter.convert("2025-02-01"), false, 1, null));
        projectToDelete = projectRepo.save(getMockProject(null, "Test Project One",
                new Date(), null, false, 2, manager));
        parentTask = parentTaskRepo.save(getMockParentTask(null, "Default Task"));
        task = taskRepo.save(getMockTask(null, "Default Task", DateConverter.convert("2019-01-01"), null, 1, null, project, null));
        taskRepo.save(getMockTask(null, "Task with same end date", DateConverter.convert("2019-01-01"), DateConverter.convert("2019-01-01"), 1, null, project, null));
        taskRepo.save(getMockTask(null, "Task with greater end date", DateConverter.convert("2019-01-01"), DateConverter.convert("2019-02-01"), 1, null, project, null));
        taskRepo.save(getMockTask(null, "Task with future end date", DateConverter.convert("2019-01-01"), DateConverter.convert("2025-02-01"), 1, null, project, null));
        task = taskRepo.findById(task.getTaskId()).get();
        taskWithParent = taskRepo.save(getMockTask(null, "Default Task One", DateConverter.convert("2019-01-01"), null, 1, null, project, task));
        taskToDelete = taskRepo.save(getMockTask(null, "Default Task Two", DateConverter.convert("2019-01-01"), null, 1, null, project, null));

    }

    @Test
    public void testASaveUser() throws Exception {
        UserDto userDto = new UserDto(null, "Shashank", "Test", "10");
        mvc.perform(MockMvcRequestBuilders
                .post("/saveUser")
                .content(asJsonString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId", greaterThan(0)));

    }

    @Test
    public void testASaveUserError() throws Exception {
        UserDto userDto = new UserDto(null, null, null, null);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveUser")
                .content(asJsonString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testAGetAllUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getAllUsers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    public void testADeleteUserById() throws Exception {
        Integer userId = userToDelete.getUserId();
        mvc.perform(MockMvcRequestBuilders.delete("/deleteUserById/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", is(true)));
    }

    @Test
    public void testADeleteUserByIdFailure() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/deleteUserById/1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testADeleteUserByIdFailureForZero() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/deleteUserById/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testBSaveProject() throws Exception {
        UserDto userDto = new UserDto(manager.getUserId(), "Shashank", "Lst", "10");
        ProjectDto projectDto = new ProjectDto(null, "Test Project", "2019-05-30", null, false, 1, userDto);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveProject")
                .content(asJsonString(projectDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.projectId", greaterThan(0)));

    }

    @Test
    public void testBSaveProjectStartDateIsEndDate() throws Exception {
        UserDto userDto = new UserDto(manager.getUserId(), "Shashank", "Lst", "10");
        ProjectDto projectDto = new ProjectDto(null, "Test Project", "2019-05-30", null, true, 1, userDto);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveProject")
                .content(asJsonString(projectDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.projectId", greaterThan(0)));

    }

    @Test
    public void testBSaveProjectFailure() throws Exception {
        ProjectDto projectDto = new ProjectDto(null, null, null, null, null, null, null);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveProject")
                .content(asJsonString(projectDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testBGetAllProjects() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getAllProjects")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    public void testBDeleteProjectById() throws Exception {
        Integer projectId = projectToDelete.getProjectId();
        mvc.perform(MockMvcRequestBuilders.delete("/deleteProjectById/" + projectId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", is(true)));
    }

    @Test
    public void testBDeleteProjectByIdFailure() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/deleteProjectById/1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testBDeleteProjectByIdFailureForZero() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/deleteProjectById/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testCSaveTask() throws Exception {
        TaskDto taskDto = new TaskDto(null, new ProjectDto(project.getProjectId()), "Test Task", false, 1, null, "2019-05-30", null, new UserDto(manager.getUserId()), null);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveTask")
                .content(asJsonString(taskDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.taskId", greaterThan(0)));

    }
    @Test
    public void testCSaveTaskWIthParent() throws Exception {
        TaskDto taskDto = new TaskDto(null, new ProjectDto(project.getProjectId()), "Test Task", false, 1, new TaskDto(task.getTaskId(), ""), "2019-05-30", null, new UserDto(manager.getUserId()), null);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveTask")
                .content(asJsonString(taskDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.taskId", greaterThan(0)));

    }
    @Test
    public void testCSaveTaskWIthParentBlak() throws Exception {
        TaskDto taskDto = new TaskDto(null, new ProjectDto(project.getProjectId()), "Test Task", false, 1, new TaskDto(null, ""), "2019-05-30", null, new UserDto(manager.getUserId()), null);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveTask")
                .content(asJsonString(taskDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.taskId", greaterThan(0)));

    }
    @Test
    public void testCSaveTaskThisIsParent() throws Exception {
        TaskDto taskDto = new TaskDto(null, new ProjectDto(project.getProjectId()), "Test Task", true, 1, null, "2019-05-30", null, new UserDto(manager.getUserId()), null);
        mvc.perform(MockMvcRequestBuilders
                .post("/saveTask")
                .content(asJsonString(taskDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.taskId", greaterThan(0)));

    }

    @Test
    public void testCSaveTaskFailure() throws Exception {
        TaskDto taskDto = new TaskDto();

        mvc.perform(MockMvcRequestBuilders
                .post("/saveTask")
                .content(asJsonString(taskDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testCGetAllTasks() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getAllTasks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test
    public void testCDeleteTaskById() throws Exception {
        Integer taskId = taskToDelete.getTaskId();
        mvc.perform(MockMvcRequestBuilders.delete("/deleteTaskById/" + taskId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", is(true)));
    }

    @Test
    public void testCDeleteTaskByIdFailure() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/deleteTaskById/1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    @Test
    public void testCDeleteTaskByIdFailureForZero() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/deleteTaskById/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", is("failure")));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User getMockUser(Integer userId, String firstName, String lastName, String employeeID) {
        User user = new User();
        user.setUserId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmployeeId(employeeID);
        return user;
    }

    private Project getMockProject(Integer projectId, String projectName, Date startDate,
                                   Date endDate, Boolean startDateIsEndDate,
                                   Integer priority, User manager) {
        Project project = new Project();
        project.setProjectId(projectId);
        project.setProject(projectName);
        project.setEndDate(endDate);
        project.setStartDate(startDate);
        project.setPriority(priority);

        return project;
    }
    private Task getMockTask(Integer taskId, String taskName, Date startDate,
                             Date endDate, Integer priority, String status,
                             Project project, Task parentTask) {
        Task task = new Task();

        task.setTaskId(taskId);
        task.setTask(taskName);
        task.setEndDate(endDate);
        task.setParent(parentTask);
        task.setPriority(priority);
        task.setProject(project);
        task.setStartDate(startDate);
        task.setStatus(status);

        return task;
    }

    private ParentTask getMockParentTask(Integer parentTaskId, String parentTaskName) {
        ParentTask parentTask = new ParentTask();
        parentTask.setParentId(parentTaskId);
        parentTask.setParentTask(parentTaskName);
        return parentTask;
    }
}
