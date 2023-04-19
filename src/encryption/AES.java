package encryption;
import java.math.BigInteger;
import java.util.ArrayList;

import Helper.Helper;

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
        String temp = new BigInteger(key,10).toString(2);
        temp = "0".repeat(192 - temp.length()) + temp;
        this.key = temp;
    }
    public AES(String key, int radii){
        switch(radii){
            case 2:
                this.key = key;
                break;
            case 10:
                String temp = new BigInteger(key,10).toString(2);
                temp = "0".repeat(192 - temp.length()) + temp;
                this.key = temp;
                break;
        } 
    }


    private String[][] subBytes(String[][] block){
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

    private static String subBytesKey(String input){
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
        return Helper.hexToBinaryString(hex, 8);
    }
    
    private String[][] invSubBytes(String[][] block){
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

    private String[][] shiftRow(String[][] block){
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

    private String[][] invShiftRow(String[][] block){
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
 


    private static String[][] mixColumn(String[][] block) {
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

    private static String[][] invMixColumn(String[][] block) {
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
    
    private static String[][] addRoundKey(String[][] block, String key){
        String[][] added = new String[4][4];
        String[][] keyArray = Helper.binaryToHex(key);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                added[i][j] = Integer.toHexString(
                    Integer.parseInt(block[i][j],16) ^
                    Integer.parseInt(keyArray[i][j],16));
            }
        }
        return added;
    }

    private static String addRcon(String input, int rcon){
        String input_section = input.substring(0,8);
        int added = (Integer.parseInt(input_section, 2) ^ rcon) & 0xFF;
        String addedBinary = Integer.toBinaryString(added);
        addedBinary = "0".repeat(8-addedBinary.length()) + addedBinary + input.substring(8,32);
        return addedBinary;
    }

    private static String keySchedule(String key){ // Input binary, Output binary
        String[] keys = new String[11];
        StringBuilder str = new StringBuilder();
        keys[0] = "0".repeat(192 - key.length()) + key;
        String temp;
        String temp2;
        String output = keys[0];
        for(int i = 1; i < 9; i++){
            //w[5] editing
            temp2 = keys[i-1].substring(168,192) + keys[i-1].substring(160,168); //rotate
            temp2 = subBytesKey(temp2); //subbyte
            temp2 = addRcon(temp2, rcon[i-1]);
            temp = Helper.xor(keys[i-1].substring(0,32), temp2, 32);//w[0] xor w[3]
            str.append(temp);
            temp = Helper.xor(temp, keys[i-1].substring(32,64), 32); // temp xor w[1]
            str.append(temp);
            temp = Helper.xor(temp, keys[i-1].substring(64,96), 32); // temp xor w[2]
            str.append(temp);
            temp = Helper.xor(temp, keys[i-1].substring(96,128), 32); // temp xor w[3]
            str.append(temp);
            temp = Helper.xor(temp, keys[i-1].substring(128,160), 32); // temp xor w[4]
            str.append(temp);
            temp = Helper.xor(temp, keys[i-1].substring(160,192), 32); // temp xor w[5]
            str.append(temp);
            keys[i] = str.toString();
            output += str.toString();
            str = new StringBuilder();
        }
        return output;
    }

    public String[][] encryptBlock(String[][] block){
        String keys = keySchedule(this.key);
        String[][] encrypt = addRoundKey(block, keys.substring(0, 128));

        for(int i = 1; i < 12; i++){
            //(Rounds based off key size)
            // Byte Substitution
            encrypt = subBytes(encrypt);

            //Shift Row
            encrypt = shiftRow(encrypt);

            // Mix Column
            encrypt = mixColumn(encrypt);

            //Key addition 
            encrypt = addRoundKey(encrypt, keys.substring(i*128, i*128+128));
        }
        //Final Round
        encrypt = subBytes(encrypt);
        //Shift Row
        encrypt = shiftRow(encrypt);
        //Key addition 

        encrypt = addRoundKey(encrypt, keys.substring(1536, 1664));
        //Final Round no mix column

        return encrypt;
    }
    
    public String[][] decryptBlock(String[][] block){
        String keys = keySchedule(this.key);
        String[][] decrypt = addRoundKey(block, keys.substring(1536, 1664));
        System.out.println(new BigInteger(keys.substring(1536, 1664), 2).toString(16));
        System.out.println("Round 0: " +Helper.ArrayToString(decrypt));
        for(int i = 11; i > 0; i--){
            //(Rounds based off key size)
            //Shift Row
            decrypt = invShiftRow(decrypt);

            // Byte Substitution
            decrypt = invSubBytes(decrypt);
   
            //Key addition 

            decrypt = addRoundKey(decrypt, keys.substring(i*128, i*128+128));

            // Mix Column
            decrypt = invMixColumn(decrypt);


        }
        decrypt = invShiftRow(decrypt);

        //Final Round
        decrypt = invSubBytes(decrypt);
        //Shift Row
        //Key addition 
        decrypt = addRoundKey(decrypt, keys.substring(0, 128));
        //Final Round no mix column

        return decrypt;
    }

    public byte[] encryptFile(byte[] bytes){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String val = Integer.toHexString(bytes[i] & 0xFF);
            if(val.length() == 1){
                val = "0" + val;
            }
            str.append(val);
        }
        String blocks = "0".repeat(32 -(str.toString().length() % 32)) + str.toString();
        ArrayList<String[][]> ciphers = new ArrayList<String[][]>();
        for(int i = 0; i < blocks.length(); i+=32){
            String[][] current = Helper.StringTo2dArray(blocks.substring(i, i+32));
            String[][] currentCipher = this.encryptBlock(current);
            ciphers.add(currentCipher);
        }
        return Helper.toByteArray(ciphers);
    }

    public byte[] decryptFile(byte[] ciphers){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ciphers.length; i++) {
            String val = Integer.toHexString(ciphers[i] & 0xFF);
            if(val.length() == 1){
                val = "0" + val;
            }
            str.append(val);
        }
        String cipherString = str.toString();
        str = new StringBuilder();
        for(int i = 0; i < cipherString.length(); i+=32){
            String decryptCurrent = Helper.ArrayToString(
                this.decryptBlock(Helper.StringTo2dArray(cipherString.substring(i, i+32)))
            );
            str.append(decryptCurrent);
        }

        String removePadding = str.toString();
        while(removePadding.substring(0,2).equals("00")){
            removePadding = removePadding.substring(2);
        }
        return Helper.toByteArray(removePadding);
    }

    public String encryptString(String input){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < input.length(); i++){ // getting ascii hex values
            int temp = (int) input.charAt(i);
            str.append(Integer.toHexString(temp));
        }
        String hex = str.toString();
        hex = "0".repeat(32 - hex.length() % 32)+hex; //padding to block
        str = new StringBuilder();
        for(int i = 0; i < hex.length(); i+=32){ // encrypting blocks
            String[][] current = Helper.StringTo2dArray(hex.substring(i, i+32));
            String[][] cipherCurrent = this.encryptBlock(current);
            str.append(Helper.ArrayToString(cipherCurrent));
        }
        return str.toString();
    }

    public String decryptString(String cipher){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < cipher.length(); i+=32){
            String[][] current = Helper.StringTo2dArray(cipher.substring(i, i+32));
            String[][] decryptCurrent = this.decryptBlock(current);
            str.append(Helper.ArrayToString(decryptCurrent));
        }
        String decryptedHex = str.toString();
        while(decryptedHex.substring(0, 2).equals("00")){
            decryptedHex = decryptedHex.substring(2);
        }
        str = new StringBuilder();
        for(int i = 0; i < decryptedHex.length(); i+=2){
            char c = (char) Integer.parseInt(decryptedHex.substring(i, i+2), 16);
            str.append(c);
        }
        return str.toString();
    }

}



