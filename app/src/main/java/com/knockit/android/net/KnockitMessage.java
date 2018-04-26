package com.knockit.android.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class KnockitMessage implements Comparable, Comparator{

    public static final String MESSAGE_TYPE = "type";
    public static final String MESSAGE_TIME = "time";

    public static final int MESSAGE_TYPE_KNOKING = 0;
    public static final int MESSAGE_TYPE_LOW_BATTERY = 1;

    @SerializedName("messageType")
    @Expose
    private int messageType;

    private long time;

    /**
     * Empty Ctor
     */
    public KnockitMessage() {

    }

    /**
     * Ctor
     * @param messageType message type
     */
    public KnockitMessage(int messageType) {
        this.messageType = messageType;
        this.time = System.currentTimeMillis();
    }

    /**
     * Ctor
     * @param messageType message type
     * @param time time
     */
    public KnockitMessage(int messageType, long time) {
        this.messageType = messageType;
        this.time = time;
    }
    /**
     * get message type
     * @return message type
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * set message type
     * @param messageType message type
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * get time
     * @return time
     */
    public long getTime() {
        return time;
    }

    /**
     * set time
     * @param time time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *
     * @param messageBody messageBody string
     * @return a KnockitMessage constructed from the messageBody string
     */
    public static KnockitMessage getMessage(String messageBody) {
            if (messageBody != null) {
                KnockitMessage knockitMessage = new Gson().fromJson(messageBody, KnockitMessage.class);
                knockitMessage.setTime(System.currentTimeMillis());
                return knockitMessage;
            }
            return null;

    }


    @Override
    public int compare(Object o, Object t1) {
        return ((Long)((KnockitMessage)o).getTime()).compareTo(((Long)((KnockitMessage)t1).getTime()));
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return ((Long)((KnockitMessage)o).getTime()).compareTo(this.getTime());
    }
}
