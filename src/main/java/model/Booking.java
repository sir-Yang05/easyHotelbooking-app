package model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Booking {
    private IntegerProperty id         = new SimpleIntegerProperty();
    private StringProperty  roomType   = new SimpleStringProperty();
    private ObjectProperty<LocalDate> checkinDate  = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> checkoutDate = new SimpleObjectProperty<>();
    private StringProperty  username   = new SimpleStringProperty();

    public Booking(int id, String type, LocalDate in, LocalDate out, String user){
        this.id.set(id);
        this.roomType.set(type);
        this.checkinDate.set(in);
        this.checkoutDate.set(out);
        this.username.set(user);
    }
    public int    getId()         { return id.get(); }
    public String getRoomType()   { return roomType.get(); }
    public LocalDate getCheckinDate()  { return checkinDate.get(); }
    public LocalDate getCheckoutDate() { return checkoutDate.get(); }
    public String getUsername()   { return username.get(); }
    // Property getters for bindings...
}
