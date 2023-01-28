package game;

public class PreviousBoard implements PreviousBoardToCareTaker, PreviousBoardToUserInputExpression {

	String boardName;
	
	public PreviousBoard(String boardName) {
		this.boardName = boardName;
	}
	
	@Override
	public String getBoard() {
		return this.boardName;
	}

}
