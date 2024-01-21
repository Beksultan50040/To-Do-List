package com.example.todolist.controllers;

import com.example.todolist.models.dtos.TaskDto;
import com.example.todolist.models.entity.Task;
import com.example.todolist.models.enums.Status;
import com.example.todolist.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;
    private TaskDto taskDto;

    Long id;
    @BeforeEach
    public void setUp(){
        id = 1L;

        task = new Task();
        task.setId(id);
        task.setDescription("test");
        task.setStatus(Status.NOT_DONE);
        task.setCreatedDate(LocalDateTime.now());

        taskDto = new TaskDto();
        taskDto.setDescription("test");
        taskDto.setStatus(Status.DONE);
        taskDto.setCreatedDate(LocalDateTime.now());
    }

    @Test
    public void getAllTasks() throws Exception {
        List<Task> tasks = List.of(task, task);
        given(taskService.getAll()).willReturn(tasks);

        mockMvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getTaskById_ExistingTaskId_ReturnsTask() throws Exception {
        given(taskService.getById(anyLong())).willReturn(task);

        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.status", is(String.valueOf(task.getStatus()))));
    }

    @Test
    public void createTask() throws Exception {
        Task createdTask = new Task();
        createdTask.setId(id);
        createdTask.setDescription(taskDto.getDescription());
        createdTask.setStatus(taskDto.getStatus());
        createdTask.setCreatedDate(taskDto.getCreatedDate());

        given(taskService.save(any(TaskDto.class))).willReturn(createdTask);

        // Performing the request and verifying the response
        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.description", is(createdTask.getDescription())))
                .andExpect(jsonPath("$.status", is(String.valueOf(createdTask.getStatus()))));
    }

    @Test
    public void updateTask_ExistingTaskIdAndValidTaskDto_ReturnsUpdatedTask() throws Exception {

        Task updatedTask = new Task();
        updatedTask.setId(id);
        updatedTask.setDescription(taskDto.getDescription());
        updatedTask.setStatus(taskDto.getStatus());
        updatedTask.setCreatedDate(taskDto.getCreatedDate());

        given(taskService.update(eq(id), any(TaskDto.class))).willReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/task//{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.description", is(updatedTask.getDescription())))
                .andExpect(jsonPath("$.status", is(String.valueOf(updatedTask.getStatus()))));
    }
}