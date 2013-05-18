package sysc3303.tftp_project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Korey Conway (100838924)
 * @author Monisha (100871444)
 * @author Arzaan (100826631)
 */

public class ErrorSimulator {
	protected InetAddress serverAddress;
	protected int serverRequestPort = 6900;
	protected int clientRequestPort = 6800;

	protected boolean stopping = false;

	/**
	 * Constructor
	 */
	public ErrorSimulator() {
		try {
			serverAddress = InetAddress.getLocalHost();
			new RequestReceiveThread().start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ErrorSimulator errorSimulator = new ErrorSimulator();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("Command: ");
			String command = scanner.nextLine();

			// Continue if blank line was passed
			if (command.length() == 0) {
				continue;
			}

			if (command.equals("help")) {
				System.out.println("Available commands:");
				System.out.println("    help: prints this help menu");
				System.out
						.println("    stop: stop the server (when current transfers finish)");
			} else if (command.equals("stop")) {
				System.out
						.println("Stopping simulator (when current transfers finish)");
				errorSimulator.stop();
			} else {
				System.out
						.println("Invalid command. These are the available commands:");
				System.out.println("    help: prints this help menu");
				System.out
						.println("    stop: stop the error simulator (when current transfers finish)");
			}
		}
	}

	public void stop() {
		stopping = true;
	}

	protected class RequestReceiveThread extends Thread {
		protected DatagramSocket socket;
		protected static final int maxPacketSize = 100;

		public RequestReceiveThread() {
			try {
				socket = new DatagramSocket(clientRequestPort);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (!stopping) {
					byte[] data = new byte[maxPacketSize];
					DatagramPacket dp = new DatagramPacket(data, data.length);
					socket.receive(dp);
					new ForwardThread(dp).start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected class ForwardThread extends Thread {
		protected DatagramSocket socket;
		protected int timeoutMs = 10000; // 10 second receive timeout
		protected DatagramPacket requestPacket;
		protected InetAddress clientAddress;
		protected int clientPort, serverPort;

		ForwardThread(DatagramPacket requestPacket) {
<<<<<<< HEAD
			try {
				this.requestPacket = requestPacket;
				clientSocket = new DatagramSocket(requestPacket.getPort());
				clientSocket.setSoTimeout(timeoutMs);
				serverSocket = new DatagramSocket();
				serverSocket.setSoTimeout(timeoutMs);

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
=======
			this.requestPacket = requestPacket;
>>>>>>> 3d0953c2bffcc5a4354f299d87d545c95c989dd4
		}

		public void run() {
			try {
				socket = new DatagramSocket();
				socket.setSoTimeout(timeoutMs);
				clientAddress = requestPacket.getAddress();
				clientPort = requestPacket.getPort();

				// Send request to server
				DatagramPacket dp = new DatagramPacket(requestPacket.getData(),
						requestPacket.getLength(), serverAddress,
						serverRequestPort);
<<<<<<< HEAD
				serverSocket.send(dp);
=======
				socket.send(dp);
>>>>>>> 3d0953c2bffcc5a4354f299d87d545c95c989dd4

				// Receive from server
				dp = Packet.createDatagramForReceiving();
				socket.receive(dp);
				serverPort = dp.getPort();

				while (true) {
					// Forward to client
					dp = new DatagramPacket(dp.getData(), dp.getLength(),
							clientAddress, clientPort);
					socket.send(dp);

					// Wait for response from client
					dp = Packet.createDatagramForReceiving();
					socket.receive(dp);

					// Forward to server
					dp = new DatagramPacket(dp.getData(), dp.getLength(),
							serverAddress, serverPort);
					socket.receive(dp);
				}
			} catch (SocketTimeoutException e) {
				System.out
						.println("Socket timeout: closing thread. (Transfer may have simply finished)");
			} catch (IOException e) {
				System.out.println("Socket error: closing thread.");
			}
		}
	}
}
