package dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Session;

import java.io.IOException;

public class DashboardController {
    @FXML
    private void handleSearchOrder(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ordersearch/OrderSearch.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @FXML
    private void handleChatBot(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/chat/ChatBot.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @FXML
    private void handleRoomBooking(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/roombooking/RoomBooking.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @FXML
    private void handleBack(ActionEvent e) throws IOException {
        // Back to login
        Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
