package encryption;
import math.BigInt;
public class AES {
    String[][] sBox = {
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

    BigInt key;
    public AES(String key){
        this.key = new BigInt("key"); 
        // 128 bit key - 10 rounds
        // 192 bit key - 12 rounds
        // 256 bit key - 14 rounds
    }

    public static String[][] bytesToHex(byte[] input) {
        String[][] hex = new String[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                hex[i][j] = String.format("%02X", input[k]);
                k++;
            }
        } 
        return hex;
    }
    

    public String[][] subBytes(String[][] block){
        String[][] subBytes = new String[4][4];
        int x, y;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                x = Integer.parseInt(block[i][j].substring(0,1),16);
                y = Integer.parseInt(block[i][j].substring(1,2),16);
                subBytes[i][j] = sBox[x][y];
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

    public static String[][] mixColumn(String[][] block){
        String[][] mixed = new String[4][4];


        return mixed;
    }


    public int encryptBlock(String block){
        //convert message to numbers.
        if(block.length() < 128){ 
            block = "0".repeat(128 - block.length()) + block;
        }



        for(int i = 0; i < 9; i++){
        //(Rounds based off key size)
        // Byte Substitution

        //Shift Row
        // Mix Column
        //Key addition 
        }
        //Final Round no mix column
        return 0;
    }

    public String decrypt(){

        return "";
    }

    public static void main(String[] args) {
        byte[] temp = {-128,32,12,12,12,-23,-1,0,-128,32,12,12,12,-23,-1,0};
        for(int i = 0; i < temp.length; i++){
            System.out.println(Byte.toUnsignedInt(temp[i]));
        }
        String[][] temp2 = bytesToHex(temp);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(temp2[i][j]+ " ");
            }
            System.out.println();
        }
    }

}

