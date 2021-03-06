package com.ivygames.common.analytics;

import android.support.annotation.NonNull;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import com.google.example.games.basegameutils.BuildConfig;

import org.commons.logger.Ln;

final class UiEventImpl {
    private static final boolean LOG_ENABLED = BuildConfig.DEBUG;

    private static final String GA_CAT_UI = "ui";

    @NonNull
    private final Tracker mTracker;

    UiEventImpl(@NonNull Tracker tracker) {
        mTracker = tracker;
    }

    void screenView(@NonNull String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void send(@NonNull String action) {
        if (LOG_ENABLED) {
            Ln.v("action=" + action);
        }
        EventBuilder builder = new EventBuilder(UiEventImpl.GA_CAT_UI, action);
        mTracker.send(builder.build());
    }

    public void send(@NonNull String action, @NonNull String label) {
        if (LOG_ENABLED) {
            Ln.v("action=" + action + "; label=" + label);
        }
        EventBuilder builder = new EventBuilder(UiEventImpl.GA_CAT_UI, action).setLabel(label);
        mTracker.send(builder.build());
    }

    public void send(@NonNull String action, int value) {
        if (LOG_ENABLED) {
            Ln.v("action=" + action + "; value=" + value);
        }
        EventBuilder builder = new EventBuilder(UiEventImpl.GA_CAT_UI, action).setValue(value);
        mTracker.send(builder.build());
    }
}