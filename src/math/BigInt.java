package math;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
public class BigInt {
    private String value;
    // Constructor    
    public BigInt(String value){
        this.value = value;

    }

    //Getter and Setter
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Helper
    public static BigInt randomBigInt(BigInt min, BigInt max){
        Random rand = new Random();
        BigInteger minBig = new BigInteger(min.getValue());
        BigInteger maxBig = new BigInteger(max.getValue());
        int maxBits = maxBig.bitLength();
        BigInteger res;
        do{
            res = new BigInteger(maxBits, rand);
        } while (res.compareTo(minBig) < 0 || res.compareTo(maxBig) > 0); //checks if higher than min or lower than max. inclusive
        return new BigInt(res.toString());
    }

    public char[] toChar() {
        return this.value.toCharArray();
    } 

    public int length(){
        return this.value.length();
    }


    public static String[] padNumbers(BigInt a, BigInt b){ //Returns two BigInt of same length by padding with zeroes.
        String padded;
        if(a.length() == b.length()){
            return new String[] {a.value, b.value};
        } else if (a.length() > b.length()){
            padded = "0".repeat(a.length() - b.length()) + b.value;
            return new String[] {a.value, padded};
        } else {
            padded = "0".repeat(b.length() - a.length()) + a.value;
            return new String[] {padded, b.value};
        }
    }

    public static BigInt[] findR(BigInt n){
        BigInt N = n.subtract(new BigInt("1"));
        BigInt[] results = new BigInt[2];
        int count = 0;
        while(N.checkEven()){
            N = N.divideBy2();
            count++;
        }
        results[0] = N;
        results[1] = new BigInt(Integer.toString(count));
        return results;
    }

    //Comparison
    public boolean equal(BigInt compare){
        return this.value.equals(compare.value);
    }

    public boolean lessThan(BigInt compare){ // this < compare
        if(this.value.length() < compare.value.length()){
            return true;
        }
        if(this.value.length() > compare.value.length()){
            return false;
        }

        char[] current = this.toChar();
        char[] comparison = compare.toChar();


        for(int i = 0; i < this.length(); i++){
            if(Character.getNumericValue(current[i]) < Character.getNumericValue(comparison[i])){
                return true;
            }
            if(Character.getNumericValue(current[i]) > Character.getNumericValue(comparison[i])){
                return false;
            }
        }
        return false;
    }

    public boolean lessThanEqual(BigInt compare){ // this < compare
        if(this.value.length() < compare.value.length()){
            return true;
        }
        if(this.value.length() > compare.value.length()){
            return false;
        }

        char[] current = this.toChar();
        char[] comparison = compare.toChar();


        for(int i = 0; i < this.length(); i++){
            if(Character.getNumericValue(current[i]) < Character.getNumericValue(comparison[i])){
                return true;
            }
            if(Character.getNumericValue(current[i]) > Character.getNumericValue(comparison[i])){
                return false;
            }
        }
        return true;
    }

    public boolean greaterThan(BigInt compare){ // this < compare
        if(this.value.length() > compare.value.length()){
            return true;
        }
        if(this.value.length() < compare.value.length()){
            return false;
        }

        char[] current = this.toChar();
        char[] comparison = compare.toChar();


        for(int i = 0; i < this.length(); i++){
            if(Character.getNumericValue(current[i]) > Character.getNumericValue(comparison[i])){
                return true;
            }
            if(Character.getNumericValue(current[i]) < Character.getNumericValue(comparison[i])){
                return false;
            }
        }
        return false;
    }

    public boolean greaterThanEqual(BigInt compare){ // this < compare
        if(this.value.length() > compare.value.length()){
            return true;
        }
        if(this.value.length() < compare.value.length()){
            return false;
        }

        char[] current = this.toChar();
        char[] comparison = compare.toChar();


        for(int i = 0; i < this.length(); i++){
            if(Character.getNumericValue(current[i]) > Character.getNumericValue(comparison[i])){
                return true;
            }
            if(Character.getNumericValue(current[i]) < Character.getNumericValue(comparison[i])){
                return false;
            }
        }
        return true;
    }


    //Operations
    public BigInt add(BigInt toAdd){
        String[] padded = padNumbers(this, toAdd);
        StringBuilder sb = new StringBuilder();
        char[] a = padded[0].toCharArray();
        char[] b= padded[1].toCharArray();
        int carry = 0;


        for(int i = a.length-1; i >= 0; i--){
            int current_a = Character.getNumericValue(a[i]);
            int current_b = Character.getNumericValue(b[i]);
            int temp = current_a + current_b + carry;
            sb.append(temp % 10);
            carry = temp / 10;
        }

        if(carry == 0){
            return new BigInt(sb.reverse().toString());
        } else {
            return new BigInt('1' + sb.reverse().toString());
        }

    }

    public BigInt subtract(BigInt toSubtract){
        if(this.equal(toSubtract)){
            return new BigInt("0");
        } else if (this.lessThan(toSubtract)){
            return new BigInt("0");
        }
        else if(toSubtract.equal(new BigInt("0"))){
            return this;
        }
        int carry = 0;
        String[] padded = padNumbers(this, toSubtract);
        StringBuilder sb = new StringBuilder();
        char[] a = padded[0].toCharArray();
        char[] b = padded[1].toCharArray();

        for(int i = a.length-1; i >= 0; i--){
            int a_current = a[i] + carry;
            int b_current = b[i];
            if(a_current < b_current){
                carry = -1;
                a_current += 10;
            } else {
                carry = 0;
            }
            sb.append(a_current - b_current);
        }

        while(sb.charAt(sb.length()-1) == '0'){
            sb.deleteCharAt(sb.length() -1);
        }
        return new BigInt(sb.reverse().toString());
    }

    public BigInt multiply(BigInt toMultiply){
        if(this.value == "0" || toMultiply.value == "0"){
            return new BigInt("0");
        } else if (this.value == "1"){
            return toMultiply;
        } else if (toMultiply.value == "1"){
            return this;
        } 

        String[] padded = padNumbers(this, toMultiply);
        StringBuilder sb = new StringBuilder();
        char[] a = padded[0].toCharArray();
        char[] b = padded[1].toCharArray();
        String tempProduct = "";
        BigInt product = new BigInt("0");
        int carry = 0;
        int temp = 0;
        for(int i = a.length - 1; i >= 0; i--){
            for(int j = a.length - 1; j >= 0; j--){
                int current_a = Character.getNumericValue(a[i]);
                int current_b = Character.getNumericValue(b[j]);

                temp = (current_a * current_b) + carry;
                if(j != 0){
                    carry = temp / 10;
                    temp -= (carry*10);
                    sb.append(temp);
                } else {
                    carry = 0;
                    tempProduct = Integer.toString(temp) + sb.reverse().toString();
                }
            }
            product = product.add(new BigInt(tempProduct+"0".repeat(a.length - 1 - i)));
            sb = new StringBuilder();

        }
        StringBuffer sbuff = new StringBuffer(product.value); //gets rid of leading zeroes
        while(sbuff.charAt(0) =='0'){
            sbuff.deleteCharAt(0);
        }

        return new BigInt(sbuff.toString());
    }

    public BigInt exp2(){
        return this.multiply(this);
    }

    public BigInt divideBy2(){
        if(this.value.equals("1")){
            return new BigInt("0");
        }
        if(this.value.equals("0")){
            return new BigInt("0");
        }
        char[] charList = this.toChar();
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        for(int i = 0; i < charList.length; i++) {
            int current = Character.getNumericValue(charList[i]);
            int digit = current / 2 + carry;
            carry = current % 2 * 5;
            sb.append(digit);
        }
        while(sb.charAt(0) == '0'){
            sb.deleteCharAt(0);
        }        
        return new BigInt(sb.toString());
    }
    
    // public BigInt mod(BigInt modulo){
    //     BigInt remainder = this;
    //     while(remainder.greaterThan(modulo)){
    //         remainder = remainder.subtract(modulo);
    //     }
    //     if(remainder.equal(modulo)){
    //         return new BigInt("0");
    //     }
    //     return remainder;
    // }

    //https://www.geeksforgeeks.org/bigint-big-integers-in-c-with-example/
    public BigInt mod(BigInt modulo){
        if(this.lessThan(modulo)){
            return this;
        }
        if(this.equal(modulo)){
            return new BigInt("0");
        }
        
        int i, lgcat = 0, cc;
        int n = this.length();
        int[] cat = new int[n];
        BigInt t = new BigInt("0");

        for (i = n; t.multiply(new BigInt("10"))
        .add(new BigInt(String.valueOf(this.value.charAt(n-i)))).lessThan(modulo); i--) {
            t = t.multiply(new BigInt("10"));
            t = t.add(new BigInt(String.valueOf(this.value.charAt(n-i))));
        }
        for (; i > 0; i--) {
            t = t.multiply(new BigInt("10"));
            t = t.add(new BigInt(String.valueOf(this.value.charAt(n-i))));

            cc = 9;
            while(new BigInt(Integer.toString(cc)).multiply(modulo).greaterThan(t)){
                cc--;
                if(cc == 0){
                    break;
                }
            }
            if(cc == 0){
                t = t.subtract((new BigInt("0").multiply(modulo)));

            } else {
                t = t.subtract((new BigInt(Integer.toString(cc)).multiply(modulo)));
            }

            cat[lgcat++] = cc;
        }
        return t;
    }

    public boolean checkEven(){
        char[] chars = this.toChar();
        if(chars[chars.length -1] % 2 == 0){
            return true;
        } else {
            return false;
        }
    }


    public static ArrayList<BigInt> toBinary(BigInt input){
        ArrayList<BigInt> K = new ArrayList<BigInt>();
        BigInt temp = input;
        BigInt temp2;
        int i = 0;
        while(temp.greaterThan(new BigInt("0"))){ // when k > 0 == 1
            K.add(temp.checkEven() ? new BigInt("0") : new BigInt("1"));       
            temp2 = temp.subtract(K.get(i));
            temp = temp2.divideBy2();
            i++;


        }
        return K;
    }

    public static BigInt modularExpo(BigInt a, BigInt k, BigInt n){
        // a^k mod n
        ArrayList<BigInt> K = toBinary(k);
        if(n.equal(new BigInt("1"))){
            return new BigInt("0");
        }
        BigInt b = new BigInt("1");
        if(k.equal(new BigInt("0"))){
            return b;
        }
        BigInt A = a;
        if(K.get(0).equal(new BigInt("1"))){
            b = a;
        }
        for(int i = 1; i < K.size(); i++){
            A = A.multiply(A).mod(n);
            if(K.get(i).equal(new BigInt("1"))){
                b = A.multiply(b).mod(n);

            }

        }
        return b;
    }

    public static boolean isPrime(BigInt n, int t){ // n >= 3, t(security parameter) >= 1
        if(n.checkEven()){ //checks for even
            return false;
        }
        // n - 1 = 2^r * s
        BigInt rAndS[] = findR(n);
        BigInt r = rAndS[0];
        BigInt s = rAndS[1];
        for(int i = 0; i < t; i++){
            BigInt a = randomBigInt(new BigInt("2"), n.subtract(new BigInt("2"))); // random number between 2 and n-2
            BigInt y = modularExpo(a, r, n);
            if(!y.equal(new BigInt("1")) && !y.equal(n.subtract(new BigInt("1")))){ // if y != 1 and y != n-1
                BigInt j = new BigInt("0");
                while(
                    j.lessThanEqual(s.subtract(new BigInt("1")))  // j <= s-1
                    && !y.equal(n.subtract(new BigInt("1"))) //y != n-1
                )
                {
                    y = modularExpo(y, new BigInt("2"), n);
                    if(y.equal(new BigInt("1"))){
                        return false;
                    }
                    j = j.add(new BigInt("1"));
                }

                if(!y.equal(n.subtract(new BigInt("1")))){
                    return false;
                }
            }
          
        }
        return true;

    }
    
    // public static byte[] generateRandomNumber(int numBits) {

    // }

    public static void main(String[] args) {

    }
    

}

