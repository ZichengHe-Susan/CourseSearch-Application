package edu.virginia.sde.reviews;

import org.hibernate.*;
import jakarta.persistence.*;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Collections;
import java.sql.*;

public class UserDAO{
    private User currentUser;
    /**
     * Create - Add a User to the Database.
     * @param user
     * @throws SQLException
     */
    public void addUser(User user){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (HibernateException e){
            if (transaction != null){
                transaction.rollback();
            }
            throw new HibernateException("Error saving user to the database", e);
        }
    }

    /**
     *If not found the id, return null;
     */
    public User getUserByID(int id){

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.get(User.class, id);
        } catch (HibernateException e){
            throw new HibernateException("Error retrieving user by id", e);
        }
    }

    /**
     *If not found the username, return null;
     */
    public User getUserByUsername(String username){
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            String hql = "FROM User WHERE username = :username";
            Query<User> usernameQuery = session.createQuery(hql, User.class);
            usernameQuery.setParameter("username", username);
            return usernameQuery.uniqueResult();
        } catch (HibernateException e){
            throw new HibernateException("Error retrieving user by username", e);
        }
    }

    /**
     * Gets a list of all Users in the database
     */
    public List<User> getAllUsers(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            String hql = "FROM User";
            Query<User> userQuery = session.createQuery(hql, User.class);
            return userQuery.list();
        } catch (HibernateException e){
            throw new HibernateException("Error retrieving all users", e);
        }
    }


    public void updateUser(User user){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (HibernateException e){
            if (transaction != null){
                transaction.rollback();
            }
            throw new HibernateException("Error updating user", e);
        }
    }

    /**
     * Delete by username. If username is not in the database, return false.
     * @param username
     */
    public Boolean deleteUserByUsername(String username){
        Transaction transaction = null;
        Boolean isDeleted = false;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            String hql = "FROM User WHERE username = :username";
            Query<User> usernameQuery = session.createQuery(hql, User.class);
            usernameQuery.setParameter("username", username);
            User user = usernameQuery.uniqueResult();

            if (user != null){
                session.remove(user);
                isDeleted = true;
            }

            transaction.commit();

        }catch(HibernateException e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Error deleting user",e);
        }
        return isDeleted;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }
}
