package controller;

import message.Message;

public interface ConnectionErrorListener {
    void onConnectionError(Message message);
}
