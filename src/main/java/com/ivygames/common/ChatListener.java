package com.ivygames.common;

import android.support.annotation.NonNull;

public interface ChatListener {
    void showChatCrouton(@NonNull ChatMessage message);
}
