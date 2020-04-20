package com.mycompany.a3;

public interface ICollection {
	public void add(GameObject obj);
	public IIterator<?> getIterator();
}
