package server;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class FrameExam
{
	private static FrameExam exam = new FrameExam();
	JFrame frame = new JFrame("Quiz");
	JTextArea label = new JTextArea();
	JScrollPane sp = new JScrollPane(label);
	
	public static FrameExam getFrame() {
		return exam;
	}
	
	public void createFrame()
	{
		//frame.add(label);
		frame.setSize(500, 600);
		frame.setVisible(true);
		//label.setHorizontalAlignment(SwingConstants.CENTER);
		//label.setVerticalAlignment(SwingConstants.CENTER);
		//contentPane.add(sp);
		frame.add(label);
	}
	
	public void text(String text) {
		label.setText(text);
		frame.add(label);
	}
}

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
			if(con != null) {System.out.println("데이터 베이스 접속 성공");}
		} catch(ClassNotFoundException e) {
			System.out.println("데이터 베이스 로드 실패");
		} catch(SQLException e) {
			System.out.println("데이터 베이스 접속 실패");
		}
	}
	
	public String select() {
		String sql = "select * from a";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			String Code;
			int Code2;
			while(rs.next()) {
				Code2 = rs.getInt("answerNum");
				System.out.print(Code2);
				Code = rs.getString("answer0");
				System.out.println(". " + Code);
				Code = rs.getString("answer1");
				System.out.println("1번 보기 : " + Code);
				Code = rs.getString("answer2");
				System.out.println("2번 보기 : " + Code);
				Code = rs.getString("answer3");
				System.out.println("3번 보기 : " + Code);
				Code = rs.getString("answer4");
				System.out.println("4번 보기 : " + Code);
				return Code;
			}
		}catch(SQLException e) {
			System.out.println("쿼리 수행 실패");
		}
		return null;
	}
}

public class ChatServer {

	public static ArrayList<PrintWriter> m_OutputList;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		m_OutputList = new ArrayList<PrintWriter>();
		
			try {				
				FrameExam temp = new FrameExam();
				temp = FrameExam.getFrame();
				temp.createFrame();
				
				ServerSocket s_socket = new ServerSocket(8888);
				
				while(true)
				{
					Socket c_socket = s_socket.accept();
					
					ClientManagerThread c_thread = new ClientManagerThread();
					c_thread.setSocket(c_socket);
					
					m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
					
					c_thread.start();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}

