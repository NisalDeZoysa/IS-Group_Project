package ServerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	static int port = 6600;

	public static void main(String[] args) throws IOException {
		System.out.println("Counting Center Server");
		System.out.println("=================================");
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		try {
			while(true) {
				Socket socket = serverSocket.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input = in.readLine();
				
				System.out.println("Voting Center says: " + input);
				
				socket.close();
				in.close();
			}
		}finally {
			serverSocket.close();
		}
	}
}
