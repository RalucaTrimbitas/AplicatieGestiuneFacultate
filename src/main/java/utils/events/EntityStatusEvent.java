package utils.events;

import domain.Entity;

public class EntityStatusEvent<ID, E extends Entity<ID>> implements Event {
    private EntityExecutionStatusEventType type;
    private E entity;

    public EntityStatusEvent(EntityExecutionStatusEventType type, E entity) {
        this.type = type;
        this.entity = entity;
    }

    public EntityExecutionStatusEventType getType() {
        return type;
    }

    public void setType(EntityExecutionStatusEventType type) {
        this.type = type;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }
}
