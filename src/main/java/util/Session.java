package util;

public class Session {
    private static String username;
    private static String role;
    public static String getUsername() { return username; }
    public static void setUsername(String u) { username = u; }
    public static String getRole() { return role; }
    public static void setRole(String r) { role = r; }
}
