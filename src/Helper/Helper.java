package Helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Helper {
    public static BigInteger[] EEA(BigInteger a, BigInteger b){
        BigInteger[] output = new BigInteger[2];
        BigInteger temp = BigInteger.ZERO;
        BigInteger s = BigInteger.ZERO;
        BigInteger old_s = BigInteger.ONE;
        BigInteger t = BigInteger.ONE;
        BigInteger old_t = BigInteger.ZERO;
        BigInteger r = b;
        BigInteger old_r = a;
        while(!(r.equals(BigInteger.ZERO))){
            BigInteger q = old_r.divide(r);
            temp = r;
            r = old_r.subtract(q.multiply(temp));
            old_r = temp;
    
            temp = s;
            s = old_s.subtract(q.multiply(temp));
            old_s = temp;
    
            temp = t;
            t = old_t.subtract(q.multiply(temp));
            old_t = temp;
        }
        output[0] = old_t;
        output[1] = old_s;
        return output;
    }

    public static BigInteger randomBigInt(BigInteger min, BigInteger max) {
        Random rand = new Random();
        int maxBits = max.bitLength();
        BigInteger res;
        do {
            res = new BigInteger(maxBits, rand);
        } while (res.compareTo(min) < 0 || res.compareTo(max) > 0); // checks if higher than min or lower than
                                                                          // max. inclusive
        return res;
    }

    public static ArrayList<BigInteger> toBinary(BigInteger input) {
        ArrayList<BigInteger> K = new ArrayList<BigInteger>();
        BigInteger temp = input;
        BigInteger temp2;
        int i = 0;
        while (temp.compareTo(BigInteger.ZERO) > 0) { // when k > 0 == 1
            K.add(temp.mod(BigInteger.TWO));
            temp2 = temp.subtract(K.get(i));
            temp = temp2.divide(BigInteger.TWO);
            i++;
        }
        return K;
    }

    public static BigInteger modularExpo(BigInteger a, BigInteger k, BigInteger n) {
        // a^k mod n
        ArrayList<BigInteger> K = toBinary(k);
        if (n.equals(BigInteger.ONE)) {
            return BigInteger.ZERO;
        }
        BigInteger b = BigInteger.ONE;
        if (k.equals(BigInteger.ZERO)) {
            return b;
        }
        BigInteger A = a;
        if (K.get(0).equals(BigInteger.ONE)) {
            b = a;
        }
        for (int i = 1; i < K.size(); i++) {
            A = A.multiply(A).mod(n);
            if (K.get(i).equals(BigInteger.ONE)) {
                b = A.multiply(b).mod(n);

            }
        }
        return b;
    }

    public static BigInteger[] findR(BigInteger n) {
        BigInteger N = n.subtract(BigInteger.ONE);
        BigInteger[] results = new BigInteger[2];
        int count = 0;
        while (N.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            N = N.divide(BigInteger.TWO);
            count++;
        }
        results[0] = N;
        results[1] = new BigInteger(Integer.toString(count));
        return results;
    }


    public static boolean isPrime(BigInteger n, int t) { // n >= 3, t(security parameter) >= 1
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) { // checks for even
            return false;
        }
        // n - 1 = 2^r * s
        BigInteger rAndS[] = findR(n);
        BigInteger r = rAndS[0];
        BigInteger s = rAndS[1];
        for (int i = 0; i < t; i++) {
            BigInteger a = randomBigInt(BigInteger.TWO, n.subtract(BigInteger.TWO)); // random number between 2 and n-2
            BigInteger y = modularExpo(a, r, n);
            if (!y.equals(BigInteger.ONE) && !y.equals(n.subtract(BigInteger.ONE))) { // if y != 1 and y != n-1
                BigInteger j = BigInteger.ZERO;
                while ((j.compareTo(s.subtract(BigInteger.ONE)) < 0) // j <= s-1
                        && !y.equals(n.subtract(BigInteger.ONE)) // y != n-1
                ) {
                    y = modularExpo(y, BigInteger.TWO, n);
                    if (y.equals(BigInteger.ONE)) {
                        return false;
                    }
                    j = j.add(BigInteger.ONE);
                }

                if (!y.equals(n.subtract(BigInteger.ONE))) {
                    return false;
                }
            }

        }
        return true;
    }

    public static BigInteger generatePrime(BigInteger min, BigInteger max) {
        BigInteger val;
        do {
            val = randomBigInt(min, max);
        } while (!isPrime(val, 3));
        return val;
    }

    public static void printArray(String[][] input){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(input[i][j]+ " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    
    public static String[][] binaryToHex(String binary){ // input: 128-bit
        String[][] hex = new String[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                hex[j][i] = Integer.toHexString(
                    Integer.parseInt(binary.substring(k,k+8),2)
                    ); //Binary to Hex
                k+=8;
            }
        }
        return hex;
    }

    public static String binaryToHexString(String binary){
        StringBuilder str = new StringBuilder();
        String temp;
        for(int i = 0; i < 32; i+=8){
            temp = Integer.toHexString(
                Integer.parseInt(binary.substring(i,i+8),2)
                );
            if(temp.length() == 1){
                temp = "0"+temp;
            }
            str.append(temp);
        }
        return str.toString();
    }

    public static String hexToBinaryString(String hex, int bitSize){
        String temp;
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < hex.length(); i+=2){
            temp = Integer.toBinaryString(Integer.parseInt(hex.substring(i, i+2),16));
            temp = "0".repeat(bitSize-temp.length()) + temp;   
            str.append(temp);
        }
        return str.toString();
    }

    public static String hexToBinaryString(String[] hex, int bitSize){
        String temp;
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < hex.length; i++){
            temp = Integer.toBinaryString(Integer.parseInt(hex[i],16));
            temp = "0".repeat(bitSize-temp.length()) + temp;   
            str.append(temp);
        }
        return str.toString();
    }

    public static String hexToBinaryString(String[][] hex, int bitSize){
        String temp;
        String hexString = ArrayToString(hex);
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < hexString.length(); i+=2){
            temp = Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i+2),16));
            temp = "0".repeat(bitSize-temp.length()) + temp;   
            str.append(temp);
        }
        return str.toString();
    }

    public static String xor(String a, String b, int size){
        //padded to 32 bit
        if(a.length() < size){
            a = "0".repeat(size - a.length()) + a;
        }
        if(b.length() < size){
            b = "0".repeat(size - b.length()) + b;
        }
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < size; i++){
            if(a.charAt(i) == b.charAt(i)){
                str.append("0");
            } else {
                str.append("1");
            }
        }
        return str.toString();

    }

    public static String[][] StringTo2dArray(String input){
        String[][] output = new String[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                output[j][i] = input.substring(k,k+2);
                k+=2;
            }
        }
        return output;
    }

    
    public static String ArrayToString(String[][] input){
        StringBuilder str = new StringBuilder();
        String current;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                current = input[j][i];
                if(current.length() == 1){
                    current = "0"+current;
                }
                str.append(current);
            }
        }
        return str.toString();
    }

    public static String FileToBlocks(byte[] bytes){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String val = Integer.toHexString(bytes[i] & 0xFF);
            if(val.length() == 1){
                val = "0" + val;
            }
            str.append(val);
        }
        return str.toString();
    }


    public static byte[] toByteArray(String str){
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length(); i+=2){
            String current = str.substring(i, i+2);
            int currentVal = Integer.parseInt(current, 16);
            if(currentVal >= 128){
                currentVal -= 256;
            }
            bytes[i/2] = (byte) currentVal;
        }
        return bytes;
    }

    public static byte[] toByteArray(ArrayList<String[][]> message){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < message.size(); i++){
            String cipherCurrent = ArrayToString(message.get(i));
            str.append(cipherCurrent);
        }
        return toByteArray(str.toString());
    }
}
