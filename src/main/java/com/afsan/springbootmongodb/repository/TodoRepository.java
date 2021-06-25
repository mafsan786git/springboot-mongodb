package com.afsan.springbootmongodb.repository;

import com.afsan.springbootmongodb.model.TodoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends MongoRepository<TodoDTO,String> {

    @Query("{'todo':?0}")
    Optional<TodoDTO> findByTodo(String todo);
}
