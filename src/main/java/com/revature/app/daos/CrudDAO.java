package com.revature.app.daos;

import java.util.ArrayList;

public interface CrudDAO<T>{

    void save(T newObject);
    T getById(String id);
    ArrayList<T> getAll();
    void update(T updateObject);
    void deleteById(String Id);
}
