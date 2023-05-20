package view;

import controller.ConnectionErrorListener;
import controller.ConnectionSuccessListener;
import message.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectionWindow extends JDialog implements ConnectionSuccessListener, ConnectionErrorListener {
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final Integer DEFAULT_PORT = 8080;

    private final JTextArea errorArea = new JTextArea();

    private ConnectionButtonListener connectionButtonListener;

    public ConnectionWindow(JFrame frame) {
        super(frame, "Connection", true);

        JTextField nameField = new JTextField();
        JTextField ipField = new JTextField(DEFAULT_IP);
        JTextField portField = new JTextField(DEFAULT_PORT.toString());

        GridLayout layout = new GridLayout(8, 1);
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);

        contentPane.add(new JLabel("Enter your name:"));
        contentPane.add(nameField);
        contentPane.add(new JLabel("Enter IP:"));
        contentPane.add(ipField);
        contentPane.add(new JLabel("Enter port:"));
        contentPane.add(portField);

        contentPane.add(createOkButton(nameField, ipField, portField));
        contentPane.add(this.errorArea);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(210, 310));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private JButton createOkButton(JTextField nameField, JTextField ipField, JTextField portField) {
        JButton button = new JButton("Connect");
        button.setPreferredSize(new Dimension(210, 20));
        button.addActionListener(e -> {
            connectionButtonListener.onConnectionParametersEntered(
                    nameField.getText(), ipField.getText(), portField.getText());
        });
        return button;
    }

    public void setConnectionButtonListener(ConnectionButtonListener connectionButtonListener) {
        this.connectionButtonListener = connectionButtonListener;
    }

    @Override
    public void onConnectionError(Message message) {
        this.errorArea.setText("Connection error. \n" + message.message());
    }

    @Override
    public void onSuccessfulConnection() {
        setVisible(false);
    }

    public void showWindow() {
        setVisible(true);
    }
}
