package encryption;
import java.math.BigInteger;
public class RSA {
    // final static BigInteger bit1024 = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137216");
    final static BigInteger bit1024 = new BigInteger("12389120931238901");

    private BigInteger[] publicKey = new BigInteger[2];
    private BigInteger privateKey;

    private BigInteger p;
    private BigInteger q;
    private BigInteger n; //p * q
    private BigInteger phi; // (p-1) * (q-1)
    private BigInteger e; // [1, phi]
    private BigInteger d; // EEA (e, phi)

    public RSA(){
        generateValues();
    }
    private void generateValues(){
        this.p = Helper.generatePrime(new BigInteger("3"), bit1024);
        this.q = Helper.generatePrime(new BigInteger("3"), bit1024); 
        this.n = p.multiply(q);
        this.phi = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
        BigInteger[] temp;
        do {
            this.e = Helper.generatePrime(BigInteger.ONE, this.phi.subtract(BigInteger.ONE)); //Makes sure D is positive
            temp = Helper.EEA(this.e, this.phi);
        } while(temp[1].compareTo(BigInteger.ZERO) < 0);
        this.d = temp[1];
        this.publicKey[0] = this.n;
        this.publicKey[1] = this.e;
        this.privateKey = this.d;
    }

    public void print(){
        System.out.println("Public Key: "+this.publicKey[0] + " " + this.publicKey[1]);
        System.out.println("Private Key: "+this.privateKey);
        System.out.println("P: "+this.p);
        System.out.println("Q: "+this.q);
        System.out.println("N: "+this.n);
        System.out.println("Phi: "+this.phi);
        System.out.println("E: "+this.e);
        System.out.println("D: "+this.d);
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
        RSA test = new RSA();
        String cipher = test.encryptBlock("12345");
        String message = test.decryptBlock(cipher);
        System.out.println(cipher);
        System.out.println(message);


    }
}
