package repository.FileRepository;

import com.sun.tools.javac.util.Pair;
import domain.Nota;
import domain.NotaDTO;
import utils.Paths;
import domain.validators.NotaValidator;
import domain.validators.Validator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static utils.FormatData.DATE_FORMAT;

public class NotaFileRepository extends FileRepository<Nota, Pair<String, String>> {
    private static NotaFileRepository single_instance = null;

    public NotaFileRepository(Validator<Nota> validator, String nameFile) {
        super(validator, nameFile);
    }

    public static NotaFileRepository getInstance() {
        if (single_instance == null)
            single_instance = new NotaFileRepository(new NotaValidator(), Paths.NOTA);
        return single_instance;
    }

    @Override
    public Nota createEntity(String[] fields) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate data=LocalDate.from(formatter.parse(fields[2]));
        Nota nota = new Nota(data, fields[3], Float.parseFloat(fields[4]),fields[5]);
        nota.setId(new Pair<>(fields[0], fields[1]));
        return nota;
    }

    @Override
    public Nota update(Nota entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity" + Nota.class.getName() + "NULL");
        validator.validate(entity);
        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Nota oldValue = entities.get(entity.getId());
        oldValue.setValoare(entity.getValoare());
        oldValue.setData(entity.getData());
        writeAllToFile();
        return null;
    }

    public void saveText(NotaDTO notaDTO) {
        String file = "./data/catalog/" + notaDTO.getIdStudent().toString() + ".txt";
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write("Tema: " + notaDTO.getIdTema());
            out.newLine();
            out.write("Nota: " + notaDTO.getValoare());
            out.newLine();
            out.write("Predata in saptamana: " + notaDTO.getNrSaptamana());
            out.newLine();
            out.write("Deadline: " + notaDTO.getDeadlineWeek());
            out.newLine();
            out.write("Feedback: " + notaDTO.getFeedback());
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
