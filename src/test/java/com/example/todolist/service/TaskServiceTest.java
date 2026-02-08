package com.example.todolist.service;

import com.example.todolist.dto.TaskCreateDTO;
import com.example.todolist.dto.TaskResponseDTO;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.exception.ResourceNotFoundException;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        createDTO = new TaskCreateDTO();
        createDTO.setTitle("Test Task");
        createDTO.setDescription("Test Description");
    }

    @Test
    void getAllTasks_ReturnsListOfTaskResponseDTO() {
        // Arrange
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Second Task");
        task2.setDescription("Second Description");
        task2.setCompleted(true);
        task2.setCreatedAt(LocalDateTime.now());
        task2.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task, task2));

        // Act
        List<TaskResponseDTO> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        assertEquals("Second Task", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_ExistingId_ReturnsTaskResponseDTO() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskResponseDTO result = taskService.getTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertFalse(result.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_NonExistingId_ThrowsTaskNotFoundException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void createTask_ValidDTO_ReturnsTaskResponseDTO() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        TaskResponseDTO result = taskService.createTask(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertFalse(result.getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ExistingId_ReturnsUpdatedTaskResponseDTO() {
        // Arrange
        TaskCreateDTO updateDTO = new TaskCreateDTO();
        updateDTO.setTitle("Updated Task");
        updateDTO.setDescription("Updated Description");

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setCompleted(false);
        updatedTask.setCreatedAt(task.getCreatedAt());
        updatedTask.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        TaskResponseDTO result = taskService.updateTask(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_NonExistingId_ThrowsResourceNotFoundException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTask(999L, createDTO);
        });
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void toggleTaskCompletion_ExistingTask_TogglesCompleted() {
        // Arrange
        task.setCompleted(false);
        Task toggledTask = new Task();
        toggledTask.setId(1L);
        toggledTask.setTitle(task.getTitle());
        toggledTask.setDescription(task.getDescription());
        toggledTask.setCompleted(true);
        toggledTask.setCreatedAt(task.getCreatedAt());
        toggledTask.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(toggledTask);

        // Act
        TaskResponseDTO result = taskService.toggleTaskCompletion(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_ExistingId_DeletesSuccessfully() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_NonExistingId_ThrowsResourceNotFoundException() {
        // Arrange
        when(taskRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(999L);
        });
        verify(taskRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void getTasksByStatus_CompletedTrue_ReturnsCompletedTasks() {
        // Arrange
        task.setCompleted(true);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Another Completed Task");
        task2.setDescription("Description");
        task2.setCompleted(true);
        task2.setCreatedAt(LocalDateTime.now());
        task2.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.findByCompleted(true)).thenReturn(Arrays.asList(task, task2));

        // Act
        List<TaskResponseDTO> result = taskService.getTasksByStatus(true);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(TaskResponseDTO::getCompleted));
        verify(taskRepository, times(1)).findByCompleted(true);
    }

    @Test
    void getTasksByStatus_CompletedFalse_ReturnsIncompleteTasks() {
        // Arrange
        when(taskRepository.findByCompleted(false)).thenReturn(Arrays.asList(task));

        // Act
        List<TaskResponseDTO> result = taskService.getTasksByStatus(false);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getCompleted());
        verify(taskRepository, times(1)).findByCompleted(false);
    }
}