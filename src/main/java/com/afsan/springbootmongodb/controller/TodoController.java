package com.afsan.springbootmongodb.controller;


import com.afsan.springbootmongodb.exception.TodoCollectionException;
import com.afsan.springbootmongodb.model.TodoDTO;
import com.afsan.springbootmongodb.repository.TodoRepository;
import com.afsan.springbootmongodb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepo;

    @Autowired
    private TodoService todoService;


    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos(){
        List<TodoDTO> todoDTOList =  todoService.getAllTodos();
        return new ResponseEntity<List<TodoDTO>>(todoDTOList, todoDTOList.size() > 0 ? HttpStatus.OK :
                HttpStatus.NOT_FOUND);
    }

    @PostMapping("/todos")
    public ResponseEntity<?> addTodo(@RequestBody TodoDTO todoDTO){
        try{
            todoService.createTodo(todoDTO);
            return new ResponseEntity<TodoDTO>(todoDTO,HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (TodoCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id){
        try{
            TodoDTO todoDTO = todoService.getSingleTodo(id);
            return new ResponseEntity<>(todoDTO,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id,@RequestBody TodoDTO todoDTO) throws TodoCollectionException{
        Optional<TodoDTO> todoOptional = todoRepo.findById(id);
        try
        {
            todoService.updateTodo(id,todoDTO);
            return new ResponseEntity<>("Updated Todo with id "+id,HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id){
        try{
            todoService.deleteTodoById(id);
            return new ResponseEntity<>("Successfully deleted by Id "+id,HttpStatus.OK);
        }catch (TodoCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

}
