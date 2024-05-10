package com.example.enrgydrinksgpts.chats;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.enrgydrinksgpts.R;
import java.util.ArrayList;
import java.util.List;


public class ChatViewActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatMessage> chatMessages;
    private EditText inputEditText;
    private Button sendButton;
    private GroqApi groqApi;
    private String aiInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        chatMessages = new ArrayList<>();
        chatMessageAdapter = new ChatMessageAdapter(chatMessages);
        aiInstruction = getIntent().getStringExtra("aiInstruction");
        groqApi = new GroqApi(this, chatMessages, chatMessageAdapter, chatRecyclerView, aiInstruction);

        setupRecyclerView();

        sendButton.setOnClickListener(v -> {
            String message = inputEditText.getText().toString();

            if (!message.isEmpty()) {
                chatMessages.add(new ChatMessage(message, ChatMessage.SENDER_USER));
                chatMessageAdapter.notifyItemInserted(chatMessages.size() - 1);
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                groqApi.sendMessageToAPI(message);
                inputEditText.setText("");
            }
        });
    }

    private void setupRecyclerView() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatMessageAdapter);
    }
}
