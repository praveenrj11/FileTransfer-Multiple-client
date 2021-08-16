package Com.FileTransfer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleFileClient {

	public final static int SOCKET_PORT = 8885;
	public final static String SERVER = "127.0.0.1";
	public final static String FILE_TO_RECEIVED = "C:\\Temp\\Output\\praveen.zip";

	public final static int FILE_SIZE = 200000000;

	public static void main(String[] args) throws IOException {
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		Socket sock = null;
		try {
			sock = new Socket(SERVER, SOCKET_PORT);
			System.out.println("Connecting...");

//		received Ip from server for authentication
			BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String ServerResponse = input.readLine();
			String[] res = ServerResponse.split("/");
			String serverssentip = null;

//		reading client Ip here
			String localip = sock.getLocalAddress().toString();
			String[] res1 = localip.split("/");
			String clientip = null;

			serverssentip = res[1];
			clientip = res1[1];
			System.out.println("serverssentip" + res[1]);
			System.out.println("clientip" + res1[1]);

			if (clientip.equals(serverssentip)) {
				System.out.println("enterrrrrrddddddd");
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				out.println(sock.getLocalAddress());
			} else {
				PrintWriter out1 = new PrintWriter(sock.getOutputStream(), true);
				out1.println("FAIL" + ":" + sock.getLocalAddress());
			}

			// receive file
			if (clientip.equals(serverssentip)) {
				byte[] mybytearray = new byte[FILE_SIZE];
				InputStream is = sock.getInputStream();
				fos = new FileOutputStream(FILE_TO_RECEIVED);
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				System.out.println("File Recived");
			} else {
				System.out.println("File Not Recived");
			}
		} finally {
			if (fos != null)
				fos.close();
			if (bos != null)
				bos.close();
			if (sock != null)
				sock.close();
		}
	}

}