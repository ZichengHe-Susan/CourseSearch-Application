package edu.virginia.sde.reviews;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ReviewDAO{

    /**
     * check if there's already a review by the same user for the same course.
     * if an existing review is found, the review will not be added in the database, return false
     */
    public Boolean addReview(Review review){
        Transaction transaction = null;
        Boolean isAdded = false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Review existingReview = getReview(review.getUser(),review.getCourse());

            if (existingReview == null){
                session.persist(review);
                isAdded = true;
            }

            transaction.commit();

        } catch (HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Error adding review to the database", e);
        }

        return isAdded;
    }


    public List<Review> getAllReviews(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review";
            Query<Review> reviewQuery = session.createQuery(hql, Review.class);
            return reviewQuery.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving all reviews", e);
        }
    }

    public List<Review> getReviewsByCourse(Course course){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review WHERE course = :course";
            Query<Review> query = session.createQuery(hql, Review.class);
            query.setParameter("course", course);
            return query.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving reviews by course", e);
        }
    }

    public List<Review> getReviewsByUser(User user){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review WHERE user = :user";
            Query<Review> query = session.createQuery(hql, Review.class);
            query.setParameter("user", user);
            return query.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving reviews by user", e);
        }
    }

    /**
     * get a single comment that the user wrote for a specific course
     * return null if no review is found
     */
    public Review getReview(User user, Course course){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Review WHERE user = :user AND course = :course";
            Query<Review> query = session.createQuery(hql, Review.class);
            query.setParameter("user", user);
            query.setParameter("course", course);
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving review for user and course", e);
        }
    }

    /**
     * @param review: contains the new Review
     *  updates the existing review in the database
     */
    public void updateReview(Review review){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Error updating review", e);
        }
    }

    public Boolean deleteReview(User user, Course course){
        Boolean isDeleted = false;
        Transaction transaction = null;

        Review review = getReview(user, course);
        if (review == null) {
            return isDeleted;
        }

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.remove(review);
            isDeleted = true;
            transaction.commit();
        }catch(HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Error deleting review",e);
        }


        return isDeleted;
    }


}
