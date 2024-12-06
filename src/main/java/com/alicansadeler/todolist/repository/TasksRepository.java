package com.alicansadeler.todolist.repository;

import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.entity.Tasks;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.enums.Priority;
import com.alicansadeler.todolist.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByUser(User user);
    List<Tasks> findByStatus(Status status);
    List<Tasks> findByPriority(Priority priority);
    List<Tasks> findByDueDateBefore(Date date);
    List<Tasks> findByUserAndStatus(User user, Status status);
    List<Tasks> findByUserOrderByDueDateAsc(User user);
}
