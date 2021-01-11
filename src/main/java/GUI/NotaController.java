package GUI;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.Service;
import utils.events.EntityChangeEvent;
import utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotaController implements Observer<EntityChangeEvent> {
    private Service service;

    ObservableList<NotaDTO2> modelNota = FXCollections.observableArrayList();

    @FXML
    TableColumn<NotaDTO2, String> tableColumnID;
    @FXML
    TableColumn<NotaDTO2, String> tableColumnNumeStudent;
    @FXML
    TableColumn<NotaDTO2, String> tableColumnDescriereTema;
    @FXML
    TableColumn<NotaDTO2, Float> tableColumnValoare;
    @FXML
    TableColumn<NotaDTO2, String> tableColumnNumeProfesor;
    @FXML
    TableColumn<NotaDTO2, String> tableColumnDataPredare;
    @FXML
    TextArea textAreaFeedback;
    @FXML
    TableView<NotaDTO2> tableViewNote;
    @FXML
    TextField textFieldNumeStudent;

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        modelNota.setAll(getNoteList());
    }

    private List<NotaDTO2> getNoteList() {
        List<NotaDTO2> listaNote = new ArrayList<>();
        Iterable<Nota> note = service.findAllNota();
        note.forEach(nota -> {
            Student student = service.findOneStudent(nota.getId().fst);
            Tema tema = service.findOneTema(nota.getId().snd);
            NotaDTO2 notaDTO2 = new NotaDTO2(student.getNume(), tema.getDescriere(), nota.getValoare(), nota.getProfesor(), nota.getDataString(),nota.getFeedback());
            notaDTO2.setId(nota.getId());
            listaNote.add(notaDTO2);
        });
        return listaNote;
    }

    @FXML
    private void handleFilter() {
        Predicate<NotaDTO2> p1 = notaDTO2 -> notaDTO2.getNumeStudent().startsWith(textFieldNumeStudent.getText());
        modelNota.setAll(getNoteList()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory("id"));
        tableColumnNumeStudent.setCellValueFactory(new PropertyValueFactory("numeStudent"));
        tableColumnDescriereTema.setCellValueFactory(new PropertyValueFactory("descriereTema"));
        tableColumnValoare.setCellValueFactory(new PropertyValueFactory("valoare"));
        tableColumnNumeProfesor.setCellValueFactory(new PropertyValueFactory("numeProfesor"));
        tableColumnDataPredare.setCellValueFactory(new PropertyValueFactory("dataPredare"));
        tableViewNote.setItems(modelNota);

        textFieldNumeStudent.textProperty().addListener(((observableValue, s, t1) -> handleFilter()));

        tableViewNote.getSelectionModel().selectedItemProperty().addListener(((observableValue, notaDtoView, t1) -> {
            if (tableViewNote.getSelectionModel().getSelectedItem() != null)
                textAreaFeedback.setText(tableViewNote.getSelectionModel().getSelectedItem().getFeedback());
        }));
        textAreaFeedback.setEditable(false);
    }

    private void showEditNotaDialog(NotaDTO2 notaDTO2) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editGuiNotaView.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Nota");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            EditNotaController editNotaController = loader.getController();
            editNotaController.setService(service, dialogStage, notaDTO2);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {
        modelNota.setAll(getNoteList());

    }

    public void handleAddNota(ActionEvent event) {
        showEditNotaDialog(null);
    }

    public void handleDeleteNota(ActionEvent event){
        NotaDTO2 notaDTO2 = tableViewNote.getSelectionModel().getSelectedItem();
        if (notaDTO2 != null) {
             service.deleteNota(notaDTO2.getId());
            ControllerAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sterge efectuata cu succes",
                    "Nota selectata a fost stearsa!");
        } else
            ControllerAlert.showErrorMessage(null,"Nu ati selectat nicio nota!");

    }

    public void handleUpdateNota(ActionEvent event){
        NotaDTO2 nota = tableViewNote.getSelectionModel().getSelectedItem();
        if (nota!= null){
            showEditNotaDialog(nota);
        }
        else
            ControllerAlert.showErrorMessage(null,"Nu ati selectat nicio nota!");
    }
}
