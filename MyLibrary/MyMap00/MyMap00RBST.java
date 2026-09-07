package MyLibrary.MyMap00;

import MyLibrary.FnList.*;
import MyLibrary.LnStrm.*;
import MyLibrary.FnTuple.FnTupl2;
import MyLibrary.LnList.*;

import java.util.function.Consumer;
import java.util.function.BiConsumer;

// randomized Binary Search Tree Generic input

public class MyMap00RBST<K extends Comparable<K>, V> implements MyMap00<K, V> {
    private Node root;
    private class Node {
	K key;
	V val;
	int size;
    Node lchild;
	Node rchild;

    Node(K key, V val){
        this.key = key;
        this.val = val;
        this.size = 1;
        this.lchild = null;
        this.rchild = null;
    }
    }
 

    public MyMap00RBST(){
        root = null;
    }

    public int size(){
        return sizeOf(root);
    }
    private int sizeOf(Node node){
        return (node == null) ? 0 : node.size;
    }

    public boolean isFull(){
        return false;
    }

    public boolean isEmpty(){
        return root == null;
    }

    private LnStrm<FnTupl2<K, V>> nodeToStream(Node node){
        if (node == null){
            return LnStrmSUtil.nil0();
        }
        // RBST implements inorder traversal
        // left -> root -> right
        LnStrm<FnTupl2<K, V>> left = nodeToStream(node.lchild);

        LnStrm<FnTupl2<K,V>> mid = 
            LnStrmSUtil.cons0(
                new FnTupl2<K,V>(node.key, node.val),
                LnStrmSUtil.nil0()
            );

        LnStrm<FnTupl2<K,V>> right = nodeToStream(node.rchild);

        return LnStrmSUtil.append0(
            left, 
            LnStrmSUtil.append0(mid, right));
    }
    public LnStrm<FnTupl2<K,V>> keyval_strmize(){
        return nodeToStream(root);
    }

    // random integer index using bounds of Tree size
    private int randIntIdx_size(){
        return FnListSUtil.rand$int$make(size()).hd();
    }

    public void foritm(BiConsumer<? super K, ? super V> work){
        keyval_strmize().foritm0(kv -> work.accept(kv.sub0, kv.sub1));
    }

    

    public V search$raw(Node node, K key){
        if (node == null){
            return null;
        }
        if (key.compareTo(node.key) == 0){
            return node.val;
        } else if (key.compareTo(node.key) < 0){
            return search$raw(node.lchild, key);
        } else {
            return search$raw(node.rchild, key);
        }
        
    }

    public V search$old(K key){
        return search$raw(root, key);
    }

    public V search$opt(K key){
        if (key == null){
            return null;
        }
        return search$old(key);
    }

    public V search$exn(K key){
        V value = search$opt(key);
        if (value == null) {
            throw new MyMap00NoKeyExn();
        }
        return value;
    }

    public void insert$new(K key, V val) {
        root = insert$raw(root, key, val);
    }

    public V insert$opt(K key, V val){
        if (key == null){
            return null;
        }
        V oldrmd = search$opt(key);
        if (oldrmd != null){
            overwrite(root, key, val);
            return oldrmd;
        }
        insert$new(key, val);
        return null;
    }

    private void overwrite(Node node, K key, V val){
        if (key.compareTo(node.key) == 0){
            node.val = val;
        } else if (key.compareTo(node.key)< 0){
            overwrite(node.lchild, key, val);
        } else {
            overwrite(node.rchild, key, val);
        }
    }

    private Node insert$raw(Node node, K key, V val) {
        if (node == null) return new Node(key, val);

        // coin flip: 1/(size+1) chance of insert-at-root
        int r = FnListSUtil.rand$int$make(node.size + 1).hd();
        if (r == 0) return insertAtRoot(node, key, val);

        if (key.compareTo(node.key) < 0) {
            node.lchild = insert$raw(node.lchild, key, val);
        } else {
            node.rchild = insert$raw(node.rchild, key, val);
        }
        node.size = 1 + sizeOf(node.lchild) + sizeOf(node.rchild);
        return node;
    }

    private Node insertAtRoot(Node node, K key, V val){
        if(node == null){
            return new Node(key, val);
        }
        if(key.compareTo(node.key) < 0){
            node.lchild = insertAtRoot(node.lchild, key, val);
            return rotateRight(node);
        } else {
            node.rchild = insertAtRoot(node.rchild, key, val);
            return rotateLeft(node);
        }
    }



    private Node rotateRight(Node y) {
        Node x = y.lchild;
        y.lchild = x.rchild;
        x.rchild = y;
        y.size = 1 + sizeOf(y.lchild) + sizeOf(y.rchild);   // lower first
        x.size = 1 + sizeOf(x.lchild) + sizeOf(x.rchild);   // upper second
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.rchild;
        x.rchild = y.lchild;
        y.lchild = x;
        x.size = 1 + sizeOf(x.lchild) + sizeOf(x.rchild);
        y.size = 1 + sizeOf(y.lchild) + sizeOf(y.rchild);
        return y;
    }

    private FnTupl2<Node, V> remove$raw(Node node, K key){
        if(node == null){
            return new FnTupl2<Node, V>(null, null);
        }

        V removed;

        if (key.compareTo(node.key) < 0){
            FnTupl2<Node,V> result = remove$raw(node.lchild, key);
            node.lchild = result.sub0;
            removed = result.sub1;
        } else if (key.compareTo(node.key)>0){
            FnTupl2<Node,V> result = remove$raw(node.rchild, key);
            node.rchild = result.sub0;
            removed = result.sub1;
        } else {
            removed = node.val;
            node = join(node.lchild, node.rchild);
        }
        if (node !=null){
            node.size = 1 + sizeOf(node.lchild) + sizeOf(node.rchild);
        }
        return new FnTupl2<Node, V>(node, removed);
    }

    private Node join(Node left, Node right){
        if (left == null){
            return right;
        }
        if (right == null){
            return left;
        }

        int total = left.size + right.size;
        int randomInt = FnListSUtil.rand$int$make(total).hd();

        if (randomInt < left.size){
            left.rchild = join(left.rchild, right);
            left.size = 1 + sizeOf(left.lchild)+sizeOf(left.rchild);
            return left;
        } else{
            right.lchild = join(left, right.lchild);
            right.size = 1 + sizeOf(right.lchild) + sizeOf(right.rchild);
            return right;
        }
    }

    public V remove$old(K key){
        FnTupl2<Node, V> result = remove$raw(root, key);
        root = result.sub0;
        return result.sub1;
    }

    public V remove$opt(K key){
        if (key == null){
            return null;
        }
        return remove$old(key);
    }

    public V remove$exn(K key){
        V removed = remove$opt(key);
        if (removed == null){
            throw new MyMap00NoKeyExn();
        }
        return removed;
    }

}
