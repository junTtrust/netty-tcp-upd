package org.atm.dc.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings({"nls","resource"})
public class FileUtil {
	public static String filepath = "D://a.xml";
	public static String keystory = "com.jd";

	// static {
	// Properties prop = new Properties();
	// InputStream in = FileUtil. class .getResourceAsStream( "app.properties"
	// );
	// try {
	// prop.load(in);
	// filepath = prop.getProperty( "path" ).trim();
	// key = prop.getProperty( "key" ).trim();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	public static String getXmlString() {
		String xmlString;
		byte[] strBuffer = null;
		int flen = 0;
		File xmlfile = new File(filepath);
		try {
			InputStream in = new FileInputStream(xmlfile);
			flen = (int) xmlfile.length();
			strBuffer = new byte[flen];
			in.read(strBuffer, 0, flen);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		xmlString = new String(strBuffer); // 构建String时，可用byte[]类型，
		return xmlString;
	}

	public static void WriteStringToFile(String filePath, String filecountcontent, boolean flag) {
		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			if (flag) {
				ps.append(filecountcontent);// 在已有的基础上添加字符串
			} else {
				ps.println(filecountcontent);// 往文件里写入字符串
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getEncryptionKey(String ips) {
		String keyresult = ips + "file-xml";
		EncrypDES des2;
		try {
			des2 = new EncrypDES(keystory);
			String keyEncryptionStr = des2.encrypt(keyresult);
			return keyEncryptionStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		WriteStringToFile("D://c.xml", getXmlString(), false);
	}

	public static Object readObjectFromFile(String path) {
		Object temp = null;
		File file = new File(path);
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
			System.out.println("read object success!");
		} catch (IOException e) {
			System.out.println("read object failed");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static String getLocalIp() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString(); // 获取本机ip
		return ip;
	}

}
