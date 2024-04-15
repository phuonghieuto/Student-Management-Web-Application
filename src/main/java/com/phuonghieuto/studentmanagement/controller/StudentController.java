package com.phuonghieuto.studentmanagement.controller;

import com.phuonghieuto.studentmanagement.entity.Student;
import com.phuonghieuto.studentmanagement.service.StudentService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@Controller
public class StudentController {
    static final int DEFAULT_PAGE_SIZE = 5;
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

    @GetMapping("/add")
    public String addStudent(Model model) {
        model.addAttribute("student", new Student());

        return "add";
    }

    @GetMapping("/edit")
    public String editStudent(Model model, @RequestParam ObjectId id) {
        final Optional<Student> record = studentService.getStudent(id);

        model.addAttribute("student", record.orElseGet(Student::new));
        model.addAttribute("id", id);
        return "edit";
    }

    @PostMapping("/save")
    public String save(final Model model, @ModelAttribute final Student student, final BindingResult errors) {

        studentService.saveStudent(student);
        return "redirect:students";
    }

    @GetMapping("/delete")
    public String deleteStudent(Model model, @RequestParam ObjectId id) {
        final Optional<Student> record = studentService.getStudent(id);

        model.addAttribute("student", record.orElseGet(Student::new));
        model.addAttribute("id", id);
        return "delete";
    }

    @PostMapping("/delete")
    public String deletion(final Model model, @RequestParam ObjectId id) {

        studentService.deleteStudent(id);

        return "redirect:students";
    }

    @GetMapping("/view")
    public String viewStudent(Model model, @RequestParam ObjectId id) {
        final Optional<Student> record = studentService.getStudent(id);

        model.addAttribute("student", record.orElseGet(Student::new));
        model.addAttribute("id", id);
        return "view";
    }
}
