package encryption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Currency;
public class AES {
    static String[][] sBox = {
        {"63", 	"7c", 	"77", 	"7b", 	"f2", 	"6b", 	"6f", 	"c5", 	"30", 	"01", 	"67", 	"2b", 	"fe", 	"d7", 	"ab", 	"76"},
        {"ca", 	"82", 	"c9", 	"7d", 	"fa", 	"59", 	"47", 	"f0", 	"ad", 	"d4", 	"a2", 	"af", 	"9c", 	"a4", 	"72", 	"c0"},
     	{"b7", 	"fd", 	"93", 	"26", 	"36", 	"3f", 	"f7", 	"cc", 	"34", 	"a5", 	"e5", 	"f1", 	"71", 	"d8", 	"31", 	"15"},
     	{"04", 	"c7", 	"23", 	"c3", 	"18", 	"96", 	"05", 	"9a", 	"07", 	"12", 	"80", 	"e2", 	"eb", 	"27", 	"b2", 	"75"},
     	{"09", 	"83", 	"2c", 	"1a", 	"1b", 	"6e", 	"5a", 	"a0", 	"52", 	"3b", 	"d6", 	"b3", 	"29", 	"e3", 	"2f", 	"84"},
     	{"53", 	"d1", 	"00", 	"ed", 	"20", 	"fc", 	"b1", 	"5b", 	"6a", 	"cb", 	"be", 	"39", 	"4a", 	"4c", 	"58", 	"cf"},
     	{"d0", 	"ef", 	"aa", 	"fb", 	"43", 	"4d", 	"33", 	"85", 	"45", 	"f9", 	"02", 	"7f", 	"50", 	"3c", 	"9f", 	"a8"},
     	{"51", 	"a3", 	"40", 	"8f", 	"92", 	"9d", 	"38", 	"f5", 	"bc", 	"b6", 	"da", 	"21", 	"10", 	"ff", 	"f3", 	"d2"},
     	{"cd", 	"0c", 	"13", 	"ec", 	"5f", 	"97", 	"44", 	"17", 	"c4", 	"a7", 	"7e", 	"3d", 	"64", 	"5d", 	"19", 	"73"},
     	{"60", 	"81", 	"4f", 	"dc", 	"22", 	"2a", 	"90", 	"88", 	"46", 	"ee", 	"b8", 	"14", 	"de", 	"5e", 	"0b", 	"db"},
     	{"e0", 	"32", 	"3a", 	"0a", 	"49", 	"06", 	"24", 	"5c", 	"c2", 	"d3", 	"ac", 	"62", 	"91", 	"95", 	"e4", 	"79"},
     	{"e7", 	"c8", 	"37", 	"6d", 	"8d", 	"d5", 	"4e", 	"a9", 	"6c", 	"56", 	"f4", 	"ea", 	"65", 	"7a", 	"ae", 	"08"},
     	{"ba", 	"78", 	"25", 	"2e", 	"1c", 	"a6", 	"b4", 	"c6", 	"e8", 	"dd", 	"74", 	"1f", 	"4b", 	"bd", 	"8b", 	"8a"},
     	{"70", 	"3e", 	"b5", 	"66", 	"48", 	"03", 	"f6", 	"0e", 	"61", 	"35", 	"57", 	"b9", 	"86", 	"c1", 	"1d", 	"9e"},
     	{"e1", 	"f8", 	"98", 	"11", 	"69", 	"d9", 	"8e", 	"94", 	"9b", 	"1e", 	"87", 	"e9", 	"ce", 	"55", 	"28", 	"df"},
     	{"8c", 	"a1", 	"89", 	"0d", 	"bf", 	"e6", 	"42", 	"68", 	"41", 	"99", 	"2d", 	"0f", 	"b0", 	"54", 	"bb", 	"16"}, 
    };

    static String[][] invSBox = {
        {"52", 	"09", 	"6a", 	"d5", 	"30", 	"36", 	"a5", 	"38", 	"bf", 	"40", 	"a3", 	"9e", 	"81", 	"f3", 	"d7", 	"fb"},
        {"7c", 	"e3", 	"39", 	"82", 	"9b", 	"2f", 	"ff", 	"87", 	"34", 	"8e", 	"43", 	"44", 	"c4", 	"de", 	"e9", 	"cb"},
        {"54", 	"7b", 	"94", 	"32", 	"a6", 	"c2", 	"23", 	"3d", 	"ee", 	"4c", 	"95", 	"0b", 	"42", 	"fa", 	"c3", 	"4e"},
        {"08", 	"2e", 	"a1", 	"66", 	"28", 	"d9", 	"24", 	"b2", 	"76", 	"5b", 	"a2", 	"49", 	"6d", 	"8b", 	"d1", 	"25"},
        {"72", 	"f8", 	"f6", 	"64", 	"86", 	"68", 	"98", 	"16", 	"d4", 	"a4", 	"5c", 	"cc", 	"5d", 	"65", 	"b6", 	"92"},
        {"6c", 	"70", 	"48", 	"50", 	"fd", 	"ed", 	"b9", 	"da", 	"5e", 	"15", 	"46", 	"57", 	"a7", 	"8d", 	"9d", 	"84"},
        {"90", 	"d8", 	"ab", 	"00", 	"8c", 	"bc", 	"d3", 	"0a", 	"f7", 	"e4", 	"58", 	"05", 	"b8", 	"b3", 	"45", 	"06"},
        {"d0", 	"2c", 	"1e", 	"8f", 	"ca", 	"3f", 	"0f", 	"02", 	"c1", 	"af", 	"bd", 	"03", 	"01", 	"13", 	"8a", 	"6b"},
        {"3a", 	"91", 	"11", 	"41", 	"4f", 	"67", 	"dc", 	"ea", 	"97", 	"f2", 	"cf", 	"ce", 	"f0", 	"b4", 	"e6", 	"73"},
        {"96", 	"ac", 	"74", 	"22", 	"e7", 	"ad", 	"35", 	"85", 	"e2", 	"f9", 	"37", 	"e8", 	"1c", 	"75", 	"df", 	"6e"},
        {"47", 	"f1", 	"1a", 	"71", 	"1d", 	"29", 	"c5", 	"89", 	"6f", 	"b7", 	"62", 	"0e", 	"aa", 	"18", 	"be", 	"1b"},
        {"fc", 	"56", 	"3e", 	"4b", 	"c6", 	"d2", 	"79", 	"20", 	"9a", 	"db", 	"c0", 	"fe", 	"78", 	"cd", 	"5a", 	"f4"},
        {"1f", 	"dd", 	"a8", 	"33", 	"88", 	"07", 	"c7", 	"31", 	"b1", 	"12", 	"10", 	"59", 	"27", 	"80", 	"ec", 	"5f"},
        {"60", 	"51", 	"7f", 	"a9", 	"19", 	"b5", 	"4a", 	"0d", 	"2d", 	"e5", 	"7a", 	"9f", 	"93", 	"c9", 	"9c", 	"ef"},
        {"a0", 	"e0", 	"3b", 	"4d", 	"ae", 	"2a", 	"f5", 	"b0", 	"c8", 	"eb", 	"bb", 	"3c", 	"83", 	"53", 	"99", 	"61"},
        {"17", 	"2b", 	"04", 	"7e", 	"ba", 	"77", 	"d6", 	"26", 	"e1", 	"69", 	"14", 	"63", 	"55", 	"21", 	"0c", 	"7d"},
    };


    static int[][] mixPublic = {
        {2,3,1,1},
        {1,2,3,1},
        {1,1,2,3},
        {3,1,1,2}
    };

    static int[][] invMixPublic = {
        {14,11,13,9},
        {9,14,11,13},
        {13,9,14,11},
        {11,13,9,14},
    };

    static int[] rcon = {
        0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36
    };

    String key;
    public AES(String key){
        this.key = key;
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

    public static void bytesToHexBlock(byte[] input) {

        // String[][][] ex = new String[4][4];
        // int k = 0;
        // for(int i = 0; i < 4; i++){
        //     for (int j = 0; j < 4; j++){
        //         hex[i][j] = String.format("%02X", input[k]);
        //         k++;
        //     }
        // } 
        // return hex;
    }
    

    public String[][] subBytes(String[][] block){
        String[][] subBytes = new String[4][4];
        int x, y;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(block[i][j].length() == 1){
                    x = Integer.parseInt("0",16);
                    y = Integer.parseInt(block[i][j].substring(0,1),16);
                } else {
                    x = Integer.parseInt(block[i][j].substring(0,1),16);
                    y = Integer.parseInt(block[i][j].substring(1,2),16);
                }
                subBytes[i][j] = sBox[x][y];
            }
        }
        return subBytes; 
    }

    public static String subBytesKey(String input){
        int x, y;
        String[] hex = new String[4];
        for(int i = 0; i < 32; i+=8){
            x = Integer.parseInt(
                input.substring(i, i+4), 2
            );
            y = Integer.parseInt(
                input.substring(i+4, i+8), 2
            );
            
            hex[i / 8] = sBox[x][y];
        }
        return hexToBinaryString(hex, 8);
    }
    
    public String[][] invSubBytes(String[][] block){
        String[][] subBytes = new String[4][4];
        int x, y;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(block[i][j].length() == 1){
                    x = Integer.parseInt("0",16);
                    y = Integer.parseInt(block[i][j].substring(0,1),16);
                } else {
                    x = Integer.parseInt(block[i][j].substring(0,1),16);
                    y = Integer.parseInt(block[i][j].substring(1,2),16);
                }
                subBytes[i][j] = invSBox[x][y];
            }
        }
        return subBytes; 
    }

    public String[][] shiftRow(String[][] block){
        String[][] shift = new String[4][4];
        shift[0] = block[0]; //no shift
        //shift left once
        shift[1][0] = block[1][1];
        shift[1][1] = block[1][2];
        shift[1][2] = block[1][3];
        shift[1][3] = block[1][0];
        //shift left twice
        shift[2][0] = block[2][2];
        shift[2][1] = block[2][3];
        shift[2][2] = block[2][0];
        shift[2][3] = block[2][1];
        //shift left thrice
        shift[3][0] = block[3][3];
        shift[3][1] = block[3][0];
        shift[3][2] = block[3][1];
        shift[3][3] = block[3][2];
        
        return shift;
    }

    public String[][] invShiftRow(String[][] block){
        String[][] shift = new String[4][4];
        shift[0] = block[0]; //no shift
        //shift right once
        shift[1][0] = block[1][3];
        shift[1][1] = block[1][0];
        shift[1][2] = block[1][1];
        shift[1][3] = block[1][2];
        //shift right twice
        shift[2][0] = block[2][2];
        shift[2][1] = block[2][3];
        shift[2][2] = block[2][0];
        shift[2][3] = block[2][1];
        //shift right thrice
        shift[3][0] = block[3][1];
        shift[3][1] = block[3][2];
        shift[3][2] = block[3][3];
        shift[3][3] = block[3][0];
        return shift;
    }
 


    public static String[][] mixColumn(String[][] block) {
        String[][] mixed = new String[4][4];
        int[] temp = new int[4];
        for (int i = 0; i < 4; i++) { // Column
            for (int j = 0; j < 4; j++) { // Row
                for (int k = 0; k < 4; k++) {
                    int element = Integer.parseInt(block[k][i], 16);
                    int mix = mixPublic[j][k];
                    temp[k] = multiply(element, mix);
                }
                mixed[j][i] = Integer.toHexString((temp[0] ^ temp[1] ^ temp[2] ^ temp[3]) & 0xff); //overflow
            }
        }
        return mixed;
    }

    public static String[][] invMixColumn(String[][] block) {
        String[][] mixed = new String[4][4];
        int[] temp = new int[4];
        for (int i = 0; i < 4; i++) { // Column
            for (int j = 0; j < 4; j++) { // Row
                for (int k = 0; k < 4; k++) {
                    int element = Integer.parseInt(block[k][i], 16);
                    int mix = invMixPublic[j][k];
                    temp[k] = multiply(element, mix);
                }
                mixed[j][i] = Integer.toHexString((temp[0] ^ temp[1] ^ temp[2] ^ temp[3]) & 0xff); //overflow
            }
        }
        return mixed;
    }
    
    private static int multiply(int a, int b) {
        int p = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
            boolean carry = (a & 0x80) != 0;
            a <<= 1;
            if (carry) {
                a ^= 0x1b;
            }
            b >>= 1;
        }
        return p;
    }
    
    public static String[][] addRoundKey(String[][] block, String key){
        String[][] added = new String[4][4];
        String[][] keyArray = binaryToHex(key);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                added[i][j] = Integer.toHexString(
                    Integer.parseInt(block[i][j],16) ^
                    Integer.parseInt(keyArray[i][j],16));
            }
        }
        return added;
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

    public static String addRcon(String input, int rcon){
        String input_section = input.substring(0,8);
        int added = (Integer.parseInt(input_section, 2) ^ rcon) & 0xFF;
        String addedBinary = Integer.toBinaryString(added);
        addedBinary = "0".repeat(8-addedBinary.length()) + addedBinary + input.substring(8,32);
        return addedBinary;
    }

    public static String[] keySchedule(String key){ // Input binary, Output binary
        String[] keys = new String[11];
        StringBuilder str = new StringBuilder();
        keys[0] = "0".repeat(128 - key.length()) + key;
        String temp;
        String temp2;
        for(int i = 1; i < 11; i++){
            //w[3] editing
            temp2 = keys[i-1].substring(104,128) + keys[i-1].substring(96,104); //rotate
            temp2 = subBytesKey(temp2); //subbyte
            temp2 = addRcon(temp2, rcon[i-1]);
            temp = xor(keys[i-1].substring(0,32), //w[0] xor w[3]
                temp2);
            str.append(temp);
            temp = xor(temp, keys[i-1].substring(32,64)); // temp xor w[1]
            str.append(temp);
            temp = xor(temp, keys[i-1].substring(64,96)); // temp xor w[2]
            str.append(temp);
            temp = xor(temp, keys[i-1].substring(96,128)); // temp xor w[3]
            str.append(temp);
            keys[i] = str.toString();

            str = new StringBuilder();
        }
        return keys;
    }

    public static String xor(String a, String b){
        //padded to 32 bit
        if(a.length() < 32){
            a = "0".repeat(32 - a.length()) + a;
        }
        if(b.length() < 32){
            b = "0".repeat(32 - b.length()) + b;
        }
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < 32; i++){
            if(a.charAt(i) == b.charAt(i)){
                str.append("0");
            } else {
                str.append("1");
            }
        }
        return str.toString();

    }


    public String[][] encryptBlock(String[][] block){
        String[] keys = keySchedule(this.key);
        String[][] encrypt = addRoundKey(block, keys[0]);
        for(int i = 0; i < 9; i++){
            //(Rounds based off key size)
            // Byte Substitution
            encrypt = subBytes(encrypt);

            //Shift Row
            encrypt = shiftRow(encrypt);

            // Mix Column
            encrypt = mixColumn(encrypt);

            //Key addition 
            encrypt = addRoundKey(encrypt, keys[i+1]);

        }
        //Final Round
        encrypt = subBytes(encrypt);
        //Shift Row
        encrypt = shiftRow(encrypt);
        //Key addition 
        encrypt = addRoundKey(encrypt, keys[10]);
        //Final Round no mix column
        return encrypt;
    }
    public String[][] decryptBlock(String[][] block){
        String[] keys = keySchedule(this.key);
        String[][] decrypt = addRoundKey(block, keys[10]);
        for(int i = 9; i > 0; i--){
            //(Rounds based off key size)
            //Shift Row
            decrypt = invShiftRow(decrypt);

            // Byte Substitution
            decrypt = invSubBytes(decrypt);
   
            //Key addition 
            decrypt = addRoundKey(decrypt, keys[i]);

            // Mix Column
            decrypt = invMixColumn(decrypt);

        }
        decrypt = invShiftRow(decrypt);

        //Final Round
        decrypt = invSubBytes(decrypt);
        //Shift Row
        //Key addition 
        decrypt = addRoundKey(decrypt, keys[0]);
        //Final Round no mix column

        return decrypt;
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

    public static void main(String[] args) throws IOException{
        String inputKey = "000102030405060708090a0b0c0d0e0f";
        StringBuilder str = new StringBuilder();
        String temp;
        for(int i = 0; i < inputKey.length(); i+=2){
            temp = Integer.toBinaryString(Integer.parseInt(inputKey.substring(i,i+2), 16));
            temp = "0".repeat(8-temp.length()) + temp;
            str.append(temp);   
        }
        String key = str.toString();


     
        String filePath = "F:\\repos\\Filesharer\\src\\files\\pomu.jpg";
        String filePath2 = "F:\\repos\\Filesharer\\src\\files\\encrypted.jpg";
        String filePath3 = "F:\\repos\\Filesharer\\src\\files\\pomuDecrypt.jpg";

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        str = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String val = Integer.toHexString(bytes[i] & 0xFF);
            if(val.length() == 1){
                val = "0" + val;
            }
            str.append(val);
        }
        String hex = str.toString();
        String padded = "0".repeat(32 -(hex.length() % 32)) + hex;

        AES a = new AES(key);
        ArrayList<String[][]> ciphers = new ArrayList<String[][]>();
        for(int i = 0; i < padded.length(); i+=32){
            String[][] current = StringTo2dArray(padded.substring(i, i+32));
            String[][] currentCipher = a.encryptBlock(current);
            ciphers.add(currentCipher);
        }

        
        str = new StringBuilder();
        for(int i = 0; i < ciphers.size(); i++){
            String cipherCurrent = ArrayToString(ciphers.get(i));
            str.append(cipherCurrent);
        }
        String encrypted = str.toString();
        byte[] encryptedBytes = new byte[encrypted.length() / 2];
        for(int i = 0; i < encrypted.length(); i+=2){
            String current = encrypted.substring(i, i+2);
            int currentVal = Integer.parseInt(current, 16);
            if(currentVal >= 128){
                currentVal -= 256;
            }
            encryptedBytes[i/2] = (byte) currentVal;
        }
        Files.write(Paths.get(filePath2), encryptedBytes);





        str = new StringBuilder();
        for(int i = 0; i < ciphers.size(); i++){
            String decryptCurrent = ArrayToString(a.decryptBlock(ciphers.get(i)));
            str.append(decryptCurrent);
        }
        String newHex = str.toString();
        while(newHex.substring(0,2).equals("00")){
            newHex = newHex.substring(2);
        }

        byte[] newB = new byte[newHex.length() / 2];
        for(int i = 0; i < newHex.length(); i+=2){
            String current = newHex.substring(i, i+2);
            int currentVal = Integer.parseInt(current, 16);
            if(currentVal >= 128){
                currentVal -= 256;
            }
            newB[i/2] = (byte) currentVal;
        }
        Files.write(Paths.get(filePath3), newB);

   


    }

}



