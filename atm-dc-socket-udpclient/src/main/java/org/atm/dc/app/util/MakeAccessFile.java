package org.atm.dc.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("nls")
public class MakeAccessFile {
	public static void main(String[] args) throws IOException {
		doMakeAccessFile("D://c.txt"); 
	}
     public static void writeObjectToFile(Object obj,String  path)  {  
         File file =new File(path);  
         FileOutputStream out;  
         try {  
             out = new FileOutputStream(file);  
             ObjectOutputStream objOut=new ObjectOutputStream(out);  
             objOut.writeObject(obj);  
             objOut.flush();  
             objOut.close();  
             System.out.println("write object success!");   //$NON-NLS-1$
         } catch (IOException e) {  
             System.out.println("write object failed");  
             e.printStackTrace();  
         } 
     }
     public static void doMakeAccessFile(String  path) throws IOException  {  
    	 Map<String,String>  map = new  HashMap<String,String>();
    	 String line = "";
    	 BufferedReader bufferedReader  = new BufferedReader(new FileReader(path ));
 		while ((line = bufferedReader.readLine()) != null) {
 			String key = line+"_access";
 			map.put(key, "true");
        }
 		bufferedReader.close();
 		File fiel = new File(path);
 		fiel.delete();
 		String newpath = path.substring(0,path.lastIndexOf("."));
    	writeObjectToFile(map,newpath);
     } 
}
