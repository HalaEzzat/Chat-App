import java.io.*;
import java.util.Vector;
import java.net.*;
class ChatHandler extends Thread{
		DataInputStream dis;
		PrintStream ps;
		String Name;
		// counter for clients
		static int i = 0;
		public static Vector <ChatHandler> clients=new Vector <ChatHandler>();
	
		public ChatHandler(Socket cs){
			try{
				dis = new DataInputStream(cs.getInputStream());
				ps = new PrintStream(cs.getOutputStream());
				Name= "client #"+i;
				clients.add(this);
				start();		
				i++;
			}catch(IOException e)
			{
				
			}
				
		}
		public void run()
		{
			while(true)
			{	
				try
				{
					String line;
					line = dis.readLine();
					if ( line.equals("end") ) {
						clients.remove(this);
						break;
					}
					sendMessageToAll(Name+" : "+line);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
			}
		}
		void sendMessageToAll(String str)
		{
			for(ChatHandler ch:clients)
			{
				ch.ps.println(str);
			}
		}
	}
public class ChatServer{
	ServerSocket server;
	public static void main(String[] args)
	{
		new ChatServer();
	}
	public ChatServer()
	{
		
		try
		{
			server=new ServerSocket(5005);
			while(true)
			{
			    Socket socket=server.accept();
				new ChatHandler(socket);
			}       
		}catch(IOException e)
		{
			 
		}
	}
}