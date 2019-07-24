package com.task.db.core;

import java.util.List;
import java.util.UUID;

public interface EntityDao<T> {
    boolean create(T entity);
    boolean update(T entity);
    boolean update(T... entity);
    boolean delete(T entity);
    List<T> getAll();
    T getById(UUID id);
}
