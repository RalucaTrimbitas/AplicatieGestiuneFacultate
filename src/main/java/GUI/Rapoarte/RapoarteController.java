package GUI.Rapoarte;

import GUI.ControllerAlert;
import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import services.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RapoarteController {
    ObservableList<NotaDTO2> modelNota = FXCollections.observableArrayList();
    private Service service;

    @FXML
    AnchorPane viewAnchorPane;
    public void setService(Service service) {
        this.service = service;
    }

    private List<Nota> getNotaList() {
        Iterable<Nota> note = service.findAllNota();
        List<Nota> listaNote = StreamSupport.stream(note.spliterator(),false)
                .collect(Collectors.toList());
        return listaNote;
    }

    private List<Student> getStudentList() {
        Iterable<Student> studenti = service.findAllStudent();
        List<Student> listaStudenti = StreamSupport.stream(studenti.spliterator(),false)
                .collect(Collectors.toList());
        return listaStudenti;
    }

    private List<Tema> getTemaList() {
        Iterable<Tema> teme = service.findAllTema();
        List<Tema>  temaList = StreamSupport.stream(teme.spliterator(), false)
                .collect(Collectors.toList());
        return temaList;
    }

    private List<NotaDTO2> getRaportNotaLaborator(List<Nota> notaList){
        List<NotaDTO2> rezultat = new ArrayList<>();
        List<Student> studentList = getStudentList();
        List<Tema> temaList = getTemaList();
        studentList.forEach(student->{
            final float[] suma = {0};
            final float[] pondere = {0};
            final float media[] = {0};
            temaList.forEach(tema -> {
                notaList.forEach(nota->{
                    if (student.getId().equals(nota.getId().fst) && tema.getId().equals(nota.getId().snd)){
                        suma[0] += (nota.getValoare() * (tema.getDeadlineWeek() - tema.getStartWeek()));
                        pondere[0] += (tema.getDeadlineWeek() - tema.getStartWeek());
                    }
                });
                if (suma[0] == 0) {
                    suma[0] += 1;
                    pondere[0] += (tema.getDeadlineWeek() - tema.getStartWeek());
                }
            });
            media[0] = suma[0] / pondere[0];
            rezultat.add(new NotaDTO2(student.getNume(),student.getPrenume(), media[0],"",String.valueOf(student.getGrupa()),""));
        });
        if (rezultat.size() != 0)
            return rezultat;
        return null;
    }

    List<NotaDTO2> getRaportTemaGrea(List<Nota> notaList){
        List<NotaDTO2> rezultat = new ArrayList<>();
            List<Tema> temaList = getTemaList();
            temaList.forEach(tema->{
                final float[] pondere = {0};
                final float[] suma = {0};
                notaList.forEach(nota ->{
                    Student student = service.findOneStudent(nota.getId().fst);
                    if (tema.getId().equals(nota.getId().snd)){
                        suma[0] += nota.getValoare();
                        pondere[0] +=1;
                    }
                    else {
                        suma[0] += 1;
                        pondere[0] += 1;
                    }
                });
                float media = suma[0] / pondere[0];
                rezultat.add(new NotaDTO2("",tema.getDescriere(),media, String.valueOf(tema.getDeadlineWeek()),String.valueOf(tema.getStartWeek()), ""));
            });

            float minim = 10;
            for (NotaDTO2 notaDTO2 : rezultat) {
            if (notaDTO2.getValoare() < minim)
                minim = notaDTO2.getValoare();
            }

            float finalMinim = minim;
            List<NotaDTO2> afisaj = new ArrayList<>();
            rezultat.forEach(notaDTO2 -> {
                    if (notaDTO2.getValoare() == finalMinim)
                        afisaj.add(notaDTO2);
                });
            if (afisaj.size()!=0)
                return afisaj;
            return null;
    }

    private List<NotaDTO2> getRaportStudentiExamen(List<NotaDTO2> notaDTO2ViewList){
        List<NotaDTO2> rezultat = new ArrayList<>();
        notaDTO2ViewList.forEach(notaDTO2 -> {
            if (notaDTO2.getValoare() >=4)
                rezultat.add(notaDTO2);
        });
        if (rezultat.size() !=0)
            return rezultat;
        return null;
    }

    private List<NotaDTO2> getRaportStudentiPremianti(List<Nota> notaList){
        List<NotaDTO2> rezultat = new ArrayList<>();
        List<Student> studentList = getStudentList();
        studentList.forEach(student->{
            final int[] count = {0};
            notaList.forEach(nota ->{
                Tema tema = service.findOneTema(nota.getId().snd);
                int nrSaptamanaPredare= StructuraAnUniversitar.getInstance().getWeek(nota.getData(),
                        StructuraAnUniversitar.getInstance().getSemestru(nota.getData()));
                if (student.getId().equals(nota.getId().fst) && nrSaptamanaPredare <= tema.getDeadlineWeek()){
                    count[0]++;
                }
            });
            if (count[0] == getTemaList().size()) {
                rezultat.add(new NotaDTO2(student.getNume(), student.getPrenume(), (float) student.getGrupa(), "", "", ""));
            }
        });
        if (rezultat.size() !=0)
            return rezultat;
        return null;
    }

    public void handleNoteLaborator(ActionEvent ectionEvent) throws IOException{
        List<NotaDTO2> lista = getRaportNotaLaborator(getNotaList());
        if (lista == null) {
            ControllerAlert.showErrorMessage(null,"Nu s-au gasit rapoarte!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarte/notaLaboratorView.fxml"));
        AnchorPane notaPane = loader.load();
        NotaLaboratorController notaLaboratorController = loader.getController();
        notaLaboratorController.setService(service,modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(notaPane);
    }

    public void handleTemaDificila(ActionEvent ectionEvent) throws IOException{
        List<NotaDTO2> lista = getRaportTemaGrea(getNotaList());
        if (lista == null) {
            ControllerAlert.showErrorMessage(null,"Nu s-au gasit rapoarte!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarte/TemaGreaView.fxml"));
        AnchorPane notaPane = loader.load();
        TemaGreaController temaGreaController = loader.getController();
        temaGreaController.setService(service,modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(notaPane);
    }

    public void handleStudentExamen(ActionEvent actionEvent) throws IOException {
        List<NotaDTO2> lista = getRaportStudentiExamen(getRaportNotaLaborator(getNotaList()));
        if (lista == null) {
            ControllerAlert.showErrorMessage(null,"Nu s-au gasit rapoarte!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarte/notaLaboratorView.fxml"));
        AnchorPane studentPane = loader.load();
        NotaLaboratorController notaLaboratorController = loader.getController();
        notaLaboratorController.setService(service, modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(studentPane);
    }

    public void handleStudentPremiant(ActionEvent actionEvent) throws IOException {
        List<NotaDTO2> lista = getRaportStudentiPremianti(getNotaList());
        if (lista == null) {
            ControllerAlert.showErrorMessage(null,"Nu s-au gasit rapoarte!");
            return;
        }
        modelNota.setAll(lista);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/rapoarte/studentPremiant.fxml"));
        AnchorPane studentPane = loader.load();
        StudentPremiantController studentPremiantController = loader.getController();
        studentPremiantController.setService(service, modelNota);
        viewAnchorPane.getChildren().removeAll();
        viewAnchorPane.getChildren().setAll(studentPane);
    }
}
