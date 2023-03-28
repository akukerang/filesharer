package encryption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Helper {
    static BigInteger[] EEA(BigInteger a, BigInteger b){
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
}