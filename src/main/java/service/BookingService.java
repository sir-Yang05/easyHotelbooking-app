package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import model.Booking;
import util.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingService {

    private static final String COLLECTION_NAME = "bookings";

    // ✅ 添加预订（保存到 Firebase）
    public static void addBooking(String type, LocalDate in, LocalDate out, String user) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            int id = (int) (System.currentTimeMillis() % 100000); // 临时生成 id
            Booking booking = new Booking(id, type, in, out, user);
            db.collection(COLLECTION_NAME).add(
                    new java.util.HashMap<String, Object>() {{
                        put("id", booking.getId());
                        put("roomType", booking.getRoomType());
                        put("username", booking.getUsername());
                        put("checkinDate", booking.getCheckinDate().toString());
                        put("checkoutDate", booking.getCheckoutDate().toString());
                    }}
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ 获取所有订单（管理员用）
    public static List<Booking> getAll() {
        List<Booking> bookings = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
            for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                bookings.add(toBooking(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // ✅ 获取当前登录用户的订单
    public static List<Booking> getByUser() {
        String currentUser = Session.getUsername();
        return getAll().stream()
                .filter(b -> b.getUsername().equals(currentUser))
                .collect(Collectors.toList());
    }

    // ✅ 辅助方法：将 Firestore 文档转为 Booking 对象
    private static Booking toBooking(DocumentSnapshot doc) {
        int id = ((Long) doc.get("id")).intValue();
        String roomType = doc.getString("roomType");
        String username = doc.getString("username");
        LocalDate checkin = LocalDate.parse(doc.getString("checkinDate"));
        LocalDate checkout = LocalDate.parse(doc.getString("checkoutDate"));
        return new Booking(id, roomType, checkin, checkout, username);
    }
}
