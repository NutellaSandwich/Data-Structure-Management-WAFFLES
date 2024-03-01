package uk.ac.warwick.cs126.structures;

/** Class for implementing KeyValuePair for the Linked List type for the HashMap. */
public class KeyValuePair<K extends Comparable<K>,V> implements Comparable<KeyValuePair<K,V>> {

    protected K key;
    protected V value;
    
    /** Initialise Variables. */
    public KeyValuePair(K k, V v) {
        key = k;
        value = v;
    }
    
    /** @return the value of the key in the pair. */
    public K getKey() {
        return key;
    }
    
    /** @return Element stored in pair. */
    public V getValue() {
        return value;
    }

    /** Compares to Separate KeyValuePair.
     * @param o                     KeyValuePair to compare too.
     * 
     * @return comparison value. 
    */
    public int compareTo(KeyValuePair<K,V> o) {
        return o.getKey().compareTo(this.getKey());
    }
}