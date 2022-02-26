package ProjectA;

import java.util.*;

public class ListFrontCappedBackList<T> implements FrontBackCappedListInterface<T> {
    private int capacity;
    private int size;
    private int numEle;
    private final static int DEFAULT_INITIAL_CAPACITY = 10;
    private List<T> list;
    protected int modCount = 0;

    private final List<T> EMPTY_ELEMENTLIST = new ArrayList<>();
    private final List<T> DEFAULTCAPACITY_EMPTY_LIST = new ArrayList<>(10);


//    public ListFrontBackCappedList() {
//        this(DEFAULT_INITIAL_CAPACITY);
//    }

    public ListFrontCappedBackList(int capacity) {
        if (capacity > 0) {
            this.list = new ArrayList<>(capacity);
        } else if (capacity == 0) {
            this.list = new ArrayList<>(0);
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    capacity);
        }
        this.capacity = capacity;
    }

    public ListFrontCappedBackList() {
        list = DEFAULTCAPACITY_EMPTY_LIST;
    }

    public ListFrontCappedBackList(Collection<? extends T> c) {
        @SuppressWarnings("unchecked")
        List<T> a = List.copyOf(c);
        if ((size = c.size()) != 0) {
            if (c.getClass() == ArrayList.class) {
                list = a;
            } else {
                list = List.copyOf(a);
            }
        } else {
            list = EMPTY_ELEMENTLIST;
        }
    }

    public void trimToSize() {
        List<T> temp = new ArrayList<>();
        modCount++;
        if(size < capacity) {
            list = (size == 0) ? EMPTY_ELEMENTLIST:
                    list.stream()
                            .filter(Objects::nonNull)
                            .toList();
        }
    }


    @Override
    public String toString () {
        String nonNulls = "[" + Joiner.on(", ").skipNulls().join(list) + "]";
        return "size=" + size +
                "; capacity=" + capacity +
                ";\t" + nonNulls;
    }

    @Override
    public boolean addFront (T newEntry){
        if (isFull())
            return false;

        list.add(size - size++, newEntry);
        return size == list.size();
    }


    @Override
    public boolean addBack (T t){
        if (isFull())
            return false;

        list.add(size++, t);
        return true;
    }

    @Override
    public T removeFront () {
        if (isEmpty())
            return null;

        T t = list.get(size - size--);
        list.remove(t);
        return t;
    }

    @Override
    public T removeBack () {
        if (isEmpty())
            return null;

        T t = list.get(size - 1);
        list.remove(t);
        size--;
        return t;
    }

//        @Override
//        public int indexOf (T t){
//            return IntStream.range(0, size)
//                    .filter(i -> t.equals(list.get(i)))
//                    .findFirst()
//                    .orElse(-1);
//
////                .mapToObj(String::valueOf)
////                .collect(Collectors.joining(","));
//        }

    @Override
    public int lastIndexOf (T t){

        ListIterator<T> iterator = list.listIterator(size);

        int i = size - 1;

        while (iterator.hasPrevious() && i >= 0) {
            if (list.get(i).equals(t)) {
                return i;
            }
            i--;
        }
        return -1;
    }


    @Override
    public void clear () {
        modCount++;
        list.removeAll(list);
        size = 0;
    }


    @Override
    public T getEntry ( int position){
        if (position < 0 || position >= size) {
            return null;
        }

        return list.get(position);
    }


//        @Override
//        public boolean contains (T anEntry){
//            return list.contains(anEntry);
//        }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    int indexOfRange(Object o, int start, int end) {
        ArrayList<T> temp = (ArrayList<T>) list;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (temp.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(temp.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int size () {
        return size;
    }


    @Override
    public boolean isEmpty () {
        return size == 0;
    }


    @Override
    public boolean isFull () {
        return size == capacity;
    }

    public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        //preconditions already checked
        //try to double, soft memory check
        int prefLength = oldLength + Math.max(minGrowth, prefGrowth);
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            return checkMemory(oldLength, minGrowth);
        }
    }

    private static int checkMemory(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
            return SOFT_MAX_ARRAY_LENGTH;
        } else {
            return minLength;
        }
    }
}