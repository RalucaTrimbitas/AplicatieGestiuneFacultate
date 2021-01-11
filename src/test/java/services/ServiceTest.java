package services;

import com.sun.tools.javac.util.Pair;
import domain.Nota;
import domain.StructuraAnUniversitar;
import domain.Student;
import domain.Tema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository.CrudRepository;
import repository.InMemoryRepository.NotaRepository;
import repository.InMemoryRepository.StudentRepository;
import repository.InMemoryRepository.TemaRepository;
import domain.validators.NotaValidator;
import domain.validators.StudentValidator;
import domain.validators.TemaValidator;
import domain.validators.Validator;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServiceTest {
    Service service;

    @BeforeEach
    void setUp() {
        StructuraAnUniversitar anUniversitar = StructuraAnUniversitar.getInstance("data/test/anUniversitar.txt");
        Service.setCurrentWeek(anUniversitar.getWeek(LocalDate.of(2019, 11, 15),
                anUniversitar.getSemestru(LocalDate.of(2019, 11, 15))));
        Validator<Student> studentValidator = new StudentValidator();
        CrudRepository<String, Student> studentCrudRepository = new StudentRepository(studentValidator);
        Validator<Tema> temaValidator = new TemaValidator();
        CrudRepository<String, Tema> temaCrudRepository = new TemaRepository(temaValidator);
        Validator<Nota> notaValidator = new NotaValidator();
        CrudRepository<Pair<String, String>, Nota> notaCrudRepository = new NotaRepository(notaValidator);
        service = new Service(studentCrudRepository, temaCrudRepository, notaCrudRepository);
        Service.setCurrentWeek(5);
    }

    @Test
    void saveNotaThrowServiceExceptionIdInvalid() {
        assertThrows(ServiceException.class, () -> {
            service.saveNota("", "0", LocalDate.of(2019, 11, 11), "Horia", 10, "OK", "NU");
        });
    }

    @Test
    void filtrareStudentGrupaSuccessfull() {
        service.saveStudent("1", "Marina", "Gheorghe", 221, "mariana@yahoo.com", "Vasile");
        service.saveStudent("2", "Daniel", "Gheorghe", 221, "daniel@yahoo.com", "Vasile");
        assertEquals(2, service.filtrareStudentGrupa(221).size());
    }

    @Test
    void filtrareNotaStudentTemaSuccessfull() throws InstantiationException, IllegalAccessException {
        service.saveStudent("1", "Marina", "Gheorghe", 221, "mariana@yahoo.com", "Vasile");
        service.saveStudent("2", "Daniel", "Gheorghe", 221, "daniel@yahoo.com", "Vasile");
        service.saveTema("1", "MAP", 6, 7);
        service.saveNota("1", "1", LocalDate.of(2019, 11, 11), "Vasile", 10f, "ok", "NU");
        assertEquals(1, service.filtrareNotaStudentTema("1").size());
    }

    @Test
    void filtrareNotaProfesorSuccessfull() throws InstantiationException, IllegalAccessException {
        service.saveStudent("1", "Marina", "Gheorghe", 221, "mariana@yahoo.com", "Vasile");
        service.saveStudent("2", "Daniel", "Gheorghe", 221, "daniel@yahoo.com", "Vasile");
        service.saveTema("1", "MAP", 6, 7);
        service.saveNota("1", "1", LocalDate.of(2019, 11, 11), "Vasile", 10f, "ok", "NU");
        assertEquals(1, service.filtrareTemaProfesor("1", "Vasile").size());
    }

    @Test
    void filtrareNotaSaptamnaDataSuccessfull() throws InstantiationException, IllegalAccessException {
        service.saveStudent("1", "Marina", "Gheorghe", 221, "mariana@yahoo.com", "Vasile");
        service.saveStudent("2", "Daniel", "Gheorghe", 221, "daniel@yahoo.com", "Vasile");
        service.saveTema("1", "MAP", 6, 7);
        service.saveNota("1", "1", LocalDate.of(2019, 11, 11), "Vasile", 10f, "ok", "NU");
        assertEquals(1, service.filtrareNoteSaptamana("1", 7).size());
    }
}