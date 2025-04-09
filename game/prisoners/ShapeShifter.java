package game.prisoners;

import game.exceptions.GameException;
import game.security.SecuritySystem;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ShapeShifter extends AlienPrisoner {
    private int disguisesLeft;
    
    public ShapeShifter(String name, int disguises) {
        super(name);
        this.disguisesLeft = disguises;
    }
    
    @Override
    public String getAbilityDescription() {
        return "ShapeShifter - Disguises: " + disguisesLeft;
    }
    
    @Override
    public void showZoneOptions(int zone) {
        System.out.println("1) Use disguise (" + disguisesLeft + " left)");
        System.out.println("2) Find camera blind spot");
    }
    
    @Override
    public boolean attemptZoneAction(int action, SecuritySystem security, int zone, Random random) throws GameException {
        switch(action) {
            case 1: return useDisguise(security, zone, random);
            case 2: return findBlindSpot(security, zone);
            default: throw new GameException("Invalid action selected");
        }
    }
    
    private boolean useDisguise(SecuritySystem security, int zone, Random random) throws GameException {
        try {
            if(disguisesLeft <= 0) {
                throw new GameException("No disguises remaining!");
            }
            
            disguisesLeft--;
            System.out.println("\nYou morph into a guard's appearance");
            
            String[][][] conversations = {
                { // Cell Block (Zone 0)
                    {"Guard: You're new here? Which squad are you with?", 
                     "1) Delta Team (just transferred)",
                     "2) I'm with the inspection team",
                     "3) Does it matter? I have clearance!"},
                    {"Correct answer", "Wrong answer", "Wrong answer"},
                    {"• You see a 'DELTA TEAM ORIENTATION' poster on the wall",
                     "• All inspectors wear BLUE armbands (yours are missing)",
                     "• The guard's name tag says 'FOLLOW PROTOCOL AT ALL TIMES'"}
                },
                { // Security Checkpoint (Zone 1)
                    {"Officer: What's today's challenge response?", 
                     "1) 'Red sky at night'",
                     "2) 'The owl hoots at midnight'",
                     "3) 'Vigilance eternal'"},
                    {"Wrong answer", "Correct answer", "Wrong answer"},
                    {"• A bulletin board shows 'SECURITY THEME: NOCTURNAL ANIMALS'",
                     "• There's an owl emblem on the officer's uniform",
                     "• The computer screensaver shows an owl in flight"}
                },
                { // Main Corridor (Zone 2)
                    {"Lieutenant: Why are you in this restricted area?", 
                     "1) Routine maintenance on door seals",
                     "2) Delivering prisoner meal trays",
                     "3) Just taking a shortcut to the break room"},
                    {"Correct answer", "Wrong answer", "Wrong answer"},
                    {"• A work order clipboard lists 'Door Seal Inspection - TODAY'",
                     "• Meal delivery bots handle all food transport",
                     "• A sign says 'NO UNAUTHORIZED SHORTCUTS - DISCIPLINARY ACTION'"}
                },
                { // Armory (Zone 3)
                    {"Armory Sergeant: Verification code for this shift?", 
                     "1) 'Alpha-Gamma-7'",
                     "2) 'Tango-Charlie-12'",
                     "3) 'Sigma-9-Approved'"},
                    {"Wrong answer", "Correct answer", "Wrong answer"},
                    {"• The code format is always 'Letter-Letter-Number'",
                     "• A sticky note shows 'TC12' under the keyboard",
                     "• The sergeant's coffee cup has 'TC12' written on it"}
                },
                { // Guard Station (Zone 4)
                    {"Captain: Status report immediately!", 
                     "1) All sectors secure, no issues",
                     "2) Finishing my patrol route now",
                     "3) Just checking some anomaly readings"},
                    {"Partial answer", "Correct answer", "Wrong answer"},
                    {"• The status board shows Sector D is under maintenance",
                     "• The patrol schedule shows this is the end-of-route check",
                     "• Anomaly reports require immediate radio alerts"}
                }
            };

            System.out.println("\n" + conversations[zone][0][0]);
            System.out.println("\nPossible responses:");
            for(int i=1; i<=3; i++) {
                System.out.println(conversations[zone][0][i]);
            }
            
            System.out.println("\nYou notice these details:");
            for(int i=0; i<3; i++) {
                System.out.println("- " + conversations[zone][2][i]);
            }
            
            System.out.print("\nYour choice (1-3): ");
            int response = scanner.nextInt();
            scanner.nextLine();
            
            if (response < 1 || response > 3) {
                throw new GameException("Invalid choice. Must be between 1 and 3");
            }
            
            String answerType = conversations[zone][1][response-1];
            
            switch(answerType) {
                case "Correct answer":
                    System.out.println("\nGuard: Verified, proceed ahead.");
                    return true;
                case "Partial answer":
                    System.out.println("\nGuard: Hmm... make it quick.");
                    return random.nextDouble() < 0.7;
                default:
                    System.out.println("\nGuard: That's not right! Alert!");
                    security.increaseAlertLevel();
                    return false;
            }
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear invalid input
            throw new GameException("Invalid input. Please enter a number.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new GameException("Invalid zone configuration");
        }
    }
    
    private boolean findBlindSpot(SecuritySystem security, int zone) throws GameException {
        try {
            String[] puzzles = {
                "Scramble: T-A-M-E-R-A (Camera type)",
                "Missing letters: S _ R V _ L _ A _ E (Monitoring)",
                "Reverse: yticilpmis (Camera movement)",
                "Anagram: L-E-N-S-A-T-I-O-N (Camera feature)",
                "Complete: Pan, Tilt, ____ (Camera function)"
            };
            String[] answers = {"camera", "surveillance", "simplicity", "lensation", "zoom"};
            
            System.out.println("\nLocating camera blind spot...");
            return solveWordPuzzle(puzzles[zone % puzzles.length], 
                                 answers[zone % answers.length]);
        } catch (IndexOutOfBoundsException e) {
            throw new GameException("Invalid zone index for puzzle selection");
        }
    }
    
    @Override
    public void displayEscapeMethod() {
        System.out.println("Disguised as maintenance staff, you walked out with a cleaning crew!");
    }
} 