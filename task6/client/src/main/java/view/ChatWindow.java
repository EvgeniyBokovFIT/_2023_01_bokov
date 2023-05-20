package view;

import controller.ConnectionSuccessListener;
import controller.ServerMessageListener;
import controller.UserListListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class ChatWindow extends JFrame implements ServerMessageListener, ConnectionSuccessListener, UserListListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private final JTextArea chat = new JTextArea(10, 35);
    private final JTextArea chatMembers = new JTextArea("Chat members: ", 10, 20);
    private final JTextField messageField = new JTextField();

    private ViewMessageListener viewMessageListener;

    public ChatWindow() {
        super("Chat");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        chat.setEditable(false);
        chat.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chatMembers.setEditable(false);
        chatMembers.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(chat);
        add(scrollPane);

        add(chatMembers, BorderLayout.WEST);
        add(messageField, BorderLayout.SOUTH);

        messageField.addActionListener(e -> {
            viewMessageListener.onMessage(messageField.getText());
            messageField.setText(null);
        });
    }

    public void setMessageListener(ViewMessageListener viewMessageListener) {
        this.viewMessageListener = viewMessageListener;
    }

    private void writeToChat(String message) {
        chat.append(message + "\n");
        chat.setCaretPosition(chat.getDocument().getLength());
    }

    @Override
    public void onServerMessage(String message) {
        writeToChat(message);
    }

    @Override
    public void onSuccessfulConnection() {
        setVisible(true);
    }

    @Override
    public void onUserListUpdate(String userList) {
        chatMembers.setText("Chat members:\n" + userList);
        chatMembers.setCaretPosition(chatMembers.getDocument().getLength());
    }
}
