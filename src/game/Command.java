package game;

public interface Command {
	//Commands to execute
	void undoMove();
	void redoMove();
	void move(final Agent agent);
}
