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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

class setData{
	private String ip = "";
	private String port = "";
	
	public setData(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}
}

public class ChatServer extends Application{
	public static ExecutorService threadpool;
	public static Vector<ClientManagerThread> clients = new Vector<ClientManagerThread>();
	public static ArrayList<String> name = new ArrayList<String>(); 
	public static ArrayList<String> score = new ArrayList<String>();
	ServerSocket serverSocket;
	static String quiz = "";
	static String resultLog = "";
	
	Scene scene;
	static Label userNameLabel = new Label("유저 이름");
	static TextField userNameField = new TextField();
	static Button dialogbutton = new Button("접속");
	HBox preRoot = new HBox(3.,userNameLabel,userNameField,dialogbutton);
	static Dialog<setData> dialog = new Dialog<>();
	static String serverIP = null;
   	static String serverPort = null;
	
	HBox root = new HBox();
	static TextArea rootMessage = new TextArea("");
   	static TextArea chatlog = new TextArea("");
   	static TextArea loging = new TextArea("접속 중인 사람\n------------\n");
   	static TextArea quizDB = new TextArea("퀴즈 목록\n--------\n");
   	static Button sendButton = new Button();
   	static GridPane grid;
	static TextField chatField = new TextField();

	@Override
	public void start(Stage stage) {
	   	try {	   	
	   		grid = new GridPane();
	   		
	   		root.setId("chatvbox");
	   		
	   		dialog.setTitle("이름 입력 창");
	    	dialog.setHeaderText(null);
	    	dialog.setContentText(null);
	    	dialog.setContentText("이름 : ");

	    	ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
	    	dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
	    	
	    	TextField ipField2 = new TextField();
	    	ipField2.setText("192.168.0.1");
	    	TextField portField2 = new TextField();
	    	portField2.setText("8888");
	   		
	    	Label IP = new Label("IP : ");
	    	IP.setMinWidth(80);
	    	IP.setTextFill(Color.web("#FFFFFF"));
	    	Label PORT = new Label("PORT : ");
	    	PORT.setMinWidth(80);
	    	PORT.setTextFill(Color.web("#FFFFFF"));
	   		
	    	HBox IP_text = new HBox(2., IP, ipField2);
	    	IP_text.setAlignment(Pos.CENTER);
	    	HBox PORT_text = new HBox(2., PORT, portField2);
	    	PORT_text.setAlignment(Pos.CENTER);
	   		
	    	VBox dialogPane = new VBox(2.,IP_text, PORT_text);
	   		
	    	dialogPane.setId("dialogPane");   	
	    	dialogPane.setPadding(new Insets(10, 10, 10, 10));
	    	dialogPane.setSpacing(15);
	    	
	    	dialogPane.setPrefWidth(400);
	    	dialogPane.setPrefHeight(200);
	    	dialog.getDialogPane().setContent(dialogPane);
	    	dialogPane.getStylesheets().clear();
	    	dialogPane.getStylesheets().add(getClass().getResource("./login.css").toExternalForm());
	    	dialogPane.setAlignment(Pos.CENTER);
	    	dialog.setResultConverter(new Callback<ButtonType, setData>() {	
				@Override
				public setData call(ButtonType ok) {					
					if(ok == okButton) {
						return new setData(ipField2.getText(), portField2.getText());
					}
		    		
					return null;
				}
	    	});
	    	
	    	Optional<setData> result = dialog.showAndWait();
	    	if(result.isPresent()) {
	    		System.out.println(ipField2.getText() + " " + " " + portField2.getText());
	   
	    		serverIP = ipField2.getText();
	    		serverPort = portField2.getText();
	    		
	    		startServer(serverIP, Integer.parseInt(serverPort));
	    		Platform.runLater(() -> {
	    			chatlog.appendText("[ 서버 가동 ]\n");
	    		});
	    		
	    		showSceneChanged(stage, root);
	    	}
	   		
		   	root.setPadding(new Insets(10)); // 안쪽 여백 설정
		   	root.setSpacing(10); // 컨트롤 간의 수평 간격 설정
		   	
		   	chatlog.setOpacity(0.8);
	    	chatlog.setEditable(false);
	    	chatlog.prefWidthProperty().bind(stage.widthProperty());
	    	chatlog.prefHeightProperty().bind(stage.heightProperty());
	    	
	    	loging.setOpacity(0.8);
	    	loging.setEditable(false);
	    	loging.prefWidthProperty().bind(stage.widthProperty());
	    	loging.prefHeightProperty().bind(stage.heightProperty());
		    	
	    	quizDB.setOpacity(0.8);
	    	quizDB.setEditable(false);
	    	quizDB.prefWidthProperty().bind(stage.widthProperty());
	    	quizDB.prefHeightProperty().bind(stage.heightProperty());
		    	
	    	sendButton.setText("전 송");
	    	sendButton.prefWidthProperty().bind(stage.widthProperty());
	    	sendButton.setMaxWidth(1000);
		    	
	    	chatField.setOpacity(0.8);
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
	    	
	    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            public void handle(WindowEvent we) {
	    	    	try{
	    	    		Thread.sleep(100);
	    	    	}catch(Exception e){
	    	    		e.printStackTrace();
	    	    	}
	    	    	stopServer();
	    	    	stage.hide();
	            }
	    	});
	    	
	    	ObservableList<Node> list = root.getChildren();
	    	list.add(grid);
	
		    Scene scene = new Scene(root, 500, 500);
	
	    	stage.setTitle("채팅창");
	    	stage.setScene(scene);
	    	stage.show();	
    	
    	} catch(Exception e) {
    		System.out.println("JavaFX 오류");
    	}
	}
	
	private void showSceneChanged(Stage stage, Parent nextRoot) { 
    	stage.hide();
 
    	scene = new Scene(nextRoot, 500, 500);
    	stage.setTitle("서버 운영자 창");
    	scene.getStylesheets().clear();
    	scene.getStylesheets().add(getClass().getResource("./chatRoom.css").toExternalForm());
    	stage.setScene(scene); 
    	
    	stage.show();
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
			System.out.println("서버 종료 중 오류");
		}
	}
	
	
	public static void main(String[] args) {
		//ChatServer cs = new ChatServer();
		//cs.startServer("192.168.43.206", 8888);
		launch(args);
	}

}

