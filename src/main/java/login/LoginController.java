package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Session;
import sendemail.EmailSender;
import model.User;

import java.io.IOException;
import java.util.regex.Pattern;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class LoginController {
    // 登录界面字段
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    // 注册界面字段
    @FXML
    private TextField emailField;

    // 邮箱验证用的正则表达式
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    // ===== 登录方法 =====
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields.").showAndWait();
            return;
        }

        // 写死的 admin 登录
        if ("admin".equals(username) && "admin".equals(password)) {
            Session.setUsername("admin");
            Session.setRole("admin");
            safeGoTo(event, "/dashboard/AdminDashboard.fxml", 800, 600);
            return;
        }

        // Firebase 登录校验
        try {
            Firestore db = FirestoreClient.getFirestore();
            var doc = db.collection("users").document(username).get().get();

            if (!doc.exists()) {
                new Alert(Alert.AlertType.ERROR, "User not found.").showAndWait();
                return;
            }

            User user = doc.toObject(User.class);
            if (user == null || !user.getPassword().equals(password)) {
                new Alert(Alert.AlertType.ERROR, "Incorrect password.").showAndWait();
                return;
            }

            Session.setUsername(user.getUsername());
            Session.setRole(user.getRole());

            if ("admin".equals(user.getRole())) {
                safeGoTo(event, "/dashboard/AdminDashboard.fxml", 800, 600);
            } else {
                safeGoTo(event, "/dashboard/Dashboard.fxml", 800, 600);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to connect to database.").showAndWait();
        }
    }

    // ===== 跳转到注册页面 =====
    @FXML
    private void handleRegister(ActionEvent event) {
        safeGoTo(event, "/Register/Register.fxml", 500, 400);
    }

    // ===== 注册处理方法 =====
    @FXML
    private void confirmRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields.").showAndWait();
            return;
        }

        if (username.length() < 3) {
            new Alert(Alert.AlertType.WARNING, "Username must be at least 3 characters.").showAndWait();
            return;
        }

        if (password.length() < 6) {
            new Alert(Alert.AlertType.WARNING, "Password must be at least 6 characters.").showAndWait();
            return;
        }

        if (!isValidEmail(email)) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format.").showAndWait();
            return;
        }

        try {
            Firestore db = FirestoreClient.getFirestore();
            var docRef = db.collection("users").document(username);
            if (docRef.get().get().exists()) {
                new Alert(Alert.AlertType.WARNING, "Username already exists.").showAndWait();
                return;
            }

            User newUser = new User(username, password, email); // role 默认为 "user"
            db.collection("users").document(username).set(newUser);
            System.out.println("User saved to Firebase: " + username);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving user to database.").showAndWait();
            return;
        }

        new Alert(Alert.AlertType.INFORMATION, username + " Register Successful!").showAndWait();

        try {
            EmailSender.sendEmail(email, "Register Successful!",
                    "Welcome to our app, " + username + "!\n\n" +
                            "Your account has been successfully created.");
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Email sending failed, but registration succeeded.").showAndWait();
        }

        // 返回登录界面
        safeGoTo(event, "/login/Login.fxml", 500, 400);
    }

    // 返回登录页按钮（注册界面用）
    @FXML
    private void handleReturnLogin(ActionEvent event) {
        safeGoTo(event, "/login/Login.fxml", 500, 400);
    }

    // ===== 工具方法 =====
    private boolean isValidEmail(String email) {
        return pattern.matcher(email).matches();
    }

    private void safeGoTo(ActionEvent event, String fxmlPath, int w, int h) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, w, h));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load: " + fxmlPath).showAndWait();
        }
    }
}
