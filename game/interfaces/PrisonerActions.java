package game.interfaces;

import game.exceptions.GameException;
import game.security.SecuritySystem;
import java.util.Random;

public interface PrisonerActions {
    String getAbilityDescription();
    void showZoneOptions(int zone);
    boolean attemptZoneAction(int action, SecuritySystem security, int zone, Random random) throws GameException;
    void displayEscapeMethod();
} 