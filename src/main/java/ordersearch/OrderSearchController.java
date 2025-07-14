package ordersearch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Booking;
import service.BookingService;
import util.Session;

import java.io.IOException;
import java.util.List;

public class OrderSearchController {
    @FXML private TextField searchField;
    @FXML private TableView<Booking> ordersTable;
    @FXML private TableColumn<Booking,Integer> colId;
    @FXML private TableColumn<Booking,String> colType;
    @FXML private TableColumn<Booking,String> colUser;
    @FXML private TableColumn<Booking,String> colCheckin;
    @FXML private TableColumn<Booking,String> colCheckout;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colCheckin.setCellValueFactory(new PropertyValueFactory<>("checkinDate"));
        colCheckout.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        String kw = searchField.getText().trim();
        List<Booking> list;
        if (Session.getRole().equals("admin")) {
            list = BookingService.getAll();
        } else {
            list = BookingService.getByUser();
        }
        if (!kw.isEmpty()) {
            list.removeIf(b->!b.getRoomType().toLowerCase().contains(kw.toLowerCase()));
        }
        ordersTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void handleBack(ActionEvent e) throws IOException {
        String fxml = Session.getRole().equals("admin")
                ? "/dashboard/AdminDashboard.fxml"
                : "/dashboard/Dashboard.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}
