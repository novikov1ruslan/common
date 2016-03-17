package com.ivygames.morskoiboi.screen.boardsetup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ivygames.morskoiboi.R;
import com.ivygames.morskoiboi.Rules;
import com.ivygames.morskoiboi.RulesFactory;
import com.ivygames.morskoiboi.model.Board;
import com.ivygames.morskoiboi.model.Cell;
import com.ivygames.morskoiboi.model.Ship;
import com.ivygames.morskoiboi.screen.view.Aiming;
import com.ivygames.morskoiboi.screen.view.BaseBoardRenderer;
import com.ivygames.morskoiboi.screen.view.BaseBoardView;

import org.commons.logger.Ln;

import java.util.PriorityQueue;

public class SetupBoardView extends BaseBoardView {

    private static final long LONG_PRESS_DELAY = 1000;

    /**
     * needed to perform double clicks on the ships
     */
    private PickShipTask mPickShipTask;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final int mTouchSlop;

    private final Rules mRules = RulesFactory.getRules();

    public SetupBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        Ln.v("touch slop = " + mTouchSlop);
    }

    @Override
    protected BaseBoardRenderer getRenderer() {
        if (mRenderer == null) {
            mRenderer = new BaseBoardRenderer(getResources());
        }

        return mRenderer;
    }

    @Override
    protected SetupBoardPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new SetupBoardPresenter(10, getResources().getDimension(R.dimen.ship_border));
        }

        return (SetupBoardPresenter) mPresenter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawConflictingCells(canvas);
        drawDockedShip(canvas);
        drawPickedShip(canvas);
//        getRenderer().render(canvas, mTouchX, mTouchY);
    }

    private void drawConflictingCells(Canvas canvas) {
        for (int i = 0; i < Board.DIMENSION; i++) {
            for (int j = 0; j < Board.DIMENSION; j++) {
                Cell cell = mBoard.getCell(i, j);
                if (mRules.isCellConflicting(cell)) {
                    getRenderer().renderConflictingCell(canvas, getPresenter().getRectForCell(i, j));
                }
            }
        }
    }

    private void drawDockedShip(Canvas canvas) {
        if (getPresenter().getDockedShip() != null) {
            Bitmap bitmap = mRules.getBitmapForShipSize(getPresenter().getDockedShip().getSize());
            Point center = getPresenter().getShipDisplayAreaCenter();
            int displayLeft = center.x - bitmap.getWidth() / 2;
            int displayTop = center.y - bitmap.getHeight() / 2;
            canvas.drawBitmap(bitmap, displayLeft, displayTop, null);

            mRenderer.drawShip(canvas, getPresenter().getRectForDockedShip(), mShipPaint);
        }
    }

    private void drawPickedShip(Canvas canvas) {
        Rect shipRect = getPresenter().getPickedShipRect();
        if (shipRect != null) {
            canvas.drawRect(shipRect, mShipPaint);
        }

        Aiming aiming = getPresenter().getAiming();
        if (aiming != null) {
            mRenderer.render(canvas, aiming, mAimingPaint);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        getPresenter().updateAim((int) event.getX(), (int) event.getY());

        processMotionEvent(event);

        invalidate();

        return true;
    }

    private void processMotionEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mPickShipTask != null && mPickShipTask.hasMovedBeyondSlope(event, mTouchSlop)) {
                    mHandler.removeCallbacks(mPickShipTask);
                    mPickShipTask.run();
                    mPickShipTask = null;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (getPresenter().isInDockArea(x, y)) {
                    getPresenter().pickDockedShip(x, y);
                } else if (getPresenter().isOnBoard(x, y)) {
                    mPickShipTask = createNewPickTask(event);
                    Ln.v("scheduling long press task: " + mPickShipTask);
                    mHandler.postDelayed(mPickShipTask, LONG_PRESS_DELAY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mPickShipTask != null) {
                    cancelLongPressTask();
                    getPresenter().rotateShipAt(mBoard, x, y);
                } else if (getPresenter().hasPickedShip()) {
                    getPresenter().dropShip(mBoard);
                }
                break;
            default:
                cancelLongPressTask();
                break;
        }
    }

    private PickShipTask createNewPickTask(final MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        return new PickShipTask(event, new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPickShipTask = null;
                getPresenter().pickShipFromBoard(mBoard, x, y);
                getPresenter().updateAim(x, y);
                invalidate();
                return true;
            }
        });
    }

    private void cancelLongPressTask() {
        Ln.v("cancelling long press task: " + mPickShipTask);
        mHandler.removeCallbacks(mPickShipTask);
        mPickShipTask = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }

        mPresenter.measure(getMeasuredWidth(), getMeasuredHeight(), getHorizontalPadding(), getVerticalPadding());
    }

    public void setFleet(@NonNull PriorityQueue<Ship> ships) {
        Ln.v(ships);
        getPresenter().setFleet(ships);
        invalidate();
    }

    @Override
    public String toString() {
        if (mBoard == null) {
            Ln.e("no ships");
            return "no ships";
        }
        return super.toString() + '\n' + mBoard.toString();
    }

}
