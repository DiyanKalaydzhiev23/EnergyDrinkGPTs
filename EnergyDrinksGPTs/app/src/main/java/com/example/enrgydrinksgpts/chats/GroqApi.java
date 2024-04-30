package com.example.enrgydrinksgpts.chats;

import android.app.Activity;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.enrgydrinksgpts.BuildConfig;

public class GroqApi {

    private final Activity activity;
    private final List<ChatMessage> chatMessages;
    private final ChatMessageAdapter chatMessageAdapter;
    private final RecyclerView chatRecyclerView;

    public GroqApi(Activity activity, List<ChatMessage> chatMessages,
                   ChatMessageAdapter chatMessageAdapter, RecyclerView chatRecyclerView) {
        this.activity = activity;
        this.chatMessages = chatMessages;
        this.chatMessageAdapter = chatMessageAdapter;
        this.chatRecyclerView = chatRecyclerView;
    }

    public void sendMessageToAPI(String userMessage) {
        Thread thread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            JSONObject requestBody = buildRequestBody(userMessage);

            RequestBody body = RequestBody.create(mediaType, requestBody.toString());
            Request request = new Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + BuildConfig.groqtoken)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();
                processResponse(responseData);
            } catch (Exception e) {
                activity.runOnUiThread(() -> Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private JSONObject buildRequestBody(String userMessage) {
        JSONArray messagesArray = new JSONArray();
        for (ChatMessage chatMessage : chatMessages) {
            try {
                JSONObject msgObject = new JSONObject();
                msgObject.put("role", chatMessage.getSenderType() == ChatMessage.SENDER_USER ? "user" : "assistant");
                msgObject.put("content", chatMessage.getMessage());
                messagesArray.put(msgObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject currentUserMessage = new JSONObject();
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

        return requestBody;
    }

    private void processResponse(String responseData) throws JSONException {
        JSONObject jsonResponse = new JSONObject(responseData);
        JSONArray choicesArray = jsonResponse.getJSONArray("choices");
        JSONObject firstChoice = choicesArray.getJSONObject(0);
        JSONObject messageObject = firstChoice.getJSONObject("message");
        String assistantMessage = messageObject.getString("content");

        activity.runOnUiThread(() -> {
            chatMessages.add(new ChatMessage(assistantMessage, ChatMessage.SENDER_OTHER));
            chatMessageAdapter.notifyItemInserted(chatMessages.size() - 1);
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
        });
    }
}
