package repository.InMemoryRepository;

import domain.Entity;
import domain.validators.ValidationException;
import domain.validators.Validator;

import java.util.HashMap;
import java.util.Map;

public abstract class InMemoryRepository<ID,E extends Entity<ID>> implements CrudRepository<ID,E> {

    public Map<ID,E> entities;
    protected Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities =new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi nul!");
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if (entity==null) throw new IllegalArgumentException(" Entitatea nu poate fi nula!");
        E element= entities.get(entity.getId());
        if(element==null){
            validator.validate(entity);
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }

    @Override
    public E delete(ID id){
        if (id==null) throw new IllegalArgumentException("Id-ul nu poate fi nul!");
        if (entities.containsKey(id))
            return entities.remove(id);
        return null;
    }

    @Override
    public abstract E update(E entity);
}
