package ServerClient;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

public class Client {
	static int port = 6600;

	public static void main(String[] args) throws Exception {
        System.out.println("Voting Center Client");
        System.out.println("========================");

        // load client private key from the key file
        PrivateKey clientPrivateKey = KeyUtils.loadPrivateKey("client_private.key");
        // load server public key from the key file
        PublicKey serverPublicKey = KeyUtils.loadPublicKey("server_public.key");

        InetAddress ipAddress = InetAddress.getLocalHost();
        Socket socket = new Socket(ipAddress, port);

        Scanner scanner = new Scanner(System.in);
        String vote = getVote(scanner);
        
        // encrypt then sign approach
        // encrypt the vote
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
        byte[] encryptedVote = cipher.doFinal(vote.getBytes());

        // sign the encrypted vote
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(clientPrivateKey);
        signature.update(encryptedVote);
        byte[] signedEncryption = signature.sign();
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(Base64.getEncoder().encodeToString(encryptedVote));
        out.println(Base64.getEncoder().encodeToString(signedEncryption));

        socket.close();
        scanner.close();
    }
	
	public static String getVote(Scanner scanner) {
		System.out.println("Vote for 1,2, 3 or 4");
    	
    	System.out.print("Enter your vote: ");
		String vote = scanner.nextLine();
		
		return vote;
    }
}
