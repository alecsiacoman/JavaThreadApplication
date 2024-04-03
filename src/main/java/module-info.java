module com.example.pt2024_30423_coman_alecsia_assignment_2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.Controller to javafx.fxml;
    exports com.example.Controller;
}