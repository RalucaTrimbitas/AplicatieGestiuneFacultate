package ui;

import com.sun.tools.javac.util.Pair;
import domain.FiltrareDTO;
import domain.Nota;
import domain.Student;
import domain.Tema;
import repository.RepositoryException;
import services.Service;
import services.ServiceException;
import domain.validators.ValidationException;
import domain.validators.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Consola {
    private static Integer currentWeek;
    Scanner scanner = new Scanner(System.in);
    Validator<Student> validatorStudent;
    Validator<Tema> validatorTema;
    private Service service;

    private MenuCommand mainMenu;

    public Consola(Service service) {
        this.service = service;
        Service.setCurrentWeek(currentWeek);
    }

    public static void setCurrentWeek(Integer currentWeek) {
        Consola.currentWeek = currentWeek;
    }

    public void createMenu() {
        System.out.println("Saptamana Curenta: " + currentWeek);
        mainMenu = new MenuCommand("MainMenu: ");
        MenuCommand crudStudent = new MenuCommand("Operatii CRUD Student:");
        crudStudent.addCommand("1. Adauga un student", new AddStudentCommand());
        crudStudent.addCommand("2. Sterge un student", new DeleteStudentCommand());
        crudStudent.addCommand("3. Cauta un student", new FindStudentCommand());
        crudStudent.addCommand("4. Modifica un student", new UpdateStudentCommand());
        crudStudent.addCommand("5. Lista studenti: ", new tiparireStudenti());
        crudStudent.addCommand("6. to MainMenu", mainMenu);

        MenuCommand crudTema = new MenuCommand("Operatii CRUD Tema: ");
        crudTema.addCommand("1. Adauga o tema", new AddTemaCommand());
        crudTema.addCommand("2. Sterge o tema", new DeleteTemaCommand());
        crudTema.addCommand("3. Cauta o tema", new FindTemaCommand());
        crudTema.addCommand("4. Modifica o tema", new UpdateTemaCommand());
        crudTema.addCommand("5. Lista teme: ", new tiparireTeme());
        crudTema.addCommand("6. to MainMenu", mainMenu);

        MenuCommand crudNota = new MenuCommand("Operatii Nota: ");
        crudNota.addCommand("1.Adauga o nota", new AddNotaCommand());
        crudNota.addCommand("2. Sterge o nota", new DeleteNotaCommand());
        crudNota.addCommand("3. Gaseste o nota", new FindNotaCommand());
        crudNota.addCommand("4. Modifica o nota", new UpdateNotaCommand());
        crudNota.addCommand("5. Lista note: ", new tiparireNote());
        crudNota.addCommand("6. to MainMenu", mainMenu);

        MenuCommand rapoarte = new MenuCommand("Rapoarte: ");
        rapoarte.addCommand("1.Toti studentii unei grupe", new FiltrareStudentGrupaCommand());
        rapoarte.addCommand("2.Toti studentii care au predat o anumita tema", new FiltrareNotaStudentTemaCommand());
        rapoarte.addCommand("3.Toti studentii care au predat o anumita tema unui profesor anume", new FiltrareTemaProfesorCommand());
        rapoarte.addCommand("4.Toate notele la o anumita tema dintr-o sapt data", new FiltareTemaSaptamanaCommand());
        rapoarte.addCommand("5. to MainMenu", mainMenu);

        mainMenu.addCommand("1. Operatii CRUD Student", crudStudent);
        mainMenu.addCommand("2. Operatii CRUD Tema", crudTema);
        mainMenu.addCommand("3. Operatii Nota", crudNota);
        mainMenu.addCommand("4. Rapoarte", rapoarte);
        mainMenu.addCommand("5. Exit", new ExitCommand());
    }

    public void runMenu() {
        createMenu();
        MenuCommand currentMenu = mainMenu;
        while (true) {
            System.out.println(currentMenu.getNameMenu());
            currentMenu.execute();
            System.out.println("Introduceti o comanda: ");
            int actionNumber = scanner.nextInt();
            if (actionNumber > 0 && actionNumber <= currentMenu.getCommands().size()) {
                Command selectedCommand = currentMenu.getCommands().get(actionNumber - 1);
                if (selectedCommand instanceof MenuCommand)
                    currentMenu = (MenuCommand) selectedCommand;
                else selectedCommand.execute();
            } else System.out.println("Comanda invalida! Introduceti alta comanda...");
        }
    }

    public class AddStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner read = new Scanner(System.in);
            System.out.println("ID: ");
            String id = read.nextLine();
            System.out.println("Nume: ");
            String nume = read.nextLine();
            System.out.println("Prenume: ");
            String prenume = read.nextLine();
            System.out.println("Grupa: ");
            int grupa = read.nextInt();
            read.nextLine();
            System.out.println("E-mail: ");
            String email = read.nextLine();
            System.out.println("Cadru didactic laborator: ");
            String cadruDidacticLab = read.nextLine();
            try {
                Student student = service.saveStudent(id, nume, prenume, grupa, email, cadruDidacticLab);
                if (student == null)
                    System.out.println("Studentul " + nume + " " + prenume + " a fost adaugat cu succes!");
                else
                    System.out.println("Studentul " + student.getNume() + " " + student.getPrenume() + " exista deja!");
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            service.findAllStudent().forEach(System.out::println);
        }
    }

    public class DeleteStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            try {
                Student st = service.deleteStudent(id);
                if (st.getId()==id){
                    System.out.println("Studentul " + st.getNume() + st.getPrenume() + " a fost sters cu succes!");
                }
            } catch (RepositoryException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            service.findAllStudent().forEach(System.out::println);
        }
    }

    public class FindStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            try {
                Student student = service.findOneStudent(id);
                System.out.println("Studentul gasit: " + student.toString());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public class UpdateStudentCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            System.out.println("Nume: ");
            String nume = scanner.nextLine();
            System.out.println("Prenume: ");
            String prenume = scanner.nextLine();
            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();
            scanner.nextLine();
            System.out.println("E-mail: ");
            String email = scanner.nextLine();
            System.out.println("Cadru didactic laborator: ");
            String cadruDidacticLab = scanner.nextLine();

            try {
                Student st = service.updateStudent(id, nume, prenume, grupa, email, cadruDidacticLab);
                if (st != null)
                    System.out.println("Studentul " + nume + " " + prenume + " este deja modificat!");
                else
                    System.out.println("Studentul " + st.getNume() + st.getPrenume() + " a fost modificat!");
            } catch (ValidationException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class ExitCommand implements Command {
        @Override
        public void execute() {
            System.out.println("BYE!");
            System.exit(0);
        }
    }

    private class AddTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            System.out.println("Descriere: ");
            String descriere = scanner.nextLine();
            System.out.println("DeadlineWeek: ");
            int deadLineWeek = scanner.nextInt();
            scanner.nextLine();
            try {
                service.saveTema(id, descriere, currentWeek, deadLineWeek);
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            service.findAllTema().forEach(System.out::println);
        }
    }

    private class DeleteTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            try {
                service.deleteTema(id);
            } catch (RepositoryException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            service.findAllTema().forEach(System.out::println);
        }
    }

    private class FindTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            try {
                Tema tema = service.findOneTema(id);
                System.out.println("Tema gasita: " + tema.toString());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class UpdateTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            String id = scanner.nextLine();
            System.out.println("Descriere: ");
            String descriere = scanner.nextLine();
            System.out.println("DeadlineWeek: ");
            int deadLineWeek = scanner.nextInt();
            scanner.nextLine();
            try {
                Tema tema_noua = service.saveTema(id, descriere, currentWeek, deadLineWeek);
                service.updateTema(id, descriere, currentWeek, deadLineWeek);
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            service.findAllTema().forEach(System.out::println);
        }
    }

    private class AddNotaCommand implements Command {

        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student: ");
            String idStudent = scanner.nextLine();
            System.out.println("Motivat: yes/no ");
            String raspuns = scanner.nextLine();
            System.out.println("ID Tema: ");
            String idTema = scanner.nextLine();
            System.out.println("Zi: ");
            int zi = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Luna: ");
            int luna = scanner.nextInt();
            scanner.nextLine();
            System.out.println("An: ");
            int an = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Profesor: ");
            String profesor = scanner.nextLine();
            System.out.println("Valoare: ");
            float valoare = scanner.nextFloat();
            Scanner read = new Scanner(System.in);
            System.out.println("Feedback: ");
            String feedback = read.nextLine();
            try {
                LocalDate data = LocalDate.of(an, luna, zi);
                Tema tema = service.findOneTema(idTema);
                if (currentWeek - tema.getDeadlineWeek() == 1)
                    System.out.println("Nota maxima=9!\n ");
                if (currentWeek - tema.getDeadlineWeek() == 2)
                    System.out.println("Nota maxima=8!\n");
                if (data.isBefore(LocalDate.now()))
                    System.out.println("Profesorul nu a asignat notele la timp.");
                Nota nota = service.saveNota(idStudent, idTema, data, profesor, valoare, feedback, raspuns);
                if (nota==null)
                    System.out.println("Nota a fost adaugata cu succes!");
                else
                    System.out.println("Exista deja aceasta nota!");
            } catch (ValidationException | ServiceException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            service.findAllNota().forEach(System.out::println);
        }
    }

    private class DeleteNotaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student:");
            String idStudent = scanner.nextLine();
            System.out.println("ID Tema:");
            String idTema = scanner.nextLine();
            try {
                service.deleteNota(new Pair<>(idStudent, idTema));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class FindNotaCommand implements Command {

        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student:");
            String idStudent = scanner.nextLine();
            System.out.println("ID Tema:");
            String idTema = scanner.nextLine();
            try {
                service.findOneNota(new Pair<>(idStudent, idTema));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class UpdateNotaCommand implements Command {

        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Student:");
            String idStudent = scanner.nextLine();
            System.out.println("ID Tema:");
            String idTema = scanner.nextLine();
            System.out.println("Valoare: ");
            float valoare = scanner.nextFloat();
            System.out.println("Feedback: ");
            String feedback = scanner.nextLine();
            scanner.nextLine();
            try {
                Nota nota = service.findOneNota(new Pair<>(idStudent, idTema));
                service.updateNota(idStudent, idTema, nota.getData(), nota.getProfesor(), valoare,feedback);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private class FiltrareStudentGrupaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Grupa: ");
            int grupa = scanner.nextInt();
            scanner.nextLine();
            try {
                List<Student> lista = service.filtrareStudentGrupa(grupa);
                System.out.println("Toti studentii care apartin unei grupe: ");
                lista.forEach(System.out::println);
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class FiltrareNotaStudentTemaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Tema: ");
            String idTema = scanner.nextLine();
            try {
                List<FiltrareDTO> lista = service.filtrareNotaStudentTema(idTema);
                System.out.println("Toti studentii care au predat o anumita tema: ");
                lista.forEach(notaDto -> System.out.println(notaDto.getNumeStudent()+ " " + notaDto.getPrenumeStudent() + "--" + notaDto.getDescriereTema()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class FiltrareTemaProfesorCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Tema: ");
            String idTema = scanner.nextLine();
            System.out.println("Profesor: ");
            String profesor = scanner.nextLine();
            try {
                List<FiltrareDTO> lista = service.filtrareTemaProfesor(idTema, profesor);
                System.out.println("Toti studentii care au predat o anumita tema unui profesor anume: ");
                lista.forEach(entity -> System.out.println(entity.getNumeStudent() + " " + entity.getPrenumeStudent() + "--" + entity.getDescriereTema()
                        + " " + entity.getProfesor()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private class FiltareTemaSaptamanaCommand implements Command {
        @Override
        public void execute() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID Tema: ");
            String idTema = scanner.nextLine();
            System.out.println("Nr saptamana: ");
            int nr_saptamana = scanner.nextInt();
            scanner.nextLine();
            try {
                List<FiltrareDTO> lista = service.filtrareNoteSaptamana(idTema, nr_saptamana);
                System.out.println("Toate notele la o anumita tema,dintr-o saptamana data: ");
                lista.forEach(entity-> System.out.println(entity.getDescriereTema() + "--" + entity.getNrSaptamana()));
            } catch (ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class tiparireStudenti implements Command {
        @Override
        public void execute() {
            service.findAllStudent().forEach(System.out::println);

        }
    }

    private class tiparireTeme implements Command {
        @Override
        public void execute() {
            service.findAllTema().forEach(System.out::println);
        }
    }

    private class tiparireNote implements Command {
        @Override
        public void execute() {
            service.findAllNota().forEach(System.out::println);

        }
    }
}