package domain;

public class Student extends Entity<String>{
    private String nume;
    private String prenume;
    private Integer grupa;
    private String email;
    private String cadruDidacticIndrumatorLab;
    private int nr_motivari;

    public Student() {
    }

    public Student(String nume, String prenume, Integer grupa, String email, String cadruDidacticIndrumatorLab) {
        this.nume = nume;
        this.prenume = prenume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
    }

    @Override
    public String toString() {
        return getId() + "; " + nume + "; " + prenume + "; " + grupa + "; " + email + "; " + cadruDidacticIndrumatorLab + ";" + nr_motivari;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public Integer getGrupa() {
        return grupa;
    }

    public String getEmail() {
        return email;
    }

    public String getCadruDidacticIndrumatorLab() {
        return cadruDidacticIndrumatorLab;
    }


    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setGrupa(Integer grupa) {
        this.grupa = grupa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCadruDidacticIndrumatorLab(String cadruDidacticIndrumatorLab) {
        this.cadruDidacticIndrumatorLab = cadruDidacticIndrumatorLab;
    }

    public int getNr_motivari() {
        return nr_motivari;
    }

    public void setNr_motivari(int nr_motivari) {
        this.nr_motivari = nr_motivari;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        if (!this.nume.equals(student.getNume())) return false;
        if (!this.prenume.equals((student.getPrenume()))) return false;
        if(this.grupa!=student.getGrupa()) return false;
        if(!this.email.equals(student.getEmail())) return false;
        return this.cadruDidacticIndrumatorLab.equals(student.getCadruDidacticIndrumatorLab());
    }

}
