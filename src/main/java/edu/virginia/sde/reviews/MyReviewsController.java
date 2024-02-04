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

public class MyReviewsController {
    @FXML
    private TableView<Review> tableView;
    @FXML
    private Stage primaryStage;
    @FXML
    private User user;
    @FXML
    private Course clickedCourse;

    @FXML
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    public void initialize() {
    }
    public void setUser(User user){
        this.user = user;
        System.out.println(this.user);
        updateTable();
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableView.getSelectionModel().isEmpty()) {
                clickedCourse = tableView.getSelectionModel().getSelectedItem().getCourse();
                switchCourseReviewPage();
            }
        });
    }
    @FXML
    public void updateTable() {
        ReviewDAO reviewService = new ReviewDAO();
        List<Review> list = reviewService.getReviewsByUser(user);
        System.out.println(list);
        ObservableList<Review> obsList = FXCollections.observableList(list);
        tableView.getItems().clear();
        tableView.getItems().addAll(obsList);
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

}
