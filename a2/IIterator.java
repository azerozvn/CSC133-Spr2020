package com.mycompany.a2;

public interface IIterator<T> {
	public T getNext();
	public void remove();
	public boolean hasNext();
}
