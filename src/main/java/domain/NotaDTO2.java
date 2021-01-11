package domain;

import com.sun.tools.javac.util.Pair;
import utils.events.Event;

public class NotaDTO2 extends Entity<Pair<String,String>> implements Event {

    private String numeStudent;
    private String descriereTema;
    private Float valoare;
    private String numeProfesor;
    private String dataPredare;
    private String feedback;

    public NotaDTO2(String numeStudent, String descriereTema, Float valoare, String numeProfesor, String dataPredare, String feedback) {
        this.numeStudent = numeStudent;
        this.descriereTema = descriereTema;
        this.valoare = valoare;
        this.numeProfesor = numeProfesor;
        this.dataPredare = dataPredare;
        this.feedback = feedback;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public String getDescriereTema() {
        return descriereTema;
    }

    public void setDescriereTema(String descriereTema) {
        this.descriereTema = descriereTema;
    }

    public Float getValoare() {
        return valoare;
    }

    public void setValoare(Float valoare) {
        this.valoare = valoare;
    }

    public String getNumeProfesor() {
        return numeProfesor;
    }

    public void setNumeProfesor(String numeProfesor) {
        this.numeProfesor = numeProfesor;
    }

    public String getDataPredare() {
        return dataPredare;
    }

    public void setDataPredare(String dataPredare) {
        this.dataPredare = dataPredare;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
