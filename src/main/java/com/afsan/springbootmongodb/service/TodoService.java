package com.afsan.springbootmongodb.service;
import com.afsan.springbootmongodb.exception.TodoCollectionException;
import com.afsan.springbootmongodb.model.TodoDTO;
import com.afsan.springbootmongodb.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepo;


    public void createTodo(TodoDTO todoDTO) throws TodoCollectionException, ConstraintViolationException {
        Optional<TodoDTO> todoDTOOptional = todoRepo.findByTodo(todoDTO.getTodo());
        if(todoDTOOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        }
        else{
            todoDTO.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todoDTO);
        }
    }

    public List<TodoDTO> getAllTodos(){
        List<TodoDTO> todoDTOList  = todoRepo.findAll();
        if(todoDTOList.size() > 0)
            return todoDTOList;
        else
            return new ArrayList<TodoDTO>();
    }

    public TodoDTO getSingleTodo(String id) throws TodoCollectionException {
        Optional<TodoDTO> todoDTOOptional = todoRepo.findById(id);
        if(!todoDTOOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }else
            return todoDTOOptional.get();
    }

    public void updateTodo(String id,TodoDTO todoDTO) throws TodoCollectionException{
        Optional<TodoDTO> todoDTOOptional = todoRepo.findById(id);
        Optional<TodoDTO> todoWithSameName = todoRepo.findByTodo(todoDTO.getTodo());
        if(todoDTOOptional.isPresent()){
            if(todoWithSameName.isPresent() && !todoWithSameName.get().getId().equals(id)){
                throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
            }
            TodoDTO todoUpdate = todoDTOOptional.get();
            todoUpdate.setTodo(todoDTO.getTodo());
            todoUpdate.setDescription(todoDTO.getDescription());
            todoUpdate.setCompleted(todoDTO.getCompleted());
            todoUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todoUpdate);
        }else{
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
    }

    public void deleteTodoById(String id) throws TodoCollectionException{
        Optional<TodoDTO> todoDTOOptional  = todoRepo.findById(id);
        if(!todoDTOOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }else{
            todoRepo.deleteById(id);

        }
    }
}
