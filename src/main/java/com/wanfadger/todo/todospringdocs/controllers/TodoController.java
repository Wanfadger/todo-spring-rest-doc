package com.wanfadger.todo.todospringdocs.controllers;

import com.wanfadger.todo.todospringdocs.models.Todo;
import com.wanfadger.todo.todospringdocs.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    private TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/todos")
    public Iterable<Todo> fetchTodos(){
        return todoRepository.findAll();
    }

    @PostMapping("/todos")
    public Todo persistTodo(@RequestBody Todo todo){
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo;
    }

    @GetMapping("/todos/{id}")
    public Todo fetchTodo(@PathVariable("id") String id){
        return todoRepository.findById(id).get();
    }

    @PutMapping("/todos/{id}")
    public Todo updateTodo(@PathVariable("id") String id , @RequestBody Todo todo){
        todo.setId(id);
        return todoRepository.save(todo);
    }

    @DeleteMapping("/todos/{id}")
    public void deleteTodo(@PathVariable("id") String id){
         todoRepository.deleteById(id);
    }




}
