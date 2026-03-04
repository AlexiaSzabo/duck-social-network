package org.example.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.example.domain.Message;
import org.example.domain.User;
import org.example.service.MessageService;

import java.util.List;

public class ChatController {

    @FXML private ListView<String> messageListView;
    @FXML private TextField messageField;

    private MessageService messageService;
    private User loggedUser;
    private User chatUser;

    private Message selectedMessageForReply = null;
    private List<Message> currentMessages;
    private Timeline refreshTimeline;

    // Setam serviciile si utilizatorii pentru chat
    public void setServices(MessageService ms, User loggedUser, User chatUser) {
        this.messageService = ms;
        this.loggedUser = loggedUser;
        this.chatUser = chatUser;

        loadMessages();

        // Listener pentru selectarea unui mesaj pentru reply
        messageListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < currentMessages.size()) {
                selectedMessageForReply = currentMessages.get(index);
            }
        });

        // Refresh periodic la fiecare 1 secundă
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> loadMessages()));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    // incarca mesajele si le afiseaza cu thread de reply
    private void loadMessages() {
        currentMessages = messageService.getConversation(loggedUser, chatUser);
        messageListView.setItems(FXCollections.observableArrayList());

        for (Message m : currentMessages) {
            String display = formatMessageRecursive(m, 0);
            messageListView.getItems().add(display);
        }
    }

    // Formateaza mesajul si reply-urile recursiv
    private String formatMessageRecursive(Message message, int indentLevel) {
        String indent = " ".repeat(indentLevel * 4);
        String text = indent + message.getFrom().getUsername() + ": " + message.getMessage();

        if (message.getReplyTo() != null) {
            text += " (reply to: " + message.getReplyTo().getFrom().getUsername() + ": "
                    + message.getReplyTo().getMessage() + ")";
        }
        return text;
    }

    @FXML
    private void handleSendMessage() {
        String text = messageField.getText().trim();
        if (text.isEmpty()) return;

        // Trimitem mesajul cu reply daca exista
        messageService.sendMessage(loggedUser, List.of(chatUser), text, selectedMessageForReply);

        messageField.clear();
        selectedMessageForReply = null;
        loadMessages();
    }

    // Oprire refresh cand se închide fereastra
    public void stopRefresh() {
        if (refreshTimeline != null) refreshTimeline.stop();
    }
}
