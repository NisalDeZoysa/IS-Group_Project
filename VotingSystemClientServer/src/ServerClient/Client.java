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

    public static void main(String[] args) {
        System.out.println("Voting Center Client");
        System.out.println("========================");

        try {
            PrivateKey clientPrivateKey = KeyUtils.loadPrivateKey("client_private.key");
            PublicKey serverPublicKey = KeyUtils.loadPublicKey("server_public.key");

            Scanner scanner = new Scanner(System.in);

            for (int i = 1; i <= 10; i++) {
                System.out.println("\n--- Voter " + i + " ---");
                String vote = getVote(scanner);

                // Encrypt vote
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
                byte[] encryptedVote = cipher.doFinal(vote.getBytes());

                // Sign the encrypted vote
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initSign(clientPrivateKey);
                signature.update(encryptedVote);
                byte[] signedEncryption = signature.sign();

                // Send to server
                try (Socket socket = new Socket(InetAddress.getLocalHost(), port);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    out.println(Base64.getEncoder().encodeToString(encryptedVote));
                    out.println(Base64.getEncoder().encodeToString(signedEncryption));

                    System.out.println("Vote " + i + " sent successfully.");
                } catch (Exception se) {
                    System.err.println("❌ Error sending vote " + i + ": " + se.getMessage());
                }
            }

            scanner.close();
            System.out.println("\nAll 10 votes sent. Client exiting.");
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getVote(Scanner scanner) {
        String vote = "";
        while (true) {
            System.out.print("Enter your vote (1, 2, 3, or 4): ");
            if (scanner.hasNextLine()) {
                vote = scanner.nextLine().trim();
                if (vote.matches("[1-4]")) {
                    break;
                } else {
                    System.out.println("⚠️ Invalid vote. Please enter 1, 2, 3, or 4.");
                }
            }
        }
        return vote;
    }
}
