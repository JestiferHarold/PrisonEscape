package game.interfaces;

import game.prisoners.AlienPrisoner;
import java.util.Random;

public interface SecurityActions {
    int getAlertLevel();
    void increaseAlertLevel();
    boolean detectPrisoner(AlienPrisoner prisoner, Random random);
    void adaptToPrisoner(AlienPrisoner prisoner);
} 