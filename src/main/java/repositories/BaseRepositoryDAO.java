package repositories;

import entityManagerFactory.EntityManagerRepository;

import javax.persistence.*;
import java.util.List;

public abstract class BaseRepositoryDAO<Entity, Id extends Number> {
    protected abstract Class<Entity> getEntityClass();

    EntityManager entityManager = EntityManagerRepository.getEntityManager();


    public List<Entity> selectAll() {
        entityManager.getTransaction().begin();
        TypedQuery<Entity> query = entityManager.createQuery("select entity from " + getEntityClass().getName() + " entity", getEntityClass());
        List<Entity> entityList = query.getResultList();
        entityManager.getTransaction().commit();
        return entityList;
    }

    public Entity findByUserName(String userName) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.username=:userName");
        query.setParameter("userName", userName);
        Entity entity = null;
        try {
            entity = (Entity) query.getSingleResult();
        } catch (NoResultException n)
        {
            System.out.println("this username not exist");
        }
        entityManager.getTransaction().commit();
        return entity;
    }

    public Entity selectById(Id id) {
        entityManager.getTransaction().begin();
        Entity entity = entityManager.find(getEntityClass(), id);
        entityManager.getTransaction().commit();
        return entity;
    }

    public Entity save(Entity entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            System.out.println("Registration completed successfully");
        } catch (PersistenceException p) {
            System.out.println(p.getLocalizedMessage());
            System.out.println(p.getMessage());
            System.out.println("The information entered is not valid");
            return null;
        }
        return entity;
    }

    public void removeByFkAccount(String inputEntity, long fkAccount) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("delete from " + inputEntity + " e where e.account.id=:fkAccount");
        query.setParameter("fkAccount", fkAccount);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    public void removeByFkPost(String inputEntity, long fkPost) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("delete from " + inputEntity + " e where e.post.id=:fkPost");
        query.setParameter("fkPost", fkPost);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    public void remove(Entity entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    public void removeById(Id id) {
        Entity entity = selectById(id);
        if (entity != null) {
            remove(entity);
            System.out.println("entity was deleted");
        } else System.out.println("this entity not exist");
    }

    public void update(Entity entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }
}
