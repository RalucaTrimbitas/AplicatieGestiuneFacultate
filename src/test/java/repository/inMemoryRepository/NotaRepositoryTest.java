package repository.inMemoryRepository;

import com.sun.tools.javac.util.Pair;
import domain.Nota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository.NotaRepository;
import domain.validators.NotaValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotaRepositoryTest {
    Nota nota;
    NotaRepository notaRepository;

    @BeforeEach
    public void setUp() throws Exception {
        Validator<Nota> validatorNota = new NotaValidator();
        notaRepository = new NotaRepository(validatorNota);
        nota = new Nota(LocalDate.of(2019, 11, 11), "Vasile", 10);
        nota.setId(Pair.of("1", "1"));
        notaRepository.save(nota);
    }

    @Test
    public void updateSuccessfull() {
        nota = new Nota(LocalDate.of(2019, 11, 11), "Vasile", 5);
        nota.setId(Pair.of("1", "1"));
        assertEquals(null, notaRepository.update(nota));
    }

    @Test
    public void updateFailed() {
        assertEquals(nota, notaRepository.update(nota));
    }

    @Test
    public void updateThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            notaRepository.update(null);
        });
    }

    @Test
    public void updateThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Nota nota = new Nota(LocalDate.of(2019, 11, 11), "", -1);
            nota.setId(Pair.of("0", "0"));
            notaRepository.update(nota);
        });
    }
}