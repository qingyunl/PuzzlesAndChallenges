/*
Iterative merge sort
*/

public class  MergeSort{
	
	
	
	public static void MergeSortMe(Integer[] a){
		 
		int size = 1;
		while(size < a.length){
			for(int i=0;i<a.length;i+=(2*size)){
				int[] temp;
				if((a.length - i)%(2*size)!=0){
					temp = new int[a.length - i];
				}else{
					temp = new int[2*size];
				}
				//k<(2*size) && ((i+k) < a.length)
				for(int k=0;k<temp.length;k++){
					temp[k] = a[i+k];
				}
				int t = 0;
				int c = 0;
				int r = t + size;
				while(c<temp.length){
					if(t<(size) && r<(2*size) && r<temp.length){
						if(temp[t] > temp[r]){
							a[i+c] = temp[r];
							r++;
						}else{
							a[i+c] = temp[t];
							t++;
						}
					}else if (t < (size)){
						a[i+c] = temp[t];
						t++;
					}else if (r < (2*size)){
						a[i+c] = temp[r];
						r++;
					}
					c++;
				}
			}
			size*=2;
		}

		/*for(int i=0;i<a.length;i++){
			System.out.println(a[i]+"->");
		}*/
	}
	public static void main(String[] args){
		int n = 100000;
		Integer[] a = new Integer[n];
		for(int i=0;i<n;i++){
			a[i] = n-i;
		}
		MergeSortMe(a);
		for(int i=0;i<a.length;i++){
			System.out.println(a[i]);
		}
	}
}
