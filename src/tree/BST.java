package tree;

public class BST {

	public BSTNode<Integer> root;

	public BST() {
		root = null;
	}

	public boolean find(int id) {
		BSTNode<Integer> current = root;
		while (current != null) {
			if (current.data == id) {
				return true;
			} else if (current.data > id) {
				current = current.left;
			} else {
				current = current.right;
			}
		}
		return false;
	}

	public void insert(int id) {
		BSTNode<Integer> newNode = new BSTNode<Integer>(id);
		if (root == null) {
			root = newNode;
			return;
		}
		BSTNode<Integer> current = root;
		BSTNode<Integer> parent = null;
		while (true) {
			parent = current;
			if (id < current.data) {
				current = current.left;
				if (current == null) {
					parent.left = newNode;
					return;
				}
			} else {
				current = current.right;
				if (current == null) {
					parent.right = newNode;
					return;
				}
			}
		}
	}

	public void insertRecursively(BSTNode<Integer> root, int id) {
		if (root == null) {
			root = new BSTNode<Integer>(id);
			return;
		} else if (root.data < id) {
			if (root.left == null) {
				root.left = new BSTNode<Integer>(id);
				return;
			} else {
				insertRecursively(root.left, id);
			}
		} else if (root.data > id) {
			if (root.right == null) {
				root.right = new BSTNode<Integer>(id);
				return;
			} else {
				insertRecursively(root.right, id);
			}
		}
	}

	public boolean isEmpty() {
		return (root == null);
	}

	public void display(BSTNode<Integer> root) {
		if (root != null) {
			display(root.left);
			System.out.print(" " + root.data);
			display(root.right);
		}
	}

	public void clear() {
		root = null;
	}

}
