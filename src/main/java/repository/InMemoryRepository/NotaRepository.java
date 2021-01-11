package repository.InMemoryRepository;

import com.sun.tools.javac.util.Pair;
import domain.Nota;
import domain.validators.Validator;

public class NotaRepository extends InMemoryRepository<Pair<String, String>, Nota> {
    public NotaRepository(Validator<Nota> validator) {
        super(validator);
    }

    @Override
    public Nota update(Nota entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity " + Nota.class.getName() + " NULL!");
        validator.validate(entity);
        if (entities.get(entity.getId()).equals(entity)) {
            return entity;
        }
        Nota oldValue = entities.get(entity.getId());
        oldValue.setValoare(entity.getValoare());
        oldValue.setData(entity.getData());
        return null;
    }
}
