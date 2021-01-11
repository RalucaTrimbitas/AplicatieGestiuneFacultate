package repository.InMemoryRepository;

import domain.Tema;
import domain.validators.Validator;

public class TemaRepository extends InMemoryRepository<String, Tema> {

    public TemaRepository(Validator<Tema> validator) {
        super(validator);
    }

    @Override
    public Tema update(Tema entity) {
        if (entity == null)
            throw new IllegalArgumentException(("Entity " + Tema.class.getName() + " is NULL"));
        validator.validate(entity);
        if(entities.get(entity.getId()).equals(entity)){
            return entity;
        }
        // trebuie implementata verificarea nr saptamnii curente < entity.deadLine
        Tema oldValue=entities.get(entity.getId());
        oldValue.setDescriere(entity.getDescriere());
        oldValue.setStartWeek(entity.getStartWeek());
        oldValue.setDeadlineWeek(entity.getDeadlineWeek());
        return null;
    }
}
