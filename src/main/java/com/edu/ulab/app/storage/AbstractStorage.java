package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.AbstractProjectEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public abstract class AbstractStorage<T extends AbstractProjectEntity> {
    private final Map<Long, T> repository = new HashMap<>();

    public T save(T entity) {
        return repository.put(entity.getId(), entity);
    }

    public T findById(Long id) {
        return repository.get(id);
    }

    public Set<Long> getAllId() {
        return repository.keySet();
    }

    public void delete(Long id) {
        repository.remove(id);
    }
}
