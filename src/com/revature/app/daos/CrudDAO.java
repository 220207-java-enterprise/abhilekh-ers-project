package com.revature.app.daos;

public interface CrudDAO<T>{

    void save(T newObject);
    T getById(String id);
    T[] getAll();
    void update(T updatedObject);
    void deleteById(String Id);
}
