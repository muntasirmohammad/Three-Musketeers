package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class ThreeMusketeers implements Command{

  private final Board board;
  private Agent musketeerAgent, guardAgent;
  private final static Scanner scanner = new Scanner(System.in);
  private final List<Move> moves = new ArrayList<>();
  private final List<Move> undoneMoves = new ArrayList<>();
  // ArrayList of all moves made including undo
  private final List<Object> allMoves = new ArrayList<>();
  // Stack of whether the moves were simply moves, or undone moves, or redone moves
  private final Stack<Object> moveKind = new Stack<>();

  //At the beginning of the game no moves were undone
  private boolean undone = false;
  private Move lastMove;

	// All possible game modes
	public enum GameMode {
		Human("Human vs Human"),
		HumanRandom("Human vs Computer (Random)"),
		HumanGreedy("Human vs Computer (Greedy)");

		private final String gameMode;
		GameMode(final String gameMode) {
			this.gameMode = gameMode;
		}
	}

	/**
	 * Default constructor to load Starter board
	 */
	public ThreeMusketeers() {
		this.board = new Board();
	}

	/**
	 * Constructor to load custom board
	 * @param boardFilePath filepath of custom board
	 */
	public ThreeMusketeers(String boardFilePath) {
		this.board = new Board(boardFilePath);
	}

	/**
	 * Play game with human input mode selector
	 */
	public void play(){
		BGM();
		System.out.println("Welcome! \n");
		final GameMode mode = getModeInput();
		System.out.println("Playing " + mode.gameMode);
		play(mode);
	}

	/**
	 * Play game without human input mode selector
	 * @param mode the GameMode to run
	 */
	public void play(GameMode mode){
		selectMode(mode);
		runGame();
	}

	/**
	 * Mode selector sets the correct agents based on the given GameMode
	 * @param mode the selected GameMode
	 */
	private void selectMode(GameMode mode) {
		switch (mode) {
		case Human -> {
			musketeerAgent = new HumanAgent(board);
			guardAgent = new HumanAgent(board);
		}
		case HumanRandom -> {
			String side = getSideInput();

			// The following statement may look weird, but it's what is known as a ternary statement.
			// Essentially, it sets musketeerAgent equal to a new HumanAgent if the value M is entered,
			// Otherwise, it sets musketeerAgent equal to a new RandomAgent
			musketeerAgent = side.equals("M") ? new HumanAgent(board) : new RandomAgent(board);

			guardAgent = side.equals("G") ? new HumanAgent(board) : new RandomAgent(board);
		}
		case HumanGreedy -> {
			String side = getSideInput();
			musketeerAgent = side.equals("M") ? new HumanAgent(board) : new GreedyAgent(board);
			guardAgent = side.equals("G") ? new HumanAgent(board) : new GreedyAgent(board);
		}
		}
	}

	/**
	 * Runs the game, handling human input for move actions
	 * Handles moves for different agents based on current turn 
	 */
	private void runGame() {
		while(!board.isGameOver()) {
			System.out.println("\n" + board);

			final Agent currentAgent;
			if (board.getTurn() == Piece.Type.MUSKETEER)
				currentAgent = musketeerAgent;
			else
				currentAgent = guardAgent;

            if (currentAgent instanceof HumanAgent) // Human move
                switch (getInputOption()) {
                    case "M":
                        move(currentAgent);
                        this.undone  = false;
                        break;
                    case "U":
                        if (moves.size() == 0) {
                            System.out.println("No moves to undo.");
                            this.undone  = false;
                            continue;
                        }
                        else if (moves.size() == 1 || isHumansPlaying()) {
                            undoMove();
                            this.undone  = true;
                        }
                        else {
                            undoMove();
                            undoMove();
                            this.undone  = true;
                        }
                        break;
                    case "S":
                        board.saveBoard();
                        break;
                    case "R":
                    	if (this.undone) {
                    		redoMove();
                    		this.undone = false;
                    	}
                    	else {
                    		System.out.println("No moves were undone to redo.");
                    		continue;
                    	}
                    	break;
                    case "F":
                    	if (board.getTurn() == Piece.Type.MUSKETEER) {
                    		board.setWinner(Piece.Type.GUARD);
                    		System.out.println(board);
                    		System.out.printf("\n%s won!%n", board.getWinner().getType());
                    	}
                    	else {
                    		board.setWinner(Piece.Type.MUSKETEER);
                    		System.out.println(board);
                    		System.out.printf("\n%s won!%n", board.getWinner().getType());
                    	}
                    	this.showMoves();
                    	System.exit(0);
                }
            else { // Computer move
                System.out.printf("[%s] Calculating move...\n", currentAgent.getClass().getSimpleName());
                move(currentAgent);
            }
        }

        System.out.println(board);
        System.out.printf("\n%s won!%n", board.getWinner().getType());
        this.showMoves();
    }

    /**
     * Gets a move from the given agent, adds a copy of the move using the copy constructor to the moves stack, and
     * does the move on the board.
     * @param agent Agent to get the move from.
     */
    @Override
    public void move(final Agent agent) {
        final Move move = agent.getMove();
        if (this.board.isValidMove(move)) {
        	// Adds to the allMoves array and the moveKind stack
        	this.allMoves.add(move);
        	String turn = this.board.getTurn().getType();
        	String s = turn + " moved: ";
        	this.moveKind.add(s);
        }
        moves.add(new Move(move));
        board.move(move);
    }

	/**
	 * Removes a move from the top of the moves stack and undoes the move on the board.
	 */
	@Override
	public void undoMove() {
    	this.lastMove = moves.remove(moves.size() - 1);
        board.undoMove(lastMove);
        undoneMoves.add(lastMove);
        String turn = this.board.getTurn().getType();
        String s = turn + " undid: ";
        this.moveKind.add(s);
        this.allMoves.add(lastMove);
        System.out.println("Undid the previous move.");
    }
    
    @Override
 public void redoMove() {
    	Move last = undoneMoves.remove(undoneMoves.size()-1);
    	board.redoMove(last);
    	moves.add(last);
    	String turn = this.board.getTurn().getType();
    	String s = turn + " redid: ";
    	this.moveKind.add(s);
    	this.allMoves.add(last);
		  System.out.println("Redid the previous undone move.");
	} 
    
    private void getEndGameMoves() {
    	Iterator<Object> allMovesIterator = new ListIterator(this.allMoves).createIterator();
    	Iterator<Object> moveKindIterator = new StackIterator(this.moveKind).createIterator();
    	String endGameMoves = "";
    	while (allMovesIterator.hasNext() && moveKindIterator.hasNext()) {
    		 String s = allMovesIterator.next().toString();
    		 String x = moveKindIterator.next().toString();
    		 endGameMoves += x + s + "\n";
    	}
    	System.out.println(endGameMoves);
    }
    
    private void showMoves() {
    	System.out.println("\nWould you like to see all moves that were made during this game?\nPlease enter Y/N");
    	while (!scanner.hasNext("[YyNn]")) {
    		System.out.println("Invalid option. Enter \"Y\" or \"N\"");
    		scanner.next();
    	}
    	String choice = scanner.next().toLowerCase();
    	if (choice.equals("y")) {
    		this.getEndGameMoves();
    	}
    }

	/**
	 * Get human input for move action
	 * @return the selected move action, 'M': move, 'U': undo, and 'S': save
	 */
	private String getInputOption() {
		System.out.printf("[%s] Enter 'M' to move, 'U' to undo, 'R' to redo, 'F' to surrender, and 'S' to save: ", board.getTurn().getType());
		while (!scanner.hasNext("[MURFSmurfs]")) {
			System.out.print("Invalid option. Enter 'M', 'U', 'R', 'F', or 'S': ");
			scanner.next();
		}
		return scanner.next().toUpperCase();
	}

	/**
	 * Returns whether both sides are human players
	 * @return True if both sides are Human, False if one of the sides is a computer
	 */
	private boolean isHumansPlaying() {
		return musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent;
	}

	/**
	 * Get human input for side selection
	 * @return the selected Human side for Human vs Computer games,  'M': Musketeer, G': Guard
	 */
	private String getSideInput() {
		System.out.print("Enter 'M' to be a Musketeer or 'G' to be a Guard: ");
		while (!scanner.hasNext("[MGmg]")) {
			System.out.println("Invalid option. Enter 'M' or 'G': ");
			scanner.next();
		}
		return scanner.next().toUpperCase();
	}

	/**
	 * Get human input for selecting the game mode
	 * @return the chosen GameMode
	 */
	private GameMode getModeInput() {
		System.out.println("""
				0: Human vs Human
				1: Human vs Computer (Random)
				2: Human vs Computer (Greedy)""");
		System.out.print("Choose a game mode to play i.e. enter a number: ");
		while (!scanner.hasNextInt()) {
			System.out.print("Invalid option. Enter 0, 1, or 2: ");
			scanner.next();
		}
		final int mode = scanner.nextInt();
		if (mode < 0 || mode > 2) {
			System.out.println("Invalid option.");
			return getModeInput();
		}
		return GameMode.values()[mode];
	}

	/**
	 * Play the background music
	 */
	private void BGM() {
		File directory = new File("music");
  	  	int fileCount = directory.list().length;
  	  	System.out.println(fileCount + " music files are found.");
  	  	System.out.printf("Enter a number (from 1 to " + fileCount 
  	  			+ ") to pick the specified music, or 'R' for random music.");
  	  	String fileNum = new String("[");
  	  	for (int i = 1; i <= fileCount; i++) {
  	  		fileNum += i;
  	  	}
  	  	fileNum += "Rr]";
  	  	while (!scanner.hasNext(fileNum)) {
  	  		System.out.print("Invalid option. "
  	  				+ "Enter a number with range of 1 to " 
  	  				+ fileCount + ", or 'R': ");
  	  		scanner.next();
  	  	}
  	  	String s = scanner.next();
  	  	String path = "music/1.wav";
  	  	if (s.equals("R") || s.equals("r")) {
  	  		Random rand = new Random();
  	  		int n = rand.nextInt(3) + 1;
  	  		path = "music/" + n +".wav";
  	  		System.out.println("BGM" + n + " is playing");
  	  	}
  	  	else {
  	  		path = "music/" + s +".wav";
  	  		System.out.println("BGM" + s + " is playing");
  	  	}
  	  	BackgroundMusic player = BackgroundMusic.getInstance();
  	  	player.playBGM(path);
	}

	/**
	 * Get human input for setting up the game
	 * @return the chosen setup option
	 */
	private static String getSetupInputOption() {
		System.out.printf("Enter 'C' to change the board, 'P' to set the board to the previous one, and 'S' to start the game.");
		while (!scanner.hasNext("[CPScps]")) {
			System.out.print("Invalid option. Enter 'C', 'P', or 'S': ");
			scanner.next();
		}
		return scanner.next().toUpperCase();
	}

	public static void main(String[] args) {
		String boardFileName = "Boards/Starter.txt";
		Context context = new Context("");
		UserInputExpression expression = new UserInputExpression();
		while (!context.getInput().equals("S")) {
			context.setInput(getSetupInputOption());
			boardFileName = expression.interpret(context, boardFileName);
			System.out.println(context.getOutput());
		}
		ThreeMusketeers game = new ThreeMusketeers(boardFileName);
		game.play();
	}
}
