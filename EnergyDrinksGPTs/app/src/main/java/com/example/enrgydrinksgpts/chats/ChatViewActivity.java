package com.example.enrgydrinksgpts.chats;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.BuildConfig;
import com.example.enrgydrinksgpts.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatViewActivity extends AppCompatActivity {
    private final String groqApiToken = BuildConfig.groqtoken;
    private RecyclerView chatRecyclerView;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatMessage> chatMessages;
    private EditText inputEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<>();
        setupRecyclerView();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputEditText.getText().toString();
                chatMessages.add(new ChatMessage(message, ChatMessage.SENDER_USER));
                chatMessageAdapter.notifyItemInserted(chatMessages.size() - 1);
                System.out.println("Its called with message " + message);
                sendMessageToAPI(message);
                inputEditText.setText(""); // Clear the input field
            }
        });
    }

    private void setupRecyclerView() {
        chatMessageAdapter = new ChatMessageAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatMessageAdapter);
    }

    private void sendMessageToAPI(String userMessage) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}], \"model\": \"mixtral-8x7b-32768\"}");
                Request request = new Request.Builder()
                        .url("https://api.groq.com/openai/v1/chat/completions")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + groqApiToken)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    assert response.body() != null;
                    String responseData = response.body().string();

                    System.out.println("the response data is: " + groqApiToken);
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray choicesArray = jsonResponse.getJSONArray("choices");
                    JSONObject firstChoice = choicesArray.getJSONObject(0);
                    JSONObject messageObject = firstChoice.getJSONObject("message");
                    String assistantMessage = messageObject.getString("content");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatMessages.add(new ChatMessage(assistantMessage, ChatMessage.SENDER_OTHER));
                            chatMessageAdapter.notifyItemInserted(chatMessages.size() - 1);
                        }
                    });
                } catch (Exception e) {
                    System.out.println("the err " + e);
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
