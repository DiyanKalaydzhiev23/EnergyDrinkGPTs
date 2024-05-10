package com.example.enrgydrinksgpts.chats;

import android.app.Activity;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.BuildConfig;

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

public class GroqApi {

    private final Activity activity;
    private final List<ChatMessage> chatMessages;
    private final ChatMessageAdapter chatMessageAdapter;
    private final RecyclerView chatRecyclerView;
    private final String aiInstruction;

    public GroqApi(
            Activity activity,
            List<ChatMessage> chatMessages,
            ChatMessageAdapter chatMessageAdapter,
            RecyclerView chatRecyclerView,
            String aiInstruction
    ) {
        this.activity = activity;
        this.chatMessages = chatMessages;
        this.chatMessageAdapter = chatMessageAdapter;
        this.chatRecyclerView = chatRecyclerView;
        this.aiInstruction = aiInstruction;
    }

    public void sendMessageToAPI(String userMessage) {
        Thread thread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            JSONObject requestBody = null;
            try {
                requestBody = buildRequestBody(userMessage);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            RequestBody body = RequestBody.create(mediaType, requestBody.toString());
            Request request = new Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + BuildConfig.groqtoken)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("the response is ai " + response.toString());
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

    private JSONObject buildRequestBody(String userMessage) throws JSONException {
        JSONArray messagesArray = new JSONArray();
        JSONObject msgObject = new JSONObject();

        msgObject.put("role", "system");
        msgObject.put("content", this.aiInstruction);

        messagesArray.put(msgObject);

        for (ChatMessage chatMessage : chatMessages) {
            try {
                msgObject = new JSONObject();
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
