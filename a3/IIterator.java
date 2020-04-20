package com.mycompany.a3;

public interface IIterator<T> {
	public T getNext();
	public void remove();
	public boolean hasNext();
}
