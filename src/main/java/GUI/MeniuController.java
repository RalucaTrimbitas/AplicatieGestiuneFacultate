package GUI;

import GUI.Rapoarte.RapoarteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.Service;

import java.io.IOException;

public class MeniuController {
    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void handleViewStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/guiStudentView.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Student");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            StudentController studentController = loader.getController();
            studentController.setService(service);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewNota(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/guiNotaView.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Note");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            NotaController notaController = loader.getController();
            notaController.setService(service);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewRapoarte() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/rapoarte/rapoarteView.fxml"));
            AnchorPane root = loader.load();
            Stage notaStage = new Stage();
            notaStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            notaStage.setScene(scene);
            RapoarteController rapoarteController = loader.getController();
            rapoarteController.setService(service);
            notaStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
