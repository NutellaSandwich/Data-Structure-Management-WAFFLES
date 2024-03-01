package uk.ac.warwick.cs126.structures;

/** Class for implementing MySet and operations related. */
public class MySet<E>{
	private MyArrayList<E> array = new MyArrayList<>();

	/** Attempts to add an element to the array. 
     * 
     * @param element                   Generic element of whatever is inputted.
     * 
     * @return True or False for if the element is successfully added or not.  
    */
	public boolean add(E element){
		if(array.contains(element)==false){
			return(array.add(element));
		}
		return false;
	}
    /**@return String representation of the elements inside the array. */
	public String toString(){
		return (array.toString());
	}

	/** Clears the array. 
     * Creates new array and sets array to that. 
    */
	public void clear(){
		array.clear();
	}

	/** Sees if element is in the array. 
     * 
     * @param element                   Generic element to be searched for. 
     * 
     * @return True or False for if the element is contained within the array or not. 
    */
	public boolean contains(E element){
		return array.contains(element);
	}

	/** Checks if the array is empty.
     * @return True or False for if the array is Empty or not. 
    */
	public boolean isEmpty(){
		return array.isEmpty();
	}

	/** Attempts to remove a given element from the array.
     * 
     * @param element                   Element that is trying to be removed.
     * 
     * Shifts all elements down if the element is successfully removed.
     * 
     * @return True or Flase for if the element is removed or not. 
    */
	public boolean remove(E element){
		return array.remove(element);
	}

	/**@return Size of array. */
	public int size(){
		return array.size();
	}

	/** Gets the element at the given index. 
     * 
     * @param index                         Given index to find element at.
     * 
     * @return Element at given index.
    */
	public E get(int index){
		return array.get(index);
	}

	/** Performs Instersection between two sets.
	 * 
	 * @param e1 					First Set inputted. 
	 * @param e2 					Second Set inputted. 
	 * 
	 * Updates e1 to only contain elements that are in both sets. 
	 * 
	 * @return Set containing elements in both e1 and e2. 
	 */
	public MySet<E> intersection(MySet<E> e1, MySet<E> e2){
		int e1Size = e1.size();
		for(int i=0;i<e1Size;i++){
			if(e2.contains(e1.get(i))==false){
				e1.remove(e1.get(i));
				i--;
				e1Size--;
			}
		}
		return e1;

	}

	/** Performs Difference between two sets.
	 * 
	 * @param e1 					First Set inputted. 
	 * @param e2 					Second Set inputted. 
	 * 
	 * Only adds to new set the terms in e1 but not in e2 
	 * 
	 * @return Set containg difference between e1 and e2.  
	 */
	public MySet<E> difference(MySet<E> e1, MySet<E> e2){
		MySet <E> eA = new MySet<>();
		int e1Size = e1.size();
		for(int i=0;i<e1.size();i++){
			eA.add(e1.get(i));
		}
		for(int i=0;i<e1Size;i++){
			if(e2.contains(eA.get(i))==true){
				eA.remove(eA.get(i));
				i--;
				e1Size--;
			}
		}
		return eA;
	}

	/** Performs Symmetric Difference between two sets.
	 * 
	 * @param e1 					First Set inputted. 
	 * @param e2 					Second Set inputted. 
	 * 
	 * Only adds to new set the terms in e1 but not in e2
	 * Or the terms in e2 but not in e1.  
	 * 
	 * @return Set containg Symmetric difference between e1 and e2.  
	 */
	public MySet<E> symDifference(MySet<E> e1, MySet<E> e2){
		MySet <E> eA = new MySet<>();
		MySet <E> eB = new MySet<>();
		eA = difference(e1,e2);
		eB = difference(e2,e1);
		for(int i=0;i<eB.size();i++){
			eA.add(eB.get(i));
		}

		return eA;
	}

}