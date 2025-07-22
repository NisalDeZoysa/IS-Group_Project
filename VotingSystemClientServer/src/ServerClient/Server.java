package ServerClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;

public class Server {
	static int port = 6600;

	public static void main(String[] args) throws Exception {
        System.out.println("Counting Center Server");
        System.out.println("========================");

        // load server private key from the key file
        PrivateKey serverPrivateKey = KeyUtils.loadPrivateKey("server_private.key");
        // load client public key from the key file
        PublicKey clientPublicKey = KeyUtils.loadPublicKey("client_public.key");

        ServerSocket serverSocket = new ServerSocket(port);

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                // read the encrypted vote sent by the client
                String encryptedVote = in.readLine();
                byte[] encryptedVoteBytes = Base64.getDecoder().decode(encryptedVote);
                
                // read the signature of the encrypted vote sent by the client
                String signature = in.readLine();
                byte[] signatureBytes = Base64.getDecoder().decode(signature);

                // verify the signature
                Signature sig = Signature.getInstance("SHA256withRSA");
                sig.initVerify(clientPublicKey);
                sig.update(encryptedVoteBytes);
                boolean isVerified = sig.verify(signatureBytes);

                // decrypt and take vote if the verification is successful 
                if (isVerified) {
                	System.out.println("Signature verification successful");
                	
                	// decrypt the encrypted vote
                	Cipher cipher = Cipher.getInstance("RSA");
                	cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
                	byte[] decryptedBytes = cipher.doFinal(encryptedVoteBytes);
                	
                	String vote = new String(decryptedBytes);
                	System.out.println("Received verified vote: " + vote);
                } else {
                    System.out.println("Signature verification failed");
                }

                socket.close();
            }
        } finally {
            serverSocket.close();
        }
    }
}
