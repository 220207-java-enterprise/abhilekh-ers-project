package com.revature.app.daos;

import com.revature.app.models.User;

public class UserDAO  implements CrudDAO<User>{

    public User findUserByUsernameAndPassword(String username, String password){
        return null;
    }

    @Override
    public void save(User newObject) {

    }

    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public User[] getAll() {
        return new User[0];
    }

    @Override
    public void update(User updatedObject) {

    }

    @Override
    public void deleteById(String Id) {

    }
}
