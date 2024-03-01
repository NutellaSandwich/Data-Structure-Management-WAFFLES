package uk.ac.warwick.cs126.structures;
/** Class for ListElement of a LinkedList. */
public class ListElement<E>{
	private final E value;
	private ListElement<E> next;
	private ListElement<E> prev;

	/** Initialise Variables. */
	public ListElement(E value){
		this.value = value;
	}

	/** Gets the value stored in the element.
	 * @return The element stored. 
    */
	public E getValue(){
		return this.value;
	}

	/** Gets the next element along. */
	public ListElement<E> getNext(){
		return this.next;
	}

	/** Gets the previous element along. */
	public ListElement<E> getPrev(){
		return this.prev;
	}

	/** Sets the value of the next element.
	 * @param e 					The ListElement value to be set. 
	*/
	public void setNext(ListElement<E> e){
		this.next = e;
	}

	/** Sets the value of the previous element.
	 * @param e 					The ListElement value to be set. 
	*/
	public void setPrev(ListElement<E> e){
		this.prev = e;
	}
}