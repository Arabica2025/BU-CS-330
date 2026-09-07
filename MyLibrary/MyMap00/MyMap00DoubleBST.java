package MyLibrary.MyMap00;

//
// HX-2026-04-09: 50 points
// A partial implementation of
// randomized doubly linked binary search tree
// 20 points for insert and 30 points for remove
//
public class MyMap00DoubleBST {
    Node root = null;
    public class Node {
	int key; // key stored in the node
	int size; // size of the tree rooted as the node
	Node parent; // parent of the node
	Node lchild; // left-child of the node
	Node rchild; // right-child of the node

		Node(int key){
			this.key = key;
			this.size = 1;
			this.parent = null;
			this.lchild = null;
			this.rchild = null;
		}
    }

	
    public boolean insert(int key) {
	// HX-2026-04-09: 20 points
	// If key is in the tree stored at [root],
	// [insert] does nothing and just returns false.
	// If key is not in the tree stored at [root],
	// the key is inserted as a leaf node and the new
	// tree is still a binary search tree and [insert]
	// returns true (to indicate insertion is done).
	
	// case: empty tree
		if (root == null){
			root = new Node(key);
			return true;
		}
	// if not empty, insert
		return inserthelp(root, key);
    }

	private boolean inserthelp(Node node, int key){
		if (key == node.key){
			return false; // if input key already exists, false (not inserting)
		}

		// case 1: input key < node.key
		if (key < node.key){
			// left child is empty
			if(node.lchild == null){
				Node newlN = new Node(key);
				newlN.parent = node;
				node.lchild = newlN;
				node.size+=1;
				return true;
			}
			// recursive call to dig deeper
			boolean lres = inserthelp(node.lchild, key);
			if (lres){
				node.size+=1;
			}
			return lres;
		} else{
			// case 2: input key > node.key
			// right child is empty
			if(node.rchild == null){
				// insert the key and increment size
				Node newrN = new Node(key);
				newrN.parent = node;
				node.rchild = newrN;
				node.size+=1;
				return true;
			}
			boolean rres = inserthelp(node.rchild, key);
			if (rres){
				node.size+=1;
			}
			return rres;
		}
	}
	// remove main
    public boolean remove(int key) {
	// HX-2026-04-09: 20 points
	// If key is in the tree stored at [root],
	// [remove] removes the key and the new tree
	// obtained is still a binary search tree and
	// [remove] returns true to indicate the removal
	// is done.
	// If key is not in the tree stored at [root],
	// [remove] does nothing and returns false to
	// indicate that no removal of the key k is done.
		// empty tree; nothing to remove
		if (root == null){
			return false;
		}
		return rmhelp(root, key);
    }

	// remove helper; searching key in Doubly Linked BST
	private boolean rmhelp(Node node, int key){
		// if key not found, return false (cannot remove at all)
		if (node == null){
			return false;
		}

		// if key found,
		// execute what needs to be done when key found
		if (key == node.key){
			rmKfound(node);
			return true;
		}
		// search for key
		// case: input key is less than the key in the current node
		if (key < node.key){
			// recursive call to find the key and remove
			boolean lres = rmhelp(node.lchild, key);
			// if key found and removed, decrement size
			if (lres){
				node.size-=1;
			}
			//return the result (found: true, not found: false)
			return lres;
			// case: input key is greater than the key in the current node
		} else{

			// recursive cal to find the key and remove
			boolean rres = rmhelp(node.rchild, key);
			// if key found and removed, decrement size
			if(rres){
				node.size-=1;
			}
			// return the result(found: true, not found: false)
			return rres;
		}

	}

	// defining what needs to be executed when key found
	private void rmKfound(Node node){

		// case 1: the key-matching node has two children
		if (node.lchild != null && node.rchild !=null){
			// i am replacing the current node with the samllest of the rchild
			// start from the right child of the current node
			Node replace = node.rchild;
			// decrement size once here since we are 1 level down
			replace.size -=1;

			// we are looking for the smallest of the rchild 
			while (replace.lchild !=null){
				// update the ndoe with lchild and decrement size as we go down
				replace = replace.lchild;
				replace.size -=1;
			}

			// update the key
			node.key = replace.key;
			// actual replace method here
			rmreplace(replace, replace.rchild);
		} else { // they key-matching node has either one or no children
			// just replace what's underneath
			Node child = (node.lchild != null) ? node.lchild : node.rchild;
			rmreplace(node, child);
		}
	}

	private void rmreplace(Node node, Node child){
		// root is the only node that has no parent
		if (node.parent == null){
			root = child;
			// if the node we are looking for is the left child on the last level
		} else if (node == node.parent.lchild){
			// replace the left child
			node.parent.lchild = child;
			// if the noew we are looking for is the right child
		} else{
			//replace the righ child
			node.parent.rchild=child;
		}

		// doubly linked BST; need to update parent pointer
		// unless we are just removing the input key, update the parent pointer
		if (child != null){
			child.parent = node.parent;
		}
	}
    public static void main (String[] args) {
	// Please add minimal testing code for insert()
	// Please add minimal testing code for remove()
	// return /*void*/;

		MyMap00DoubleBST test = new MyMap00DoubleBST();

		System.out.println("insert test");
		// all true but false for the last one
		System.out.println(test.insert(5));
		System.out.println(test.insert(3));
		System.out.println(test.insert(7));
		System.out.println(test.insert(1));
		System.out.println(test.insert(4));
		System.out.println(test.insert(3));

		System.out.println("remove test");
		System.out.println(test.remove(3)); // true
		System.out.println(test.remove(10)); // false; not found
		System.out.println(test.remove(5)); //. true(root)

    }
}
