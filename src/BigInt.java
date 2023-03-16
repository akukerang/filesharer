import java.math.BigInteger;
import java.util.ArrayList;

public class BigInt {
    private String value;
    // Constructor    
    BigInt(String value){
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
    
    public BigInt mod(BigInt modulo){
        BigInt remainder = this;
        while(remainder.greaterThan(modulo)){

            remainder = remainder.subtract(modulo);
        }
        if(remainder.equal(modulo)){
            return new BigInt("0");
        }
        return remainder;
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




    public static void main(String[] args) {
        BigInt a = new BigInt("8902348093240329");
        BigInt b = new BigInt("1");
        ArrayList<BigInt> aee = toBinary(a);
        // System.out.println(a.mod(new BigInt("2")).value);

        for(int i = aee.size() -1; i >= 0; i--){
            System.out.print(aee.get(i).value);
        }

    }


}

