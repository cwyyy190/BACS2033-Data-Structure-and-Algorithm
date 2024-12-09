package ADT;

import java.util.Iterator;

public class ArrayList<T> implements ListInterface<T> {

    //array attributes
    private T[] array;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 5;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int startingCapacity) {
        numberOfEntries = 0;
        array = (T[]) new Object[startingCapacity]; 
    }

    @Override
    public boolean add(T newEntry) {
        if (isFull()) {
            expandArray();
        }
        array[numberOfEntries] = newEntry;
        numberOfEntries++;
        return true;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        boolean isSuccessful = true;

        if (isFull()) {
            expandArray();
        }

        if ((newPosition >= 1) && (newPosition <= numberOfEntries + 1)) {
            makeRoom(newPosition);
            array[newPosition - 1] = newEntry;
            numberOfEntries++;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    @Override
    public T remove(int givenPosition) {
        T result = null;

        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            result = array[givenPosition - 1];

            if (givenPosition < numberOfEntries) {
                removeGap(givenPosition);
            }

            numberOfEntries--;
        }
        return result;
    }

    @Override
    public void clear() {
        numberOfEntries = 0;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        boolean isSuccessful = true;

        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            array[givenPosition - 1] = newEntry;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    @Override
    public T getEntry(T obj) {
        T result = null;
        
        int i = 0;
        for (i = 0; i < numberOfEntries; i++) {
            if (array[i].equals(obj)) {
                result = array[i];
                break;
            }
        }
        return result;
    }

    @Override
    public T getEntry(int givenPosition) {
        T result = null;

        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            result = array[givenPosition - 1];
        }
        return result;
    }
    
    @Override
    public int getPosition(T obj){
        int position=0;
        
        for(int idx = 0; idx< numberOfEntries; idx++){
            if(obj.equals(array[idx])){
                position = idx+1;
                break;
            }
        }
        return position;
    }

    @Override
    public boolean contains(T anEntry) {
        boolean found = false;
        for (int index = 0; !found && (index < numberOfEntries); index++) {
            if (anEntry.equals(array[index])) {
                found = true;
            }
        }
        return found;
    }

    @Override
     public boolean compareTo(T obj) {
        boolean result = false;
        
        int i = 0;
        for (i = 0; i < numberOfEntries; i++) {
            if (array[i].hashCode() == obj.hashCode()) {
                result = true;
                break;
            }
        }
        return result;
    }
     
    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        return numberOfEntries == array.length;
    }
  
    @Override
    public String toString() {
        String outputStr = "";
        for (int index = 0; index < numberOfEntries; ++index) {
            outputStr += array[index] + "\n";
        }

        return outputStr;
    }

    private void makeRoom(int newPosition) {
        int newIndex = newPosition - 1;
        int lastIndex = numberOfEntries - 1;

        // move each entry to next higher index, starting at end of
        // array and continuing until the entry at newIndex is moved
        for (int index = lastIndex; index >= newIndex; index--) {
            array[index + 1] = array[index];
        }
    }

    private void removeGap(int givenPosition) {
        // move each entry to next lower position starting at entry after the
        // one removed and continuing until end of array
        int removedIndex = givenPosition - 1;
        int lastIndex = numberOfEntries - 1;

        for (int index = removedIndex; index < lastIndex; index++) {
            array[index] = array[index + 1];
        }
    }

    private void expandArray() {
        //STEP 1: copy current array to temporary array
        T[] temparray = array;

        //STEP 2: expand the current srray (usually make it twice bigger)
        //HINT: CHECK THE CONSTRUCTOR
        array = (T[]) new Object[array.length * 2];

        //STEP3: copy all member in temporary array to new array
        for (int idx = 0; idx < temparray.length; idx++) {
            array[idx] = temparray[idx];
        }
    }
}
