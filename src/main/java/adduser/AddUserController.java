package adduser;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AddUserController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private void handleAddUser() { new Alert(Alert.AlertType.INFORMATION, "User added!").show(); }
    @FXML private void handleBack() { usernameField.getScene().getWindow().hide(); }
}
