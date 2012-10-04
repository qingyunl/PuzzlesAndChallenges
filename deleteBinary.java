import java.util.LinkedList;
import java.util.Queue;

/*
method "deleteBinaryTree" deletes the binary tree iteratively
method "constructBinaryTree" constructs a binary tree given pre-order and in-order traversal. 
*/
public class deleteBinary {

	/**
	 * @param args
	 */
	static int preIndex = 0;
	public static Integer[] getSubArray(Integer[] order, int start, int end){
		Integer[] ret = new Integer[end-start+1];
		int j = 0 ;
		for(int i=start;i<end;i++){
			ret[j] = order[i];
			j++;
		}
		return ret;
	}
	public static void deleteBinaryTree(Node node) throws IllegalStateException{
		Queue<Node> q = new LinkedList<Node>();
		if(node == null)
			return;
		q.add(node);
		while(!q.isEmpty()){
			Node delNode = q.remove();
			Node right;
			Node left;
			if(( left = delNode.getLeft())!=null)
				q.add(left);
			if(( right = delNode.getLeft())!=null)
				q.add(right);
			delNode = null; //Help garbage collection to delete it
		}
		
	}
	
	public static Node constructBinaryTree(Integer[] preOrder, Integer[] inOrder, int index, int start, int end ){
		if(start > end)
			return null;
		
		
		Node node = new Node(preOrder[index],null,null);
		++preIndex;
		if(start == end)
			return node;
		
		int newStart = start;
		int newEnd = end;
		
		for(int i=start;i<=end;i++){
			if(inOrder[i] == preOrder[index]){
				newEnd = i;
				break;
			}
		}
		
		node.setLeft(constructBinaryTree(preOrder, inOrder,preIndex , start, newEnd-1));
		node.setRight(constructBinaryTree(preOrder, inOrder, preIndex, newEnd+1, end));
		
		return node;
	}
	public static Node constructBinaryTree(Integer[] preOrder, Integer[] inOrder){
		
		return constructBinaryTree(preOrder, inOrder,0,0,preOrder.length -1);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		Integer[] preOrder = {1,2,3,5,4,6,7};
		Integer[] inOrder = {5,3,2,4,1,6,7};
		Node root = constructBinaryTree(preOrder, inOrder);
	}

	static class Node{
		int value;
		Node right;
		Node left;

		public Node(){

		}
		public Node(int value, Node right, Node left) {
			super();
			this.value = value;
			this.right = right;
			this.left = left;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public Node getRight() {
			return right;
		}
		public void setRight(Node right) {
			this.right = right;
		}
		public Node getLeft() {
			return left;
		}
		public void setLeft(Node left) {
			this.left = left;
		}
	}
}
