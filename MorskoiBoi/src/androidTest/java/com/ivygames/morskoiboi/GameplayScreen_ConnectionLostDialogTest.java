package com.ivygames.morskoiboi;

import com.ivygames.morskoiboi.model.GameEvent;
import com.ivygames.morskoiboi.screen.boardsetup.BoardSetupScreen;
import com.ivygames.morskoiboi.screen.gameplay.GameplayScreen;

import org.junit.Test;

import static android.support.test.espresso.Espresso.pressBack;

public class GameplayScreen_ConnectionLostDialogTest extends GameplayScreen_ {

    @Test
    public void WhenConnectionLost__DialogDisplayed() {
        showScreen();
        ((GameplayScreen) screen()).onEventMainThread(GameEvent.CONNECTION_LOST);
        checkDisplayed(connectionLostDialog());
    }

    @Test
    public void PressingBack__DismissesDialog_GameFinishes_SelectGameScreensShown() {
        WhenConnectionLost__DialogDisplayed();
        pressBack();
        checkDoesNotExist(connectionLostDialog());
        FinishGame_BackToSelectGame();
    }

    @Test
    public void PressingOk__DismissesDialog_GameFinishes_SelectGameScreensShown() {
        WhenConnectionLost__DialogDisplayed();
        clickOn(okButton());
        checkDoesNotExist(connectionLostDialog());
        FinishGame_BackToSelectGame();
    }

}
