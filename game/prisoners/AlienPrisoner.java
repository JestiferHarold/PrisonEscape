package game.prisoners;

import game.interfaces.PrisonerActions;
import game.exceptions.GameException;
import java.util.Scanner;

public abstract class AlienPrisoner implements PrisonerActions {
    protected String name;
    protected boolean caught;
    protected Scanner scanner = new Scanner(System.in);
    
    public AlienPrisoner(String name) {
        this.name = name;
        this.caught = false;
    }
    
    public boolean isCaught() { return caught; }
    public void setCaught(boolean caught) { this.caught = caught; }
    
    protected boolean solveWordPuzzle(String hint, String answer) throws GameException {
        try {
            System.out.println("\nWord Puzzle: " + hint);
            System.out.print("Your answer: ");
            String attempt = scanner.nextLine();
            if (attempt == null || attempt.trim().isEmpty()) {
                throw new GameException("Answer cannot be empty");
            }
            return attempt.equalsIgnoreCase(answer);
        } catch (Exception e) {
            throw new GameException("Failed to process puzzle answer: " + e.getMessage());
        }
    }
} 