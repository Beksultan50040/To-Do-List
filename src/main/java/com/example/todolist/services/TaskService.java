package com.example.todolist.services;

import com.example.todolist.repositories.TaskRepository;
import com.example.todolist.exceptions.ApiError;
import com.example.todolist.mappers.TaskMapper;
import com.example.todolist.models.dtos.TaskDto;
import com.example.todolist.models.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAll(){
        return taskRepository.findAll();
    }
    public Task getById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Unknown task with id: " + id,
                        new ArrayList<>()));
    }

    public Task save(TaskDto dto){
        Task task = TaskMapper.INSTANCE.DtoToTask(dto);
        return taskRepository.save(task);
    }

    public Task update(Long id, TaskDto dto){

        Task task = getById(id);

        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setCreatedDate(dto.getCreatedDate());
        task.setClosedDate(dto.getClosedDate());

        return taskRepository.save(task);
    }

    public String delete(Long id){

        taskRepository.delete(getById(id));
        return "Successfully deleted";
    }
}
