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



public class ChatServer extends Application{
	public static ExecutorService threadpool;
	public static Vector<ClientManagerThread> clients = new Vector<ClientManagerThread>();
	public static ArrayList<String> name = new ArrayList<String>(); 

	ServerSocket serverSocket;
	static String quiz = "";
	
	HBox root = new HBox();
	static TextArea rootMessage = new TextArea("");
   	static TextArea chatlog = new TextArea();
   	static TextArea loging = new TextArea("접속 중인 사람\n------------\n");
   	static TextArea quizDB = new TextArea("퀴즈 목록\n--------\n");
   	static Button sendButton = new Button();
   	static GridPane grid = new GridPane();
	static TextField chatField = new TextField();

   	//static TextInputDialog dialog = new TextInputDialog();
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
		    	
	    	chatField.prefWidthProperty().bind(stage.widthProperty());
	
	    	grid.setVgap(10);
	    	grid.setHgap(10);
	    	grid.add(chatlog, 0, 0, 2, 2);
	    	grid.add(chatField, 0, 2, 1, 1);
	    	grid.add(sendButton, 1, 2, 1, 1);
	    	grid.add(loging, 2, 0, 1, 1); 
	    	grid.add(quizDB, 2, 1, 1, 1);
	    	    	
	    	sendButton.setOnAction(new EventHandler<ActionEvent>() { 		
	    		@Override
	    		public void handle(ActionEvent event) {
	    			quiz = chatField.getText();
	    			chatlog.appendText(chatField.getText() + "\n");
	    			chatField.setText("");
	    	    	chatField.requestFocus();
	    	    }
	    	});
		    	
	    	chatField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    		@Override
	    		public void handle(KeyEvent event) {
	    			if(event.getCode() == KeyCode.ENTER) {
	    				quiz = chatField.getText();
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

