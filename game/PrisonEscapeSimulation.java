package game;

import game.prisoners.AlienPrisoner;
import game.prisoners.ShapeShifter;
import game.prisoners.TechnoCreature;
import game.prisoners.BruteTitan;
import game.security.SecuritySystem;
import game.exceptions.GameException;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class PrisonEscapeSimulation {
    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            Random random = new Random();
            
            System.out.println("=== INTERGALACTIC PRISON ESCAPE ===");
            System.out.println("Choose your prisoner type:");
            System.out.println("1) Shape-Shifter - Master of disguise");
            System.out.println("2) Techno-Creature - Hacking expert");
            System.out.println("3) Brute Titan - Unstoppable force");
            System.out.print("Select (1-3): ");
            
            int type = getValidInput(scanner, 1, 3);
            
            AlienPrisoner prisoner = createPrisoner(type);
            SecuritySystem security = new SecuritySystem();
            
            String[] zones = {
                "Cell Block", 
                "Security Checkpoint", 
                "Main Corridor", 
                "Armory", 
                "Guard Station", 
                "Exit Gate"
            };
            
            int currentZone = 0;
            boolean escaped = false;
            
            while(!escaped && !prisoner.isCaught() && currentZone < zones.length) {
                try {
                    System.out.println("\n=== CURRENT LOCATION: " + zones[currentZone] + " ===");
                    System.out.println("Security Level: " + security.getAlertLevel());
                    System.out.println("Prisoner Status: " + prisoner.getAbilityDescription());
                    
                    System.out.println("\nOptions:");
                    prisoner.showZoneOptions(currentZone);
                    System.out.print("Choose action: ");
                    int action = getValidInput(scanner, 1, 3);
                    
                    boolean zoneCleared = prisoner.attemptZoneAction(action, security, currentZone, random);
                    
                    if(zoneCleared) {
                        System.out.println("\nYou successfully cleared " + zones[currentZone] + "!");
                        currentZone++;
                        
                        if(currentZone >= zones.length) {
                            escaped = true;
                            System.out.println("\n=== ESCAPE SUCCESSFUL ===");
                            prisoner.displayEscapeMethod();
                        } else {
                            security.adaptToPrisoner(prisoner);
                        }
                    } else {
                        System.out.println("\nFailed to clear " + zones[currentZone]);
                        security.increaseAlertLevel();
                        if(security.detectPrisoner(prisoner, random)) {
                            prisoner.setCaught(true);
                        }
                    }
                } catch (GameException e) {
                    System.out.println("\nError: " + e.getMessage());
                    security.increaseAlertLevel();
                }
            }
            
            if(!escaped && prisoner.isCaught()) {
                System.out.println("\n=== CAPTURED ===");
                System.out.println("You were caught in " + zones[currentZone]);
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    private static int getValidInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Invalid input. Please enter a number between %d and %d: ", min, max);
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static AlienPrisoner createPrisoner(int type) {
        switch(type) {
            case 1: return new ShapeShifter("Morphis", 3);
            case 2: return new TechnoCreature("Cypher-X", 5);
            case 3: return new BruteTitan("Ragnok", 100);
            default: return new ShapeShifter("Morphis", 3);
        }
    }
} 