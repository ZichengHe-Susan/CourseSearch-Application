package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.util.List;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label errorMessage;
    @FXML
    private Stage primaryStage;
    @FXML
    private UserDAO service;
    @FXML
    private Button closeButton;
    @FXML
    private User user;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    public void initialize() {
        service = new UserDAO();
    }
    @FXML
    public void handleLoginButton() {
        try {
            errorMessage.setText("");

            var username = usernameField.getText();
            var password = passwordField.getText();
            User user = service.getUserByUsername(username);
            if(user == null) {
                usernameField.clear();
                passwordField.clear();
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("User not found. Try again.");
            } else if(password.length() < 8){
                passwordField.clear();
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("Password must be at least 8 characters.");
            } else if(!password.equals(user.getPassword())){
                passwordField.clear();
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("Incorrect Password.");
            } else {
                service.setCurrentUser(user);
                this.user = user;
                switchToCourseSearch();
            }
        }catch(HibernateException e) {
            usernameField.clear();
            passwordField.clear();
            errorMessage.setStyle("-fx-text-fill: red");
            errorMessage.setText("User not found. Try again.");
        }
    }
    @FXML
    public void switchToCourseSearch() {
        try {
            var fxmlLoader = new FXMLLoader(CourseSearchController.class.getResource("course-search.fxml"));
            var newScene = new Scene(fxmlLoader.load());
            var controller = (CourseSearchController) fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUser(user);
            primaryStage.setTitle("Course Search Page");
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleCreateNewUserButton() {
        try {
            errorMessage.setText("");

            UserDAO service = new UserDAO();
            var username = usernameField.getText();
            var password = passwordField.getText();
            if(password.length() < 8) {
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("Password must be at least 8 characters.");
                usernameField.clear();
                passwordField.clear();
            }else if(username.isEmpty()){
                errorMessage.setStyle("-fx-text-fill: red");
                errorMessage.setText("Username can't be empty.");
                usernameField.clear();
                passwordField.clear();
            }else{
                User user = new User(username, password);
                service.addUser(user);
                usernameField.clear();
                passwordField.clear();
                errorMessage.setStyle("-fx-text-fill: blue");
                errorMessage.setText("User successfully created");
            }
        }catch(HibernateException e) {
            errorMessage.setStyle("-fx-text-fill: red");
            errorMessage.setText("Username already exists. Try again.");
        }
    }
    @FXML
    public void handleCloseButton(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
