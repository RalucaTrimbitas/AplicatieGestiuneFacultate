package GUI;

import com.sun.tools.javac.util.Pair;
import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import services.Service;
import utils.ComboBoxAutoComplete;
import domain.validators.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static utils.FormatData.DATE_FORMAT;

public class EditNotaController {

    @FXML
    private TextField textFieldNumeStudent;
    @FXML
    private TextField textFieldValoare;
    @FXML
    private TextField textFieldNumeProfesor;
    @FXML
    private TextField textFieldMotivare;
    @FXML
    private TextField textFieldDataPredare;

    @FXML
    private TextArea textAreaFeedback;
    @FXML
    ComboBox comboBoxTema;

    private Service service;
    NotaDTO2 nota;
    Stage dialogStage;

    List<String> descrieriTeme = new ArrayList<>();
    String descriereTemaDefault = new String();

    @FXML
    private void initialize() {
        comboBoxTema.getItems().setAll(descrieriTeme);
        comboBoxTema.setValue(descriereTemaDefault);
        new ComboBoxAutoComplete<>(comboBoxTema);
        textAreaFeedback.setWrapText(true);
    }

    public void setService(Service service, Stage stage, NotaDTO2 n) {
        this.service = service;
        this.dialogStage = stage;
        this.nota = n;
        if (null != n) {
            setFields(n);
            textFieldNumeStudent.setEditable(false);
            textFieldMotivare.setEditable(false);
            textFieldDataPredare.setEditable(false);
            comboBoxTema.setEditable(false);
        }
        descrieriTeme.addAll(getTemaDescriere());
        descriereTemaDefault = this.getTemaCurenta().getDescriere();
        initialize();
    }

    private void setFields(NotaDTO2 n) {
        textFieldNumeStudent.setText(n.getNumeStudent());
        textFieldValoare.setText(String.valueOf(n.getValoare()));
        textFieldNumeProfesor.setText(n.getNumeProfesor());
        comboBoxTema.setValue(n.getDescriereTema());
        textFieldMotivare.setText("-");
        textFieldDataPredare.setText(n.getDataPredare());
    }

    private Tema getTemaCurenta() {
        List<Tema> temaList = getTemaList();
        temaList = temaList.stream()
                .filter(tema -> tema.getDeadlineWeek() == service.getCurrentWeek())
                .collect(Collectors.toList());
        if (temaList.size() != 0)
            return temaList.get(0);
        return getTemaList().stream().collect(Collectors.toList()).get(0);
    }

    private List<String> getTemaDescriere() {
        List<Tema> temaList = getTemaList();
        List<String> rezultat = new ArrayList<>();
        temaList.forEach(tema -> {
            if (!rezultat.contains(tema.getDescriere()))
                rezultat.add(tema.getDescriere());
        });
        return rezultat;
    }

    private List<Student> getStudentList() {
        Iterable<Student> studenti = service.findAllStudent();
        List<Student> listaStudenti = StreamSupport.stream(studenti.spliterator(), false)
                .collect(Collectors.toList());
        return listaStudenti;
    }

    private List<Nota> getNoteList() {
        Iterable<Nota> note = service.findAllNota();
        List<Nota> listaNote = StreamSupport.stream(note.spliterator(), false)
                .collect(Collectors.toList());
        return listaNote;
    }

    private List<Tema> getTemaList() {
        Iterable<Tema> teme = service.findAllTema();
        List<Tema> temaList = StreamSupport.stream(teme.spliterator(), false)
                .collect(Collectors.toList());
        return temaList;
    }

    private Nota getNotaDetails(String numeStudent, String descriereTema) {
        List<Nota> notaList = getNoteList();
        final Nota[] rezultat = new Nota[1];
        notaList.stream()
                .forEach(nota -> {
                    Student student = service.findOneStudent(nota.getId().fst);
                    Tema tema = service.findOneTema((nota.getId().snd));
                    if (student.getNume().equals(numeStudent) && tema.getDescriere().equals(descriereTema)) {
                        Pair id = Pair.of(student.getId(), tema.getId());
                        if (nota.getId().equals(id))
                            rezultat[0] = nota;
                    }
                });
        return rezultat[0];
    }

    private Student getStudentDetails(String numeStudent, String idStudent){
        List<Student> listaStudenti = getStudentList();
        if (idStudent == null)
            listaStudenti = listaStudenti.stream()
                    .filter(student -> student.getNume().equals(numeStudent))
                    .collect(Collectors.toList());
        else {
            listaStudenti = listaStudenti.stream()
                    .filter(student -> student.getNume().equals(numeStudent) && student.getId().equals(idStudent))
                    .collect(Collectors.toList());
        }
        if (listaStudenti.size() != 0)
            return listaStudenti.get(0);
        return null;
    }

    private Tema getTemaDetails(String descriereTema, String idTema) {
        List<Tema> listaTeme = getTemaList();
        if (idTema == null)
            listaTeme =  listaTeme.stream()
                    .filter(tema -> tema.getDescriere().equals(descriereTema))
                    .collect(Collectors.toList());
        else {
            listaTeme = listaTeme.stream()
                    .filter(tema -> tema.getDescriere().equals(descriereTema) && tema.getId().equals(idTema))
                    .collect(Collectors.toList());
        }
        if (listaTeme.size() != 0)
            return listaTeme.get(0);
        return null;
    }

    private boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean dataFormat(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate data = LocalDate.from(formatter.parse(input));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String numeStudent = textFieldNumeStudent.getText();
        String descriereTema = String.valueOf(comboBoxTema.getValue());
        String valoare = textFieldValoare.getText();
        String numeProfesor = textFieldNumeProfesor.getText();
        String raspuns = textFieldMotivare.getText();
        String dataPredare = textFieldDataPredare.getText();
        String feedback = textAreaFeedback.getText();
        Student student = this.getStudentDetails(numeStudent, null);
        Tema tema = this.getTemaDetails(descriereTema, null);
        if (student == null) {
            ControllerAlert.showErrorMessage(null, "Studentul " + numeStudent + " nu exista!");
            return;
        }
        if (tema == null) {
            ControllerAlert.showErrorMessage(null, "Tema " + descriereTema + " nu exista!");
            return;
        }
        if (!dataFormat(dataPredare)) {
            ControllerAlert.showErrorMessage(null, "Formatul datei trebuie sa fie: dd/MM/yyyy");
            return;
        }
        if (!isFloat(valoare)) {
            ControllerAlert.showErrorMessage(null, "Valoarea trebuie sa fie numerica!");
            return;
        }
        if (numeProfesor == null || numeProfesor.equals("")) {
            ControllerAlert.showErrorMessage(null, "Numele profesorul este invalid!");
            return;
        }
        NotaDTO2 notaDTO2 = new NotaDTO2(numeStudent, descriereTema, Float.parseFloat(valoare), numeProfesor, dataPredare, feedback);
        Nota notaDetails = this.getNotaDetails(numeStudent, descriereTema);
        if (notaDetails != null)
            notaDTO2.setId(notaDetails.getId());
        else
            notaDTO2.setId(Pair.of(student.getId(), tema.getId()));
        if (notaDTO2 != null)
            addNota(notaDTO2, raspuns);
        else
            updateNota(notaDTO2);
    }

    private void updateNota(NotaDTO2 notaDTO2) {
        try {
            Student student = this.getStudentDetails(notaDTO2.getNumeStudent(), notaDTO2.getId().fst);
            Tema tema = this.getTemaDetails(notaDTO2.getDescriereTema(), notaDTO2.getId().snd);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate data = LocalDate.from(formatter.parse(notaDTO2.getDataPredare()));
            Nota nota = service.updateNota(student.getId(), tema.getId(), data, notaDTO2.getNumeProfesor(), notaDTO2.getValoare(),notaDTO2.getFeedback());
            if (nota != null) {
                ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare Nota", "Nota a fost modificata!");
                dialogStage.close();
            }
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void addNota(NotaDTO2 notaDTO2, String raspuns) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate data = LocalDate.from(formatter.parse(notaDTO2.getDataPredare()));
            Student st = this.getStudentDetails(notaDTO2.getNumeStudent(), notaDTO2.getId().fst);
            Tema tema = this.getTemaDetails(notaDTO2.getDescriereTema(), notaDTO2.getId().snd);
            Nota notaAdaugata = service.saveNota(st.getId(), tema.getId(), data, notaDTO2.getNumeProfesor(), notaDTO2.getValoare(), notaDTO2.getFeedback(), raspuns);
            Nota notaAdaugataDetails = this.getNotaDetails(st.getNume(), tema.getDescriere());
            String contentText = "Informatii nota: \n";
            contentText += "Nume Student: " + notaDTO2.getNumeStudent() + "\n";
            contentText += "Descriere Tema: " + notaDTO2.getDescriereTema() + "\n";
            if (raspuns.equals("yes")) {
                contentText += "Motivat: da\n";
            }
            contentText += "Valoare Nota: " + notaDTO2.getValoare() + "\n";
            if ((notaDTO2.getValoare() - notaAdaugataDetails.getValoare() > 0)) {
                contentText += "Penalizare: " + (notaDTO2.getValoare() - notaAdaugataDetails.getValoare()) + " puncte\n";
                String feedbackText = " Nota a fost diminuata cu " + (notaDTO2.getValoare() - notaAdaugataDetails.getValoare()) + " puncte datorita intarzierilor.";
                textAreaFeedback.appendText(feedbackText);
            }
            contentText += "Feedback: " + textAreaFeedback.getText() + "\n";
            if (data.isBefore(LocalDate.now()))
                contentText += "Profesorul nu a asignat nota la timp.";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentText);
            alert.setTitle("Adauga Nota");
            alert.setHeaderText("Detalii despre Nota");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (notaAdaugata == null) {
                    dialogStage.close();
                } else
                    ControllerAlert.showErrorMessage(null, "Exista nota cu id-ul: " + nota.getId());
            } else
                service.deleteNota(notaAdaugataDetails.getId());
        } catch (ValidationException e) {
            ControllerAlert.showErrorMessage(null, e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

}
