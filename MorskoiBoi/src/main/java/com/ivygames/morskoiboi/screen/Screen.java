package com.ivygames.morskoiboi.screen;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ivygames.morskoiboi.BattleshipActivity;

public abstract class Screen {

    @NonNull
    protected final BattleshipActivity mParent;

    protected Screen(@NonNull BattleshipActivity parent) {
        mParent = parent;
    }

    @NonNull
    protected final FragmentManager getFragmentManager() {
        return mParent.getFragmentManager();
    }

    public abstract View getView();

    protected final View inflate(int layoutId) {
        return mParent.getLayoutInflater().inflate(layoutId, null);
    }

    protected final String getString(int resId) {
        return mParent.getString(resId);
    }

    protected final String getString(int resId, Object... formatArgs) {
        return mParent.getString(resId, formatArgs);
    }

    protected final void startActivity(Intent intent) {
        mParent.startActivity(intent);
    }

    protected final void startActivityForResult(Intent intent, int requestCode) {
        mParent.startActivityForResult(intent, requestCode);
    }

    protected final LayoutInflater getLayoutInflater() {
        return mParent.getLayoutInflater();
    }

    protected final View inflate(int resId, ViewGroup container) {
        return getLayoutInflater().inflate(resId, container, false);
    }

    protected final Resources getResources() {
        return mParent.getResources();
    }
}
