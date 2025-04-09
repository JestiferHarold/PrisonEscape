package game.prisoners;

import game.exceptions.GameException;
import game.security.SecuritySystem;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class TechnoCreature extends AlienPrisoner {
    private int energy;
    private boolean systemHacked;
    
    public TechnoCreature(String name, int energy) {
        super(name);
        this.energy = energy;
        this.systemHacked = false;
    }
    
    @Override
    public String getAbilityDescription() {
        return "TechnoCreature - Energy: " + energy + " | System Hacked: " + (systemHacked ? "YES" : "NO");
    }
    
    @Override
    public void showZoneOptions(int zone) {
        System.out.println("1) Hack security system (" + energy + " energy)");
        if(this.systemHacked) {
            System.out.println("2) Divert guards");
        } else {
            System.out.println("2) [LOCKED - Hack system first]");
        }
        System.out.println("3) Override door controls");
    }
    
    @Override
    public boolean attemptZoneAction(int action, SecuritySystem security, int zone, Random random) throws GameException {
        switch(action) {
            case 1: 
                boolean result = hackSystem(security, zone);
                if(result) {
                    this.systemHacked = true;
                }
                return result;
            case 2: 
                if(this.systemHacked) {
                    return divertGuards(security, random);
                } else {
                    throw new GameException("You must successfully hack the system first!");
                }
            case 3: return overrideDoors(zone);
            default: throw new GameException("Invalid action selected");
        }
    }
    
    private boolean hackSystem(SecuritySystem security, int zone) throws GameException {
        try {
            if(energy <= 0) {
                throw new GameException("Not enough energy!");
            }
            
            energy--;
            String[] puzzles = {
                "Decrypt: 3-15-4-5 (A=1, B=2...)",
                "Solve: 1010 + 1101 = ? (binary)",
                "Next in sequence: 2, 3, 5, 7, ?",
                "Password hint: Warden's birth planet",
                "Override code format: XX-XXX-XX"
            };
            String[] answers = {"code", "10111", "11", "mars", "47-239-15"};
            
            System.out.println("\nInitiating hack...");
            return solveWordPuzzle(puzzles[zone % puzzles.length], 
                                 answers[zone % answers.length]);
        } catch (IndexOutOfBoundsException e) {
            throw new GameException("Invalid zone index for hack puzzle");
        }
    }
    
    private boolean divertGuards(SecuritySystem security, Random random) throws GameException {
        try {
            System.out.println("\nUsing hacked system to divert guards...");
            System.out.println("Select diversion tactic:");
            System.out.println("1) Fire alarm in east wing");
            System.out.println("2) Cell door malfunction");
            System.out.println("3) Security breach alert");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice < 1 || choice > 3) {
                throw new GameException("Invalid diversion choice");
            }
            
            int successChance = 80 - (security.getAlertLevel() * 10);
            if(random.nextInt(100) < successChance) {
                System.out.println("All guards diverted successfully!");
                return true;
            } else {
                System.out.println("Some guards remain! You must fight them!");
                return combatRemainingSecurity(security, random);
            }
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            throw new GameException("Invalid input. Please enter a number.");
        }
    }
    
    private boolean combatRemainingSecurity(SecuritySystem security, Random random) throws GameException {
        try {
            System.out.println("\nEngaging remaining guards in combat!");
            int securityStrength = security.getAlertLevel() * 15;
            
            while(energy > 0 && securityStrength > 0) {
                System.out.println("\nYour energy: " + energy);
                System.out.println("Security strength: " + securityStrength);
                System.out.println("Choose attack:");
                System.out.println("1) Energy blast (30 damage, costs 1 energy)");
                System.out.println("2) System overload (50 damage, costs 2 energy)");
                
                int attack = scanner.nextInt();
                scanner.nextLine();
                
                int damage = 0;
                if(attack == 1 && energy >= 1) {
                    damage = 30;
                    energy -= 1;
                    System.out.println("You blast security for " + damage + " damage!");
                } else if(attack == 2 && energy >= 2) {
                    damage = 50;
                    energy -= 2;
                    System.out.println("You overload systems for " + damage + " damage!");
                } else {
                    System.out.println("Not enough energy!");
                    continue;
                }
                
                securityStrength -= damage;
                
                if(securityStrength > 0) {
                    int securityDamage = 5 + random.nextInt(10);
                    System.out.println("Security fights back for " + securityDamage + " damage!");
                    energy = Math.max(0, energy - (securityDamage/5));
                }
            }
            
            if(energy <= 0) {
                throw new GameException("You've been overwhelmed!");
            }
            System.out.println("You defeated the remaining guards!");
            return true;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            throw new GameException("Invalid input. Please enter a number.");
        }
    }
    
    private boolean overrideDoors(int zone) throws GameException {
        try {
            System.out.println("\nBypassing door controls...");
            System.out.println("Solve the security puzzle to override doors:");
            
            String[] doorPuzzles = {
                "What is the third prime number after 7?",
                "Decrypt this: 20-8-5-16-1-19-19-23-15-18-4",
                "Complete the sequence: 2, 5, 10, 17, __",
                "What is 1011 in decimal? (binary conversion)"
            };
            String[] doorAnswers = {"13", "thepassword", "26", "11"};
            
            int puzzleIndex = zone % doorPuzzles.length;
            System.out.println(doorPuzzles[puzzleIndex]);
            System.out.print("Your answer: ");
            String answer = scanner.nextLine();
            
            if(answer == null || answer.trim().isEmpty()) {
                throw new GameException("Answer cannot be empty");
            }
            
            return answer.equalsIgnoreCase(doorAnswers[puzzleIndex]);
        } catch (IndexOutOfBoundsException e) {
            throw new GameException("Invalid zone index for door puzzle");
        }
    }
    
    @Override
    public void displayEscapeMethod() {
        System.out.println("You hacked the mainframe and walked out the front door!");
    }
} 