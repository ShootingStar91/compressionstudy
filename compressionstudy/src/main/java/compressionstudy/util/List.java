
package compressionstudy.util;

/**
 *
 * @author Arttu Kangas
 * @param <Type>
 */
public class List<Type> {
    
    int size;
    int items;
    Type [] list;
    
    public List() {
        items = 0;
        size = 10;
        list = (Type[]) new Object[size];
    }
    
    public Type get(int index) {
        if (index >= items) {
            throw new NullPointerException();
        }
        return list[index];
    }

    public void add(Type item) {
        if (items == size) {
            extend();
        }
        
        list[items] = item;
        items++;
    }
    
    private void extend() {
        int growth;
        if (size < 100) {
            growth = 10;
        } else if (size < 1000) {
            growth = 100;
        } else {
            growth = 1000;
        }
        size += growth;
        renew();
    }
    
    private void renew() {
        Type [] newlist = (Type[]) new Object[size];
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
    
    public void remove(int index) {
        if (index >= items || index < 0) {
            System.out.println("List class: Remove error: Index out of bounds");
        }
        list[index] = null;
        items--;
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
