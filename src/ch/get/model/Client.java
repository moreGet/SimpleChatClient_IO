package ch.get.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import ch.get.common.ServerFlag;
import ch.get.common.ServerSplitCode;
import ch.get.contoller.ComponentController;
import ch.get.util.LoggerUtil;
import ch.get.util.UuidUtil;
import ch.get.view.RootLayoutController;

public class Client implements Runnable {
	
	public final int SOCKET_TIME_OUT = 3000;
	
	private Socket socket;
	private String clientId;
	private String nickName;
	
	/*
	 * INIT SOCKET I/O
	 */
	private InputStreamReader isr;
	private BufferedReader br;
	
	private OutputStreamWriter osw;
	private PrintWriter pw;
	
	public Client() {
		// ������ ����
		this.clientId = UuidUtil.getUuid();
		
		Integer id = new Random().nextInt(10000);
		id = id <= 999 ? id+1000 : id;
		
		this.nickName = id.toString();
	}
	
	@Override
	public void run() {
		doJoin();
	}
	
	public void doJoin() {
		try {
			this.socket = new Socket();

			// 1. ���� ����
			socket.connect(new InetSocketAddress("127.0.0.1", 10000), SOCKET_TIME_OUT);
			
			if (socket.isConnected()) {
				isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
				br = new BufferedReader(isr);
				
				osw = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
				pw = new PrintWriter(osw, true);
				
				ComponentController.printServerLog(
						RootLayoutController.getInstance().getMainLogTextArea(), "������ ���� �Ͽ����ϴ�.");
				
				doSendMessage(ServerFlag.JOIN, this.nickName);
				doReadMessage(); // 2. �б� ������ ����
				
				ComponentController.changeBtnText(
						RootLayoutController.getInstance().getConnectBtn(), "������");
			}
		} catch (IOException e) {
			ComponentController.printServerLog(
					RootLayoutController.getInstance().getMainLogTextArea(), "������ ���� �� �� �����ϴ�.");
			doQuit();
		} 
	}
	
	// ������ ���� ���� �޼����� �д� Ŭ���̾�Ʈ ������
	private void doReadMessage() {
		LoggerUtil.info("READ I/O THREAD RUN...");
		
		Thread msgReadThread = new Thread(() -> {
			try {
				while (true) {
					String msg = br.readLine();
					
					if (msg == null) {
						ComponentController.changeBtnText(
								RootLayoutController.getInstance().getConnectBtn(), 
								"����");
						throw new IOException();
					}
					
					ComponentController.printServerLog(
							RootLayoutController.getInstance().getMainLogTextArea(), msg);
				}
			} catch (IOException e) {
				ComponentController.printServerLog(
						RootLayoutController.getInstance().getMainLogTextArea(), "���� ����...");
			}
		});
		
		msgReadThread.setDaemon(true);
		msgReadThread.start();
	}
	
	public void doSendMessage(ServerFlag serverFlag, String msg) {
		try {
			if (socket.isConnected()) {
				StringBuffer sb = new StringBuffer();
				WeakReference<StringBuffer> wr = new WeakReference<StringBuffer>(sb);
				
				sb.append(serverFlag.getFlagValue());
				sb.append(ServerSplitCode.SPLIT.getCode());
				sb.append(msg);
				
				pw.println(sb.toString());
				LoggerUtil.info("SENDING MSG : "  + sb.toString() + " SERVER CONNECTION STATUS : " + socket.isConnected());
				sb = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doQuit() {
		try {
			if (socket != null || !socket.isClosed()) {;
				try {
					if (socket.isConnected()) {
						StringBuffer sb = new StringBuffer();
						WeakReference<StringBuffer> wr = new WeakReference<StringBuffer>(sb);
						
						sb.append(ServerFlag.QUIT.getFlagValue());
						sb.append(ServerSplitCode.SPLIT.getCode());
						sb.append(this.nickName + "���� �����̽��ϴ�.");
						
						pw.println(sb.toString());
						sb = null;
						
						ComponentController.changeBtnText(
								RootLayoutController.getInstance().getConnectBtn(), "����");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				socket.close();
				socket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public boolean isConnected() {
		if (socket == null || !socket.isConnected()) {
			return false;
		}
		
		return true;
	}
//	public Socket getSocket() {
//		return socket;
//	}
}