package game;

import java.util.Iterator;
import java.util.List;

public class ListIterator implements MoveIterator{
	List<Object> a;
	
	public ListIterator(List<Object> a) {
		this.a = a;
	}

	@Override
	public Iterator<Object> createIterator() {
		return this.a.iterator();
	}

}
