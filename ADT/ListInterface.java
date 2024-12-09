package ADT;

import java.util.Iterator;

public interface ListInterface<T> {

    public boolean add(T newEntry);

    public boolean add(int newPosition, T newEntry);

    public T remove(int givenPosition);

    public void clear();

    public boolean replace(int givenPosition, T newEntry);

    public T getEntry(T obj);

    public T getEntry(int givenPosition);

    public int getPosition(T obj);

    public boolean contains(T anEntry);

    public boolean compareTo(T obj);

    public int getNumberOfEntries();

    public boolean isEmpty();

    public boolean isFull();

}
