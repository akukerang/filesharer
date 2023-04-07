package encryption;
import java.math.BigInteger;

public class RSA {
    final static BigInteger bit1024 = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137216");
    // final static BigInteger bit1024 = new BigInteger("1797693134862315907729305190789");

    private BigInteger[] publicKey = new BigInteger[2];
    private BigInteger privateKey;

    public RSA(BigInteger[] publicKey, BigInteger privateKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public RSA(Keys key){
        String[] split = key.publicKey.split(" ");
        this.privateKey = new BigInteger(key.privateKey);
        this.publicKey[0] = new BigInteger(split[0]);
        this.publicKey[1] = new BigInteger(split[1]);
    }
    
    public RSA(String privateKey){
        this.privateKey = new BigInteger(privateKey);
    }

    public RSA(String[] publicKey){
        this.publicKey[0] = new BigInteger(publicKey[0]);
        this.publicKey[1] = new BigInteger(publicKey[1]);  
    }


    public static Keys generateKeys(){
        String publicKey;
        String privateKey;
        BigInteger p = Helper.generatePrime(new BigInteger("3"), bit1024);
        BigInteger q = Helper.generatePrime(new BigInteger("3"), bit1024); 
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
        BigInteger[] temp;
        BigInteger e;
        do {
            e = Helper.generatePrime(BigInteger.ONE, phi.subtract(BigInteger.ONE)); //Makes sure D is positive
            temp = Helper.EEA(e, phi);
        } while(temp[1].compareTo(BigInteger.ZERO) < 0);
        BigInteger d = temp[1];
        publicKey = n.toString() + " " + e.toString();
        privateKey = d.toString();
        return new Keys(publicKey, privateKey);
    }

    public String encryptBlock(String message){
        // implement conversion of message to number
        //Message needs to be between 0, n-1.
        BigInteger messageNum = new BigInteger(message);
        BigInteger cipher = Helper.modularExpo(messageNum, this.publicKey[1], this.publicKey[0]);
        return cipher.toString(10);
    }

    public String decryptBlock(String cipher){
        BigInteger message = Helper.modularExpo(new BigInteger(cipher), this.privateKey, this.publicKey[0]);

        return message.toString(10);
    }


    public static void main(String[] args) {
        Keys temp = generateKeys();



        RSA test = new RSA(temp);
        String cipher = test.encryptBlock("12345");
        String message = test.decryptBlock(cipher);
        System.out.println(cipher);
        System.out.println(message);



    }
}
