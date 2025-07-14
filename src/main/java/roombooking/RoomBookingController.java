package roombooking;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.BookingService;
import util.Session;

import java.io.InputStream;
import java.time.LocalDate;

public class RoomBookingController {
    @FXML private ChoiceBox<String> roomTypeBox;
    @FXML private DatePicker checkinDatePicker;
    @FXML private DatePicker checkoutDatePicker;
    @FXML private ImageView roomImage;

    @FXML
    public void initialize() {
        roomTypeBox.getItems().addAll("Single","Double","Suite");
        roomTypeBox.getSelectionModel().selectedItemProperty()
                .addListener((obs,oldV,newV)->loadImage(newV));
    }

    private void loadImage(String type) {
        String file = type.toLowerCase() + ".jpg";
        InputStream is = getClass().getResourceAsStream("/images/" + file);
        if (is!=null) roomImage.setImage(new Image(is));
    }

    @FXML
    private void handleBook(ActionEvent e) {
        String type = roomTypeBox.getValue();
        LocalDate in  = checkinDatePicker.getValue();
        LocalDate out = checkoutDatePicker.getValue();
        if (type==null || in==null || out==null) {
            new Alert(Alert.AlertType.WARNING, "请选择房型、入住/离店日期").showAndWait();
            return;
        }
        BookingService.addBooking(type, in, out, Session.getUsername());
        new Alert(Alert.AlertType.INFORMATION, "预订成功！").showAndWait();
    }

    @FXML
    private void handleBack(ActionEvent e) throws Exception {
        // 根据角色跳回不同 Dashboard
        String fxml = Session.getRole().equals("admin")
                ? "/dashboard/AdminDashboard.fxml"
                : "/dashboard/Dashboard.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}
