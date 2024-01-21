package com.example.todolist.controllers;

import com.example.todolist.models.dtos.TaskDto;
import com.example.todolist.models.entity.Task;
import com.example.todolist.services.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable @NotNull(message = "task id must not be empty") Long id){
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskDto dto){
        return ResponseEntity.ok(taskService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable @NotNull(message = "task id must not be empty") Long id,
                                           @RequestBody @Valid TaskDto dto){
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable @NotNull(message = "task id must not be empty") Long id){
        return ResponseEntity.ok(taskService.delete(id));
    }
}
