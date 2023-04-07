package encryption;
public class Keys{
    public String privateKey;
    public String publicKey;
    public Keys(String publicKey, String privateKey){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}