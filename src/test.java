import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class test {
    public static void main(String[] args) {


        String filePath = "F:\\repos\\Filesharer\\src\\files\\test.zip";

        // file to byte[], Path
        try{
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            for(int i = 0; i < bytes.length; i++){
                System.out.print(bytes[i] + ", ");
            }

            // Files.write(Paths.get(filePath2), bytes);
        } catch(IOException e)  {
            System.out.println(e);
        }
    }
}
