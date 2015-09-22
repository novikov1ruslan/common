package com.ivygames.morskoiboi.model;

public class Progress {

    public Progress(int rank) {
        mRank = rank;
    }

    private final int mRank;

    public int getScores() {
        return mRank;
    }

    @Override
    public String toString() {
        return "[rank=" + mRank + "]";
    }
}
