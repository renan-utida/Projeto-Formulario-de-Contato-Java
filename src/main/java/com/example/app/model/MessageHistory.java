package com.example.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Gerenciador do histórico de mensagens enviadas. */
public class MessageHistory {
    private final List<Message> messages = new ArrayList<>();
    private final int maxSize;

    public MessageHistory(int maxSize) {
        this.maxSize = Math.max(1, maxSize);
    }

    public MessageHistory() {
        this(50); // Padrão: até 50 mensagens
    }

    public synchronized void addMessage(Message message) {
        if (message == null) return;

        messages.add(message);

        // Remove mensagens antigas se exceder o limite
        while (messages.size() > maxSize) {
            messages.remove(0);
        }
    }

    public synchronized List<Message> getAllMessages() {
        return Collections.unmodifiableList(new ArrayList<>(messages));
    }

    public synchronized Message getLastMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    public synchronized int getMessageCount() {
        return messages.size();
    }

    public synchronized void clearHistory() {
        messages.clear();
    }

    public synchronized List<Message> getMessagesByEmail(String email) {
        if (email == null) return Collections.emptyList();
        return messages.stream()
                .filter(m -> email.equalsIgnoreCase(m.getEmail()))
                .collect(java.util.stream.Collectors.toList());
    }
}
