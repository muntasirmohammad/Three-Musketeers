package game;

import java.util.Iterator;
import java.util.Stack;

public class StackIterator implements MoveIterator{
	Stack<Object> s;
	
	public StackIterator(Stack<Object> s) {
		this.s = s;
	}

	@Override
	public Iterator<Object> createIterator() {
		return this.s.iterator();
	}

}
