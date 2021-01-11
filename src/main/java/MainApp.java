import GUI.MeniuController;
import com.sun.tools.javac.util.Pair;
import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.InMemoryRepository.CrudRepository;
import repository.XMLFileRepository.XMLNotaFileRepository;
import repository.XMLFileRepository.XMLStudentFileRepository;
import repository.XMLFileRepository.XMLTemaFileRepository;
import services.Service;
import utils.Paths;
import domain.validators.NotaValidator;
import domain.validators.StudentValidator;
import domain.validators.TemaValidator;

import java.io.IOException;

public class MainApp extends Application {
    
    CrudRepository<String, Student> studentFileRepository = new XMLStudentFileRepository(new StudentValidator(), Paths.STUDENT);
    CrudRepository<String, Tema> temaFileRepository = new XMLTemaFileRepository(new TemaValidator(),Paths.TEMA);
    CrudRepository<Pair<String,String>, Nota> notaFileRepository = new XMLNotaFileRepository(new NotaValidator(),Paths.NOTA);
    Service service = new Service(studentFileRepository,temaFileRepository,notaFileRepository);


    @Override
    public void start(Stage primaryStage) throws IOException {
        initView(primaryStage);
        primaryStage.setWidth(400);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/guiMeniuView.fxml"));
        AnchorPane anchorPane = loader.load();

        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.setTitle("Meniu");

        MeniuController meniuController = loader.getController();
        meniuController.setService(service);

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Message");
        dialogStage.initModality(Modality.WINDOW_MODAL);


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
