
package compressionstudy.util;

/**
 * Custom List implementation, basically same as Java's ArrayList
 * @author Arttu Kangas
 * @param <Type>
 */
public class CList<Type> {
    
    int size;
    int items;
    Object [] list;
    
    public CList() {
        items = 0;
        size = 100000;
        list = new Object[size];
    }
    
    /**
     * Get item from index
     * @param index Index to get item from
     * @return Item at index
     */
    public Type get(int index) {
        if (index >= items) {
            throw new NullPointerException();
        }
        return (Type) list[index];
    }

    /**
     * Add item to end of list
     * @param item Item to add
     */
    public void add(Type item) {
        if (items == size) {
            extend();
        }
        
        list[items] = item;
        items++;
    }
    
    private void extend() {
        int growth = 100000;
        size += growth;
        renew();
    }
    
    private void renew() {
        Object [] newlist = new Object[size];
        for (int i = 0; i < items; i++) {
            newlist[i] = list[i];
        }
        list = newlist;
    }
    
    private void cut() {
        size = items + 1000;
        renew();
    }
    
    /**
     * Replaces the object at index to a new object
     * @param index Index where you want to put new object to
     * @param object New object
     */
    public void replace(int index, Type object) {
        list[index] = object;
    }
    
    /**
     * Removes the item from index and puts the remaining list together
     * @param index Index where item to be removed is
     */
    public void remove(int index) {
        list[index] = null;
        items--;
        for (int i = index; i < items; i++) {
            list[i] = list[i + 1];
        }
        
        if (size - items > 20000) {
            cut();
        }
    }
    
    public int size() {
        return items;
    }

}
