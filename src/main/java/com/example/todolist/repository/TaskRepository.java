package com.example.todolist.repository;


import com.example.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Spring Data JPA genera automáticamente la implementación
    List<Task> findByCompleted(Boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String title);
}
