package ServerClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class Server {
    static int port = 6600;
    static int voteCount = 0;
    static final int MAX_VOTES = 4;

    static Map<String, Integer> voteResults = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Counting Center Server");
        System.out.println("========================");

        PrivateKey serverPrivateKey = KeyUtils.loadPrivateKey("server_private.key");
        PublicKey clientPublicKey = KeyUtils.loadPublicKey("client_public.key");

        ServerSocket serverSocket = new ServerSocket(port);

        try {
            while (voteCount < MAX_VOTES) {
                Socket socket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String encryptedVote = in.readLine();
                byte[] encryptedVoteBytes = Base64.getDecoder().decode(encryptedVote);

                String signature = in.readLine();
                byte[] signatureBytes = Base64.getDecoder().decode(signature);

                Signature sig = Signature.getInstance("SHA256withRSA");
                sig.initVerify(clientPublicKey);
                sig.update(encryptedVoteBytes);
                boolean isVerified = sig.verify(signatureBytes);

                if (isVerified) {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
                    byte[] decryptedBytes = cipher.doFinal(encryptedVoteBytes);

                    String vote = new String(decryptedBytes);
                    System.out.println("Received verified vote: " + vote);

                    voteResults.put(vote, voteResults.getOrDefault(vote, 0) + 1);
                    voteCount++;
                } else {
                    System.out.println("Signature verification failed");
                }

                socket.close();
            }
        } finally {
            serverSocket.close();
        }

        // Display results after collecting all votes
        System.out.println("\n===== Final Vote Count =====");
        for (Map.Entry<String, Integer> entry : voteResults.entrySet()) {
            System.out.println("Candidate " + entry.getKey() + ": " + entry.getValue() + " vote(s)");
        }
    }
}
