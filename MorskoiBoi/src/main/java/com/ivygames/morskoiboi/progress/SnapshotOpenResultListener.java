package com.ivygames.morskoiboi.progress;

import com.google.android.gms.games.snapshot.Snapshot;
import com.ivygames.morskoiboi.model.Progress;

interface SnapshotOpenResultListener {
    void onConflict(String conflictId, Snapshot resolveSnapshot);

    void onUpdateLocalWith(Progress cloudProgress);

    void onUpdateServerWith(Progress mLocalProgress);
}
