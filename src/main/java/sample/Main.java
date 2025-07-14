package sample;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
        primaryStage.setTitle("HotelFX Complete App");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


public static void main(String[] args) {
    try {
        FirebaseInitializer.initialize(); // 初始化 Firebase
    } catch (Exception e) {
        e.printStackTrace();
        return;
    }
    launch(args);
}

}
