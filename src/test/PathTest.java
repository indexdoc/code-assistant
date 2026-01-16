package test;

import java.io.File;
import java.io.IOException;

public class PathTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File f = new File("E:\\Workspaces\\UMLCoder\\PDMCoder2\\filetest.txt");
		System.out.println("f.getAbsolutePath():"+f.getAbsolutePath());
		System.out.println("f.getCanonicalPath():"+f.getCanonicalPath());
		System.out.println("f.getParent():"+f.getParent());
		System.out.println("f.getName():"+f.getName());
		System.out.println("f.getPath():"+f.getPath());
		System.out.println("f.getParentFile():"+f.getParentFile());	
	}

}
