package GUI;

import domain.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.Service;
import domain.validators.ValidationException;

public class EditStudentController {

    @FXML
    private TextField textFieldID;
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldPrenume;
    @FXML
    private TextField textFieldGrupa;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProfesor;

    private Service service;
    Student student;
    Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setService(Service service, Stage stage, Student s){
        this.service = service;
        this.dialogStage = stage;
        this.student=s;
        if (null != s) {
            setFields(s);
            textFieldID.setEditable(false);
        }
    }

    @FXML
    public void handleSave(javafx.event.ActionEvent event){
        String ID = textFieldID.getText();
        String Nume = textFieldNume.getText();
        String Prenume = textFieldPrenume.getText();
        String Grupa = textFieldGrupa.getText();
        String Email= textFieldEmail.getText();
        String Profesor = textFieldProfesor.getText();
        Student s = new Student(Nume,Prenume,Integer.parseInt(Grupa),Email,Profesor);
        s.setId(ID);
        if (student == null)
            addStudent(s);
        else
            updateStudent(s);
    }

    private void addStudent(Student st) {
        try {
            Student st1 = service.saveStudent(st.getId(), st.getNume(), st.getPrenume(),st.getGrupa(), st.getEmail(), st.getCadruDidacticIndrumatorLab());
            if (st1 == null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare Student", "Studentul a fost salvat!");
                dialogStage.close();
            }
            else
                ControllerAlert.showErrorMessage(null, "Exista un studentul cu id-ul: " + st.getId());
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateStudent(Student st) {
        try {
            Student st1 = service.updateStudent(st.getId(), st.getNume(), st.getPrenume(),st.getGrupa(), st.getEmail(), st.getCadruDidacticIndrumatorLab());
            if (st1 == null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare Student", "Studentul a fost modificat!");
                dialogStage.close();
            }
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

    private void setFields(Student s)
    {
        textFieldID.setText(s.getId());
        textFieldNume.setText(s.getNume());
        textFieldPrenume.setText(s.getPrenume());
        textFieldGrupa.setText(String.valueOf(s.getGrupa()));
        textFieldEmail.setText(s.getEmail());
        textFieldProfesor.setText(s.getCadruDidacticIndrumatorLab());
    }

}
