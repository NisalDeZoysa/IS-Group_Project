package ServerClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	static int port = 6600;

	public static void main(String[] args) throws IOException {
		System.out.println("Voting Center Client");
		System.out.println("=================================");
		
		InetAddress ipAddress = InetAddress.getLocalHost();
		
		Socket socket = new Socket(ipAddress, port);
		
		Scanner scanner = new Scanner(System.in);
		String vote = getVote(scanner);
		
		if (vote == "1" || vote == "2" || vote == "3" || vote == "4") {
			scanner.close();
        } else {
            System.out.println("Invalid input. Please enter 1, 2, 3, or 4");
            vote = getVote(scanner);
        }
		
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(vote);
		
		socket.close();
		
		System.exit(0);
	}
	
	public static String getVote(Scanner scanner) {
		System.out.println("Vote for 1,2, 3 or 4");
    	
    	System.out.print("Enter your vote: ");
		String vote = scanner.nextLine();
		
		return vote;
    }
}
