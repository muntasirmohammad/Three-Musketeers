package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInputExpression {
	
	private ArrayList<PreviousBoardToCareTaker> previousBoards = new ArrayList<>();
	
	public UserInputExpression() {
		super();
	}
	
	public String interpret(Context context, String currBoardName) {
		String boardName = currBoardName;
		if (context.getInput().equals(changeBoard())) {
			String newBoardName = getBoardName();
			if (newBoardName.equals("")) {
				context.setOutput("No such board exists. Using board: " + boardName);
			}
			else {
				boardName = newBoardName;
				previousBoards.add(backupPreviousBoard(currBoardName));
				context.setOutput("Using new board: " + boardName);
			}
		}
		else if (context.getInput().equals(previousBoard())) {
			if (!previousBoards.isEmpty()){
				boardName = restorePreviousBoard(previousBoards.remove(previousBoards.size()-1));
				context.setOutput("Using the previous board: " + boardName);
			}
			else {
				context.setOutput("There is no previous board. Using: " + boardName);
			}
		}
		else if (context.getInput().equals(startGame())) {
			context.setOutput("Starting Game");
		}
		return boardName;
	}
	
	public String changeBoard() {
		return "C";
	}
	
	public String previousBoard() {
		return "P";
	}
	
	public String startGame() {
		return "S";
	}
	
	private PreviousBoardToCareTaker backupPreviousBoard(String currentBoard) {
    	PreviousBoard prevBoard = new PreviousBoard(currentBoard);
    	return (PreviousBoardToCareTaker) prevBoard;
    }
    
    private String restorePreviousBoard(PreviousBoardToCareTaker memento) {
    	return ((PreviousBoardToUserInputExpression) memento).getBoard();
    }
	
	/**
	 * Asks the user for what board they would like to play on
	 * @return the name of the file path to the board chosen by the user
	 */
	private String getBoardName() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the name of the board you would like to play on.");
		while (!scan.hasNext(".*\\.txt")) {
		    System.out.println("Not valid board name. Try Again.");
		    scan.next();
		}
		String boardName = scan.next();

		File boardsFolder = new File("Boards/");
		for (File boardFile : boardsFolder.listFiles()) {
			if (boardFile.getName().equals(boardName)) {
				return "Boards/" + boardName;
			}
		}
		return "";
	}
	
}
