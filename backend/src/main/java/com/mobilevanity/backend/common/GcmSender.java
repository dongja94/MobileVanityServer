package com.mobilevanity.backend.common;

import com.google.android.gcm.server.*;

import java.io.IOException;

/**
 * Created by Administrator on 2016-08-31.
 */
public class GcmSender {
    Sender sender = new Sender(GcmConstant.SERVER_KEY);

    private static GcmSender instance;
    public static GcmSender getInstance() {
        if (instance == null) {
            synchronized (GcmSender.class) {
                if (instance == null) {
                    instance = new GcmSender();
                }
            }
        }
        return instance;
    }

    private GcmSender() {

    }

    public boolean sendGCM(String registrationId) {
        Message message = new Message.Builder().addData("type", "noti").build();
        try {
            com.google.android.gcm.server.Result result = sender.send(message, registrationId, 3);
            if (result.getMessageId() != null) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
