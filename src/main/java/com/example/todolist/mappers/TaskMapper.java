package com.example.todolist.mappers;

import com.example.todolist.models.dtos.TaskDto;
import com.example.todolist.models.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task DtoToTask(TaskDto dto);
}