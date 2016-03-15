package com.ivygames.morskoiboi.screen.boardsetup;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.ivygames.morskoiboi.model.Ship;
import com.ivygames.morskoiboi.model.Vector2;
import com.ivygames.morskoiboi.screen.view.Aiming;
import com.ivygames.morskoiboi.screen.view.BasePresenter;

final class SetupBoardPresenter extends BasePresenter {
    private final Rect mShipSelectionRect = new Rect();
    private final Rect mShipDisplayRect = new Rect();
    private final Point shipDisplayCenter = new Point();
    private final Rect mPickedShipRect = new Rect();
    private final RectF rectF = new RectF();
//    private int mTouchX;
//    private int mTouchY;

    public SetupBoardPresenter(int boardSize, float dimension) {
        super(boardSize, dimension);
    }

    @Override
    public void measure(int w, int h, int hPadding, int vPadding) {
        // calculate mShipSelectionRect (it starts from left=0, top=0)
        mShipSelectionRect.right = w / 2;
        mShipSelectionRect.bottom = h / 4;

        // calculate mShipDisplayRect (it starts from top=0)
        mShipDisplayRect.left = mShipSelectionRect.right + 1;
        mShipDisplayRect.right = w;
        mShipDisplayRect.bottom = mShipSelectionRect.bottom;

        shipDisplayCenter.set(mShipDisplayRect.centerX(), mShipDisplayRect.centerY());

        h = h - mShipSelectionRect.height();
        super.measure(w, h, hPadding, vPadding);
        setBoardVerticalOffset(mShipDisplayRect.height());
    }

    private int getShipWidthInPx(int shipSize) {
        return shipSize * mCellSizePx;
    }

    private boolean isInShipSelectionArea(int x, int y) {
        return mShipSelectionRect.contains(x, y);
    }

    @NonNull
    public Point getTopLeftPointInTopArea(int shipSize) {
        int left = mShipSelectionRect.centerX() - getShipWidthInPx(shipSize) / 2;
        int top = mShipSelectionRect.centerY() - mCellSizePx / 2;
        return new Point(left, top);
    }

    public Point getShipDisplayAreaCenter() {
        return shipDisplayCenter;
    }

    private Vector2 getPickedShipCoordinate() {
        int shipInBoardCoordinatesX = mPickedShipRect.left - mBoardRect.left + mHalfCellSize;
        int shipInBoardCoordinatesY = mPickedShipRect.top - mBoardRect.top + mHalfCellSize;
        int i = shipInBoardCoordinatesX / mCellSizePx;
        int j = shipInBoardCoordinatesY / mCellSizePx;
        return Vector2.get(i, j);
    }

    private void centerPickedShipRectAround(@NonNull Ship ship, int x, int y) {
        int widthInPx = getShipWidthInPx(ship.getSize());
        int halfWidthInPx = widthInPx / 2;
        boolean isHorizontal = ship.isHorizontal();
        mPickedShipRect.left = x - (isHorizontal ? halfWidthInPx : mHalfCellSize);
        mPickedShipRect.top = y - (isHorizontal ? mHalfCellSize : halfWidthInPx);
        mPickedShipRect.right = mPickedShipRect.left + (isHorizontal ? widthInPx : mCellSizePx);
        mPickedShipRect.bottom = mPickedShipRect.top + (isHorizontal ? mCellSizePx : widthInPx);
    }

    public Rect getPickedShipRect() {
        return mPickedShipRect;
    }

    @NonNull
    public Aiming getAimingForPickedShip(Vector2 mAim, Ship mPickedShip) {
        int width = mPickedShip.isHorizontal() ? mPickedShip.getSize() : 1;
        int height = mPickedShip.isHorizontal() ? 1 : mPickedShip.getSize();
        return getAiming(mAim, width, height);
    }

    public Vector2 getAimForShip(@NonNull Ship ship, MotionEvent event) {
        return getAimForShip(ship, (int) event.getX(), (int) event.getY());
    }


    private Vector2 getAimForShip(@NonNull Ship ship, int x, int y) {
        centerPickedShipRectAround(ship, x, y);
        return getPickedShipCoordinate();
    }

    public int getTouchJ(int y) {
        return (y - mBoardRect.top) / mCellSizePx;
    }

    public int getTouchI(int x) {
        return (x - mBoardRect.left) / mCellSizePx;
    }

    public boolean isInShipSelectionArea(MotionEvent event) {
        return isInShipSelectionArea((int) event.getX(), (int) event.getY());
    }

    public final RectF getInvalidRect(int i, int j) {
        float left = mBoardRect.left + i * mCellSizePx + 1;
        float top = mBoardRect.top + j * mCellSizePx + 1;
        float right = left + mCellSizePx;
        float bottom = top + mCellSizePx;

        rectF.left = left + 1;
        rectF.top = top + 1;
        rectF.right = right;
        rectF.bottom = bottom;

        return rectF;
    }

}
