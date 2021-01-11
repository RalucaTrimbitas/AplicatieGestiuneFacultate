package GUI.Rapoarte;

import domain.NotaDTO2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.Service;

public class TemaGreaController {
    ObservableList<NotaDTO2> model = FXCollections.observableArrayList();
    private Service service;

    @FXML
    TableColumn tableColumnDescriereTema;
    @FXML
    TableColumn tableColumnStartWeek;
    @FXML
    TableColumn tableColumnDeadlineWeek;
    @FXML
    TableColumn tableColumnMedia;
    @FXML
    TableView tableViewTema;

    public void setService( Service service,ObservableList<NotaDTO2> model) {
        this.model = model;
        this.service = service;
        initialize();
    }

    private void initialize() {
        tableColumnDescriereTema.setCellValueFactory(new PropertyValueFactory("descriereTema"));
        tableColumnStartWeek.setCellValueFactory(new PropertyValueFactory("dataPredare"));
        tableColumnDeadlineWeek.setCellValueFactory(new PropertyValueFactory("numeProfesor"));
        tableColumnMedia.setCellValueFactory(new PropertyValueFactory("valoare"));

        tableViewTema.setItems(model);
    }
}
