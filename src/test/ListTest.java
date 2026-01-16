package test;

import java.util.ArrayList;
import java.util.HashMap;

public class ListTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		HashMap<ArrayList<String>,String> hm =new HashMap<ArrayList<String>, String>();
//		ArrayList<String> al1 = new ArrayList<String>();
//		al1.add("1");
//		al1.add("2");
//		al1.add("3");
//		al1.add("4");
//		ArrayList<String> al2 = new ArrayList<String>();
//		al2.add("1");
//		al2.add("2");
//		al2.add("3");
//		al2.add("5");
//		System.out.println(al1.hashCode());
//		System.out.println(al2.hashCode());
//		System.out.println(al1.equals(al2));
		String[] s1= new String[]{"1","2","3"};
		String[] s2= new String[]{"1","2","3"};
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());
			System.out.println(s1.equals(s2));
		
	}

}
