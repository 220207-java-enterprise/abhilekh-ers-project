package com.revature.app.daos;

import java.util.List;

public interface CrudDAO<T>{

    void save(T newObject);
    T getById(String id);
    List<T> getAll();
    void update(T updateObject);
    void deleteById(String Id);
}
