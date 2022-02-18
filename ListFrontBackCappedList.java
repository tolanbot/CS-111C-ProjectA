import com.google.common.base.Joiner;

import java.util.*;
import java.util.stream.IntStream;

public class ListFrontBackCappedList<T> implements FrontBackCappedListInterface<T> {
    private final int capacity;
    private int size = 0;
    private final static int DEFAULT_INITIAL_CAPACITY = 10;
    private final List<T> list;
    protected int modCount = 0;


    public ListFrontBackCappedList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ListFrontBackCappedList(int capacity) {
        if (capacity > 0) {
            list = new ArrayList<>(capacity);
        } else if (capacity == 0) {
            list = new ArrayList<>(0);
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    capacity);
        }this.capacity = capacity;
    }

    @Override
    public String toString() {
        String nonNulls = "[" + Joiner.on(", ").skipNulls().join(list) + "]";
        return "size=" + size +
                "; capacity=" + capacity +
                ";\t" + nonNulls;
    }

    @Override
    public boolean addFront(T newEntry) {
        if(isFull())
            return false;

        list.add(size-size++,newEntry);
        return size == list.size();
    }


    @Override
    public boolean addBack(T t) {
        if(isFull())
            return false;

        list.add(size++,t);
        return true;
    }

    @Override
    public T removeFront() {
        if(isEmpty())
            return null;

        T t = list.get(size-size--);
        list.remove(t);
        return t;
    }

    @Override
    public T removeBack() {
        if(isEmpty())
            return null;

        T t = list.get(size-1);
        list.remove(t);
        size--;
        return t;
    }

    @Override
    public int indexOf(T t) {
        return IntStream.range(0, size)
                .filter(i -> t.equals(list.get(i)))
                .findFirst()
                .orElse(-1);

//                .mapToObj(String::valueOf)
//                .collect(Collectors.joining(","));
    }

    @Override
    public int lastIndexOf(T t) {

        ListIterator<T> iterator = list.listIterator(size);

        int i = size-1;

        while (iterator.hasPrevious() && i >= 0) {
            if(list.get(i).equals(t)){
                return i;
            }i--;
        }return -1;
    }


    @Override
    public void clear() {
        modCount++;
        list.removeAll(list);
        size = 0;
    }


    @Override
    public T getEntry(int position) {
        if (position < 0 || position >= size) {
            return null;
        }

        return list.get(position);
    }


    @Override
    public boolean contains(T anEntry) {
        return list.contains(anEntry);
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() { return size == 0; }


    @Override
    public boolean isFull() { return size == capacity; }
}
