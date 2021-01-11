package repository.inMemoryRepository;

import domain.Student;
import org.junit.Before;
import org.junit.Test;
import repository.InMemoryRepository.CrudRepository;
import repository.InMemoryRepository.StudentRepository;
import domain.validators.StudentValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class InMemoryRepositoryTest {
    Student student;
    CrudRepository<String, Student> studentRepository;

    @Before
    public void setUp() throws Exception {
        Validator<Student> validatorStudent = new StudentValidator();
        studentRepository = new StudentRepository(validatorStudent);
        student = new Student("Ceausescu", "Nicolae",221, "nicolae_ceausecu@yahoo.com","Stalin");
        student.setId("1");
        studentRepository.save(student);
    }

    @Test
    public void findOneSuccessfull() {
        assertEquals("Ceausescu", studentRepository.findOne("1").getNume());
    }

    @Test
    public void findOneFailed() {
        assertEquals(null, studentRepository.findOne("1924"));
        assertEquals("Ceausescu", studentRepository.findOne("1").getNume());
    }

    @Test
    public void findOneThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.findOne(null);
        });
    }

    @Test
    public void findAll() {
        int size = 0;
        for (Student s : studentRepository.findAll()) {
            size++;
        }
        assertEquals(1, size);
    }

    @Test
    public void saveSuccessfull() {
        Student student2 = new Student("Vladimir", "Putin", 225, "vladimir@yahoo.com", "John");
        student2.setId("2");
        assertEquals(null, studentRepository.save(student2));
    }

    @Test
    public void saveFailed() {
        assertEquals(student, studentRepository.save(student));
    }

    @Test
    public void saveThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Student student = new Student("", "", -1, "", "");
            student.setId(null);
            studentRepository.save(student);
        });
    }

    @Test
    public void saveThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.save(null);
        });
    }

    @Test
    public void deleteSuccessfull() {
        assertEquals("Ceausescu", studentRepository.delete("1").getNume());
    }

    @Test
    public void deleteFailed() {
        assertEquals(null, studentRepository.delete("10"));
    }

    @Test
    public void deleteThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentRepository.delete(null);
        });
    }
}