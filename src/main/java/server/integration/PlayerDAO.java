package server.integration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import server.model.Player;

public class PlayerDAO {

    private final EntityManagerFactory emFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    public PlayerDAO() {
        emFactory = Persistence.createEntityManagerFactory("projectPU");
    }

    public void registerClient(Player player) {
        try {
            EntityManager entityManager = beginTransaction();
            entityManager.persist(player);
        } finally {
            commitTransaction();
        }
    }

    public void deleteClient(Player player) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(entityManager.merge(player));
        commitTransaction();
    }

    public Player findCliendByName(String username) {
        try {
            EntityManager em = beginTransaction();
            try {
                Query query = em.createQuery("SELECT u FROM Player u WHERE u.username=:username");
                query.setParameter("username", username);
                return (Player) query.getSingleResult();
            } catch (NoResultException noSuchName) {
                return null;
            }
        } finally {
            commitTransaction();
        }

    }
    
    private EntityManager beginTransaction() {
        EntityManager em = emFactory.createEntityManager();
        threadLocalEntityManager.set(em);
        EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return em;
    }

    private void commitTransaction() {
        threadLocalEntityManager.get().getTransaction().commit();
    }
}
