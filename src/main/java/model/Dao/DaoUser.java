package model.Dao;

import model.Entities.User;

import javax.persistence.Query;
import java.util.List;

public class DaoUser extends DaoAbstract<User> {
    public DaoUser() {
        super(User.class);
    }

    public User authorizationUser(String login, String password)  {
        Query query = getEntityManager().createQuery("FROM " + getEntityName() + " WHERE login = :login AND " +
                "password = :password");
        query.setParameter("login", login);
        query.setParameter("password", password);
        List<User> users = query.getResultList();
        // DB must check
        //        if (users.size() > 1)
        //            throw new DaoException("Users with equals login and password more then one. Amount = " + users.size());
        if (users == null || users.isEmpty())
            return null;
        //if (users.size() == 1)
        return users.get(0);
    }

    public User getUserByLogin(String login)  {
        Query query = getEntityManager().createQuery("FROM " + getEntityName() + " WHERE login = :login");
        query.setParameter("login", login);
        List<User> users = query.getResultList();
        if (users == null || users.isEmpty())
            return null;
        return users.get(0);
    }
}