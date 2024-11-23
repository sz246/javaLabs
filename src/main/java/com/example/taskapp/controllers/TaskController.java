package com.example.taskapp.controllers;

import com.example.taskapp.config.TaskRepository;
import com.example.taskapp.config.CategoryRepository;
import com.example.taskapp.models.Task;
import com.example.taskapp.models.Category;
import com.example.taskapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listTasks(Model model, Principal principal) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks";
    }

    @GetMapping("/add")
    public String showAddTaskPage(Model model) {
        model.addAttribute("task", new Task());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "addTask";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task, @AuthenticationPrincipal User user) {
        task.setUser(user);
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit-task/{id}")
    public String showEditTaskPage(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        model.addAttribute("task", task);
        return "editTask";
    }

    @PostMapping("/edit-task/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task) {
        task.setId(id);
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete-task/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}
