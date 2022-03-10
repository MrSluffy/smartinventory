package com.smart.inventory.application.data.services.todo;

import com.smart.inventory.application.data.entities.Employer;
import com.smart.inventory.application.data.entities.TodoItem;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.ITodoItemRepository;
import com.smart.inventory.application.util.Utilities;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class TodoService implements ITodoService{

    private final IEmployerRepository employerRepository;
    private final ITodoItemRepository todoItemRepository;

    public TodoService(IEmployerRepository employerRepository,
                       ITodoItemRepository todoItemRepository) {
        this.employerRepository = employerRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    public void addTodo(String title,
                        Employer employer,
                        @Nonnull Utilities utilities) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(title);
        todoItem.getCompanySet().add(utilities.company);
        todoItem.setCompany(utilities.company);
        todoItem.getEmployerSet().add(employer);
        todoItem.setEmployer(employer);
        todoItemRepository.save(todoItem);
    }



    @Override
    public List<TodoItem> findAllTodoItem(Utilities util) {
        return todoItemRepository.findByCompanyId(util.company.getId());
    }

    @Override
    public List<Employer> findAllEmployer(Utilities util) {
        return employerRepository.search(util.company.getId());
    }

    @Override
    public void deleteTodo() {

    }

}
