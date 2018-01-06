package server.integration;

import java.util.ArrayList;
import java.util.List;
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

    public void deletePlayer(Player player) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(entityManager.merge(player));
        commitTransaction();
    }

    public Player findPlayer(String username) {
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

    public List<Player> listPlayers() {

        EntityManager em = beginTransaction();
        Query query = em.createQuery("SELECT p FROM Player p");
        List<Player> players;

        try {
            players = query.getResultList();
        } catch (NoResultException e) {
            players = new ArrayList<>();
        }

        commitTransaction();
        return players;
    }

    public void updateInfo(Player player) {
        EntityManager entityManager = beginTransaction();
        entityManager.merge(player);
        commitTransaction();
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
