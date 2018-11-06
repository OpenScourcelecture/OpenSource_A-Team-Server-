package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import server.ChatServer;


public class ClientManagerThread extends Thread{

	private Socket m_socket;
	private String m_ID;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		DBManager dbm = new DBManager();
		//FrameExam exam = new FrameExam();
		//exam.createFrame();
		
		try {
			FrameExam temp = new FrameExam();
			temp = FrameExam.getFrame();
			
			BufferedReader tmpbuffer = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
			String text;
			
			while(true)
			{
				text = tmpbuffer.readLine();

				if(text == null)
				{
					System.out.println(m_ID + "이(가) 나갔습니다.");
					for(int i = 0; i < ChatServer.m_OutputList.size(); ++i)
					{
						ChatServer.m_OutputList.get(i).println(m_ID + "이(가) 나갔습니다.");
						ChatServer.m_OutputList.get(i).flush();
					}
					
					temp.label.append(m_ID + "이(가) 나갔습니다.\n");
					break;
				}
				
				String[] split = text.split("-Client");
				if(split.length == 2 && split[0].equals("ID"))
				{
					m_ID = split[1];
					System.out.println(m_ID + "이(가) 입장하였습니다.");
					for(int i = 0; i < ChatServer.m_OutputList.size(); ++i)
					{
						ChatServer.m_OutputList.get(i).println(m_ID + "이(가) 입장하였습니다.");
						ChatServer.m_OutputList.get(i).flush();
					}
					
					temp.label.append(m_ID + "이(가) 입장하였습니다.\n");
					continue;
				}
				
				for(int i = 0; i < ChatServer.m_OutputList.size(); ++i)
				{
					ChatServer.m_OutputList.get(i).println(m_ID + "> "+ text);
					ChatServer.m_OutputList.get(i).flush();
				}
				
				temp.label.append(m_ID + "> " +text+"\n");
				System.out.println(m_ID + "> " + text);
				
				switch(text) {
				case "1번": dbm.select();
				}

			}
			
			ChatServer.m_OutputList.remove(new PrintWriter(m_socket.getOutputStream()));
			m_socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSocket(Socket _socket)
	{
		m_socket = _socket;
	}
}
