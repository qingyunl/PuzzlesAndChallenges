
/*
Finding the mode element in an array.
i.e. Element with maximum frequency in array 
*/
import java.util.HashMap;

/*Approach and DataStructures used:
 * Track the maximum count of element occurred so far and maximum element itself in two int variables.
 * Track the frequency of each element using HashMap as it gives amortized O(1) access time.
 * Iterate through the array and keep track of the frequency. Whenever an element with higher frequency than max seen so far is encountered,
 * change the maximum frequency.
 * The worst case/average case/best case time complexity if O(n)
*/
/*Assumptions: 
 * IF there are more than two elements with highest frequency, the element that achieved the highest frequency first 
 *	will be returned as 'mode'. e.g. In {3,4,3,9,3,9,9} -- 3 would be returned as mode.
 *	Return Element Assumption: It returns the minimum value of integer as mode when there does not exist any element in the array.This 
 *	should be noted by the calling function.
 * 
 */
public class MajorityElement {

	public static int mode( int  myArray[] ) { //Returns the element with maximum frequency in the int array
		

		int maxCountOfElement = 0; //To track the maximum count of element so far.
		int maxElement = Integer.MIN_VALUE; //To track the maximum element so far.
		HashMap<Integer,Integer> hashCountArrayElement = new HashMap<Integer,Integer>(); //To track the element and their corresponding count so far.

		for(int i=0;i<myArray.length;i++){
			int countOfCurrentElement;
			if(hashCountArrayElement.containsKey(myArray[i])){ //If it is already there, increment the count and check whether it is the max freq element.  
				countOfCurrentElement = hashCountArrayElement.get(myArray[i]);
				++countOfCurrentElement;
				if(maxCountOfElement < countOfCurrentElement ){ //If max freq than change the max frequent element and its count
					maxCountOfElement = countOfCurrentElement;
					maxElement = myArray[i];
				}
				hashCountArrayElement.put(myArray[i],countOfCurrentElement);
			}else{
				hashCountArrayElement.put(myArray[i],1); //Else insert the new element in HashMap
			}
		}
		System.out.println("Mode = " + maxElement);
		return maxElement;
	}

	public static void main(String[] args){
		int[] a = {9, 3, 2, 11, 87, 4, 3, 9,3, 21, 11, 91, 11, 9, 2, 9};
		mode(a);
	}

}
