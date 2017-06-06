package aes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    public byte[] readFile(String fileName) {
    	try {
			Path fileLocation = Paths.get("./" + fileName);
			return Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			System.out.println("Could not read file " + fileName);
			//e.printStackTrace();
		}
		return null;
    }
    
    public void writeFile(byte[] data, String fileName) {
		try {
			Path fileDestination = Paths.get("./" + fileName); 
			Files.write(fileDestination, data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
