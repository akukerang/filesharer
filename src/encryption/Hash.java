package encryption;

public class Hash {
    static String IV = "a65bf65d2b18a5b48fcdff8df54796d6";
    
    private static String binaryToHexString(String binary, int bitSize){
        StringBuilder str = new StringBuilder();
        String temp;
        for(int i = 0; i < bitSize; i+=4){
            temp = Integer.toHexString(
                Integer.parseInt(binary.substring(i,i+4),2)
                );
            str.append(temp);
        }
        return str.toString();
    }


    private static String[] pad(String message){ //plaintext -> Array of 128-bit hexadecimal
        StringBuilder str = new StringBuilder();
        String temp;
        for(int i = 0; i < message.length(); i++){
            temp = Integer.toHexString(message.charAt(i));
            if(temp.length() == 1){
                temp = "0"+temp;
            }
            str.append(temp);
        }
        String padded = "0".repeat(32 - (str.toString().length() % 32)) + str.toString();
        String[] blocks = new String[padded.length()/32];
        for(int i = 0; i< padded.length(); i+=32){
            blocks[i/32] = padded.substring(i, i+32);
        }

        return blocks;
    }

    public static String HashMessage(String message){ //Davies Meyer
        String[] blocks = pad(message);
        AES a;
        // H0 = IV
        // H1 = E_x0 (H0) xor H0
        // H2 = E_x1 (H1) xor H1
        a = new AES(Helper.hexToBinaryString(blocks[0],8), 2);
        String[][] encrypted = a.encryptBlock(Helper.StringTo2dArray(IV)); // Encryption output String[][] of hex
        String temp = Helper.hexToBinaryString(encrypted, 8); // converts to binary
        String xor = Helper.xor(temp, Helper.hexToBinaryString(IV, 8), 128); //xor h_i-1 with E(h_i-1)
        String prev = binaryToHexString(xor, 128); //converts back to 2d array, also storing previous H
        for(int i = 1; i < blocks.length; i++){
            a = new AES(Helper.hexToBinaryString(blocks[i],8));
            String[][] prevBlocks = Helper.StringTo2dArray(prev);
            encrypted = a.encryptBlock(prevBlocks);
            temp = Helper.hexToBinaryString(encrypted, 8); // converts to binary
            xor = Helper.xor(temp, Helper.hexToBinaryString(prev, 8), 128); //xor h_i-1 with E(h_i-1)
            prev = binaryToHexString(xor, 128); //converts back to 2d array, also storing previous H

        }
        return prev;
    }    

}
