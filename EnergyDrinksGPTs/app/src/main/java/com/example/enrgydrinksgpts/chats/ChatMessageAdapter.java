package com.example.enrgydrinksgpts.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.R;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {
    private List<ChatMessage> chatMessages;
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;

    public ChatMessageAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).getSenderType() == ChatMessage.SENDER_USER ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == VIEW_TYPE_USER) {
            view = inflater.inflate(R.layout.message_user, parent, false);
        } else {
            view = inflater.inflate(R.layout.message_bot, parent, false);
        }

        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.messageTextView.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        ChatMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
