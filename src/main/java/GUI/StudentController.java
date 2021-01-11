package GUI;

import domain.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.Service;
import utils.events.EntityChangeEvent;
import utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StudentController implements Observer<EntityChangeEvent> {
    private Service service;

    ObservableList<Student> modelStudent = FXCollections.observableArrayList();

    @FXML
    TableColumn<Student, String> tableColumnID;
    @FXML
    TableColumn<Student, String> tableColumnNume;
    @FXML
    TableColumn<Student, String> tableColumnPrenume;
    @FXML
    TableColumn<Student, Integer > tableColumnGrupa;
    @FXML
    TableColumn<Student,String> tableColumnEmail;
    @FXML
    TableColumn<Student,String> tableColumnProfesor;
    @FXML
    TableView<Student> tableViewStudenti;

    @FXML
    TextField textFieldNume;

    public void setService(Service service) {

        this.service = service;
        service.addObserver(this);
        modelStudent.setAll(getStudentList());
    }

    private List<Student> getStudentList() {
        Iterable<Student> studenti = service.findAllStudent();
        List<Student>  listaStudenti = StreamSupport.stream(studenti.spliterator(), false)
                .collect(Collectors.toList());
        return listaStudenti;
    }

    @FXML
    private void handleFilter() {
        Predicate<Student> p1 = student -> student.getNume().startsWith(textFieldNume.getText());
        modelStudent.setAll(getStudentList()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory("nume"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory("prenume"));
        tableColumnGrupa.setCellValueFactory(new PropertyValueFactory("grupa"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tableColumnProfesor.setCellValueFactory(new PropertyValueFactory("cadruDidacticIndrumatorLab"));
        tableViewStudenti.setItems(modelStudent);

        textFieldNume.textProperty().addListener(((observableValue, s, t1) -> handleFilter()));
    }

    private void showEditStudentDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editGuiStudentView.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            EditStudentController editStudentController = loader.getController();
            editStudentController.setService(service, dialogStage, student);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        showEditStudentDialog(null);
    }

    public void handleDeleteStudent(ActionEvent event){
        Student student = tableViewStudenti.getSelectionModel().getSelectedItem();
        if(student != null){
            Student studentSters = service.deleteStudent(student.getId());
            if (studentSters != null)
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sterge efectuata cu succes",
                        "Studentul selectat a fost sters!");
        }
        else ControllerAlert.showErrorMessage(null,"Nu ati selectat niciun student!");
    }

    @FXML
    private void handleUpdateStudent(ActionEvent event){
        Student student = tableViewStudenti.getSelectionModel().getSelectedItem();
        if (student != null) {
            showEditStudentDialog(student);
        } else
            ControllerAlert.showErrorMessage(null,"Nu ati selectat niciun student!");
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {
        modelStudent.setAll(getStudentList());
    }
}
