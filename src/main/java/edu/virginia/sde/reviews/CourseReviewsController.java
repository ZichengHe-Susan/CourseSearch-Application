package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class CourseReviewsController {
    @FXML
    private Stage primaryStage;
    @FXML
    private TableView<Review> tableView;
    @FXML
    private ChoiceBox<Integer> ratingChoice;
    @FXML
    private Label averageReview;
    @FXML
    private TextField reviewComment;
    @FXML
    private Label courseName;
    @FXML
    private Course course;
    @FXML
    private ReviewDAO service;
    @FXML
    private Label errorMessage;
    @FXML
    private User user;

    @FXML
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    public void initialize() {
        service = new ReviewDAO();
        List<Integer> ratings = Arrays.asList(1, 2, 3, 4, 5);
        ratingChoice.getItems().addAll(ratings);
    }

    public void setCourse(Course course1) {
        course = course1;
        courseName.setText(course.getSubject() + " " + course.getCourseNumber() + ": " + course.getCourseTitle());
        setAverageReview();
        updateTable();
    }

    @FXML
    public void updateTable() {
        ReviewDAO service = new ReviewDAO();
        List<Review> list = service.getReviewsByCourse(course);
        ObservableList<Review> obsList = FXCollections.observableList(list);
        tableView.getItems().clear();
        tableView.getItems().addAll(obsList);
    }
    @FXML
    public void handleAddButton() {
        if(course != null) {
            if(ratingChoice.getValue()!=null){
            int rating = ratingChoice.getValue();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String comment = reviewComment.getText();
            Review review = new Review(course, user, rating, timestamp, comment);
            ReviewDAO reviewService = new ReviewDAO();
            Review oldReview = reviewService.getReview(user,course);
            if( oldReview != null){
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("You have reviewed this course already.");
            }else{
                reviewService.addReview(review);
                errorMessage.setStyle("-fx-text-fill: blue");
                errorMessage.setText("Successfully added new review");
            }
                updateTable();
                setAverageReview();
            } else {
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("Please give your rating");
            }
        } else {
            errorMessage.setText("Invalid course entry. Try again.");
        }
    }
    @FXML
    public void handleEditButton() {
        if(course != null) {
                ReviewDAO reviewService = new ReviewDAO();
                Review oldReview = reviewService.getReview(user,course);
            if( oldReview != null){
                int rating = ratingChoice.getValue();
                String comment = reviewComment.getText();
                if(rating == oldReview.getRating() && oldReview.getComment().equals(comment)){
                    errorMessage.setStyle("-fx-text-fill: red");
                    errorMessage.setText("You haven't made any edits");
                    System.out.println("gotten here1");
                }else{
                    if(ratingChoice.getValue()!=null){
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        Review review = new Review(course, user, rating, timestamp, comment);
                        reviewService.deleteReview(user, course);
                        reviewService.addReview(review);
                        updateTable();
                        setAverageReview();
                        errorMessage.setStyle("-fx-text-fill: green");
                        errorMessage.setText("Review successfully edited"); }
                }
            }else{
                errorMessage.setStyle("-fx-text-fill: Red");
                errorMessage.setText("Please add your review first");
            }
        }
    }
    @FXML
    public void handleDeleteButton() {
        ReviewDAO reviewService = new ReviewDAO();
        if(reviewService.getReview(user,course) != null){
            reviewService.deleteReview(user, course);
            errorMessage.setStyle("-fx-text-fill: blue");
            errorMessage.setText("Successfully deleted your review");
            reviewComment.setText("");
            ratingChoice.setValue(null);
            updateTable();
            setAverageReview();
        }else{
            errorMessage.setStyle("-fx-text-fill: red");
            errorMessage.setText("You have not reviewed this course yet!");
        }

    }

    @FXML
    public void handleBackButton() {
        try {
            var fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("course-search.fxml"));
            var newScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUser(user);
            primaryStage.setTitle("Course Search");
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setAverageReview(){
        List<Review> reviews = service.getReviewsByCourse(course);
        OptionalDouble average = reviews.stream()
                .mapToInt(Review::getRating)
                .average();
        if (average.isPresent()) {
            averageReview.setText(String.format("%.2f", average.getAsDouble()));
        } else {
            averageReview.setText("No Rating");
        }
    }

    public void reviewComment(){
        ReviewDAO reviewService = new ReviewDAO();
        if (reviewService.getReview(user,course) != null){
            ratingChoice.setValue(reviewService.getReview(user, course).getRating());
            reviewComment.setText(reviewService.getReview(user,course).getComment());
        }

    }

    public void setUser(User user) {
        this.user = user;
        reviewComment();
    }
}
