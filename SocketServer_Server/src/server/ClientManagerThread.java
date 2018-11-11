package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientManagerThread extends Thread{

	Socket socket;
	static String temp;
	public ClientManagerThread(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	public void logingList() {
		ChatServer.loging.setText("접속 중인 사람\n------------\n");
		for(int i=0; i<ChatServer.name.size(); i++) {
			ChatServer.loging.appendText((ChatServer.name.get(i)).substring(5) + "\n");
		}
	}
	
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						
						int length = in.read(buffer);
						if (length == -1) throw new IOException();
						System.out.println("[메세지 수신 성공] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						
						String message = new String(buffer, 0, length, "UTF-8");
						
						if(message.length() > 5 && message.substring(0,5).equals("name:")) {
							ChatServer.name.add(message);
							Thread.currentThread().setName(message);
							logingList();
						}
						
						for(ClientManagerThread client : ChatServer.clients) {
							if(message.length() > 5 && message.substring(0,5).equals("name:"))
								client.send(message);
							else {
								client.send("m" + Thread.currentThread().getName().substring(5) + " > " + message);
							}
						}
						
						for(int i=0; i<ChatServer.name.size(); i++) {
							if(message.substring(1).equals(ChatServer.name.get(i))) {
								ChatServer.name.remove(i);
								break;	
							}
						}
						
						logingList();
					}
				} catch(Exception e) {
					try {
						System.out.println("[메세지 수신 오류] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());	
						
						ChatServer.clients.remove(ClientManagerThread.this);
						socket.close();
					} catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		};
		
		ChatServer.threadpool.submit(thread);
	}
	
	public void send(String message) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				} catch(Exception e) {
					try {
						System.out.println("[메세지 송신 오류] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						socket.close();
					} catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		};
			
		ChatServer.threadpool.submit(thread);
	}
}
