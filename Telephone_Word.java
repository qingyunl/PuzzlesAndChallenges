/*
Given a telephone number, generate all the permutation of string in which the telephoen number could be generated
*/
import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

public class Telephone_Word  {

	static int q=0;
	public static void generatePhoneNo(int[] telephoneNo,ArrayList<ArrayList<Character>> mapping,StringBuilder str,int no){
		if(no == 7){
			for(Character c : mapping.get(telephoneNo[no-1])){
				str.append(c);
				q++;
				System.out.print(str.toString() + ",");
				str.deleteCharAt(no-1);
			}
			return;
		}
		for(Character c : mapping.get(telephoneNo[no-1])){
			str.append(c);
			generatePhoneNo(telephoneNo,mapping,str,no+1);
			str.deleteCharAt(no-1);
		}

	}

	public static void main(String args[] ) throws Exception {

		ArrayList<ArrayList<Character>> mapping = new ArrayList<ArrayList<Character>>();
		int j=97;
		for(int i=0;i<10;i++){
			if(i<2){
				ArrayList<Character> listMap = new ArrayList<Character>(1);
				listMap.add((char)(48+i));
				mapping.add(i,listMap);
				continue;
			}
			ArrayList<Character> listMap = new ArrayList<Character>();
			for(int k=0;k<3;k++){
				listMap.add((char)j);
				j++;
			}
			if((i==7) || (i==9)){
				listMap.add((char)j++);
			}
			mapping.add(i,listMap);
		}
		if(args.length < 1){
			//	return ;
		}
		//String path = new String(args[0]);
		String path = new String();
		path = "C:\\Users\\Rushi\\Desktop\\Interview Street Test Cases\\Quixey\\input01.txt";

		BufferedReader br = new BufferedReader(new FileReader(path));
		int[] telephoneNo = new int[7];

		String line = new String();
		int c = 0;
		while(c < 7){
			try{
				line = br.readLine();
				if(line == null)
					break; 
				telephoneNo[c] = Integer.parseInt(line);
				c++;
			}
			catch(Exception e){
				System.out.println("An error occurred. Please verify that the input correspond to the requirement or restart the application");
			}finally{

			}
		}
		StringBuilder str = new StringBuilder();
		generatePhoneNo(telephoneNo,mapping,str,1);
		System.out.println("Total = "+ q);
	}
}
