package com.smart.inventory.application.data.services.todo;

import com.smart.inventory.application.data.entities.Employer;
import com.smart.inventory.application.data.entities.TodoItem;
import com.smart.inventory.application.util.Utilities;

import java.util.List;

public interface ITodoService {

        void addTodo(String title, Employer employer, Utilities utilities);

        List<TodoItem> findAllTodoItem(Utilities util);

        List<Employer> findAllEmployer(Utilities util);

        void deleteTodo();
}
