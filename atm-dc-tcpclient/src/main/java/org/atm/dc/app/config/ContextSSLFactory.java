package org.atm.dc.app.config;

import java.io.File;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.security.KeyStore;  
import java.security.NoSuchAlgorithmException;  
  
import javax.net.ssl.KeyManager;  
import javax.net.ssl.KeyManagerFactory;  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.TrustManager;  
import javax.net.ssl.TrustManagerFactory;  
  
import org.springframework.core.io.ClassPathResource;  
/** 
 * 初始化sslcontext类 
 * 
 */  
public class ContextSSLFactory {  
      
    private static final SSLContext SSL_CONTEXT_S ;  
      
    private static final SSLContext SSL_CONTEXT_C ;  
      
    static{  
        SSLContext sslContext = null ;  
        SSLContext sslContext2 = null ;  
        try {  
        	//TLS  SSLv3
            sslContext = SSLContext.getInstance("TLSv1") ;  
            sslContext2 = SSLContext.getInstance("TLSv1") ;  
        } catch (NoSuchAlgorithmException e1) {  
            e1.printStackTrace();  
        }  
        try{  
            if(getKeyManagersServer() != null && getTrustManagersServer() != null ){  
                sslContext.init(getKeyManagersServer(), getTrustManagersServer(), null);  
            }  
            if(getKeyManagersClient() != null && getTrustManagersClient() != null){  
                sslContext2.init(getKeyManagersClient(), getTrustManagersClient(), null);  
            }  
              
        }catch(Exception e){  
            e.printStackTrace() ;  
        }  
        sslContext.createSSLEngine().getSupportedCipherSuites() ;  
        sslContext2.createSSLEngine().getSupportedCipherSuites() ;  
        SSL_CONTEXT_S = sslContext ;   
        SSL_CONTEXT_C = sslContext2 ;  
    }  
    public ContextSSLFactory(){  
          
    }  
    public static SSLContext getSslContext(){  
        return SSL_CONTEXT_S ;  
    }  
    public static SSLContext getSslContext2(){  
        return SSL_CONTEXT_C ;  
    }  
    private static TrustManager[] getTrustManagersServer(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        TrustManagerFactory keyFac = null ;  
          
        TrustManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = TrustManagerFactory.getInstance("SunX509") ;  
//            is =new FileInputStream( (new ClassPathResource("main/resources/server.jks")).getFile() );  
            is =new FileInputStream( (new File("D:/workspace/atmDc/atm-dc/atm-dc-tcpclient/src/main/resources/server.jks")));  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "123456" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks) ;  
            kms = keyFac.getTrustManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
    private static TrustManager[] getTrustManagersClient(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        TrustManagerFactory keyFac = null ;  
          
        TrustManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = TrustManagerFactory.getInstance("SunX509") ;  //D:/workspace/atmDc/atm-dc/atm-dc-tcpclient/src/main/resources/client.jks   main/resources/
//            is =new FileInputStream( (new ClassPathResource("main/resources/client.jks")).getFile() );  
            is =new FileInputStream( (new File("D:/workspace/atmDc/atm-dc/atm-dc-tcpclient/src/main/resources/client.jks")));  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "123456" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks) ;  
            kms = keyFac.getTrustManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
    private static KeyManager[] getKeyManagersServer(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        KeyManagerFactory keyFac = null ;  
          
        KeyManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = KeyManagerFactory.getInstance("SunX509") ;  //D:/workspace/atmDc/atm-dc/atm-dc-tcpclient/src/main/resources/server.jks   main/resources/
//            is =new FileInputStream( (new ClassPathResource("main/resources/server.jks")).getFile() );  
            is =new FileInputStream( (new File("D:/workspace/atmDc/atm-dc/atm-dc-tcpclient/src/main/resources/server.jks")));  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "123456" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks, keyStorePass.toCharArray()) ;  
            kms = keyFac.getKeyManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
    private static KeyManager[] getKeyManagersClient(){  
        FileInputStream is = null ;  
        KeyStore ks = null ;  
        KeyManagerFactory keyFac = null ;  
          
        KeyManager[] kms = null ;  
        try {  
             // 获得KeyManagerFactory对象. 初始化位默认算法  
            keyFac = KeyManagerFactory.getInstance("SunX509") ;    //main/resources/
//            is =new FileInputStream( (new ClassPathResource("main/resources/client.jks")).getFile() );  
            is =new FileInputStream( (new File("D:/workspace/atmDc/atm-dc/atm-dc-tcpclient/src/main/resources/client.jks")));  
            ks = KeyStore.getInstance("JKS") ;  
            String keyStorePass = "123456" ;  
            ks.load(is , keyStorePass.toCharArray()) ;  
            keyFac.init(ks, keyStorePass.toCharArray()) ;  
            kms = keyFac.getKeyManagers() ;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null ){  
                try {  
                    is.close() ;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return kms ;  
    }  
}  