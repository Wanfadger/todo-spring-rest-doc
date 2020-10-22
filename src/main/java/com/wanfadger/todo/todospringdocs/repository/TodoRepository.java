package com.wanfadger.todo.todospringdocs.repository;

import com.wanfadger.todo.todospringdocs.models.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo , String> {
}
