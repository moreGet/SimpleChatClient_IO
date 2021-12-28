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
import ch.get.util.UuidUtil;
import ch.get.view.RootLayoutController;

public class Client {
	
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
		// 고유값 생성
		this.clientId = UuidUtil.getUuid();
		this.nickName = String.valueOf(new Random().nextInt(1000));
	}
	
	public void doJoin() {
		try {
			this.socket = new Socket();

			// 1. 서버 접속
			socket.connect(new InetSocketAddress("localhost", 10000));
			
			if (socket.isConnected()) {
				isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
				br = new BufferedReader(isr);
				
				osw = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
				pw = new PrintWriter(osw);
				
				ComponentController.printServerLog(
						RootLayoutController.getInstance().getMainLogTextArea(), "서버에 접속 하였습니다.");
				
				doSendMessage(ServerFlag.JOIN, this.nickName);
				doReadMessage(); // 2. 읽기 쓰레드 수행
			}
		} catch (IOException e) {
			e.printStackTrace();
			doQuit();
		} 
	}
	
	// 서버로 부터 오는 메세지를 읽는 클라이언트 쓰레드
	private void doReadMessage() {
			Thread msgReadThread = new Thread(() -> {
				try {
					String msg = br.readLine();
					ComponentController.printServerLog(
							RootLayoutController.getInstance().getMainLogTextArea(), msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			msgReadThread.setDaemon(true);
			msgReadThread.start();
	}
	
	public void doSendMessage(ServerFlag serverFlag, String msg) {
		if (socket.isConnected()) {
			StringBuffer sb = new StringBuffer();
			WeakReference<StringBuffer> wr = new WeakReference<StringBuffer>(sb);
			
			sb.append(serverFlag.getFlagValue());
			sb.append(ServerSplitCode.SPLIT.getCode());
			sb.append(msg);
			
			pw.println(sb.toString());
			sb = null;
		}
	}
	
	public void doQuit() {
		try {
			if (socket != null || !socket.isClosed()) {
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
}