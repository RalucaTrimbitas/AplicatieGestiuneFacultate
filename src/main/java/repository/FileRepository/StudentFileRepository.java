package repository.FileRepository;

import domain.Student;
import domain.validators.Validator;

public class StudentFileRepository extends FileRepository<Student,String> {
    private String nameFile;

    public StudentFileRepository(Validator<Student> validator, String nameFile) {
        super(validator, nameFile);
    }

    @Override
    public Student createEntity(String[] fields) {
        try {
            Student student = new Student(fields[1], fields[2], Integer.parseInt(fields[3]), fields[4], fields[5]);
            student.setId(fields[0]);
            student.setNr_motivari(Integer.parseInt(fields[6]));
            return student;
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student update(Student entity) {
        if (entity == null) throw new IllegalArgumentException("Entity " + Student.class.getName() + " is NULL");

        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Student oldValue = entities.get(entity.getId());
        oldValue.setNume(entity.getNume());
        oldValue.setPrenume(entity.getPrenume());
        oldValue.setGrupa(entity.getGrupa());
        oldValue.setEmail(entity.getEmail());
        oldValue.setCadruDidacticIndrumatorLab(entity.getCadruDidacticIndrumatorLab());
        oldValue.setNr_motivari(entity.getNr_motivari());
        writeAllToFile();
        return null;
    }
}
