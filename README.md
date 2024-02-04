CourseSearch Application

## To Run

[Java file to run: CourseReviewsApplication.java]
[vm argument: --module-path (PATH_TO_JAVAFX_LIB_FOLDER) --add-modules javafx.controls,javafx.fxml ]


The application described is a JavaFX-based course review system that facilitates the management and sharing of user-generated course evaluations. It features a user authentication system, allowing users to log in, create new accounts, and log out. The main functionality revolves around the ability to search for university courses using various criteria, add new courses to the system, and review them by providing ratings and optional comments. It maintains user anonymity by not displaying usernames with reviews.

For data persistence, the application uses a file-based SQLite database to store user accounts, course details, and reviews, ensuring that data remains intact between sessions. Users have the ability to add, edit, and delete their own reviews, and view a list of reviews they've made across different courses.

The application guarantees data integrity by enforcing unique usernames and preventing duplicate reviews for the same course by the same user. It also adheres to the 2nd Normal Form to ensure database normalization.

To manage database interactions, the application can use either JDBC for direct SQL operations or Hibernate 6 for object-relational mapping, with the choice left to the developer. Hibernate would require additional configuration but would reduce the need for boilerplate SQL code.

The GUI is designed to be user-friendly and visually clean, operating within a 1280x720 resolution, and it handles input validation and error messaging within the application window, avoiding crashes due to user input errors. The application structure is divided into distinct scenes for login, course searching, course reviewing, and viewing personal reviews, each providing a specific subset of the functionality and allowing navigation between them.
