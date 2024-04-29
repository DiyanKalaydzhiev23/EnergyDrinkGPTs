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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private ChatMessageAdapter ChatMessageAdapter;
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
                if (!message.isEmpty()) {
                    chatMessages.add(new ChatMessage(message, ChatMessage.SENDER_USER));
                    ChatMessageAdapter.notifyItemInserted(chatMessages.size() - 1);
                    sendMessageToAPI(message);
                }
                inputEditText.setText(""); // Clear the input field after sending
            }
        });
    }

    private void setupRecyclerView() {
        ChatMessageAdapter = new ChatMessageAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(ChatMessageAdapter);
    }

    private void sendMessageToAPI(String userMessage) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");

                JSONArray messagesArray = new JSONArray();
                for (ChatMessage ChatMessage : chatMessages) {
                    try {
                        JSONObject msgObject = new JSONObject();
                        msgObject.put("role", ChatMessage.getSenderType() == ChatMessage.SENDER_USER ? "user" : "assistant");
                        msgObject.put("content", ChatMessage.getMessage());
                        messagesArray.put(msgObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject currentUserMessage = new JSONObject();
                try {
                    currentUserMessage.put("role", "user");
                    currentUserMessage.put("content", userMessage);
                    messagesArray.put(currentUserMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("messages", messagesArray);
                    requestBody.put("model", "mixtral-8x7b-32768");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(mediaType, requestBody.toString());
                Request request = new Request.Builder()
                        .url("https://api.groq.com/openai/v1/chat/completions")
                        .post(body)
                        .addHeader("Authorization", "Bearer " + groqApiToken)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONArray choicesArray = jsonResponse.getJSONArray("choices");
                    JSONObject firstChoice = choicesArray.getJSONObject(0);
                    JSONObject messageObject = firstChoice.getJSONObject("message");
                    String assistantMessage = messageObject.getString("content");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatMessages.add(new ChatMessage(assistantMessage, ChatMessage.SENDER_OTHER));
                            ChatMessageAdapter.notifyItemInserted(chatMessages.size() - 1);
                            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Error in API communication: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
