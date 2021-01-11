import com.sun.tools.javac.util.Pair;
import domain.Nota;
import domain.StructuraAnUniversitar;
import domain.Student;
import domain.Tema;
import repository.InMemoryRepository.CrudRepository;
import repository.XMLFileRepository.XMLNotaFileRepository;
import repository.XMLFileRepository.XMLStudentFileRepository;
import repository.XMLFileRepository.XMLTemaFileRepository;
import services.Service;
import services.config.ApplicationContext;
import ui.Consola;
import utils.Paths;
import domain.validators.*;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws ValidationException {

        StructuraAnUniversitar structuraAnUniversitar = StructuraAnUniversitar.getInstance(Paths.AN_UNIVERSITAR);

        Validator<Student> validatorStudent = new StudentValidator();
        Validator<Tema> validatorTema = new TemaValidator();
        Validator<Nota> validatorNota = new NotaValidator();

        //CrudRepository<String, Student> studentFileRepository = new XMLStudentFileRepository(validatorStudent, Paths.STUDENT);
       // CrudRepository<String, Student> studentFileRepository = new StudentFileRepository(validatorStudent, Paths.STUDENT);
        //CrudRepository<String,Tema> temaFileRepository = new XMLTemaFileRepository(validatorTema, Paths.TEMA);
        //CrudRepository<String, Tema> temaFileRepository = new TemaFileRepository(validatorTema,Paths.TEMA);
        //CrudRepository<Pair<String,String>, Nota> notaFileRepository = new NotaFileRepository(validatorNota,Paths.NOTA);
        //CrudRepository<Pair<String,String>,Nota> notaFileRepository = new XMLNotaFileRepository(validatorNota,Paths.NOTA);
        CrudRepository<String, Student> studentFileRepository = new XMLStudentFileRepository(validatorStudent,
                ApplicationContext.getPROPERTIES().getProperty("data.enitites.student"));
        CrudRepository<String, Tema> temaFileRepository = new XMLTemaFileRepository(validatorTema,
                ApplicationContext.getPROPERTIES().getProperty("data.enitites.tema"));
        CrudRepository<Pair<String, String>, Nota> notaFileRepository = new XMLNotaFileRepository(validatorNota,
                ApplicationContext.getPROPERTIES().getProperty("data.enitites.nota"));
        Service service=new Service(studentFileRepository,temaFileRepository,notaFileRepository);
        Consola.setCurrentWeek(structuraAnUniversitar.getWeek(LocalDate.now(), structuraAnUniversitar.getSemestru(LocalDate.now())));
        Consola consola=new Consola(service);
        //consola.runMenu();
        MainApp.main(args);
    }
}
