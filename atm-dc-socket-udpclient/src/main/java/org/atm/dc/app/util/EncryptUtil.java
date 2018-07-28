package org.atm.dc.app.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
/**
 * 加密/解密工具
 * @author ershuai
 * @date 2017年4月18日 上午11:27:36
 */
public class EncryptUtil {
 
	private final byte[] DESIV = new byte[] { 0x12, 0x34, 0x56, 120, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef };// 向量
 
	private AlgorithmParameterSpec iv = null;// 加密算法的参数接口
	private Key key = null;
	
	private String charset = "utf-8";
	
	/**
	 * 初始化
	 * @param deSkey	密钥
	 * @throws Exception
	 */
	public EncryptUtil(String deSkey, String charset) throws Exception {
		if (isNotEmpty(charset)) {
			this.charset = charset;
		}
		DESKeySpec keySpec = new DESKeySpec(deSkey.getBytes(this.charset));// 设置密钥参数
		iv = new IvParameterSpec(DESIV);// 设置向量
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
		key = keyFactory.generateSecret(keySpec);// 得到密钥对象
	}
	
	/**
	 * 加密
	 * @author ershuai
	 * @date 2017年4月19日 上午9:40:53
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String encode(String data) throws Exception {
		Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
		enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
		byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(pasByte);
	}
	
	/**
	 * 解密
	 * @author ershuai
	 * @date 2017年4月19日 上午9:41:01
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String decode(String data) throws Exception {
		Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, key, iv);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
		return new String(pasByte, "UTF-8");
	}
	public boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}
	 public static String encryption(String filecontent,String key){
		 String test = filecontent;  
		try {
			//String key = "9ba45bfd500642328ec03ad8ef1b6e75";// 自定义密钥
			EncryptUtil des = new EncryptUtil(key, "utf-8");
			
			  EncrypDES des2 = new EncrypDES(key);
				String  a = des2.encrypt(filecontent);   
	            // System.out.println("解密后的字符：" + des2.decrypt(des2.encrypt(msg2)));
			
			
//			System.out.println("加密前的字符：" + test);
				
			String b = 	des.encode(a);
			System.out.println("加密后的字符：" +b );
			System.out.println("解密后的字符：" + des2.decrypt(des.decode(b)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 自定义密钥   
    	return null ;
    }
	public static void main(String[] args) {
//		try {
//			String test = "ershuai";
//			String key = "9ba45bfd500642328ec03ad8ef1b6e75";// 自定义密钥
//			EncryptUtil des = new EncryptUtil(key, "utf-8");
//			System.out.println("加密前的字符：" + test);
//			System.out.println("加密后的字符：" + des.encode(test));
//			System.out.println("解密后的字符：" + des.decode(des.encode(test)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String test = "ershuai<xml>opopqo";
		String key = "9ba45bfd500642328ec03ad8ef1b6e75";// 自定义密钥
		encryption(test,key);
	}
	
}
