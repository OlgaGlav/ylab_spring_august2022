package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.AbstractProjectEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public abstract class AbstractStorage {

    private final Map<Long, AbstractProjectEntity> repository = new HashMap<>();

    public AbstractProjectEntity save(AbstractProjectEntity entity) {
        return repository.put(entity.getId(), entity);
    }

    public <T extends AbstractProjectEntity> T findById(Long id) {
        return (T) repository.get(id);
    }

    public Set<Long> getAllId() {
        return repository.keySet();
    }
}
