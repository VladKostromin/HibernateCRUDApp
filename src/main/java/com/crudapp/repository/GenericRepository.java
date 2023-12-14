package com.crudapp.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    T findByID(ID id);
    List<T> getAll();
    T save(T t);
    T update(T t);
    T deleteByID(ID id);

}
