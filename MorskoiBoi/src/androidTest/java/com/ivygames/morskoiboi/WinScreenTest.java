package com.ivygames.morskoiboi;

import com.ivygames.morskoiboi.model.Game;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WinScreenTest extends WinScreen_ {

    @Test
    public void WhenScreenDisplayed__GamesCounterIncremented() {
        showScreen();
        verify(settings(), times(1)).incrementGamesPlayedCounter();
    }

    @Test
    public void WhenScreenDisplayedForAndroidGame__AchievementsProcessed() {
        setGameType(Game.Type.VS_ANDROID);
        showScreen();
        expectProcessAchievementsBeCalled(times(1));
    }

    @Test
    public void WhenScreenDisplayedForNonAndroidGame__AchievementsNotProcessed() {
        setGameType(Game.Type.BLUETOOTH);
        showScreen();
        expectProcessAchievementsBeCalled(never());
    }

    @Test
    public void WhenScreenDisplayedWithPositiveScoreBalance__ProgressUpdated() {
        setScores(100);
        setPenalty(0);
        showScreen();
        expectUpdateProgressBeCalled(times(1));
        verify(settings(), times(1)).incrementProgress(100);
        verify(settings(), times(1)).setProgressPenalty(0);
    }

    @Test
    public void WhenScreenDisplayedWithNegativeScoreBalance__PenaltyUpdated() {
        setScores(100);
        setPenalty(200);
        showScreen();
        expectUpdateProgressBeCalled(never());
        verify(settings(), never()).incrementProgress(anyInt());
        verify(settings(), times(1)).setProgressPenalty(100);
    }

    @Test
    public void WhenGameTypeIsAndroid__ScoresAndDurationShown() {
        setGameType(Game.Type.VS_ANDROID);
        when(game.getTimeSpent()).thenReturn(135000L);
        setScores(100);
        showScreen();
        onView(timeView()).check(matches(withText("2:15")));
        onView(scoresView()).check(matches(withText("100")));
    }

    @Test
    public void WhenGameTypeIsNotAndroid__ScoresAndDurationNotShown() {
        setGameType(Game.Type.BLUETOOTH);
        showScreen();
        checkNotDisplayed(timeView());
        checkNotDisplayed(scoresView());
    }

    @Test
    public void WhenSignedIn__SignInOptionHidden() {
        setGameType(Game.Type.VS_ANDROID);
        setSignedIn(true);
        showScreen();
        checkNotDisplayed(signInBar());
    }

    @Test
    public void WhenNotAndroidGame__SignInOptionHidden() {
        setGameType(Game.Type.BLUETOOTH);
        setSignedIn(false);
        showScreen();
        checkNotDisplayed(signInBar());
    }

    @Test
    public void WhenAndroidGameAndNotSignedIn__SignInOptionDisplayed() {
        setGameType(Game.Type.VS_ANDROID);
        setSignedIn(false);
        showScreen();
        checkDisplayed(signInBar());
    }

    @Test
    public void AfterSignInClicked__SignInOptionHidden() {
        WhenAndroidGameAndNotSignedIn__SignInOptionDisplayed();
        clickOn(withId(R.id.sign_in_button));
        verify(apiClient(), times(1)).connect();
        signInSucceeded((SignInListener) screen());
        checkNotDisplayed(signInBar());
    }

    @Test
    public void WhenOpponentNotSurrendered__YesNoButtonsShowed() {
        surrendered = false;
        showScreen();
        checkDisplayed(yesButton());
        checkDisplayed(noButton());
    }

    @Test
    public void WhenOpponentSurrendered__InsteadOfYesNoContinueButtonShowed() {
        surrendered = true;
        showScreen();
        checkNotDisplayed(yesButton());
        checkNotDisplayed(noButton());
        checkDisplayed(continueButton());
    }

    @Test
    public void AfterYesPressed__BoardSetupScreenShown() {
        surrendered = false;
        showScreen();
        when(rules.getAllShipsSizes()).thenReturn(new int[]{});
        clickOn(yesButton());
        checkDisplayed(BOARD_SETUP_LAYOUT);
    }

    @Test
    public void AfterNoPressedForAndroid__SelectGameScreenShown() {
        setGameType(Game.Type.VS_ANDROID);
        surrendered = false;
        showScreen();
        clickOn(noButton());
        FinishGame_BackToSelectGame();
    }

    @Test
    public void WhenOpponentSurrendersPressingBack__FinishesGameOpensSelectGameScreen() {
        surrendered = true;
        setGameType(Game.Type.VS_ANDROID);
        showScreen();
        pressBack();
        FinishGame_BackToSelectGame();
    }

    @Test
    public void WhenScreenDestroyedForAndroidConnectedGame__ScoresSubmitted() {
        setGameType(Game.Type.VS_ANDROID);
        setSignedIn(true);
        showScreen();
        pressBack();
        expectSubmitScoreBeCalled(times(1));
    }

    @Test
    public void WhenScreenDestroyedForNonAndroidGame__ScoresNotSubmitted() {
        setGameType(Game.Type.BLUETOOTH);
        setSignedIn(true);
        showScreen();
        pressBack();
        expectSubmitScoreBeCalled(never());
    }

    @Test
    public void WhenScreenDestroyedWhenNotConnected__ScoresNotSubmitted() {
        setGameType(Game.Type.VS_ANDROID);
        setSignedIn(false);
        showScreen();
        pressBack();
        expectSubmitScoreBeCalled(never());
    }

}
