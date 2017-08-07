
//package src;

import java.io.*;
import java.util.*;
import java.lang.Math;
import java.math.BigInteger;

public class Programming1 {
    
    
    /*The countingSortMethod is similar to the method described in the textbook.
      It takes in as inputs A ~ The array of ints to be sorted, and k~ The the base of each integer.
    */
    public static List<Integer> countingSort(int[] A, int k){
      
        Integer[] C = new Integer[k];
        Integer[] B = new Integer[A.length];
        
        for(int i = 1; i < k; i++){
            C[i] = 0;           
        }
        
        for(int j = 1; j < A.length; j++){
                C[A[j]] = C[A[j]] + 1;
        }
        
        for(int i = 2; i < k;i++){
            C[i] = C[i] + C[i-1];
        }
        
        for(int j = A.length-1; j>=1; j--){
            B[C[A[j]]] = A[j];
            C[A[j]]= C[A[j]]-1;
        }
        
        List <Integer> d = new LinkedList<Integer>(Arrays.asList(B));
       
        d.remove(0);
        
        return d;
    }
    
    /*The convertToInts method converts a string into an array of integers. 
      The integers are in the range 1-27
    */
    public static int[] convertToInts(String word){
        
        char[] ch = word.toCharArray();
        int[] ints = new int[ch.length +1];
        ints[0] = 0;
        
        for(int i=1; i<ints.length; i++){
            ints[i] = Character.getNumericValue(ch[i-1])-9;
        }
       
        return ints;
    }
    /*
    The base26 method converts a List of integers in to a unique number value.
    BigInteger is used as the values can become very high depending on the length of the word
    */
    public static BigInteger base26(List<Integer> word){
      
        BigInteger total = BigInteger.valueOf(0);
        int exponent = word.size() - 1;
     
        for(int i=0; i<word.size(); i++){
            
            BigInteger charValue = BigInteger.valueOf(word.get(i));
            BigInteger exp = BigInteger.valueOf(26).pow(exponent);
            BigInteger result = charValue.multiply(exp);
            total = total.add(result);
            exponent--;
        }
       
        return total;
    }
   
    public static void main(String[] args) {
        
        BufferedReader in;
        String newFile;
        
        int dictLength; //The dictLength is a prime number near input size 
        
        if(args[0].equals("dict1.txt")){
            dictLength = 73303; 
            newFile = "anagram1.txt";
        }
        else{
            dictLength = 6757477;
            newFile = "anagram2.txt";
        }
        
       //Initialize table to twice input size
        BigInteger tableSize = BigInteger.valueOf(2* dictLength);  
        
        List<String>[] hashTable = new ArrayList[tableSize.intValue()];

        
        try{
        
        in = new BufferedReader(new FileReader(args[0]));
        
        String line;
      
        while((line = in.readLine()) != null){
            
            //Compute the hash function value
            BigInteger hashValue = base26(countingSort(convertToInts(line),27)).mod(tableSize);
            int lineVal = base26(countingSort(convertToInts(line),27)).intValue();
            
            //Insert word into table if slot is null
            if(hashTable[hashValue.intValue()] == null){
                hashTable[hashValue.intValue()] = new ArrayList<String>(Arrays.asList(line));
            }
            
            else{
                
                boolean found = false;
                int bucket = hashValue.intValue();
                
                //Use quadratic probing to determine which slot to place the word in
                if(base26(countingSort(convertToInts(hashTable[bucket].get(0)),27)).intValue() != lineVal){
                    
                    BigInteger hk = base26(countingSort(convertToInts(line),27));
                    BigInteger c1 = BigInteger.valueOf(5);
                    BigInteger c2 = BigInteger.valueOf(3);
                    BigInteger i = BigInteger.valueOf(1);
                    BigInteger one = c1.multiply(i);
                    BigInteger two = c2.multiply(i.pow(2));
                    
                    
                    while(found == false){
                        
                        BigInteger total = (hk.add(c1.multiply(i)).add(c2.multiply(i.pow(2)))).mod(tableSize);
                        bucket = total.intValue();
                        i = BigInteger.valueOf(i.intValue() +1);
                        if(bucket == hashTable.length-1){
                            bucket = 0;
                        }
                        
                        if(hashTable[bucket] == null){
                            hashTable[bucket] = new ArrayList<String>(Arrays.asList(line));
                            found = true;
                            
                        }
                        else if(base26(countingSort(convertToInts(hashTable[bucket].get(0)),27)).intValue() == lineVal){
              
                            hashTable[bucket].add(line);
                            found = true;
                            
                        }
                     
                    }
                    
                }
                else{
                 
                    hashTable[hashValue.intValue()].add(line);
                    
                }
                
            }

        }

        }
        
        catch (Exception e) {
            System.out.println(e);
        }
        
        int count = 0;
        try{
           //Write the anagram classes to a file
            
            FileWriter writer = new FileWriter(newFile);
            for(int i = 0; i < hashTable.length; i++){
                if(hashTable[i]!=null && (hashTable[i].get(0) != " ")){
                        writer.write(hashTable[i].toString());
                        writer.write("\n");
                        count +=1;
                }

            }
          
        }
        catch (Exception e) {
            System.out.println(e);
        }
        
        System.out.println(count);
    }
    
}
