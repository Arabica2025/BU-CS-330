package MyLibrary.MyMap00;

import MyLibrary.LnStrm.*;
import MyLibrary.FnTuple.*;
import java.util.function.BiConsumer;
// singly linked
public class MyMap00SingBST<K extends Comparable<K>, V> implements MyMap00<K,V> {
    private Node root;
    private int size;

    private class Node{
        K key;
        V val;
        Node lchild;
        Node rchild;

        Node(K key, V val){
            this.key = key;
            this.val = val;
            this.lchild = null;
            this.rchild = null;
        }
    }

    public MyMap00SingBST(){
        root = null;
        size = 0;
    }

    public int size(){
        return size;
    }
    public boolean isFull(){
        return false;
    }
    public boolean isEmpty(){
        return root == null;
    }

    private boolean isEmptyAt(Node node){
        return node == null;
    }

    private LnStrm<FnTupl2<K,V>> nodeToStrm(Node node){
        if (isEmptyAt(node)){
            return LnStrmSUtil.nil0();
        }
        LnStrm<FnTupl2<K,V>> left = nodeToStrm(node.lchild);

        LnStrm<FnTupl2<K,V>> mid = LnStrmSUtil.cons0(
            new FnTupl2<K,V>(node.key, node.val),
            LnStrmSUtil.nil0());

        LnStrm<FnTupl2<K,V>> right = nodeToStrm(node.rchild);

        return LnStrmSUtil.append0(left, LnStrmSUtil.append0(mid, right));
    }

    public LnStrm<FnTupl2<K,V>> keyval_strmize(){
        return nodeToStrm(root);
    }

    public void foritm(BiConsumer<? super K, ? super V> work){
        keyval_strmize().foritm0(kv -> work.accept(kv.sub0, kv.sub1));
    }


    private V search$raw(Node node, K key){
        if (isEmptyAt(node)){
            return null;
        }
        if (key.compareTo(node.key) == 0){
            return node.val;
        }

        return key.compareTo(node.key) < 0 ? search$raw(node.lchild, key) : search$raw(node.rchild,key);

    }

    public V search$old(K key){
        return search$raw(root,key);
    }

    public V search$opt(K key){
        if(key == null) return null;

        return search$raw(root, key);
    }

    public V search$exn(K key){
        if(key == null) throw new MyMap00NoKeyExn();
        V value = search$raw(root, key);
        if (value == null) throw new MyMap00NoKeyExn();
        return value;
    }

    private Node insert$raw(Node node, K key, V val){
        if (isEmptyAt(node)) return new Node(key, val);
        if (key.compareTo(node.key)< 0){
            node.lchild = insert$raw(node.lchild, key, val);
        } else {
            node.rchild = insert$raw(node.rchild, key, val);
        }
        return node;
    }

    private void update(Node node, K key, V val){
        if (key.compareTo(node.key) == 0){
            node.val = val;
        } else if (key.compareTo(node.key) < 0){
            update(node.lchild, key, val);
        }else {
            update(node.rchild,key,val);
        }
    }

    public void insert$new(K key, V val){
        root = insert$raw(root, key, val);
        size++;
    }

    public V insert$opt(K key, V val){
        if (key == null) return null;
        V old = search$raw(root, key);
        if (old != null){
            update(root, key, val);
            return old;
        }
        root = insert$raw(root, key, val);
        size++;
        return null;
    }

    private FnTupl2<Node, V> remove$raw(Node node, K key){
        if (isEmptyAt(node)) return new FnTupl2<Node, V>(null, null);

        V rmd;
        if (key.compareTo(node.key) < 0){
            FnTupl2<Node, V> toRemove = remove$raw(node.lchild, key);
            node.lchild = toRemove.sub0;
            rmd = toRemove.sub1;
        } else if (key.compareTo(node.key)> 0){
            FnTupl2<Node, V> toRemove = remove$raw(node.rchild, key);
            node.rchild = toRemove.sub0;
            rmd = toRemove.sub1;
        } else {
            rmd = node.val;
            node = remove(node);
        }
        return new FnTupl2<Node, V>(node, rmd);
    }

    private Node remove(Node node){
        /*
        case1: no children
        case2: 1 child
        case3: 2 children
         */
        // case 1 and case 2
        if (node.lchild == null) return node.rchild;
        if (node.rchild == null) return node.lchild;

        // case 3
        Node successor = node.rchild;
        while(successor.lchild != null){
            successor = successor.lchild;
        } // pick leftmost elemnt in right subtree to replacement

        node.key = successor.key;
        node.val = successor.val;
        node.rchild = removeNodeRchild(node.rchild);
        return node;

    }
    private Node removeNodeRchild(Node node){
        if(node.lchild == null) return node.rchild;
        node.lchild = removeNodeRchild(node.lchild);
        return node;
    }

    public V remove$old(K key){
        FnTupl2<Node, V> result =remove$raw(root, key);
        root = result.sub0;
        if(result.sub1!= null) size--;
        return result.sub1;
    }

    public V remove$opt(K key){
        if(key==null) return null;

        return remove$old(key);
    }

    public V remove$exn(K key){
        if (key== null) throw new MyMap00NoKeyExn();

        FnTupl2<Node,V> result = remove$raw(root, key);
        if(result.sub1 == null) throw new MyMap00NoKeyExn();
        root = result.sub0;
        size--;
        return result.sub1;
    }


}
