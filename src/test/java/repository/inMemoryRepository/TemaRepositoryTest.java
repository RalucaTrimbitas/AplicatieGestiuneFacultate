package repository.inMemoryRepository;

import domain.StructuraAnUniversitar;
import domain.Tema;
import org.junit.Before;
import org.junit.Test;
import repository.InMemoryRepository.TemaRepository;
import domain.validators.TemaValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemaRepositoryTest {
    Tema tema;
    TemaRepository temaRepository;
    StructuraAnUniversitar structuraAnUniversitar;

    @Before
    public void setUp() throws Exception {
        Validator<Tema> validatorTema = new TemaValidator();
        temaRepository = new TemaRepository(validatorTema);
        tema = new Tema("MAP", 2, 14);
        tema.setId("1");
        temaRepository.save(tema);
    }

    @Test
    public void updateSuccessfull() {
        Tema tema1 = new Tema("PLF", 4, 14);
        tema1.setId("1");
        assertEquals(null, temaRepository.update(tema1));
    }

    @Test
    public void updateFailed() {
        assertEquals(tema, temaRepository.update(tema));
    }

    @Test
    public void updateThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            temaRepository.update(null);
        });
    }

    @Test
    public void updateThrowValidationException() {
        assertThrows(ValidationException.class, () -> {
            Tema tema = new Tema("", 0, 0);
            tema.setId("0");
            temaRepository.update(tema);
        });
    }
}