package model.Dao;


import javax.persistence.*;
import java.util.List;

public abstract class DaoAbstract<T> {

    static private EntityManagerFactory entityManagerFactory = null;
    //@PersistenceContext(unitName = "questionUnit")
    private EntityManager entityManager;// = Persistence.createEntityManagerFactory("questionUnit").createEntityManager();

    private Class<T> entityBeanType;
    private String entityName;

    public DaoAbstract(Class<T> entityBeanType) {
        this.entityBeanType = entityBeanType;
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("questionUnit");
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityName = entityManager.getMetamodel().entity(entityBeanType).getName();
    }

    protected Class<T> getEntityBeanType() {
        return entityBeanType;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected void closeEntityManager(){
        entityManager.close();
    }

    protected String getEntityName() {
        return entityName;
    }

    public T get(int id) {
        return entityManager.find(getEntityBeanType(), id);
    }

    public List<T> getAll() {
        Query query = entityManager.createQuery("FROM " + entityName);
        return query.getResultList();
    }

    public void save(T obj) {
        entityManager.persist(obj);
    }

    public void update(T obj) {
        entityManager.merge(obj);
    }

    public void delete(T obj) {
        entityManager.remove(obj);
    }
}
