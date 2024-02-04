package edu.virginia.sde.reviews;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAO {
    public void addCourse(Course course){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Error saving course to the database", e);
        }
    }

    public Course getCourseByID(int id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving Course by id", e);
        }
    }

    public List<Course> getAllCourses(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course";
            Query<Course> courseQuery = session.createQuery(hql, Course.class);
            return courseQuery.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving all courses", e);
        }
    }


    /**
     * case-insensitive and exact letter matches
     * return empty list if not find
     */
    public List<Course> getCoursesBySubject(String subject){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course WHERE upper(subject) = upper(:subject)";
            Query<Course> subjectQuery = session.createQuery(hql, Course.class);
            subjectQuery.setParameter("subject", subject);
            return subjectQuery.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving courses by subject", e);
        }
    }

    /**
     * exact letter matches
     * return empty list if not find
     */
    public List<Course> getCoursesByCourseNumber(int courseNumber){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course WHERE courseNumber = :courseNumber";
            Query<Course> courseNumberQuery = session.createQuery(hql, Course.class);
            courseNumberQuery.setParameter("courseNumber", courseNumber);
            return courseNumberQuery.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving courses by courseNumber", e);
        }
    }


    public List<Course> getCoursesByTitle(String titleSubstring) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course WHERE lower(courseTitle) like :title";
            Query<Course> courseTitlequery = session.createQuery(hql, Course.class);
            courseTitlequery.setParameter("title", "%" + titleSubstring.toLowerCase() + "%");
            return courseTitlequery.list();
        } catch (HibernateException e) {
            throw new HibernateException("Error retrieving courses by title", e);
        }
    }



}
