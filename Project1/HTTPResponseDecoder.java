import java.util.*;
import java.nio.ByteBuffer;
/**
* This class contains method for HTTP to decompose the response message it received, and stored all information in a
* hashmap. Then, we can use the hash map to get all kinds of information we need.
* 
* @author  Darren Norton, Yizhong Chen
* @since   Feb-19th-2017 
*/
public class HTTPResponseDecoder {
    HashMap<String, String> responseMap;
    ByteArrayHelper byteArrayHelper = new ByteArrayHelper();
    
    public HTTPResponseDecoder(){
        responseMap = new HashMap<String, String>();
    }
    
    /**
     * This method decode the response message(byte array) it received according to the following format
     * the  format of byte array received: Version|sp|status|sp|phrase|cr|lf|
     *                                     header|sp|value|cr|lf|
     *                                     ...
     *                                     header|sp|value|cr|lf|
     *                                     cr|lf|
     *                                     body
     * @param responseBytes
     */                                    
    public void decode(byte[] responseBytes){                       
        //store version
        
        for(byte b : responseBytes){
            System.out.println(b);
        }
                if(responseBytes.length == 0){
            System.out.println("HTTPResponseDecoder EMPTY");
        }
        int i = 0;
        char check_sp = 16;
        char check_cr = 15;
        while(responseBytes[i]!=check_sp){
            i++;
        }
        byte[] version = Arrays.copyOfRange(responseBytes,0,i-1);
        responseMap.put("version", byteArrayHelper.tostring(version));
        
        //store status
        int j = i + 1;
        while(responseBytes[j]!=check_sp){
            j++;
        }
        byte[] status = Arrays.copyOfRange(responseBytes, i+1, j);
        System.out.println("STATUS");
        for(byte b : status){
            System.out.println(b);
        }
        responseMap.put("status", Integer.toString(byteArrayHelper.toInt(status)));
        
        //store phrase
        int k = j + 1;
        while(responseBytes[k]!=check_cr){
            k++;
        }
        byte[] phrase = Arrays.copyOfRange(responseBytes,j+1,k-1);
        responseMap.put("phrase", byteArrayHelper.tostring(phrase));
        
        int n = k + 2;
        while(responseBytes[n]!= (check_cr)){
            //store header
            int m = n;
            while(responseBytes[m]!=check_sp){
                m++;
            }
            byte[] header =  Arrays.copyOfRange(responseBytes, n ,m-1);
            
            //store value
            int x = m + 1;
            while(responseBytes[x]!=check_cr){
                x++;
            }
            byte[] value = Arrays.copyOfRange(responseBytes, m + 1, x - 1);
            responseMap.put(byteArrayHelper.tostring(header), byteArrayHelper.tostring(value));
            
            n = x + 2;
        }
        
        byte[] body = Arrays.copyOfRange(responseBytes, n+2, responseBytes.length);
        responseMap.put("body", byteArrayHelper.tostring(body));
    }
    
    /**
             * Get 'version' information from the byte array received
             * 
             * @return version. if there is no such thing, return empty string
             */
    public float getVersion(){
        try{
            String s = responseMap.get("version");
            return Float.valueOf(s);
        }
        catch(Exception ex){
            System.out.println("String to float - getVersion: failed");
            return -1;
        }
    }
    
    /**
             * Get 'status' information from the byte array received
             * 
             * @return status. if there is no such thing, return empty string
             */
    public int getStatus(){
        try{
            String s = responseMap.get("status");
            System.out.println("IN METHOD STATUS : " + s);
            return Integer.parseInt(s);
        }
        catch(Exception ex){
            System.out.println("NO STATUS");
            return -1;
        }
    }
    
    /**
             * Get 'phrase' information from the byte array received
             * 
             * @return header. if there is no such thing, return empty string
             */
    public String getPhrase(){
        try{
            String s = responseMap.get("phrase");
            return s;
        }
        catch(Exception ex){
            return "";
        }
    }
    
    /**
             * Get 'header' information from the byte array received
             * 
             * @return header. if there is no such thing, return empty string
             */
    public String getHeader(String header){
        try{
            String s = responseMap.get(header);
            return s;
        }
        catch(Exception ex){
            return "";
        }
    }
    
    /**
             * Get 'body' information from the byte array received
             * 
             * @return body. if there is no such thing, return empty string
             */
    public String getBody(){
        try{
            String s = responseMap.get("body");
            return s;
        }
        catch(Exception ex){
            return "";
        }
    }
}
