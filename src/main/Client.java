package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {
	static Socket conn;
	JPanel panel;
	JTextField NewMsg;
	JTextArea ChatHistory;
	JButton Send,Cal,Key,Reset;
	JTextField a,k;
	JLabel t1,t2;
	int p,g;
	public static boolean prime(int num) {
	    int i;
	    for (i = 2; i*i <= num; ++i)
	        if (num % i == 0)
	            return false;
	    return true;
	}
	public int mod(int base, int expo, int num) {
	    int res = 1;
	    int i;
	    for (i = 1; i <= expo; ++i)
	        res = (res * base) % num;
	    return res;
	}
	public Client(int p, int g) throws UnknownHostException, IOException {
		this.p = p;
		this.g = g;
		panel = new JPanel();
		NewMsg = new JTextField();
		ChatHistory = new JTextArea();
		Send = new JButton("Gửi");
		this.setSize(500, 600);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(null);
		this.add(panel);
		this.setLocation(100, 60);
		a = new JTextField();
		a.setBounds(180, 40, 150,20 );
		panel.add(a);
		t1 = new JLabel("Số ngẫu nhiên(bí mật)");
		t1.setBounds(30, 40, 140, 20);
		panel.add(t1);
		t2 = new JLabel("Khóa được tạo");
		t2.setBounds(80, 500, 180, 20);
		panel.add(t2);
		Cal = new JButton("Tính");
		Cal.setBounds(180, 70, 60,20 );
		panel.add(Cal);
		Reset = new JButton("Làm lại");
		Reset.setBounds(250, 70, 80,20 );
		panel.add(Reset);
		k = new JTextField();
		k.setBounds(180, 500, 150, 20);
		panel.add(k);
		Key = new JButton("Tính khoá");
		Key.setBounds(180, 470, 100, 20);
		panel.add(Key);
		Cal.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  int bx = Integer.parseInt(a.getText());
				  int R2= mod(g,bx,p);
				  NewMsg.setText(String.valueOf(R2));
			  } 
			});
		Key.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  int ax = Integer.parseInt(a.getText());
				  int R2 = Integer.parseInt(ChatHistory.getText().split(":")[1]);
				  int k1 = mod(R2, ax, p);
				  k.setText(String.valueOf(k1));
			  } 
			});
		Reset.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  a.setText("");
			  } 
			});
		ChatHistory.setBounds(20, 140, 450, 200);
		panel.add(ChatHistory);
		NewMsg.setBounds(20, 400, 340, 30);
		panel.add(NewMsg);
		Send.setBounds(375, 400, 95, 30);
		panel.add(Send);
		Send.addActionListener(this);
		conn = new Socket(InetAddress.getLocalHost(), 2000);

		

		ChatHistory.setText("");
		this.setTitle("Kim nở");
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(conn.getInputStream());
				String string = dis.readUTF();
				ChatHistory.setText("Chí phèo đã gửi:"
						+ string);
			} catch (Exception e1) {
				ChatHistory.setText(ChatHistory.getText() + 'n'
						+ "Message sending fail:Network Error");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource() == Send) && (NewMsg.getText() != "")) {

//			ChatHistory.setText(ChatHistory.getText() + 'n' + "Me:"
//					+ NewMsg.getText());
			
			try {
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				dos.writeUTF(NewMsg.getText());
			} catch (Exception e1) {
				System.out.println(ChatHistory.getText() + 'n'
						+ "Message sending fail:Network Error");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, "Gửi thành công " + NewMsg.getText());
			NewMsg.setText("");
		}
	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		int p=0, g=0;
		do{
		Scanner s = new Scanner(System.in);
		System.out.print("Nhập modulo : ");
		p = s.nextInt();
		System.out.print("Nhập phần tử nguyên thủy : ");
		g = s.nextInt();
		}while(!prime(p) || !prime(g));
		new Client(p,g);
	}
}