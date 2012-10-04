/*
method "findLongestSubArray" finds the longest contigous Sub Array in a given integer array 
whose elements sums up to a given number.
Time complexity: O(n^2) solution
*/
public class LargestSubArray {

	public static void findLongestSubArray(Integer[] a,int n){
		Integer[] temp = new Integer[a.length + 1];
		temp[0] = 0;
		for(int i=0;i<a.length;i++){
			temp[i+1] = temp[i] + a[i];
		}
		int max = 0;
		int len = a.length;
		for(int i=0;i<len;i++){
			//int j= len - i;
			int k=0;
			do{
				if(k <= i){
					if((temp[k + (len-i-1)]-temp[k]) == n){
						if((len - i - 1) > max){
							max = len - i -1;
							for(int o=(k);o<=(k+max-1);o++){
								System.out.println(a[o] + " -> ");
							}
							return;
						}
					}
					k++;
				}else{
					break;
				}
			}while(true);
		}
	}
	public static void main(String[] args) {
		Integer[] a = {1,2,8,7,3,11};
		findLongestSubArray(a,18);
	}

}
