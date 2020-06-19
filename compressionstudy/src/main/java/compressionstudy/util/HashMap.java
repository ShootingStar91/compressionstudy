
package compressionstudy.util;

/**
 *
 * @author Arttu Kangas
 */
public class HashMap<KEYTYPE, VALUETYPE> {
    
    int maxSize = 65536;
    CList<Item> content;
    CList<KEYTYPE> keys;
    int items;
    
    public HashMap() {
        content = new CList<>();
        for (int i = 0; i < maxSize; i++) {
            content.add(null);
        }
        keys = new CList<>();
    }

    private class Item {
        KEYTYPE key;
        VALUETYPE value;
        Item next;
        
        public Item(KEYTYPE key, VALUETYPE value, Item next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        public void setNext(Item next) {
            this.next = next;
        }
        public Item getNext() {
            return next;
        }
        public KEYTYPE getKey() {
            return key;
        }
        public VALUETYPE getValue() {
            return value;
        }
    }
    
    public void put(KEYTYPE key, VALUETYPE value) {
        Item item = new Item(key, value, null);
        int index = getIndex(getHash(key));
        Item oldItem = content.get(index);
        if (oldItem == null) {
            content.replace(index, item);
            keys.add(key);
            items++;
        } else {
            // collision
            while (true) {
                if (key.equals(oldItem.key)) {
                    oldItem.value = value;
                    break;
                }
                if (oldItem.next == null) {
                    oldItem.next = item;
                    keys.add(key);
                    items++;
                    break;
                }
                oldItem = oldItem.next;
            }
        }
    }
    
    public VALUETYPE get(KEYTYPE key) {
        Item item = content.get(getIndex(getHash(key)));
        if (item == null) {
            return null;
        }
        if (item.next == null) {
            return item.getValue();
        }
        while (true) {
            if (item.key.equals(key)) {
                return item.getValue();
            }
            item = item.next;
        }
    }
    
    private int getIndex(int h) {
        return h & (maxSize - 1);
    }
    
    private int getHash(KEYTYPE key) {
        return key.hashCode();
    }
    
    public boolean containsKey(KEYTYPE key) {
        Item item = content.get(getIndex(getHash(key)));
        if (item == null) {
            return false;
        }
        while (true) {
            if (item.key.equals(key)) {
                return true;
            }
            if (item.next == null) {
                return false;
            }
            item = item.next;
        }
    }
    
    public CList<KEYTYPE> keySet() {
        return keys;
    }
    
}
