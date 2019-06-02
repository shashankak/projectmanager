package com.fsd.shashank.fsdcbaapi.repository;

import com.fsd.shashank.fsdcbaapi.entity.Project;
import com.fsd.shashank.fsdcbaapi.entity.Task;
import com.fsd.shashank.fsdcbaapi.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
    User findByProject(Project project);

    User findByTask(Task task);
}
