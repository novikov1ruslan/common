package com.ivygames.morskoiboi;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.ivygames.morskoiboi.model.Board;
import com.ivygames.morskoiboi.model.Cell;
import com.ivygames.morskoiboi.model.Game;
import com.ivygames.morskoiboi.model.Ship;

import java.util.Collection;

public interface Rules {
    boolean isBoardSet(Board board);

    boolean isCellConflicting(Cell cell);

    /**
     * @return true if board has full fleet and all the ships are destroyed
     */
    boolean isItDefeatedBoard(Board board);

    int[] getTotalShips();

    int calcTotalScores(@NonNull Collection<Ship> ships, @NonNull Game game, boolean surrendered);

    Bitmap getBitmapForShipSize(int size);

    /**
     * @return array containing integers each representing length of a separate ship type
     */
    int[] newShipTypesArray();

    Cell markAdjacentCellsIfNeeded(Ship ship, Cell cell);

    Collection<Ship> generateFullFleet();

}
