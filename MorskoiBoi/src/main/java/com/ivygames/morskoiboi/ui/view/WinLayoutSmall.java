package com.ivygames.morskoiboi.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ivygames.morskoiboi.R;
import com.ivygames.morskoiboi.model.Ship;

import java.util.Collection;

public class WinLayoutSmall extends NotepadLinearLayout {

	private TextView mTimeView;
	private TextView mScoreView;
	private View mScoreContainer;
	private View mSignInBar;
	private View mSignInButton;
	private View mYesButton;
	private View mNoButton;
	private View mContinueButton;

	public WinLayoutSmall(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTimeView = (TextView) findViewById(R.id.time);
		mScoreView = (TextView) findViewById(R.id.total_scores);
		mYesButton = findViewById(R.id.yes_button);
		mNoButton = findViewById(R.id.no_button);
		mContinueButton = findViewById(R.id.continue_button);

		mSignInButton = findViewById(R.id.sign_in_button);
		mSignInBar = findViewById(R.id.sign_in_bar);

		mScoreContainer = findViewById(R.id.scores_container);
	}

	public void setSignInClickListener(OnClickListener listener) {
		mSignInButton.setOnClickListener(listener);
	}

	public void setYesClickListener(OnClickListener listener) {
		mYesButton.setOnClickListener(listener);
	}

	public void setNoClickListener(OnClickListener listener) {
		mNoButton.setOnClickListener(listener);
		mContinueButton.setOnClickListener(listener);
	}

	public void setShips(Collection<Ship> ships) {
		// for extension
	}

	public void setTime(long millis) {
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		mTimeView.setText(String.format("%d:%02d", minutes, seconds % 60));
	}

	public void setTotalScore(int score) {
		mScoreView.setText(String.valueOf(score));
	}

	public void hideSignInBar() {
		mSignInBar.setVisibility(View.INVISIBLE);
	}

	public void showSignInBar() {
		mSignInBar.setVisibility(View.VISIBLE);
	}

	/**
	 * removes time, scores_container and sign_in_bar from the layout
	 */
	public void hideScorables() {
		mScoreContainer.setVisibility(View.GONE);
		mSignInBar.setVisibility(View.INVISIBLE);
		mTimeView.setVisibility(View.GONE);
	}

	public void opponentSurrendered() {
		findViewById(R.id.continue_pannel).setVisibility(GONE);
		findViewById(R.id.surrender_continue_pannel).setVisibility(VISIBLE);
	}

}
