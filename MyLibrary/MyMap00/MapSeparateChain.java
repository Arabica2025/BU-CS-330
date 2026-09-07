package MyLibrary.MyMap00;

import MyLibrary.LnList.*;
import MyLibrary.FnTuple.*;
import MyLibrary.MyMap00.*;
import MyLibrary.LnStrm.*;

import java.util.function.BiConsumer;

public class MapSeparateChain<V>
    implements MyMap00<String, V> {
    // HX-2026-04-01:
    // Please give an implementation of hash table
    // that uses separate chaining for handling collisions.
    private LnList<FnTupl2<String, V>>[] table;
    private int bucketsize;
    public String key;
    public V value;

    // constructor
    public MapSeparateChain(int arrlength){
        table = (LnList<FnTupl2<String, V>>[])new LnList[arrlength]; // lazy initialization
        this.bucketsize = 0;
    }

    public int size() {
        // the number of keys
        return bucketsize;
    }

    public boolean isEmpty(){
        // checks for emptiness
        // check if there's no keys
        return bucketsize == 0;
    }

    // lazy implementation; there is no such thing as
    // a full hash table in separate chaining
    public boolean isFull(){
        return false;
    }

    // private hash function
    private int hash(String key){
        assert(key !=null);
        String lower = key.toLowerCase();
        int hshfn = lower.charAt(0) - 'a';
        assert(!(hshfn <0) || (hshfn < size()));
        return hshfn % table.length;
    }

    public V search$opt(String key){
        int hshidx = hash(key);
        assert(key !=null);
        LnList<FnTupl2<String, V>> bucket = table[hshidx];
        while(bucket.consq1()){
            FnTupl2<String, V> kv = bucket.hd1();
            if (kv.sub0.equals(key)){
                return kv.sub1;
            }
            bucket = bucket.tl1();
        }
        return null;        
    }

    public V search$old(String key){
        return search$opt(key);
    }

    public V search$exn(String key){
        V res = search$opt(key);
        if (res == null || key == null){
            throw new MyMap00NoKeyExn();
        }
        return res;
    }

    public V insert$opt(String key, V val){
        // return old V associated with key if the same key is inserted
        // return null if the key is new

        // search first
        V oldVal = search$opt(key);
        if (oldVal == null){
            insert$new(key, val);
            return null;
        } else {
            
            int hshidx = hash(key);
            LnList<FnTupl2<String,V>> bucket = table[hshidx];
            bucket.foritm1(
                (FnTupl2<String, V> kv) -> {
                    if (kv.sub0.equals(key)){
                        kv.sub1 = val; // no need to increment size bc we are replacing
                    }
                }
            );
            return oldVal;
        }
    }

    public void insert$new(String key, V val){
        // assume the key is not in the table
        int hshidx = hash(key);

        FnTupl2<String, V> newKV = new FnTupl2<String, V>(key, val);

        if (table[hshidx] == null){
            table[hshidx] = new LnList<FnTupl2<String, V>>(newKV, new LnList<FnTupl2<String, V>>());
            assert(table[hshidx].hd1() !=null);
            bucketsize++;
        } else {
            table[hshidx] = new LnList<FnTupl2<String, V>>(newKV, table[hshidx]);
            assert(table[hshidx].hd1() != null);
            bucketsize++;
        }
    }

    public V remove$opt(String key){
        V rmval = search$opt(key);
        if (rmval == null){
            // return null if key not found
            return null;
        }

        int hshidx = hash(key);
        LnList<FnTupl2<String,V>> bucket = table[hshidx];

        // after search (meaning key exists),
        // case 1: there is only one element in the bucket 
        if (bucket.consq1() && bucket.tl1().nilq1()){
            // rmval = bucket.hd1().sub1;
            table[hshidx] = null;
            assert(table[hshidx] == null);
            bucketsize--;
            return rmval;
        }

        // case 2: the first element is the one we remove
        if (bucket.consq1() && bucket.hd1().sub0.equals(key)){
            // rmval = bucket.hd1().sub1;
            table[hshidx] = table[hshidx].tl1();
            assert(table[hshidx] != null || table[hshidx].nilq1());
            bucketsize--;
            return rmval;
        }

        // case 3: otherwise
        table[hshidx] = remove(bucket, key);
        return rmval;
    }
	private LnList<FnTupl2<String,V>> remove(LnList<FnTupl2<String,V>> bucket, String rmkey){
        LnList<FnTupl2<String, V>> rmd = new LnList<FnTupl2<String, V>>();
        bucket.foritm1(
            (FnTupl2<String, V> kv) ->{
                if (!kv.sub0.equals(rmkey)){
                    rmd.append1(new LnList<FnTupl2<String, V>>(kv, 
                        new LnList<FnTupl2<String, V>>()));
                }
            }
        );
        bucketsize--;
        return rmd;
	}

    public V remove$exn(String key){
        V res = remove$opt(key);
        if (res == null || key == null){
            throw new MyMap00NoKeyExn();
        }
        return res;
    }

    public V remove$old(String key){
        V oldK = search$opt(key);
        int hshidx = hash(key);
        LnList<FnTupl2<String,V>> bucket = table[hshidx];
        table[hshidx] = remove(bucket, key);

        return oldK;
    }

    public void foritm(BiConsumer<? super String, ? super V> work){
        for (int i = 0; i < table.length; i++){
            LnList<FnTupl2<String, V>> bucket = table[i];
            if (bucket != null){
                bucket.foritm1(
                    (FnTupl2<String, V> kv)->
                    {
                        work.accept(kv.sub0, kv.sub1);
                    }
                );
            }
        }
    }

    private LnStrm<FnTupl2<String, V>> bucketToStream(LnList<FnTupl2<String, V>> kvs) {
        if (kvs == null || kvs.nilq1()) {
            return new LnStrm<>();
        }
    return LnStrmSUtil.cons0(kvs.hd1(), bucketToStream(kvs.tl1()));
    }

    public LnStrm<FnTupl2<String, V>> keyval_strmize(){
        // streamize key-value pairs linearly
        LnStrm<FnTupl2<String, V>> res = new LnStrm<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                res = LnStrmSUtil.append0(res, bucketToStream(table[i]));
            }
        }
        return res;
    }



    // public static void main(String[] args){
    //     MyMap00<String, Integer> SCTest = new MapSeparateChain<>(5);
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





