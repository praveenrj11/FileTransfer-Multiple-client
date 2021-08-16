package Com.FileTransfer;
/*
* Author: 		Praveen R Jadhav
* Date  : 		01-Mar-2021
* File Name: 	SimpleFileServer.java
*/

/*
* Author: 		Praveen R Jadhav
* Date  : 		01-Mar-2021
* File Name: 	SimpleFileServer.java
*/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimpleFileServer {

	public final static int SOCKET_PORT = 8885;
	public final static String FILE_TO_SEND = "C:\\Temp\\openjdk-11+28_windows-x64_bin.zip";

	public final static String ProjectName = "ABCD";
	public final static String ExecutionID = "1010";

	public final static String ReadyToSend = "Sending";
	
	public static void main(String[] args) throws IOException {
		
		/** This array will be populated while selecting the Execution**/
		List<String> selectedProjectExecution = new ArrayList<String>();
		selectedProjectExecution.add("Test 1:100");
		
		/** This array list will be populated while selecting the IP address of the agent from the execution screen**/
		List<String> selectedAgentIP = new ArrayList<String>();
		selectedAgentIP.add("172.16.40.184");
		selectedAgentIP.add("172.16.40.44");
		selectedAgentIP.add("172.16.40.178");
		selectedAgentIP.add("172.16.40.69");
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		ServerSocket servsock = null;
		Socket sock = null;
		try {
			servsock = new ServerSocket(SOCKET_PORT);
			while (true) {
				System.out.println("Waiting...");
				try {
					sock = servsock.accept();

//			sending Accepted client ip to client for authentication here
					PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
					out.println(sock.getInetAddress());

//			reading client sent ip here
					BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					String request = input.readLine();
					String[] res = request.split("/");
					String clientsentip = null;

//			Accepted ip
					String clientipAccepted = sock.getInetAddress().toString();
					String[] res1 = clientipAccepted.split("/");
					String clientip = null;

					for (int i = 0; i < res.length; i++) {
						clientsentip = res[i];
						clientip = res1[i];
						System.out.println("clientsentip" + res[i]);
						System.out.println("clientip" + res1[i]);
					}

					// send file

					/*if (clientip.equals(clientsentip)) {*/
					String sendDatatoAgentIP = selectedAgentIP.get(0);
					System.out.println("===============Sending file to Agent IP: " + sendDatatoAgentIP + "==============================");
					if (clientip.equalsIgnoreCase(sendDatatoAgentIP)) {
						File myFile = new File(FILE_TO_SEND);
						byte[] mybytearray = new byte[(int) myFile.length()];
						fis = new FileInputStream(myFile);
						bis = new BufferedInputStream(fis);
						bis.read(mybytearray, 0, mybytearray.length);
						os = sock.getOutputStream();
						System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
						os.write(mybytearray, 0, mybytearray.length);
						os.flush();
						System.out.println("File Sent.");
					} else {
						System.out.println("File Not Sent");
					}
				} finally {
					if (bis != null)
						bis.close();
					if (os != null)
						os.close();
					if (sock != null)
						sock.close();
				}
			}
		} finally {
			if (servsock != null)
				servsock.close();
		}
	}
}
