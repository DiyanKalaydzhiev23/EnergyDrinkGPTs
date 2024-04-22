package com.example.enrgydrinksgpts.chats;

public class ChatMessage {
    public static final int SENDER_USER = 1;
    public static final int SENDER_OTHER = 2;

    private final String message;
    private final int senderType;

    public ChatMessage(String message, int senderType) {
        this.message = message;
        this.senderType = senderType;
    }

    public String getMessage() {
        return message;
    }

    public int getSenderType() {
        return senderType;
    }
}
