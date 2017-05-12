package com.ivygames.common;

import android.support.annotation.NonNull;

public class ChatMessage {

    @NonNull
    public final String text;
    public final boolean isMe;

    @NonNull
    public static ChatMessage newMyMessage(@NonNull String text) {
        return new ChatMessage(text, true);
    }

    @NonNull
    public static ChatMessage newEnemyMessage(@NonNull String text) {
        return new ChatMessage(text, false);
    }

    private ChatMessage(@NonNull String text, boolean isMe) {
        this.text = text.replaceAll("\n", "");
        this.isMe = isMe;
    }

    @Override
    public String toString() {
        return text;
    }
}
