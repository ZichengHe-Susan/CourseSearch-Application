package edu.virginia.sde.reviews;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "ID")
    private int id;

    @Column(name="Subject", nullable = false)
    private String subject;

    @Column(name="Course_Number", nullable = false)
    private int courseNumber;

    @Column(name="Course_Title", nullable = false)
    private String courseTitle;
    @Transient
    private String averageReview;
    public String getAverageReview() {
        return averageReview;
    }

    public void setAverageReview(String averageReview) {
        this.averageReview = averageReview;
    }

    public Course(){}

    public Course(String department, int courseNumber, String courseTitle){
        this.subject = department;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
    }

    public Course(int id, String department, int courseNumber, String courseTitle){
        this.id = id;
        this.subject = department;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String department) {
        this.subject = department;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseNumber == course.courseNumber && Objects.equals(subject, course.subject) && Objects.equals(courseTitle, course.courseTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, courseNumber, courseTitle);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", department='" + subject + '\'' +
                ", courseNumber=" + courseNumber +
                ", courseTitle='" + courseTitle + '\'' +
                '}';
    }
}
