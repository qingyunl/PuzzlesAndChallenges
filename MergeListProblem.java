/*
Merge two sorted list into one sorted list
*/

/*Approach and DataStructures used:
 * Recursive approach is used. Compare the two heads first. Whichever is small (say head1), make it head and now to link its 
 * next element simply call the  method recursively again with new parameters as (head1.next,head2). Vice versa if head2 was smaller/
 * The worst case is O(n)
 * The Best case is O(1) when only one of the two list is null
 * Average case is 0(n)
 * There is no list destroyed. Only the links are changed. So it works in O(1) space
*/
/*Assumptions: 
 * The stack is large enough to hold the at max N recursive calls.
 */
public class MergeListProblem {
	public static Node mergeLists(Node head1, Node head2) {
		if (head1 == null) { //If null simply return the head list 2 
			return head2;
		} 
		if (head2 == null) { //If null simply return the head of list 1 
			return head1;
		} 

		if (head1.value < head2.value) { 
			head1.next = mergeLists(head1.next, head2); //If head1 is small recursive call to get its next element
			return head1;
		} else {
			head2.next = mergeLists(head2.next, head1); //If head2 is small recursive call to get its next element
			return head2;
		}
	}

	public static void main(String[] args){

	}

	public class Node {
		public int value;
		public Node next;
		public Node() {
			value = 0;
			next = null;
		}
		public Node(int value, Node next) {
			this.value = value;
			this.next = next;
		}
	}
}