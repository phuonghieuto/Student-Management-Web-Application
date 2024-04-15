package com.phuonghieuto.studentmanagement.controller;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static com.phuonghieuto.studentmanagement.controller.StudentController.DEFAULT_PAGE_SIZE;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.phuonghieuto.studentmanagement.entity.Student;
import com.phuonghieuto.studentmanagement.service.StudentService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
class StudentControllerTest {
    private MockMvc mvc;

    @Mock
    private StudentService studentService;

    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        studentController = new StudentController(studentService);
        this.mvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void index_RedirectsToListView_WhenStudentHomeIsAccessed() throws Exception {

        // @formatter:off
        mvc.perform(
                        get("/")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
        ;
        // @formatter:on

        then(studentService).shouldHaveNoInteractions();
    }

    @Test
    public void list_ReturnsViewWithRecords_WhenStudentListViewIsAccessed() throws Exception {

        final int pageNumber = 0;
        final int pageSize = DEFAULT_PAGE_SIZE;
        final int totalPages = (int) (Math.random() * 100);

        final Student student1 = new Student(new ObjectId(), randomUUID().toString(), randomUUID().toString());
        final Student student2 = new Student(new ObjectId(), randomUUID().toString(), randomUUID().toString());

        final List<Student> students = Arrays.asList(student1, student2);
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> response = new PageImpl<>(students, page, totalPages);

        given(studentService.getStudents(pageNumber, pageSize)).willReturn(response);

        // @formatter:off
        mvc.perform(
                        get("/students")
                                .param("page", String.valueOf(pageNumber))
                                .param("size", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("students", hasItems(student1, student2)))
                .andExpect(view().name("list"))
        ;
        // @formatter:on

        then(studentService).should().getStudents(pageNumber, pageSize);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void list_ReturnsViewForFirstPage_WhenStudentListViewIsAccessed() throws Exception {

        final int pageNumber = 0;
        final int pageSize = DEFAULT_PAGE_SIZE;
        final int totalPages = (int) (Math.random() * 100);

        final Student student = new Student(new ObjectId(), randomUUID().toString(), randomUUID().toString());
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> response = new PageImpl<>(singletonList(student), page, totalPages);

        given(studentService.getStudents(pageNumber, pageSize)).willReturn(response);

        // @formatter:off
        mvc.perform(
                        get("/students")
                                .param("page", String.valueOf(pageNumber))
                                .param("size", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("previousPageNumber", is(-1)))
                .andExpect(model().attribute("nextPageNumber", is(1)))
                .andExpect(view().name("list"))
        ;
        // @formatter:on

        then(studentService).should().getStudents(pageNumber, pageSize);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void list_ReturnsViewForLastPage_WhenStudentListViewIsAccessed() throws Exception {

        final int pageNumber = 1;
        final int pageSize = DEFAULT_PAGE_SIZE;
        final int totalPages = DEFAULT_PAGE_SIZE;

        final Student student = new Student(new ObjectId(), randomUUID().toString(), randomUUID().toString());
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> response = new PageImpl<>(singletonList(student), page, totalPages);

        given(studentService.getStudents(pageNumber, pageSize)).willReturn(response);

        // @formatter:off
        mvc.perform(
                        get("/students")
                                .param("page", String.valueOf(pageNumber))
                                .param("size", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("previousPageNumber", is(0)))
                .andExpect(model().attribute("nextPageNumber", is(-1)))
                .andExpect(view().name("list"))
        ;
        // @formatter:on

        then(studentService).should().getStudents(pageNumber, pageSize);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void view_ReturnsViewPageWithStudentFromDatabase_WhenStudentIdExistsInDatabase() throws Exception {

        final ObjectId id = new ObjectId();

        final Student student = new Student(id, randomUUID().toString(), randomUUID().toString());

        given(studentService.getStudent(id)).willReturn(Optional.of(student));

        // @formatter:off
        mvc.perform(
                        get("/view/")
                                .param("id", String.valueOf(id))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", is(id)))
                .andExpect(model().attribute("student", is(notNullValue())))
                .andExpect(model().attribute("student", hasProperty("id", is(id))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(student.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(student.getLastName()))))
                .andExpect(view().name("view"))
        ;
        // @formatter:on

        then(studentService).should().getStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void view_ReturnsViewPageWithEmpty_WhenStudentIdDoesNotExist() throws Exception {

        final ObjectId id = new ObjectId();

        given(studentService.getStudent(id)).willReturn(Optional.empty());

        // @formatter:off
        mvc.perform(
                        get("/view/")
                                .param("id", id.toString())
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", is(id)))
                .andExpect(model().attribute("student", is(notNullValue())))
                .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(nullValue()))))
                .andExpect(view().name("view"))
        ;
        // @formatter:on

        then(studentService).should().getStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void add_ReturnsViewPageWithEmptyStudent() throws Exception {

        // @formatter:off
        mvc.perform(
                        get("/add/")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", is(notNullValue())))
                .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(nullValue()))))
                .andExpect(view().name("add"))
        ;
        // @formatter:on

        then(studentService).shouldHaveNoInteractions();
    }

    @Test
    public void edit_ReturnsEditView_WhenStudentEditViewIsAccessedAndStudentExists() throws Exception {

        final ObjectId id = new ObjectId();

        final Student student = new Student(id, randomUUID().toString(), randomUUID().toString());

        given(studentService.getStudent(id)).willReturn(Optional.of(student));

        // @formatter:off
        mvc.perform(
                        get("/edit/")
                                .param("id", id.toString())
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", hasProperty("id", is(id))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(student.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(student.getLastName()))))
                .andExpect(model().attribute("id", is(id)))
                .andExpect(view().name("edit"))
        ;
        // @formatter:on

        then(studentService).should().getStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void edit_ReturnsEditView_WhenStudentEditViewIsAccessedAndStudentDoesNotExists() throws Exception {

        final ObjectId id = new ObjectId();

        given(studentService.getStudent(id)).willReturn(Optional.empty());

        // @formatter:off
        mvc.perform(
                        get("/edit/")
                                .param("id", id.toString())
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(nullValue()))))
                .andExpect(model().attribute("id", is(id)))
                .andExpect(view().name("edit"))
        ;
        // @formatter:on

        then(studentService).should().getStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void delete_ReturnsDeleteView_WhenStudentDeleteViewIsAccessedAndStudentExists() throws Exception {

        final ObjectId id = new ObjectId();

        final Student student = new Student(id, randomUUID().toString(), randomUUID().toString());

        given(studentService.getStudent(id)).willReturn(Optional.of(student));

        // @formatter:off
        mvc.perform(
                        get("/delete/")
                                .param("id", id.toString())
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", hasProperty("id", is(id))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(student.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(student.getLastName()))))
                .andExpect(model().attribute("id", is(id)))
                .andExpect(view().name("delete"))
        ;
        // @formatter:on

        then(studentService).should().getStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void delete_ReturnsDeleteView_WhenStudentDeleteViewIsAccessedAndStudentDoesNotExists() throws Exception {

        final ObjectId id = new ObjectId();

        given(studentService.getStudent(id)).willReturn(Optional.empty());

        // @formatter:off
        mvc.perform(
                        get("/delete/")
                                .param("id", id.toString())
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(nullValue()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(nullValue()))))
                .andExpect(model().attribute("id", is(id)))
                .andExpect(view().name("delete"))
        ;
        // @formatter:on

        then(studentService).should().getStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void save_SavesStudentRecord_WhenStudentRecordIsValid() throws Exception {

        final Student student = new Student(new ObjectId(), randomUUID().toString(), randomUUID().toString());

        given(studentService.saveStudent(student)).willReturn(student);

        // @formatter:off
        mvc.perform(
                        post("/save/")
                                .flashAttr("student", student)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("students"))
        ;
        // @formatter:on

        then(studentService).should().saveStudent(student);
        then(studentService).shouldHaveNoMoreInteractions();
    }

    @Test
    public void deletion_DeletesStudentRecord_WhenStudentRecordIsValid() throws Exception {

        final ObjectId id = new ObjectId();

        willDoNothing().given(studentService).deleteStudent(id);

        // @formatter:off
        mvc.perform(
                        post("/delete/")
                                .param("id", id.toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("students"))
        ;
        // @formatter:on

        then(studentService).should().deleteStudent(id);
        then(studentService).shouldHaveNoMoreInteractions();
    }
}