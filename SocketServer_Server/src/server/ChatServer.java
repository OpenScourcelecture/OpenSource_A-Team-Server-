package server;

import java.io.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Popup;
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

public class ChatServer extends Application{
	public static ExecutorService threadpool;
	public static Vector<ClientManagerThread> clients = new Vector<ClientManagerThread>();
	public static ArrayList<String> name = new ArrayList<String>(); 

	ServerSocket serverSocket;
	
	
	VBox root = new VBox();
	static TextArea rootMessage = new TextArea();
   	static TextArea chatlog = new TextArea();
   	static TextArea loging = new TextArea("접속 중인 사람\n------------\n");
   	static TextArea quizDB = new TextArea("퀴즈 목록\n--------\n");
   	static Button sendButton = new Button();
   	static GridPane grid = new GridPane();
   	static TextInputDialog dialog = new TextInputDialog();
	
	@Override
	public void start(Stage stage) {
	   	try {	   		
		   	root.setPadding(new Insets(10)); // 안쪽 여백 설정
		   	root.setSpacing(10); // 컨트롤 간의 수평 간격 설정
		   	
		   	rootMessage.setEditable(false);
	    	rootMessage.prefWidthProperty().bind(stage.widthProperty());
		   	
	    	chatlog.setEditable(false);
	    	chatlog.prefWidthProperty().bind(stage.widthProperty());
	    	chatlog.prefHeightProperty().bind(stage.heightProperty());
	    	
	    	loging.setEditable(false);
	    	loging.prefWidthProperty().bind(stage.widthProperty());
	    	loging.prefHeightProperty().bind(stage.heightProperty());
		    	
	    	quizDB.setEditable(false);
	    	quizDB.prefWidthProperty().bind(stage.widthProperty());
	    	quizDB.prefHeightProperty().bind(stage.heightProperty());
		    	
	    	sendButton.setText("전 송");
	    	sendButton.prefWidthProperty().bind(stage.widthProperty());
	    	sendButton.setMaxWidth(1000);
		    	
	    	TextField chatField = new TextField();
	    	chatField.prefWidthProperty().bind(stage.widthProperty());
	
	    	grid.setVgap(10);
	    	grid.setHgap(10);
	    	grid.add(chatlog, 0, 0, 2, 2);
	    	grid.add(chatField, 0, 2, 1, 1);
	    	grid.add(sendButton, 1, 2, 1, 1);
	    	grid.add(loging, 2, 0, 1, 1); 
	    	grid.add(quizDB, 2, 1, 1, 1);
	    	
	    	dialog.setTitle("이름 입력 창");
	    	dialog.setHeaderText("채팅 방에서 사용할 이름을 입력하세요");
	    	dialog.setContentText("이름 : ");

	    	Optional<String> result = dialog.showAndWait();
	    	if (result.isPresent()){
	    	    System.out.println("Your name: " + result.get());
	    	}
	    	    	
	    	sendButton.setOnAction(new EventHandler<ActionEvent>() { 		
	    		@Override
	    		public void handle(ActionEvent event) {
	    	    	
	    	    	chatlog.appendText(chatField.getText() + "\n");
	    	    	chatField.setText("");
	    	    	chatField.requestFocus();
	    	    }
	    	});
		    	
	    	chatField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    		@Override
	    		public void handle(KeyEvent event) {
	    			if(event.getCode() == KeyCode.ENTER) {
		    	    	chatlog.appendText(chatField.getText() + "\n");
		    	    	chatField.setText("");
		    	    	chatField.requestFocus();
	    			}
	    		}
	    	});
	    	
	    	ObservableList<Node> list = root.getChildren();
	    	list.add(rootMessage);
	    	list.add(grid);
		    	//list.add(grid2);
	
		    	Scene scene = new Scene(root, 500, 500);
	
	    	stage.setTitle("채팅창");
	    	stage.setScene(scene);
	    	stage.show();	
    	
    	} catch(Exception e) {
    		System.out.println(e);
    	}
	}
	
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
		} catch(Exception e) {
			e.printStackTrace();
			if(!serverSocket.isClosed())
				stopServer();
			return;
		}
		
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new ClientManagerThread(socket));
						System.out.println("[클라이언트 접속] "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						chatlog.appendText("[클라이언트 접속] "
								+ "\n" + socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName() + "\n");
					} catch(Exception e) {
						if(!serverSocket.isClosed())
							stopServer();
						break;
					}
				}
			}
		};
		
		threadpool = Executors.newCachedThreadPool();
		threadpool.submit(thread);
	}
	
	public void stopServer() {
		try {
			Iterator<ClientManagerThread> iterator = clients.iterator();
			while(iterator.hasNext()) {
				ClientManagerThread client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			
			if(serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
			if(threadpool != null && !threadpool.isShutdown())
				threadpool.shutdown();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		ChatServer cs = new ChatServer();
		cs.startServer("192.168.0.13", 8888);
		launch(args);
	}

}

