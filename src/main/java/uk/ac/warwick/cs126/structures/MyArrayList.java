package uk.ac.warwick.cs126.structures;

/** Class for implementing MyArrayList and operations related. */
public class MyArrayList<E> {

    private Object[] array;
    private int size;
    private int capacity;
    /** Initialise variables. */
    public MyArrayList() {
        this.capacity = 1000;
        this.array = new Object[capacity];
        this.size = 0;
    }

    /** Attempts to add an element to the array. 
     * 
     * @param element                   Generic element of whatever is inputted.
     * 
     * Doubles the array size when reached capacity.
     * 
     * @return True or False for if the element is successfully added or not.  
    */
    public boolean add(E element) {
        try {
            if (this.size >= this.capacity) {
                this.capacity *= 2;
                Object[] temp = new Object[capacity];
                System.arraycopy(this.array, 0, temp, 0, this.size);
                this.array = temp;
            }

            array[this.size++] = element;

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Sees if element is in the array. 
     * 
     * @param element                   Generic element to be searched for. 
     * 
     * @return True or False for if the element is contained within the array or not. 
    */
    public boolean contains(E element) {
        for (int i = 0; i < this.size(); i++) {
            if (element.equals(this.get(i))) {
                return true;
            }
        }
        return false;
    }

    /** Clears the array. 
     * Creates new array and sets array to that. 
    */
    public void clear() {
        this.capacity = 1000;
        this.array = new Object[capacity];
        this.size = 0;
    }

    /** Checks if the array is empty.
     * @return True or False for if the array is Empty or not. 
    */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**@return Size of array. */
    public int size() {
        return this.size;
    }

    /** Allows casting object to type E without any warnings. */
    @SuppressWarnings("unchecked")
    /** Gets the element at the given index. 
     * 
     * @param index                         Given index to find element at.
     * 
     * @return Element at given index.
    */
    public E get(int index) {
        return (E) this.array[index];
    }

    /** Gets the index of a given element.
     * 
     * @param element                   Element that the index is to be found of.
     * 
     * @return Index of the element if it exist, otherwise -1. 
    */
    public int indexOf(E element) {
        for (int i = 0; i < this.size(); i++) {
            if (element.equals(this.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /** Attempts to remove a given element from the array.
     * 
     * @param element                   Element that is trying to be removed.
     * 
     * Shifts all elements down if the element is successfully removed.
     * 
     * @return True or Flase for if the element is removed or not. 
    */
    public boolean remove(E element) {
        int index = this.indexOf(element);
        if (index >= 0) {
            for (int i = index + 1; i < this.size; i++) {
                this.set(i - 1, this.get(i));
            }
            this.array[this.size - 1] = null;
            this.size--;
            return true;
        }
        return false;
    }

    /** Sets the value at a given index to a given element value. 
     * 
     * @param index                     Given index to change the value of.
     * @param element                   New value for the given index.
     * 
     * Replaces element at given index with given element.
     * 
     * @return Element from the given index that is replaced. 
    */
    public E set(int index, E element) {
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("index > size: " + index + " >= " + this.size);
        }
        E replaced = this.get(index);
        this.array[index] = element;
        return replaced;
    }

    /**@return String representation of the elements inside the array. */
    public String toString() {
        if (this.isEmpty()) {
            return "Empty";
        }

        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < this.size; i++) {
            ret.append("Index: " + i + "    Element: " + this.get(i) + "\n");
        }

        ret.deleteCharAt(ret.length() - 1);

        return ret.toString();
    }
}
