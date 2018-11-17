package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


class DBManager {
	String driver = "org.mariadb.jdbc.Driver";
	String url = "jdbc:mysql://192.168.0.13:3306/test";
	String uId = "root";
	String uPwd = "1234";
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public DBManager() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, uId, uPwd);
			if(con != null) {System.out.println("������ ���̽� ���� ����");}
		} catch(ClassNotFoundException e) {
			System.out.println("������ ���̽� �ε� ����");
		} catch(SQLException e) {
			System.out.println("������ ���̽� ���� ����");
		}
	}
	
	public String select(String table, int id) {
		String sql = "select * from " + table + " where num = " + id + ";";
		String temp = "";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			String Code;
			int quiznum;
			int Code2;
			while(rs.next()) {
				quiznum = rs.getInt("num");
				Code = rs.getString("problem");
				temp = temp + Code + "\n";
				System.out.println(Code);
				Code = rs.getString("ex1");
				temp = temp + Code + "\n";
				System.out.println(Code);
				Code = rs.getString("ex2");
				temp = temp + Code + "\n";
				System.out.println(Code);
				Code = rs.getString("ex3");
				temp = temp + Code + "\n";
				System.out.println(Code);
				Code = rs.getString("ex4");
				temp = temp + Code + "\n";
				System.out.println(Code);
				Code2 = rs.getInt("result");
				System.out.println(Code2);
				
				return "mquiz^" + quiznum + "^" + temp + "^" + Code2;
			}
		}catch(SQLException e) {
			System.out.println("���� ���� ����");
		}
		
		return null;
	}
}

public class ClientManagerThread extends Thread{
	Socket socket;
	static String temp;
	DBManager dbm = new DBManager();	
	
	public ClientManagerThread(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	public void logingList() {
		ChatServer.loging.setText("���� ���� ���\n------------\n");
		for(int i=0; i<ChatServer.name.size(); i++) {
			ChatServer.loging.appendText((ChatServer.name.get(i)).substring(5) + "\n");
		}
	}
	
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					DBManager dbm = new DBManager();
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[2048];
						
						int length = in.read(buffer);
						if (length == -1) throw new IOException();
						System.out.println("[�޼��� ���� ����] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						
						String message = new String(buffer, 0, length, "UTF-8");
						
						// ��� â�� ������ �߰��ϴ� �κ�
						if(message.length() > 5 && message.substring(0,5).equals("name:")) {
							ChatServer.name.add(message);
							Thread.currentThread().setName(message);
							logingList();
						}
						
						// �����̶�� �Է��� �ý� ���� ���
						
				
						for(ClientManagerThread client : ChatServer.clients) {
							if(message.length() > 5 && message.substring(0,5).equals("name:")) {
								client.send("m" + Thread.currentThread().getName().substring(5) + "���� �����Ͽ����ϴ�.");
								for(int i=0; i<ChatServer.clients.size(); i++) {
									try {
										Thread.sleep(200);
									} catch(Exception e) {
										e.printStackTrace();
									}
									client.send("userinfo" + ChatServer.name.get(i).substring(5));
									
								}
								
							}
							
							else if(message.length() >= 2 && message.substring(0,1).equals("q")) {
								client.send("m" + Thread.currentThread().getName().substring(5) + "���� ä�ù��� �������ϴ�.");
								client.send("delete" + Thread.currentThread().getName().substring(5));
							}
							
							else if(message.length() >= 2 && message.substring(0,2).equals("��:")) {
								send("m" + message);
							}
							
							else {
								client.send("m" + Thread.currentThread().getName().substring(5) + " > " + message);
							}
						}
						
						if(message.equals("����")) {
							for(ClientManagerThread client : ChatServer.clients) {
								int randomNum = (int)((Math.random()*10));
								client.send(dbm.select("quiz1", randomNum));
								client.send("mstartquiz");
								try {
									Thread.sleep(200);
								} catch(Exception e) {
									e.printStackTrace();
								}
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
						System.out.println("[�޼��� ���� ����] "
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
						System.out.println("[�޼��� �۽� ����] "
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