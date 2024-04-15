package com.phuonghieuto.studentmanagement.controller;

import com.phuonghieuto.studentmanagement.entity.Student;
import com.phuonghieuto.studentmanagement.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class StudentController {

    private StudentService studentService;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/students";
    }

    @GetMapping("/students")
    public String getStudents(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int pageNumber,
                               @RequestParam(value = "size", defaultValue = "5") int pageSize) {

        final Page<Student> page = studentService.getStudents(pageNumber, pageSize);

        final int currentPageNumber = page.getNumber();
        final int previousPageNumber = page.hasPrevious() ? currentPageNumber - 1 : -1;
        final int nextPageNumber = page.hasNext() ? currentPageNumber + 1 : -1;

        model.addAttribute("students", page.getContent());
        model.addAttribute("previousPageNumber", previousPageNumber);
        model.addAttribute("nextPageNumber", nextPageNumber);
        model.addAttribute("currentPageNumber", currentPageNumber);
        model.addAttribute("pageSize", pageSize);
        return "list";
    }

    @GetMapping("/students/add")
    public String addStudent(Model model) {
        return "add";
    }

    @GetMapping("/students/edit")
    public String editStudent(Model model) {
        return "edit";
    }

    @GetMapping("/students/delete")
    public String deleteStudent(Model model) {
        return "delete";
    }

    @GetMapping("/students/view")
    public String viewStudent(Model model) {
        return "view";
    }
}
