package minesweeper.view;

import java.util.Scanner;

import minesweeper.model.GameState;
import minesweeper.model.Location;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperException;

public class MinesweeperCLI {
    private static Minesweeper minesweeper;
    private static final int rows = 4;
    private static final int cols = 4;
    private static final int mineCount = 2;
    
    public static void main(String[] args) throws MinesweeperException{
        try(Scanner scanner = new Scanner(System.in)) {
            boolean sentinel = true;
            minesweeper = new Minesweeper(rows, cols, mineCount);
            System.out.println("Mines: " + minesweeper.getMineCount());
            help();
            while(sentinel) {
                System.out.println("\n" + minesweeper);
                System.out.println("Moves: " + minesweeper.getMoveCount() + "\n");
                System.out.print("Enter command: ");
                String command = scanner.nextLine();
                String[] tokens = command.split(" ");
                switch(tokens[0]) {
                    case "quit":
                        sentinel = !quit(scanner);
                        break;
                    case "pick":
                        if(tokens.length == 3){
                            pick(tokens);
                            break;
                        }else{
                            invalid(command);
                            break;
                        }                        
                    case "hint":
                        hint();
                        break;
                    case "help":
                        help();
                        break;
                    case "reset":
                        reset();
                        break;
                    case "solve":
                        solve();
                        break;
                    default:
                        invalid(command);
                        break;
                }
            }
            System.out.println("Good bye!");
        }
    }

    private static boolean quit(Scanner scanner) {
        System.out.print("Are you sure? (y/n): ");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("y");
    }

    private static void pick(String[] tokens) throws MinesweeperException{
        try {
            int row = Integer.valueOf(tokens[1]); int col= Integer.valueOf(tokens[2]);
            if(minesweeper.getGameState() == GameState.IN_PROGRESS){
                minesweeper.makeSelection(row, col);
            }else{
                throw new MinesweeperException("Sorry! The game is already over! You " + minesweeper.getGameState() + "! Please 'reset' or 'quit'.");
            }
        } catch (IndexOutOfBoundsException e1) {
            System.out.println("Invalid location: " + tokens[1] + ", " + tokens[2] + ". Please try again.");
        } catch (NumberFormatException e2) {
            System.out.println("Invalid location: " + tokens[1] + ", " + tokens[2] + ". Please try again.");
        } catch (MinesweeperException me) {
            System.out.println(me.getMessage());
        }
    }

    private static void hint() throws MinesweeperException{
        try {
            System.out.println("Give " + minesweeper.getHint() + " a try.");
        } catch (MinesweeperException me) {
            System.out.println(me.getMessage());
        }
    }

    private static void help() {
        System.out.println("Commands: ");
        System.out.println("  help - this help message");
        System.out.println("  pick <row> <col> - uncovers cell a <row> <col>");
        System.out.println("  hint - displays a safe selection");
        System.out.println("  solve - solves the minesweeper game");
        System.out.println("  reset - resets to a new game");
        System.out.println("  quit - quits the game");
    }

    private static void reset(){
        minesweeper = new Minesweeper(rows, cols, mineCount);
        System.out.println("\n -==-==-==- <NEW GAME> -==-==-==-");
        System.out.println("Mines: " + minesweeper.getMineCount());
        help();
    }

    private static void solve(){
        while(minesweeper.getGameState() == GameState.IN_PROGRESS){
            try {
                Location location = minesweeper.getHint();
                System.out.println("Selection: "  + location);
                pick(("pick " + location.getRow() + " " + location.getCol()).split(" "));
                if(minesweeper.getGameState() == GameState.IN_PROGRESS){
                    System.out.println(minesweeper);
                    System.out.println("Moves: " + minesweeper.getMoveCount()+ "\n");
                }
            } catch (MinesweeperException e) {
                // squash
            }
        }
    }

    private static void invalid(String command) {
        System.out.println("Invalid command: " + command);
    }
}
