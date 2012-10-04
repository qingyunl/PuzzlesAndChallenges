/*
This program gives the maximum level till which a binary tree is complete.
method: "LevelCompleteBinaryTree"
e.g.    1
     2    3
   4  5  6
  The level till which above binary tree complete is 1
*/
public class BinaryTreeLevel {
	int data;
	BinaryTreeLevel left;
	BinaryTreeLevel right;

	public int getData(){
		return this.data;
	}
	public BinaryTreeLevel getLeft(){
		return this.left;
	}
	public BinaryTreeLevel getRight(){
		return this.right;
	}

	public void setData(int data){
		this.data = data;
	}
	public void setLeft(BinaryTreeLevel left){
		this.left = left;
	}
	public void setRight(BinaryTreeLevel right){
		this.right = right;
	}

	public static int LevelCompleteBinaryTree(BinaryTreeLevel node,int level){
		if(node == null){
			return (level-1);
		}
		int left = LevelCompleteBinaryTree(node.getLeft(),level+1);
		int right = LevelCompleteBinaryTree(node.getRight(),level+1);

		return Math.min(left,right);
	}

	public static void main(String[] args){

		int n=14;
		Integer[] a = new Integer[n];
		for(int i=0;i<n;i++){
			a[i] = i;
		}
		BinaryTreeLevel[] sol = new BinaryTreeLevel[a.length];
		for(int i=0;i<n;i++){
			sol[i] = new BinaryTreeLevel();
			sol[i].setData(i);
		}
		for(int i=0;i<n;i++){
			if((2*i+1) >= a.length){
				sol[i].setLeft(null);
			}else{
				sol[i].setLeft(sol[2*i+1]);

			}
			if((2*i+2) >= a.length){
				sol[i].setRight(null);
			}else{
				sol[i].setRight(sol[2*i+2]);
			}
		}
		System.out.println("Complete Binary Tree Level is :" + LevelCompleteBinaryTree(sol[0],0));

	}
}
