/*
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
    static final int MAX_VOTES = 10;

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
*/

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
    static final int MAX_VOTES = 10;

    static Map<String, Integer> voteResults = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Counting Center Server");
        System.out.println("========================");
        System.out.println("Waiting for " + MAX_VOTES + " votes...\n");

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
                    voteCount++;
                    System.out.println("Vote #" + voteCount + " received and verified: " + vote);

                    voteResults.put(vote, voteResults.getOrDefault(vote, 0) + 1);
                    
                    // Show progress
                    System.out.println("Progress: " + voteCount + "/" + MAX_VOTES + " votes received");
                    
                } else {
                    System.out.println("❌ Signature verification failed for a vote");
                }

                socket.close();
            }
        } finally {
            serverSocket.close();
        }

        // Display final results
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         FINAL ELECTION RESULTS");
        System.out.println("=".repeat(40));
        
        // Sort results by candidate number for better display
        for (int i = 1; i <= 4; i++) {
            String candidate = String.valueOf(i);
            int votes = voteResults.getOrDefault(candidate, 0);
            System.out.println("Candidate " + candidate + ": " + votes + " vote(s)");
        }
        
        System.out.println("-".repeat(40));
        System.out.println("Total votes counted: " + voteCount);
        
        // Find and display winner
        String winner = "";
        int maxVotes = 0;
        for (Map.Entry<String, Integer> entry : voteResults.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                winner = entry.getKey();
            }
        }
        
        if (!winner.isEmpty()) {
            System.out.println("\n🏆 WINNER: Candidate " + winner + " with " + maxVotes + " vote(s)!");
        }
        
        System.out.println("\nVoting session completed. Server shutting down.");
    }
}