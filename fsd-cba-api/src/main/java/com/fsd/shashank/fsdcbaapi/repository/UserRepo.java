package com.fsd.shashank.fsdcbaapi.repository;

import com.fsd.shashank.fsdcbaapi.entity.Project;
import com.fsd.shashank.fsdcbaapi.entity.Task;
import com.fsd.shashank.fsdcbaapi.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Integer> {
    User findByProject(Project project);

    User findByTask(Task task);

    List<User> findAllByProjectIsNull();

    List<User> findAllByTaskIsNull();
}
