package sk.tuke.gamestudio.service;

import org.mindrot.jbcrypt.BCrypt;
import sk.tuke.gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void registerUser(User user) throws UserException {
        if (user.getUsername() != null && user.getPassword() != null) {
            System.out.println("Registering user: " + user.getUsername() + user.getPassword());
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            user.setPassword(hashed);
            entityManager.persist(user);
        }
    }

    @Override
    public User findByUsername(String username) throws UserException {
        try {
            return entityManager.createNamedQuery("User.findByUsername", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void reset() throws UserException {
        entityManager.createNamedQuery("User.resetUsers").executeUpdate();
    }
}