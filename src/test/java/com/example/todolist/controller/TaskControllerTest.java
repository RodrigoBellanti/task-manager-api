package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDTO;
import com.example.todolist.dto.TaskResponseDTO;
import com.example.todolist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    private TaskResponseDTO taskResponse;
    private TaskCreateDTO taskCreate;

    @BeforeEach
    void setUp() {
        taskResponse = new TaskResponseDTO(
                1L,
                "Test Task",
                "Test Description",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        taskCreate = new TaskCreateDTO();
        taskCreate.setTitle("Test Task");
        taskCreate.setDescription("Test Description");
    }

    @Test
    void getAllTasks_WithoutFilter_ReturnsAllTasks() throws Exception {
        // Arrange
        List<TaskResponseDTO> tasks = Arrays.asList(taskResponse);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void getAllTasks_WithCompletedFilter_ReturnsFilteredTasks() throws Exception {
        // Arrange
        TaskResponseDTO completedTask = new TaskResponseDTO(
                2L,
                "Completed Task",
                "Description",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(taskService.getTasksByStatus(true)).thenReturn(Arrays.asList(completedTask));

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                        .param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].completed").value(true));
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(taskResponse);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void createTask_ValidData_ReturnsCreatedTask() throws Exception {
        // Arrange
        when(taskService.createTask(any(TaskCreateDTO.class))).thenReturn(taskResponse);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void createTask_TitleTooShort_ReturnsBadRequest() throws Exception {
        // Arrange
        taskCreate.setTitle("Ab");

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void createTask_EmptyTitle_ReturnsBadRequest() throws Exception {
        // Arrange
        taskCreate.setTitle("");

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void updateTask_ValidData_ReturnsUpdatedTask() throws Exception {
        // Arrange
        taskCreate.setTitle("Updated Task");
        TaskResponseDTO updatedResponse = new TaskResponseDTO(
                1L,
                "Updated Task",
                "Test Description",
                false,
                taskResponse.getCreatedAt(),
                LocalDateTime.now()
        );
        when(taskService.updateTask(eq(1L), any(TaskCreateDTO.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void toggleTaskCompletion_ExistingTask_ReturnsToggledTask() throws Exception {
        // Arrange
        TaskResponseDTO toggledResponse = new TaskResponseDTO(
                1L,
                "Test Task",
                "Test Description",
                true,
                taskResponse.getCreatedAt(),
                LocalDateTime.now()
        );
        when(taskService.toggleTaskCompletion(1L)).thenReturn(toggledResponse);

        // Act & Assert
        mockMvc.perform(patch("/api/tasks/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTask_ExistingId_ReturnsNoContent() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}