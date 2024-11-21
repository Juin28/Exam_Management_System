package comp3111.examsystem.controller;

import comp3111.examsystem.service.MsgSender;

public class MsgSenderWrapper {
    public void showMsg(String message) {
        MsgSender.showMsg(message);
    }
}
