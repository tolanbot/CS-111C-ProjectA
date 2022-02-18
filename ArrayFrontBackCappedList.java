import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayFrontBackCappedList<T> implements FrontBackCappedListInterface<T> {
    private T[] list; //tab
    private int numberOfElements;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    private int initialCapacity;
    private int index;


    @SuppressWarnings("unchecked")
    public ArrayFrontBackCappedList(int initialCapacity) {
        if(initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0: " + initialCapacity);
        }
        if(initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        T[] temp = (T[]) new Object[initialCapacity];
        list = temp;
        this.initialCapacity = initialCapacity;
        numberOfElements = 0;
    }

    public ArrayFrontBackCappedList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }


    public String toString() {
        String nonNulls = "[" + Joiner.on(", ").skipNulls().join(list) + "]";
        return "size=" + numberOfElements +
                "; capacity=" + list.length +
                ";\t" + nonNulls;
    }

    public boolean isEmpty() {
        return numberOfElements == 0;
    }

    public boolean isFull() {
        return numberOfElements >= initialCapacity;
    }

    public int size() {
        return numberOfElements;
    }

    public T getEntry(int index) {
        try {
            if (index < 0 || index > numberOfElements)
                return null;
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return list[index];
    }

    public void clear() {
        while(!isEmpty()){
            removeBack();
        }
    }

    public boolean addBack(T t) {
        if (isFull())
            return false;

        list[numberOfElements++] = t;
        return true;
    }

    public boolean addFront(T t) {
        if(isFull())
            return false;

        if(numberOfElements++ > 0) {
            for(int i = numberOfElements-2; i >= 0; i--) {
                list[i + 1] = list[i];
            }
        }
        list[0] = t;
        return true;
    }


    public T removeFront() {
        if(isEmpty())
            return null;
        int size = (numberOfElements--)-1;
        T t = list[0];
        for(int i =0;i < size; i++) {
            list[i] = list[i+1];
        }
        list[size] = null;
        return t;
    }

    public T removeBack() {
        if(isEmpty())
            return null;

        int lastIndex = (numberOfElements--) - 1;
        T t = list[lastIndex];
        list[lastIndex] = null;
        return t;
    }

    public boolean contains (T t) {
        return indexOf(t) >= 0;
    }

    public int indexOf(T anEntry) {

        for (int i = 0; i < numberOfElements; i++) {
            if (anEntry.equals(list[i])) {
                return i;
            }
        }return -1;
    }

    public int lastIndexOf(T t) {
        int index = -1;
        for(int i = 0; i < numberOfElements; i++) {
            if(list[i].equals(t)) {
                index = i;
            }
        }return index;
    }

}
