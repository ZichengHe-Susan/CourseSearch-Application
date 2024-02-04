CourseSearch Application

## To Run

[Java file to run: CourseReviewsApplication.java]

[vm argument: --module-path (PATH_TO_JAVAFX_LIB_FOLDER) --add-modules javafx.controls,javafx.fxml ]

## Application Description

+JavaFX-based course review system with user authentication for login, account creation, and logout.

+Search functionality for university courses by subject, number, or title.

+Ability to add new courses and submit reviews with ratings and optional comments.

+User anonymity in reviews by not displaying usernames.

+File-based SQLite database for persistent storage of users, courses, and reviews.

+Enforcement of unique usernames and prevention of duplicate course reviews by the same user.

+Adherence to the 2nd Normal Form for database normalization.

+Choice of JDBC for direct SQL management or Hibernate 6 for object-relational mapping.

+User-friendly GUI designed within a 1280x720 resolution, handling input validation and error messaging internally.

+Structured into distinct scenes for various functions: login, course searching, course reviewing, and personal review management.
