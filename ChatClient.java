import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ChatClient extends JFrame{
	Socket socket;
	DataInputStream dis;
	PrintStream ps;
	JTextArea ta;
	JScrollPane scroll;
	JTextField tf;
		JButton btnExit;
	JButton okButton;
	JButton save;
	FileWriter f;
	BufferedWriter bw ;
	public ChatClient(){
		
		try{
			this.setLayout(new FlowLayout());
			ta= new JTextArea(5,50);
			scroll=new JScrollPane(ta);
			scroll.setViewportView(ta);
			btnExit = new JButton("Exit");
			save=new JButton("save");
			try (BufferedReader reader = new BufferedReader(new FileReader("chat.txt"))) 
			{
				String line = null;
				while ((line = reader.readLine()) != null) 
				{
					ta.append(line+"\n");
				}
			} 
			catch (IOException x) 
			{
					System.err.format("IOException: %s%n", x);
			}
			tf= new JTextField(30);
			okButton=new JButton("send");
			socket = new Socket("127.0.0.1",5005);
			dis = new DataInputStream(socket.getInputStream());
			ps = new PrintStream(socket.getOutputStream());
			btnExit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{		
					ps.println("end"); 	
					System.exit(0);					
				}
			});
			okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{		
					String msgout = "";  
					msgout = tf.getText();  
					ps.println(msgout); 					
					
					tf.setText("");
				}
			});
			save.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{		
					try
					{
					    File file = new File("chat.txt");
						file.createNewFile();
						bw = new BufferedWriter(new FileWriter("chat.txt"));
						for(int i=0;i<ta.getText().length();i++){
							if(ta.getText().charAt(i)=='\n')
								bw.newLine();
							else
								bw.write(ta.getText().charAt(i));
						}
				
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				
					try
					{
						bw.close();
					}catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			});
			add(scroll);
			add(tf);
			add(okButton);
			add(save);
			add(btnExit);
			new Thread(new Runnable(){
				public void run(){
					while(true)
					{
						try
						{
							String replyMsg = dis.readLine()+"\n";
							ta.append(replyMsg);
						}catch(IOException e)
						{
						}
					}
			
			}}).start();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		ChatClient ui=new ChatClient();
		ui.setSize(400,500);
		ui.setVisible(true);
	}
}