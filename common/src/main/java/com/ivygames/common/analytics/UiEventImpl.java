package com.ivygames.common.analytics;

import android.support.annotation.NonNull;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

import org.commons.logger.Ln;

public final class UiEventImpl implements UiEventInterface {
    private static final String GA_CAT_UI = "ui";

    @NonNull
    private final Tracker mTracker;

    public UiEventImpl(@NonNull Tracker tracker) {
        mTracker = tracker;
    }

    @Override
    public void screenView(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void send(String action) {
        Ln.v("action=" + action);
        EventBuilder builder = new EventBuilder(UiEventImpl.GA_CAT_UI, action);
        mTracker.send(builder.build());
    }

    @Override
    public void send(String action, String label) {
        Ln.v("action=" + action + "; label=" + label);
        EventBuilder builder = new EventBuilder(UiEventImpl.GA_CAT_UI, action).setLabel(label);
        mTracker.send(builder.build());
    }

    @Override
    public void send(String action, int value) {
        Ln.v("action=" + action + "; value=" + value);
        EventBuilder builder = new EventBuilder(UiEventImpl.GA_CAT_UI, action).setValue(value);
        mTracker.send(builder.build());
    }
}