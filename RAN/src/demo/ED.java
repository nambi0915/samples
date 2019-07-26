package demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ED {
    public static void main(String[] args) throws IOException {
	byte[] fileBytes = Files.readAllBytes(Paths.get("resources\\pwd.txt"));
	String encodedPassword = new String(fileBytes);

	// String encodedStr =
	// Base64.getEncoder().encodeToString(encodedPassword.getBytes());
	// String nambi = "TmFtYmk=";
	// System.out.println(encodedStr);

	String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));
	System.out.println(decodedPassword);
    }
}
