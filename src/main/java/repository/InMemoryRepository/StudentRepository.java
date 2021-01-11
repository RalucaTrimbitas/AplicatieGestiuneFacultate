package repository.InMemoryRepository;
import domain.Student;
import domain.validators.Validator;

public class StudentRepository extends InMemoryRepository<String,Student> {

    public StudentRepository(Validator<Student> validator) {
        super(validator);
    }

    @Override
    public Student update(Student entity) {
        if (entity == null) throw new IllegalArgumentException("Entity " + Student.class.getName() + " is NULL");
        validator.validate(entity);
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
        return null;

    }
}