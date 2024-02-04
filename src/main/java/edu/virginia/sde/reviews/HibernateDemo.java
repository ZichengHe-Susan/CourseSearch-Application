package edu.virginia.sde.reviews;


import jakarta.persistence.PersistenceException;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class HibernateDemo {

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        UserDAO userDAO = new UserDAO();
        CourseDAO courseDAO = new CourseDAO();
        ReviewDAO reviewDAO = new ReviewDAO();

        User user = userDAO.getUserByID(4);
        Course course = courseDAO.getCourseByID(52);

        reviewDAO.deleteReview(user,course);

        // This works
//        Review existingReview = reviewDAO.getReview(user,course);
//        if(existingReview != null){
//            existingReview.setComment("new comment");
//            reviewDAO.updateReview(existingReview);
//            Review updatedReview = reviewDAO.getReview(user,course);
//            if ("new comment".equals(updatedReview.getComment())) {
//                System.out.println("Success");
//            } else {
//                System.out.println("Failed");
//            }
//        } else {
//            System.out.println("User not found for update test");
//        }

//        Review review = new Review(course,user,5,new Timestamp(System.currentTimeMillis()),"awesome");
//        reviewDAO.addReview(review);
//        review.setComment("comment again");
//        reviewDAO.updateReview(review);

 //       System.out.println(reviewDAO.getAllReviews());

        // Test updating an existing user
//        User existingUser = userDAO.getUserByUsername("Huarui");
//        userDAO.deleteUserByUsername("huarui");
//        System.out.println("Testing update existing user...");
//        User existingUser = userDAO.getUserByUsername("Susan");
//        if (existingUser != null) {
//            existingUser.setPassword("newPassword101");
//            userDAO.updateUser(existingUser);
//            // Fetch again to check updates
//            User updatedUser = userDAO.getUserByUsername("Susan");
//            if ("newValue".equals(updatedUser.getPassword())) {
//                System.out.println("Success");
//            } else {
//                System.out.println("Failed");
//            }
//        } else {
//            System.out.println("User not found for update test");
//        }



//        User me = session.get(User.class, 1);
//        System.out.println(me);

//        User user = new User("Susan","123456789");
//        dao.updateUser(user);
//        User existingUser = dao.getUserByUsername("Susan");
//
//        if (existingUser != null) {
//            existingUser.setPassword("asdfskaldjflkajkljf");
//            dao.updateUser(existingUser);
//        }

//        dao.addUser(user);
//        var userGet = dao.getUserByUsername("liu");
//        System.out.println(userGet);
//        var allUsers = dao.getAllUsers();
//        System.out.println(allUsers);

        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }

}
