package com.example.todolist.controller;

import com.example.todolist.dto.PageResponseDTO;
import com.example.todolist.dto.TaskCreateDTO;
import com.example.todolist.dto.TaskResponseDTO;
import com.example.todolist.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        // Si se pide paginación
        if (page != null && size != null) {
            Sort sort = direction.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<TaskResponseDTO> result;
            if (completed != null) {
                result = taskService.getTasksByStatus(completed, pageable);
            } else {
                result = taskService.getAllTasks(pageable);
            }

            // Convertir a PageResponseDTO
            return ResponseEntity.ok(PageResponseDTO.from(result));
        }

        // Sin paginación (comportamiento anterior)
        List<TaskResponseDTO> tasks;
        if (completed != null) {
            tasks = taskService.getTasksByStatus(completed);
        } else {
            tasks = taskService.getAllTasks();
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateDTO createDTO) {
        TaskResponseDTO created = taskService.createTask(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskCreateDTO updateDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, updateDTO));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponseDTO> toggleTaskCompletion(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.toggleTaskCompletion(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
