package services;

import com.sun.tools.javac.util.Pair;
import domain.*;
import repository.FileRepository.NotaFileRepository;
import repository.InMemoryRepository.CrudRepository;
import repository.XMLFileRepository.XMLNotaFileRepository;
import utils.events.EntityChangeEvent;
import utils.events.ChangeEventType;
import utils.events.EntityChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service implements Observable<EntityChangeEvent> {
    CrudRepository<String, Student> studentRepository;
    CrudRepository<String, Tema> temaRepository;
    CrudRepository<Pair<String,String>, Nota> notaRepository;
    private static Integer currentWeek;

    public Service(CrudRepository<String, Student> studentRepository, CrudRepository<String, Tema> temaRepository,CrudRepository<Pair<String,String>, Nota> notaRepository) {
        this.studentRepository = studentRepository;
        this.temaRepository = temaRepository;
        this.notaRepository = notaRepository;
    }

    public static void setCurrentWeek(Integer currentWeek) {
        Service.currentWeek = currentWeek;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    //STUDENT
    public Student findOneStudent(String id){
        return studentRepository.findOne(id);
    }

    public Iterable<Student> findAllStudent(){
        return studentRepository.findAll();
    }

    public Student saveStudent(String id, String nume, String prenume,int grupa, String email, String cadruDidacticIndrumatorLab) {
        Student st=new Student(nume,prenume,grupa,email,cadruDidacticIndrumatorLab);
        st.setId(id);
        st.setNr_motivari(2);
        Student student = studentRepository.save(st);
        if(student==null)
            notifyObserver(new EntityChangeEvent(ChangeEventType.ADD,student));
        return student;
    }

    public Student deleteStudent(String id){
        Student student = studentRepository.delete(id);
        if (student != null)
            notifyObserver(new EntityChangeEvent(ChangeEventType.DELETE, student));
        return student;
    }

    public Student updateStudent(String id,String nume, String prenume,int grupa, String email, String cadruDidacticIndrumatorLab){
        Student st=new Student(nume,prenume,grupa,email,cadruDidacticIndrumatorLab);
        st.setId(id);
        Student oldStudent=studentRepository.findOne(st.getId());
        if (oldStudent!= null){
            Student newStudent = studentRepository.update(st);
            notifyObserver(new EntityChangeEvent(ChangeEventType.UPDATE, newStudent, oldStudent));
        }
        return oldStudent;
    }

    //TEMA
    public Tema findOneTema(String id){
        return temaRepository.findOne(id);
    }

    public Iterable<Tema> findAllTema(){
        return temaRepository.findAll();
    }

    public Tema saveTema(String id,String descriere,int startWeek,int deadlineWeek){
        Tema tema=new Tema(descriere,startWeek,deadlineWeek);
        tema.setId(id);
        return temaRepository.save(tema);
    }

    public Tema deleteTema(String id){
        return temaRepository.delete(id);
    }

    public Tema updateTema(String id,String descriere,int startWeek,int deadlineWeek){
        Tema tema=new Tema(descriere,startWeek,deadlineWeek);
        tema.setId(id);
        return temaRepository.update(tema);
    }

    //NOTA
    public Nota findOneNota(Pair<String,String> id){ return notaRepository.findOne(id);
    }

    public Iterable<Nota> findAllNota() {return notaRepository.findAll();
    }

    public Nota deleteNota(Pair<String, String> id) {
        Nota nota = notaRepository.delete(id);
        if (nota!=null)
            notifyObserver(new EntityChangeEvent(ChangeEventType.DELETE, nota));
        return nota;
    }

    public Nota saveNota(String idStudent, String idTema, LocalDate data, String profesor, float valoare, String feedback, String raspuns) throws IllegalAccessException, InstantiationException {
        Student student = studentRepository.findOne(idStudent);
        Student student1 = new Student(student.getNume(), student.getPrenume(),student.getGrupa(), student.getEmail(), student.getCadruDidacticIndrumatorLab());
        student1.setNr_motivari(student.getNr_motivari());
        student1.setId(student.getId());
        Tema tema = temaRepository.findOne(idTema);
        if (student == null || tema == null)
            throw new ServiceException("Nota nu se poate asigna, id invalid.");

        if (((currentWeek - tema.getDeadlineWeek() == 1 || currentWeek - tema.getDeadlineWeek() == 2) && raspuns.equals("no")) ||
                (currentWeek - tema.getDeadlineWeek() >= 1 && raspuns.equals("yes") && student1.getNr_motivari() == 0))
            valoare -= currentWeek - tema.getDeadlineWeek();

        if (currentWeek - tema.getDeadlineWeek() >= 3 && valoare >= 0)
            valoare = 1f;

        if (currentWeek - tema.getDeadlineWeek() == 1 && raspuns.equals("yes") && student1.getNr_motivari() >= 1) {
            int nr_motivari = student1.getNr_motivari();
            nr_motivari--;
            student1.setNr_motivari(nr_motivari);
        }

        if (currentWeek - tema.getDeadlineWeek() == 2 && raspuns.equals("yes") && student1.getNr_motivari() == 2)
            student1.setNr_motivari(0);

        if (currentWeek - tema.getDeadlineWeek() == 2 && raspuns.equals("yes") && student1.getNr_motivari() == 1) {
            valoare -= 1;
            student1.setNr_motivari(0);
        }

        studentRepository.update(student1);
        Nota nota = new Nota(data, profesor, valoare,feedback);
        nota.setId(new Pair<>(idStudent, idTema));
        int nrSaptamana = StructuraAnUniversitar.getInstance().getWeek(nota.getData(), StructuraAnUniversitar.getInstance().getSemestru(nota.getData()));
        NotaDTO notaDTO = new NotaDTO(student.getNume(), tema.getId(), nota.getValoare(),nrSaptamana, tema.getDeadlineWeek(), feedback);
        XMLNotaFileRepository.getInstance().saveText(notaDTO);
        Nota n = notaRepository.save(nota);
        if(n==null)
            notifyObserver(new EntityChangeEvent(ChangeEventType.ADD,nota));
        return n;
    }

    public Nota updateNota(String idStudent, String idTema, LocalDate data, String profesor, float valoare, String feedback) {
        Nota nota = new Nota(data, profesor, valoare,feedback);
        nota.setId(new Pair<>(idStudent, idTema));
        Nota notaNoua = notaRepository.update(nota);
        notifyObserver(new EntityChangeEvent(ChangeEventType.UPDATE,nota));
        return notaNoua;
    }

    //FILTRARI
    public <E> List<E> Genfilter(List<E> lista, Predicate<E> p){
        return lista.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    public List<Student> filtrareStudentGrupa(int grupa){
        List<Student> listaStudenti = new ArrayList<Student>((Collection<? extends Student>) findAllStudent());
        Predicate<Student> p1 = st -> st.getGrupa()==grupa;
        List<Student> rezultat =  Genfilter(listaStudenti,p1);
        if(rezultat.size()==0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    public List<FiltrareDTO> filtrareNotaStudentTema(String idTema){
        List<Nota> listaStudenti = new ArrayList<Nota>((Collection<? extends Nota>) findAllNota());
        Predicate<Nota> p1 = nota -> nota.getId().snd.equals(idTema);
        List<Nota> lista = Genfilter(listaStudenti,p1);
        List<FiltrareDTO> rezultat = cautaStudent(lista);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    //toti studentii care au predat o anumita tema unui profesor anume
    public List<FiltrareDTO> filtrareTemaProfesor(String idTema, String profesor){
        List<Nota> catalog = new ArrayList<Nota>((Collection<? extends Nota>) findAllNota());
        Predicate<Nota> p1 = nota->nota.getId().snd.equals(idTema) && nota.getProfesor().equals(profesor);
        List<Nota> lista = Genfilter(catalog,p1);
        List<FiltrareDTO> rezultat = cautaStudent(lista);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    //toate notele la o anumita tema dintr-o saptamana data
    public List<FiltrareDTO> filtrareNoteSaptamana (String idTema, int NrSaptamana){
        List<Nota> catalog = new ArrayList<Nota>((Collection<? extends Nota>) findAllNota());
        Predicate<Nota> p1 = nota -> nota.getId().snd.equals(idTema) && StructuraAnUniversitar.getInstance()
                .getWeek(nota.getData(), StructuraAnUniversitar.getInstance().getSemestru(nota.getData())) == NrSaptamana;
        List<Nota> lista = Genfilter(catalog, p1);
        List<FiltrareDTO> rezultat = cautaStudent(lista);
        if (rezultat.size() == 0)
            throw new ServiceException("Nu s-au gasit rapoarte!");
        return rezultat;
    }

    public List<FiltrareDTO> cautaStudent(List<Nota> lista){
        List<FiltrareDTO> rezultat = new ArrayList<>();
        lista.forEach(nota -> {
            Student student = studentRepository.findOne(nota.getId().fst);
            Tema tema = temaRepository.findOne(nota.getId().snd);
            LocalDate data = nota.getData();
            int nrSaptamana = StructuraAnUniversitar.getInstance().getWeek(data, StructuraAnUniversitar.getInstance().getSemestru(data));
            FiltrareDTO entity = new FiltrareDTO(student.getNume(), student.getPrenume(), nota.getProfesor(), tema.getDescriere(), nrSaptamana);
            rezultat.add(entity);
        });
        return rezultat;
    }

    private List<Observer<EntityChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<EntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(EntityChangeEvent t) {
        observers.stream()
                .forEach(x->x.update(t));
    }
}
