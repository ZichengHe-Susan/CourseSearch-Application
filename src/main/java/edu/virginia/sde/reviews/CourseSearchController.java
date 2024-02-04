package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CourseSearchController {
    @FXML
    private TableView<Course> tableView;
    @FXML
    private TextField subjectSearch;
    @FXML
    private TextField numberSearch;
    @FXML
    private TextField titleSearch;
    @FXML
    private TextField subjectToAdd;
    @FXML
    private TextField numberToAdd;
    @FXML
    private TextField titleToAdd;
    @FXML
    private Label errorMessage;
    @FXML
    private Stage primaryStage;
    @FXML
    private CourseDAO service;
    private Course clickedCourse;
    @FXML
    private ReviewDAO reviewDAO;
    @FXML
    private User user;

    @FXML
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    public void initialize() {
        service = new CourseDAO();
        reviewDAO = new ReviewDAO();
        List<Course> courseList = service.getAllCourses();
        updateTable(courseList);
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableView.getSelectionModel().isEmpty()) {
                clickedCourse = tableView.getSelectionModel().getSelectedItem();
                switchCourseReviewPage();
            }
        });
    }
    @FXML
    public void updateTable(List<Course> list) {
        ObservableList<Course> obsList = FXCollections.observableList(list);
        for (Course course : obsList) {
            List<Review> reviews = reviewDAO.getReviewsByCourse(course);

            OptionalDouble average = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average();

            if (average.isPresent()) {
                course.setAverageReview(String.format("%.2f", average.getAsDouble()));
            } else {
                course.setAverageReview("No review");
            }
        }
        tableView.getItems().clear();
        tableView.getItems().addAll(obsList);
    }
    @FXML
    public void handleSearchButton() {
        List<Course> courseList = service.getAllCourses();
        Stream<Course> stream = courseList.stream();
        String subject = subjectSearch.getText();
        if (!subject.isEmpty()) {
            stream = stream.filter(course -> course.getSubject().equalsIgnoreCase(subject));
        }
        String numberText = numberSearch.getText();
        if (!numberText.isEmpty()) {
            try {
                int number = Integer.parseInt(numberText);
                stream = stream.filter(course -> course.getCourseNumber() == number);
            } catch (NumberFormatException ignored) {
            }
        }
        String title = titleSearch.getText();
        if (!title.isEmpty()) {
            stream = stream.filter(course -> course.getCourseTitle().toLowerCase().contains(title.toLowerCase()));
        }
        List<Course> filteredList = stream.collect(Collectors.toList());
        updateTable(filteredList);
    }
    @FXML
    public void handleLogoutButton() {
        try {
            var fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
            var newScene = new Scene(fxmlLoader.load());
            var controller = (LoginController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Login");
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleMyReviewsButton() {
        try {
            var fxmlLoader = new FXMLLoader(MyReviewsController.class.getResource("my-reviews.fxml"));
            var newScene = new Scene(fxmlLoader.load());
            var controller = (MyReviewsController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUser(user);
            primaryStage.setTitle("My Reviews");
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleAddButton() {
        String subject = subjectToAdd.getText();
        if(verifySubjectInput(subject)) {
            int number = 0;
            try {
                number = Integer.parseInt(numberToAdd.getText());
            } catch (NumberFormatException e) {
                errorMessage.setText("Invalid course number. Try again.");
                clearFields();
                return;
            }
            if(verifyNumberInput(number)) {
                String title = titleToAdd.getText();
                if(verifyTitleInput(title)) {
                    Course courseToAdd = new Course(subject.toUpperCase(), number, title);
                    if(!isInDatabase(courseToAdd)){
                        service.addCourse(courseToAdd);
                        List<Course> allCourses = service.getAllCourses();
                        errorMessage.setStyle("-fx-text-fill: blue");
                        errorMessage.setText("Course successfully added.");
                        updateTable(allCourses);
                    }else {
                        errorMessage.setStyle("-fx-text-fill: red");
                        errorMessage.setText("Course already exists.");
                    }
                }
            }
        }
    }
    public boolean isInDatabase(Course newCourse){
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courseList = courseDAO.getAllCourses();
        for(Course course: courseList){
            if(course.equals(newCourse)){
                return true;
            }
        }
        return false;
    }

    public boolean verifySubjectInput(String subject) {
        if(subject.length() < 2 || subject.length() > 4) {
            errorMessage.setText("Invalid course mnemonic. Try again.");
            clearFields();
            return false;
        }
        char[] subjectChar = subject.toCharArray();
        for(char c : subjectChar) {
            if(Character.isDigit(c)) {
                errorMessage.setText("Invalid course mnemonic. Try again.");
                clearFields();
                return false;
            }
        }
        return true;
    }
    public boolean verifyNumberInput(int number) {
        if(number < 1000 || number > 9999) {
            errorMessage.setText("Course number is incorrect number of digits. Try again.");
            clearFields();
            return false;
        }
        return true;
    }
    public boolean verifyTitleInput(String title) {
        if(title.isEmpty() || title.length() > 50) {
            errorMessage.setText("Title is too long or too short. Try again.");
            clearFields();
            return false;
        }
        return true;
    }
    public void clearFields() {
        subjectToAdd.clear();
        numberToAdd.clear();
        titleToAdd.clear();
    }
    @FXML
    public void switchCourseReviewPage() {
        try{
                var fxmlLoader = new FXMLLoader(CourseReviewsController.class.getResource("course-reviews.fxml"));
                Parent root = fxmlLoader.load();
                var controller = (CourseReviewsController) fxmlLoader.getController();
                controller.setCourse(clickedCourse);
                controller.setUser(user);
                primaryStage.setTitle("Course Reviews");
                primaryStage.setScene(new Scene(root));
                controller.setPrimaryStage(primaryStage);
                primaryStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return clickedCourse.getSubject()
                + clickedCourse.getCourseNumber() + ": "
                + clickedCourse.getCourseTitle();
    }


    public void setUser(User user) {
        this.user = user;
    }
}
