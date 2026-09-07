package MyLibrary.MyMap00;

import MyLibrary.FnList.*;
import MyLibrary.LnList.*;
import MyLibrary.FnTuple.*;
import MyLibrary.MyMap00.*;
import java.util.function.BiConsumer;
import MyLibrary.LnStrm.*;

import MyLibrary.MyMap00.MyMap00FullExn;
import MyLibrary.MyMap00.MyMap00NoKeyExn;

public class MapOpenAddress<V>
    implements MyMap00<String, V> {
    // HX-2026-04-01:
    // Please give an implementation of hash table
    // based on open addressing. The probing strategy
    // chosen for handling collisions is quadratic probing.
    private FnTupl2<String, V> table[];
    private int capacity; // array length
    private int numElm; // number of elements
    private final String tombstone = "DELETED";

    public MapOpenAddress(int capacity) {
        this.capacity = capacity; // array length
        this.table = new FnTupl2[capacity]; // table length
        this.numElm = 0;
    }

    public MapOpenAddress(int capacity, int numElm){
        this.capacity = capacity + numElm; // array length resized
        this.table = new FnTupl2[this.capacity];
        this.numElm = numElm;
    }

    public int size(){
        return numElm;
    }

    public boolean isEmpty(){
        return numElm == 0;
    }

    public boolean isFull() {
        return numElm == this.capacity;
    }

    private int hash(String key){
        assert(key !=null);
        String lower = key.toLowerCase();
        int hshfn = lower.charAt(0) - 'a';
        return hshfn % capacity;
    }

    private int hash_case_sensitive(String key){
        assert(key !=null);
        int hsh = 0;
        for (int i = 0; i < key.length(); i++){
            hsh = 31 * hsh + key.charAt(i);
        }
        return Math.floorMod(hsh, capacity);
    }

    public void foritm(BiConsumer<? super String, ? super V> work){
        for (int i = 0; i < capacity; i++){
            FnTupl2<String, V> kv = table[i];
            if (kv != null){
                work.accept(kv.sub0, kv.sub1);
            }
        }
    } 

    public V search$opt(String key){
        assert(key != null);
        // int hshidx = hash(key);
        int hshidx = hash_case_sensitive(key);

        for (int i = 0; i < capacity; i++){
            int idx = (hshidx + (i*i)) % capacity; // quadratic probing

            FnTupl2<String, V> kv = table[idx];

            // if not found, return null
            if (kv == null){
                return null;
            }

            // if tombstone found, continue searching
            if (kv.sub0.equals(tombstone)){
                continue;
            }

            // if found, return value
            if (kv.sub0.equals(key)){
                return kv.sub1;
            }
        }
        return null;
    }

    public V search$exn(String key){
        V value = search$opt(key);
        if (value == null){
            throw new MyMap00NoKeyExn();
        }
        return value;
    }

    public V search$old(String key){
        assert(key != null);
        // int hshidx = hash(key);
        int hshidx = hash_case_sensitive(key);

        for (int i = 0; i < capacity; i++){
            int idx = (hshidx + (i*i)) % capacity;

            FnTupl2<String, V> kv = table[idx];

            // if found, return value
            if (kv.sub0.equals(key)){
                return kv.sub1;
            }
            // if tombstone found, continue searching
            if (kv.sub0.equals(tombstone)){
                continue;
            }
        }
        return null;
    }

    public void insert$new(String key, V val){
        assert(key != null);
        // int hshidx = hash(key);
        int hshidx = hash_case_sensitive(key);
        int tombidx = -1; // to remember the first tombstone index

        for (int i = 0; i < capacity; i++){
            int idx = (hshidx + (i*i)) % capacity;

            FnTupl2<String, V> kv = table[idx];

            // if empty slot found, insert new key-value pair
            if (kv == null){
                if (tombidx != -1){
                    table[tombidx] = new FnTupl2<>(key, val);
                    
                } else {
                    table[idx] = new FnTupl2<>(key, val);
                }
                numElm++;
                return;
            }
            if (kv.sub0.equals(tombstone)){
                if (tombidx == -1){
                    tombidx = idx;
                }
                continue;
            }
        }

        throw new MyMap00FullExn();
        
    }

    public V insert$opt(String key, V val){
        assert(key != null);
        // int hshidx = hash(key);
        int hshidx = hash_case_sensitive(key);
        int tombidx = -1; // to remember the first tombstone index
        for (int i = 0; i < capacity; i++){
            int idx = (hshidx + (i*i)) % capacity;

            FnTupl2<String, V> kv = table[idx];

            // if empty slot found, insert new key-value pair
            if (kv == null){
                if (tombidx != -1){
                    table[tombidx] = new FnTupl2<String,V>(key, val);
                } else {
                    table[idx] = new FnTupl2<>(key, val);
                }
                numElm++;
                return null;
            }
            if (kv.sub0.equals(tombstone)){
                if (tombidx == -1){
                    tombidx = idx;
                }
                continue;
            }
            // update value and return old value if key found
            if (kv.sub0.equals(key)){
                V oldVal = kv.sub1;
                kv.sub1 = val;
                return oldVal;
            }
        }

        throw new MyMap00FullExn(); // otherwise, the map is full
    }

    public V remove$opt(String key){
        assert(key!=null);
        V rmval = search$opt(key);
        if (rmval == null){
            return null;
        }

        // remove the kv
        // int hshidx = hash(key);
        int hshidx = hash_case_sensitive(key);
        for (int i = 0; i < capacity; i++){
            int idx = (hshidx + (i*i)) % capacity;

            FnTupl2<String, V> kv = table[idx];
            if (kv !=null && kv.sub0.equals(key)){
                table[idx] = new FnTupl2<String, V>(tombstone, null); // mark as deleted
                numElm--;
                return rmval;
            }
        }
        return rmval;
    }

    public V remove$exn(String key){
        assert(key != null);
        V rmval = remove$opt(key);
        if (rmval == null){
            throw new MyMap00NoKeyExn();
        }
        return rmval;
    }

    public V remove$old(String key){
        assert(key != null);
        V rmval = search$opt(key);
        // int hshidx = hash(key);
        int hshidx = hash_case_sensitive(key);
        for (int i = 0; i < capacity; i++){
            int idx = (hshidx + (i*i)) % capacity;
            FnTupl2<String, V> kv = table[idx];
            if (kv != null && kv.sub0.equals(key)){
                table[idx] = new FnTupl2<String, V>(tombstone, null); // mark as deleted
                numElm--;
                return rmval;
            }
        }
        return rmval;
    }

    public LnStrm<FnTupl2<String, V>> keyval_strmize(){
        LnStrm<FnTupl2<String, V>> res = new LnStrm<>();
        for (int i = capacity-1; i >= 0; i--){
            if (table[i] != null && !table[i].sub0.equals(tombstone)){
                res = LnStrmSUtil.cons0(table[i], res);
            }
        }
        return res;
    }

    // public static void main(String[] args){
    //     MyMap00<String, Integer> SCTest = new MapOpenAddress<>(30);
    //     // 1. init test
    //     System.out.println("SCTest initial state: ");
    //     System.out.println("1. The number of elements: " + SCTest.size());
    //     System.out.println("2. Is the map empty? " + SCTest.isEmpty());
    //     System.out.println("3. Is the map full? " + SCTest.isFull());

    //     System.out.println();
    //     // 2. insert test
    //     // 2.1 new
    //     System.out.println("Insert new: (key, value)");
    //     String[] keys = {"Good", "Better", "Best", "Great", "Awesome"};
    //     for (int i = 0; i < 5; i++){
    //         SCTest.insert$new(keys[i], i);
    //         System.out.println("1. key: " + keys[i]);
    //         System.out.print("2. value: " + i);
    //         System.out.println();
    //     }

    //     // 2.2 replace
    //     System.out.println("Insert existing: (key, new value)");
    //     String[] newKeys = {"Good", "Better", "Best", "Great", "Awesome"};
    //     for (int i = 0; i < 5; i++){
    //         SCTest.insert$opt(newKeys[i], i + 5);
    //         System.out.println("1. key: " + newKeys[i]);
    //         System.out.print("2. new value: " + (i + 5));
    //         System.out.println();
    //     }

    //     // 3. search
    //     System.out.println("Search: (key)");
    //     for (String key : keys) {
    //         Integer value = SCTest.search$opt(key);
    //         System.out.println("1. key: " + key);
    //         System.out.print("2. value: " + value);
    //         System.out.println();
    //     }
    //     // try {
    //     //     System.out.println("search$exn(\"nonexnKey\") = " + SCTest.search$exn("nonexnKey"));
    //     // } catch (MyMap00NoKeyExn e) {
    //     //     System.out.println("search$exn(\"nonexnKey\") threw MyMap00NoKeyExn as expected");
    //     // }

    //     // 4. keyval_strmize
    //     System.out.println("keyval_strmize:");
    //     LnStrm<FnTupl2<String, Integer>> kvStream = SCTest.keyval_strmize();
    //     kvStream.foritm0(
    //         kv -> {
    //             System.out.print("{ " +kv.sub0 + ": " + kv.sub1 + " }" + " -> ");
    //         }
    //     );

    //     // 5. foritm
    //     System.out.println("\n=== foritm ===");
    //     SCTest.foritm((k, v) -> System.out.println(k + " => " + v));

    //     System.out.println();

    //     // 6. remove
    //     System.out.println("remove");

    //     Integer rm1 = SCTest.remove$old("Good");
    //     System.out.println("1. removed key: Good");
    //     System.out.println("2. removed value: " + rm1);
    //     System.out.println("3. The number of elements: " + SCTest.size());
    //     System.out.println("4. Is the map empty? " + SCTest.isEmpty());
    //     System.out.println("5. Is the map full? " + SCTest.isFull());
    // }

}
