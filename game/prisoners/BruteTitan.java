package game.prisoners;

import game.exceptions.GameException;
import game.security.SecuritySystem;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class BruteTitan extends AlienPrisoner {
    private int health;
    
    public BruteTitan(String name, int health) {
        super(name);
        this.health = health;
    }
    
    @Override
    public String getAbilityDescription() {
        return "BruteTitan - Health: " + health;
    }
    
    @Override
    public void showZoneOptions(int zone) {
        System.out.println("1) Smash through obstacle");
        System.out.println("2) Intimidate guards");
        System.out.println("3) Fight through security");
    }
    
    @Override
    public boolean attemptZoneAction(int action, SecuritySystem security, int zone, Random random) throws GameException {
        switch(action) {
            case 1: return smashThrough(security, random);
            case 2: return intimidate(security, random);
            case 3: return fightSecurity(security, random);
            default: throw new GameException("Invalid action selected");
        }
    }
    
    private boolean smashThrough(SecuritySystem security, Random random) throws GameException {
        try {
            System.out.println("\nYou charge at the obstacle!");
            System.out.println("Press ENTER rapidly 5 times within 5 seconds to smash through!");
            
            long startTime = System.currentTimeMillis();
            int hits = 0;
            
            while(hits < 5 && (System.currentTimeMillis() - startTime) < 5000) {
                scanner.nextLine(); // Wait for ENTER press
                hits++;
                System.out.print("SMASH! ");
            }
            
            if((System.currentTimeMillis() - startTime) >= 5000) {
                throw new GameException("Too slow! Guards caught you!");
            }
            
            if(hits >= 3) {
                System.out.println("\nYou break through!");
                return true;
            } else {
                System.out.println("\nNot enough force! Guards arrive!");
                security.increaseAlertLevel();
                return false;
            }
        } catch (Exception e) {
            throw new GameException("Error during smash attempt: " + e.getMessage());
        }
    }
    
    private boolean intimidate(SecuritySystem security, Random random) throws GameException {
        System.out.println("\nYou roar at the guards!");
        int successChance = 60 - (security.getAlertLevel() * 10);
        
        if(random.nextInt(100) < successChance) {
            System.out.println("Guards flee in terror!");
            return true;
        } else {
            System.out.println("Guards stand their ground!");
            return fightSecurity(security, random);
        }
    }
    
    private boolean fightSecurity(SecuritySystem security, Random random) throws GameException {
        try {
            System.out.println("\nEngaging in combat!");
            int securityStrength = security.getAlertLevel() * 20;
            
            while(health > 0 && securityStrength > 0) {
                System.out.println("\nYour health: " + health);
                System.out.println("Security strength: " + securityStrength);
                System.out.println("Choose attack:");
                System.out.println("1) Punch (30 damage)");
                System.out.println("2) Charge (50 damage, risky)");
                
                int attack = scanner.nextInt();
                scanner.nextLine();
                
                int damage = 0;
                if(attack == 1) {
                    damage = 30;
                    System.out.println("You punch security for " + damage + " damage!");
                } else if(attack == 2) {
                    if(random.nextInt(100) < 70) {
                        damage = 50;
                        System.out.println("You charge through security for " + damage + " damage!");
                    } else {
                        System.out.println("You stumble during the charge!");
                    }
                } else {
                    throw new GameException("Invalid attack choice");
                }
                
                securityStrength -= damage;
                
                if(securityStrength > 0) {
                    int securityDamage = 10 + random.nextInt(15);
                    System.out.println("Security fights back for " + securityDamage + " damage!");
                    health -= securityDamage;
                }
            }
            
            if(health <= 0) {
                throw new GameException("You've been overwhelmed!");
            }
            System.out.println("You defeated the guards!");
            return true;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            throw new GameException("Invalid input. Please enter a number.");
        }
    }
    
    @Override
    public void displayEscapeMethod() {
        System.out.println("You smashed through every obstacle to freedom!");
    }
} 