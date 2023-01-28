package game;

public class Context {
	
	String input;
	String output;
	
	public Context(String input) {
		this.input = input;
		this.output = "";
	}
	
	public String getInput(){
		return this.input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getOutput() {
		return this.output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
}
