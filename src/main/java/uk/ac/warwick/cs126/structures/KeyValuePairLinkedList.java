package uk.ac.warwick.cs126.structures;

/** Class for Linked List with a key value pair. */
public class KeyValuePairLinkedList<K extends Comparable<K>,V> {

    protected ListElement<KeyValuePair<K,V>> head;
    protected int size;
    
    /** Initialise Variables. */
    public KeyValuePairLinkedList() {
        head = null;
        size = 0;
    }
    
    /** Add to the Linked list
     * 
     * @param key                   Key for the pair.
     * @param value                 Actual element to add.
    */
    public void add(K key, V value) {
        this.add(new KeyValuePair<K,V>(key,value));
    }

    /** Adding the pair. 
     * 
     * @param kvp                   Whole key value pair type.
    */
    public void add(KeyValuePair<K,V> kvp) {
        ListElement<KeyValuePair<K,V>> new_element = 
                new ListElement<>(kvp);
        new_element.setNext(head);
        head = new_element;
        size++;
    }
    
    /** @return size of linked list. */
    public int size() {
        return size;
    }
    
    /** @return List element of the head. */
    public ListElement<KeyValuePair<K,V>> getHead() {
        return head;
    }
    
    /** Get the KeyValuePair with Key.
     * 
     * @param key                   Key for the Pair.
     * 
     * @return The key value pair with matching key. 
    */
    public KeyValuePair<K,V> get(K key) {
        ListElement<KeyValuePair<K,V>> temp = head;
        
        while(temp != null) {
            if(temp.getValue().getKey().equals(key)) {
                return temp.getValue();
            }
            
            temp = temp.getNext();
        }
        
        return null;
    }

    /** Remove element with key.
     * 
     * @param key                   Key for Pair.
     * 
     * Ensures that when element is removed, a new linked is created between the two with a gap between them.
     * 
     * @return True or False for if the Pair is succesfully removed or not. 
    */
    public boolean remove(K key){
        V x = null;
        if(size==0){
            return false;
        }else if (size == 1 && head.getValue().getKey().equals(key)){
            head = null;
            return true;
        }else{
            ListElement<KeyValuePair<K,V>> current = head;
            ListElement<KeyValuePair<K,V>> next = head.getNext();

            while(next!=null){
                if(next.getValue().getKey().equals(key)){
                    current.setNext(next.getNext());
                    break;
                }
                current = next;
                next = next.getNext();
            }
            return true;
        }
    }
}
