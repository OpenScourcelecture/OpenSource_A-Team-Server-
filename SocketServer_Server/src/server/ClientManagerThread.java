package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
	String url = "jdbc:mysql://" + ChatServer.serverIP + ":3306/test";
	String uId = "root";
	String uPwd = "1234";
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public DBManager() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, uId, uPwd);
			if(con != null) {System.out.println("데이터 베이스 접속 성공");}
		} catch(ClassNotFoundException e) {
			System.out.println("데이터 베이스 로드 실패");
		} catch(SQLException e) {
			System.out.println("데이터 베이스 접속 실패");
		}
	}
	
	public String showtableName() {
		try {
			DatabaseMetaData meta = con.getMetaData();			
			ResultSet rs2 = meta.getTables(null, null, null, new String[] {"TABLE"});
			String Code = "";
			String temp = "";
			while(rs2.next()) {
				temp = rs2.getString("TABLE_NAME");
				Code = Code + temp + "\n";
			}
			
			ChatServer.quizDB.setText("퀴즈 \n------------\n");
			ChatServer.quizDB.appendText(Code + "\n");
		
			return Code;
		}catch(SQLException e) {
			System.out.println("쿼리 수행 실패");
		}
		
		return "";
	}
	
	public String select(String table, int id, String name) {
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
				
				return "mquiz" + name + "^" + quiznum + "^" + temp + "^" + Code2;
			}
		}catch(SQLException e) {
			System.out.println("쿼리 수행 실패");
		}
		
		return null;
	}
}

public class ClientManagerThread extends Thread{
	Socket socket;
	static String temp;
	DBManager dbm = new DBManager();	
	static int count=0;
	
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
	
	public synchronized void receive() {
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
						System.out.println("[메세지 수신 성공] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						
						String message = new String(buffer, 0, length, "UTF-8");
						
						// 운영자 창에 유저들 추가하는 부분
						if(message.length() > 5 && message.substring(0,5).equals("name:")) {
							for(int i=0; i<ChatServer.name.size(); i++) {
								if(ChatServer.name.get(i).equals(message)) {
									message = message + (i+1);
									send("setName" + message);
								}
							}
							ChatServer.name.add(message);
							Thread.currentThread().setName(message);
							logingList();
						}
						
						// 전공이라는 입력이 올시 문제 출력
				
						for(ClientManagerThread client : ChatServer.clients) {
							if(message.length() > 5 && message.substring(0,5).equals("name:")) {
								client.send("m" + Thread.currentThread().getName().substring(5) + "님이 입장하였습니다.");
								for(int i=0; i<ChatServer.clients.size(); i++) {
									try {
										Thread.sleep(200);
									} catch(Exception e) {
										System.out.println("sleep 오류");									
									}
									client.send("userinfo" + ChatServer.name.get(i).substring(5));									
								}
							}
							
							else if(message.length() >= 4 && message.substring(0,4).equals("quit")) {
								client.send("m" + Thread.currentThread().getName().substring(5) + "님이 채팅방을 나갔습니다.");
								client.send("delete" + Thread.currentThread().getName().substring(5));
							}
							
							else if(message.length() >= 2 && message.substring(0,2).equals("답:")) {
								send("m");
							}
							
							else if(message.length() >= 7 && message.substring(0,7).equals("endquiz")) {
								count++;
								if(count==ChatServer.clients.size()) {
									for(ClientManagerThread client2 : ChatServer.clients) {
										client2.send("mresultrequest");
									}
									
									count = 0;
								}
							}
							
							else if(message.length() >= 10 && message.substring(0,10).equals("resultsend")) {
								ChatServer.chatlog.appendText(message.substring(10) + "\n");
							}
							
							else if(message.length() >= 3 && message.substring(0, 3).equals("다음/")) {}
							
							else {
								client.send("m" + Thread.currentThread().getName().substring(5) + " > " + message);
							}
						}https://github.com/OpenScourcelecture/OpenSource_A-Team-Server-.git
						
						if(message.equals("전공")) {
							for(ClientManagerThread client : ChatServer.clients) {
								int randomNum = (int)((Math.random()*17) + 1);
								client.send(dbm.select("전공", randomNum, "전공"));
								try {
									Thread.sleep(200);
								} catch(Exception e) {
									System.out.println("sleep 오류");								}
							}
						}
						
						else if(message.equals("상식")) {
							for(ClientManagerThread client : ChatServer.clients) {
								int randomNum = (int)((Math.random()*18) + 1);
								client.send(dbm.select("상식", randomNum, "상식"));
								try {
									Thread.sleep(200);
								} catch(Exception e) {
									System.out.println("sleep 오류");								}
							}
						}
						
						else if(message.length() >= 3 && message.substring(0, 3).equals("다음/")) {
							count++;
							if(count == ChatServer.clients.size()) {
								for(ClientManagerThread client : ChatServer.clients) {
									int randomNum = 0;
									
									if(message.length() >= 3 && message.substring(3).equals("전공"))
										randomNum = (int)((Math.random()*17) + 1);
									else
										randomNum = (int)((Math.random()*18) + 1);
									client.send(dbm.select(message.substring(3), randomNum, message.substring(3)));
									try {
										Thread.sleep(200);
									} catch(Exception e) {
										System.out.println("sleep 오류");
									}
								}
								
								count = 0;
							}
						}
						
						for(int i=0; i<ChatServer.name.size(); i++) {
							if(message.length() >= 4 && message.substring(4, message.length()).equals(ChatServer.name.get(i))) {
								ChatServer.name.remove(i);
								break;
							}
						}
						
						logingList();
						for(ClientManagerThread client : ChatServer.clients) {
							client.send("mDBquiz" + dbm.showtableName());
							try {
								Thread.sleep(200);
							} catch(Exception e) {
								System.out.println("DB출력오류");
							}
						}
					}
				} catch(Exception e) {
					try {
						System.out.println("[메세지 수신 오류] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());	
						ChatServer.clients.remove(ClientManagerThread.this);
						socket.close();
					} catch(Exception e2) {
						System.out.println("e2오류");
					}
				}
			}
		};
		
		ChatServer.threadpool.submit(thread);
	}
	
	public synchronized void send(String message) {
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
						System.out.println("e2오류");
					}
				}
			}
		};
			
		ChatServer.threadpool.submit(thread);
	}
	
	
}
