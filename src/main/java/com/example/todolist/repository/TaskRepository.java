package com.example.todolist.repository;


import com.example.todolist.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(Boolean completed);

    Page<Task> findByCompleted(Boolean completed, Pageable pageable);

    List<Task> findByTitleContainingIgnoreCase(String title);
}
