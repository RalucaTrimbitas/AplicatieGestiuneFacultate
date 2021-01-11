package GUI.Rapoarte;

import domain.NotaDTO2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.Service;

public class NotaLaboratorController {
    ObservableList<NotaDTO2> model = FXCollections.observableArrayList();
    private Service service;

    @FXML
    TableColumn tableColumnNumeStudent;
    @FXML
    TableColumn tableColumnPrenumeStudent;
    @FXML
    TableColumn tableColumnGrupaStudent;
    @FXML
    TableColumn tableColumnNotaFinala;
    @FXML
    TableView tableViewNoteLaborator;

    public void setService(Service service, ObservableList<NotaDTO2> model){
        this.service = service;
        this.model=model;
        initialize();
    }

    private void initialize() {
        tableColumnNumeStudent.setCellValueFactory(new PropertyValueFactory("numeStudent"));
        tableColumnPrenumeStudent.setCellValueFactory(new PropertyValueFactory("descriereTema"));
        tableColumnGrupaStudent.setCellValueFactory(new PropertyValueFactory("dataPredare"));
        tableColumnNotaFinala.setCellValueFactory(new PropertyValueFactory("valoare"));
        tableViewNoteLaborator.setItems(model);
    }

}
