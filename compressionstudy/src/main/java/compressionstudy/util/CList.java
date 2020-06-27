
package compressionstudy.util;

/**
 *
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
    
    public Type get(int index) {
        if (index >= items) {
            throw new NullPointerException();
        }
        return (Type) list[index];
    }

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
        if (size < 2000) {
            size = items + 100;
        } else {
            size = items + 500;
        }
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
    
    public void remove(int index) {
        if (index >= items || index < 0) {
            System.out.println("List class: Remove error: Index out of bounds");
        }
        list[index] = null;
        items--;
        for (int i = index; i < items; i++) {
            list[i] = list[i + 1];
        }
        
        if (size - items > 1000) {
            cut();
        }
    }
    
    public int size() {
        return items;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }

}
