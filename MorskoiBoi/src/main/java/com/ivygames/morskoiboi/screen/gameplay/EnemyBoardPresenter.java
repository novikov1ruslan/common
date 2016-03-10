package com.ivygames.morskoiboi.screen.gameplay;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.ivygames.morskoiboi.model.Vector2;
import com.ivygames.morskoiboi.screen.view.BasePresenter;
import com.ivygames.morskoiboi.screen.view.TouchState;

import org.commons.logger.Ln;

final class EnemyBoardPresenter extends BasePresenter {

    private int mAnimationHorOffset;
    private int mAnimationVerOffset;
    private final Rect mDstRect = new Rect();
    private final Rect mLockDstRect = new Rect();

    private int mTouchX;
    private int mTouchY;

    private ShotListener mShotListener;

    private TouchState mTouchState = new TouchState();
    private int mTouchAction = mTouchState.getTouchAction();

    public EnemyBoardPresenter(int boardSize, float turnBorderSize) {
        super(boardSize, turnBorderSize);
    }

    @Override
    public void measure(int w, int h, int horOffset, int verOffset, int hPadding, int vPadding) {
        super.measure(w, h, horOffset, verOffset, hPadding, vPadding);
        mAnimationHorOffset = mBoardRect.left + mHalfCellSize;
        mAnimationVerOffset = mBoardRect.top + mHalfCellSize;
    }

    public void setShotListener(ShotListener shotListener) {
        mShotListener = shotListener;
    }

    @NonNull
    public Rect getAnimationDestination(@NonNull Vector2 aim, float cellRatio) {
        int dx = aim.getX() * mCellSizePx + mAnimationHorOffset;
        int dy = aim.getY() * mCellSizePx + mAnimationVerOffset;

        int d = (int) (cellRatio * mHalfCellSize);
        mDstRect.left = dx - d;
        mDstRect.top = dy - d;
        mDstRect.right = dx + d;
        mDstRect.bottom = dy + d;

        return mDstRect;
    }

    @NonNull
    public Rect getAimRectDst(Vector2 aim) {
        int x = aim.getX();
        int y = aim.getY();
        int left = x * mCellSizePx + mBoardRect.left;
        int top = y * mCellSizePx + mBoardRect.top;
        mLockDstRect.left = left;
        mLockDstRect.top = top;
        mLockDstRect.right = left + mCellSizePx;
        mLockDstRect.bottom = top + mCellSizePx;

        return mLockDstRect;
    }

    public int getTouchedJ() {
        int y = mTouchY - mBoardRect.top;
        return y / mCellSizePx;
    }

    public int getTouchedI() {
        int x = mTouchX - mBoardRect.left;
        return x / mCellSizePx;
    }

    public Rect getBoardRect() {
        return mBoardRect;
    }

    public void onTouch(TouchState touchState) {
        mTouchState = touchState;
        mTouchX = mTouchState.getTouchX();
        mTouchY = mTouchState.getTouchY();
        Ln.v("x=" + mTouchX + "; y=" + mTouchY);
        mTouchAction = mTouchState.getTouchAction();
        // TODO: create universal procedure to map x,y to cell
        if (mTouchAction == MotionEvent.ACTION_DOWN/* && !mLocked*/) {
            mShotListener.onAimingStarted();
        }

        if (mTouchAction == MotionEvent.ACTION_UP/* && !mLocked*/) {
            // TODO: unify these 2 callbacks
            mShotListener.onAimingFinished();
            mShotListener.onShot(getTouchedI(), getTouchedJ());
        }
    }

    public boolean startedDragging() {
        return mTouchState.getDragStatus() == TouchState.START_DRAGGING;
    }

    public void unlock() {
        if (mTouchAction == MotionEvent.ACTION_DOWN || mTouchAction == MotionEvent.ACTION_MOVE) {
            mShotListener.onAimingStarted();
        }
    }
}
