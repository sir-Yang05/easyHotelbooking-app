package model;

public class User {
    private String username;
    private String password;
    private String email;
    private String role;  // ✅ 新增字段

    // 必须有无参构造函数（用于 Firebase 读取）
    public User() {}

    // 构造函数：默认注册的用户角色是 "user"
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "user";  // 默认角色
    }

    // Getters 和 Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
