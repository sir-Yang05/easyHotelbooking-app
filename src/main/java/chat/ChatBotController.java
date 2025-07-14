package chat;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ChatBotController {
    // 从 .env 加载 API Key
    private static final String OPENAI_API_KEY = Dotenv
            .configure()
            .directory(System.getProperty("user.dir"))
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load()
            .get("OPENAI_API_KEY");

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML private TextArea chatArea;
    @FXML private TextField inputField;
    @FXML private Button backBtn;

    @FXML
    private void handleSend() {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) return;

        chatArea.appendText("You: " + msg + "\n");
        inputField.clear();

        // 检查 KEY
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isBlank()) {
            chatArea.appendText("Bot: ❌ 未找到 OPENAI_API_KEY，请检查 .env\n");
            return;
        }

        String jsonBody = "{"
                + "\"model\":\"gpt-3.5-turbo\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\""
                + escape(msg)
                + "\"}]"
                + "}";

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(RequestBody.create(
                        jsonBody,
                        MediaType.get("application/json; charset=utf-8")
                ))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Platform.runLater(() ->
                        chatArea.appendText("Bot: 请求失败：" + e.getMessage() + "\n")
                );
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (!response.isSuccessful()) {
                    Platform.runLater(() ->
                            chatArea.appendText("Bot: API 错误 "
                                    + response.code() + " 响应：" + body + "\n")
                    );
                    return;
                }
                JsonNode root = mapper.readTree(body);
                JsonNode choices = root.path("choices");
                if (!choices.isArray() || choices.size()==0) {
                    Platform.runLater(() ->
                            chatArea.appendText("Bot: 未获取到 choices 字段\n")
                    );
                    return;
                }
                String reply = choices.get(0)
                        .path("message")
                        .path("content")
                        .asText()
                        .trim();
                Platform.runLater(() ->
                        chatArea.appendText("Bot: " + reply + "\n")
                );
            }
        });
    }

    private String escape(String s) {
        return s.replace("\\","\\\\").replace("\"","\\\"");
    }

    @FXML
    private void handleBack() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/dashboard/Dashboard.fxml")
        );
        Stage stage = (Stage)backBtn.getScene().getWindow();
        stage.setScene(new Scene(root,800,600));
    }
}
