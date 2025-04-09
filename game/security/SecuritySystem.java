package game.security;

import game.interfaces.SecurityActions;
import game.prisoners.AlienPrisoner;
import game.prisoners.ShapeShifter;
import game.prisoners.TechnoCreature;
import game.prisoners.BruteTitan;
import java.util.Random;

public class SecuritySystem implements SecurityActions {
    private int alertLevel;
    
    public SecuritySystem() {
        this.alertLevel = 1;
    }
    
    @Override
    public int getAlertLevel() { return alertLevel; }
    
    @Override
    public void increaseAlertLevel() {
        alertLevel = Math.min(5, alertLevel + 1);
    }
    
    @Override
    public boolean detectPrisoner(AlienPrisoner prisoner, Random random) {
        int detectionChance = alertLevel * 15;
        if(prisoner instanceof ShapeShifter) detectionChance -= 10;
        return random.nextInt(100) < detectionChance;
    }
    
    @Override
    public void adaptToPrisoner(AlienPrisoner prisoner) {
        if(prisoner instanceof ShapeShifter) {
            System.out.println("\nSecurity increases identity verification...");
            alertLevel = Math.min(5, alertLevel + 1);
        } 
        else if(prisoner instanceof TechnoCreature) {
            System.out.println("\nSecurity activates backup systems...");
            alertLevel = Math.min(5, alertLevel + 2);
        }
        else if(prisoner instanceof BruteTitan) {
            System.out.println("\nSecurity deploys riot teams...");
            alertLevel = Math.min(5, alertLevel + 3);
        }
    }
} 