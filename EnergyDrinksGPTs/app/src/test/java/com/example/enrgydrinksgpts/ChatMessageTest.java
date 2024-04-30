package com.example.enrgydrinksgpts;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.enrgydrinksgpts.chats.ChatMessage;

public class ChatMessageTest {

    @Test
    public void testChatMessageConstructorAndGetter() {
        // Given
        String expectedMessage = "Hello, world!";
        int expectedSenderType = ChatMessage.SENDER_USER;

        // When
        ChatMessage chatMessage = new ChatMessage(expectedMessage, expectedSenderType);

        // Then
        assertNotNull("ChatMessage object should not be null", chatMessage);
        assertEquals("Message should match the constructor argument", expectedMessage, chatMessage.getMessage());
        assertEquals("Sender type should match the constructor argument", expectedSenderType, chatMessage.getSenderType());
    }

    @Test
    public void testChatMessageConstructorAndGetterForOtherSender() {
        // Given
        String expectedMessage = "Reply from the other side";
        int expectedSenderType = ChatMessage.SENDER_OTHER;

        // When
        ChatMessage chatMessage = new ChatMessage(expectedMessage, expectedSenderType);

        // Then
        assertNotNull("ChatMessage object should not be null", chatMessage);
        assertEquals("Message should match the constructor argument", expectedMessage, chatMessage.getMessage());
        assertEquals("Sender type should match the constructor argument", expectedSenderType, chatMessage.getSenderType());
    }
}
