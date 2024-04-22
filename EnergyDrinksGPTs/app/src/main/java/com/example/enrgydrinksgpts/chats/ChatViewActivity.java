package com.example.enrgydrinksgpts.chats;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.R;

import java.util.ArrayList;
import java.util.List;

public class ChatViewActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatMessages = getHardcodedMessages();

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatMessageAdapter);
    }

    private List<ChatMessage> getHardcodedMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("Hello! How can I help you today?", ChatMessage.SENDER_OTHER));
        messages.add(new ChatMessage("Can you tell me more about the effects of caffeine?", ChatMessage.SENDER_USER));
        messages.add(new ChatMessage("Sure! Caffeine can temporarily make you feel more awake by blocking sleep-inducing chemicals in the brain and increasing adrenaline production.", ChatMessage.SENDER_OTHER));
        messages.add(new ChatMessage("Hello! How can I help you today?", ChatMessage.SENDER_OTHER));
        messages.add(new ChatMessage("Can you tell me more about the effects of caffeine?", ChatMessage.SENDER_USER));
        return messages;
    }
}
