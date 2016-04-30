package com.ivygames.morskoiboi.screen.gameplay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ivygames.morskoiboi.Animation;
import com.ivygames.morskoiboi.Bitmaps;
import com.ivygames.morskoiboi.R;
import com.ivygames.morskoiboi.model.PokeResult;
import com.ivygames.morskoiboi.model.Vector2;
import com.ivygames.morskoiboi.screen.view.BaseBoardRenderer;

import org.commons.logger.Ln;

import java.util.Random;

class EnemyBoardRenderer extends BaseBoardRenderer {
    private static final int TEXTURE_SIZE = 512;
    private static final float CELL_RATIO = 2f;

    private final Animation mSplashAnimation = new Animation(1000);
    private final Animation mExplosionAnimation = new Animation(1000);

    private Bitmap mNauticalBitmap;
    private Rect mSrcRect;

    private Bitmap mLockBitmapSrc;
    private Rect mLockSrcRect;

    private Animation mCurrentAnimation;

    private final EnemyBoardPresenter mPresenter;
    private Resources mResources;


    EnemyBoardRenderer(EnemyBoardPresenter presenter, Resources resources) {
        super(resources);
        mPresenter = presenter;
        mResources = resources;

        fillSplashAnimation(resources);
        fillExplosionAnimation(resources);
    }

    private void fillSplashAnimation(Resources resources) {
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_01));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_02));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_03));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_04));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_05));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_06));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_07));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_08));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_09));
        mSplashAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.splash_10));
    }

    private void fillExplosionAnimation(Resources resources) {
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_01));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_03));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_05));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_06));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_07));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_08));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_09));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_10));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_12));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_13));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_14));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_15));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_17));
        mExplosionAnimation.adFrame(Bitmaps.getBitmap(resources, R.drawable.explosion_18));
    }

    public boolean isAnimationRunning() {
        return mCurrentAnimation != null && mCurrentAnimation.isRunning();
    }

    public long animateExplosions(Canvas canvas, Vector2 aim) {
        canvas.drawBitmap(mCurrentAnimation.getCurrentFrame(), mCurrentAnimation.getBounds(),
                mPresenter.getAnimationDestination(aim, CELL_RATIO), null);
        return mCurrentAnimation.getFrameDuration();
    }

    public void startAnimation(PokeResult result) {
        mCurrentAnimation = result.cell.isMiss() ? mSplashAnimation : mExplosionAnimation;
        mCurrentAnimation.start();
    }

    public void drawNautical(Canvas canvas) {
        if (mNauticalBitmap != null) {
            canvas.drawBitmap(mNauticalBitmap, mSrcRect, mPresenter.getBoardRect(), null);
        }
    }

    public void drawAim(Canvas canvas, Rect rectDst) {
        canvas.drawBitmap(mLockBitmapSrc, mLockSrcRect, rectDst, null);
    }

    public void init(long availableMemory) {
//        if (size > getAvailableMemory() / 2) {
//
//        }

        Bitmap tmp = BitmapFactory.decodeResource(mResources, R.drawable.nautical8);
        if (tmp == null) {
            Ln.e("could not decode nautical");
        } else {
            final Random mRandom = new Random(System.currentTimeMillis());
            int x = mRandom.nextInt(tmp.getWidth() - TEXTURE_SIZE);
            int y = mRandom.nextInt(tmp.getHeight() - TEXTURE_SIZE);
            mNauticalBitmap = Bitmap.createBitmap(tmp, x, y, TEXTURE_SIZE, TEXTURE_SIZE);
            mSrcRect = new Rect(0, 0, mNauticalBitmap.getWidth(), mNauticalBitmap.getHeight());
            if (mNauticalBitmap.getWidth() != tmp.getWidth() || mNauticalBitmap.getHeight() != tmp.getHeight()) {
                tmp.recycle();
            }
        }

        mLockBitmapSrc = BitmapFactory.decodeResource(mResources, R.drawable.lock);
        mLockSrcRect = new Rect(0, 0, mLockBitmapSrc.getWidth(), mLockBitmapSrc.getHeight());
    }

    public void release() {
        if (mNauticalBitmap != null) {
            mNauticalBitmap.recycle();
            mNauticalBitmap = null;
        }
        if (mLockBitmapSrc != null) {
            mLockBitmapSrc.recycle();
            mLockBitmapSrc = null;
        }
    }

}
