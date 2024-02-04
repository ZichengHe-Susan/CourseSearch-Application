package edu.virginia.sde.reviews;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "ID")
    private int id;

    @ManyToOne // Many reviews can be linked to one course
    @JoinColumn(name = "courseID", referencedColumnName = "ID")
    private Course course;

    @ManyToOne // Many reviews can be linked to one user
    @JoinColumn(name = "userID", referencedColumnName = "ID")
    private User user;

    @Column(name = "Rating", nullable = false)
    private int rating;

    @Column(name = "Timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "Comment", nullable = true)
    private String comment;

    public Review(){}

    public Review(Course course, User user, int rating, Timestamp timestamp, String comment) {
        this.course = course;
        this.user = user;
        this.rating = rating;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public Review(int id, Course course, User user, int rating, Timestamp timestamp, String comment) {
        this.id = id;
        this.course = course;
        this.user = user;
        this.rating = rating;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }
    public String getSubject() {
        return course.getSubject();
    }

    public int getCourseNumber() {
        return course.getCourseNumber();
    }

    public String getCourseTitle() {
        return course.getCourseTitle();
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id && rating == review.rating && Objects.equals(course, review.course) && Objects.equals(user, review.user) && Objects.equals(timestamp, review.timestamp) && Objects.equals(comment, review.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, user, rating, timestamp, comment);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", course=" + course +
                ", user=" + user +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                ", comment='" + comment + '\'' +
                '}';
    }
}
