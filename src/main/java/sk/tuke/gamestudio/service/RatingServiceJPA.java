package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        //entityManager.persist(rating);
        if (rating.getRating() >= 1 && rating.getRating() <= 5) {
            try {
                // Check if rating already exists
                Integer existingRatingValue = (Integer) entityManager.createNamedQuery("Rating.getRating")
                        .setParameter("game", rating.getGame())
                        .setParameter("player", rating.getPlayer())
                        .getSingleResult();

                entityManager.createNamedQuery("Rating.setRating")
                        .setParameter("rating", rating.getRating())
                        .setParameter("game", rating.getGame())
                        .setParameter("player", rating.getPlayer())
                        .executeUpdate();

            } catch (NoResultException e) {
                entityManager.persist(rating);
            }
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Double average = (Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();
        int a = average != null ? average.intValue() : 0;
        return a;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        int value = (int) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game)
                .setParameter("player", player)
                .getSingleResult();
        return value;
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
