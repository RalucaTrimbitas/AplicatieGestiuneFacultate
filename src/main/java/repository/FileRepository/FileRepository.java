package repository.FileRepository;

import domain.Entity;
import repository.InMemoryRepository.InMemoryRepository;
import domain.validators.ValidationException;
import domain.validators.Validator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public abstract class FileRepository<E extends Entity<ID>, ID> extends InMemoryRepository<ID, E> implements CrudFileRepository<ID, E> {

    private String nameFile;

    public FileRepository(Validator<E> validator, String nameFile) {
        super(validator);
        this.nameFile = nameFile;
        loadData();
    }

    @Override
    public E findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public E save(E entity) throws ValidationException {
        E rezultat = super.save(entity);
        if (rezultat==null)
            writeOneToFile(entity);
        return rezultat;
    }

    @Override
    public E delete(ID id) {
        E rezultat = super.delete(id);
        writeAllToFile();
        return rezultat;
    }

    @Override
    public void writeAllToFile() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(nameFile))) {
            for (E entity : super.findAll()) {
                bufferedWriter.newLine();
                bufferedWriter.write(entity.toString());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeOneToFile(E entity) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(nameFile), StandardOpenOption.APPEND)) {
            bufferedWriter.write(entity.toString());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        Path path = Paths.get(nameFile);
        try {
            List<String> inputList = Files.readAllLines(path);
            inputList.forEach(this::parseLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract E createEntity(String[] fields);

    private void parseLine(String line) {
        try {
            String[] fields = line.split("; ");
            E entity = createEntity(fields);
            super.save(entity);
        } catch (ValidationException e) {
            e.getStackTrace();
        }
    }
}
