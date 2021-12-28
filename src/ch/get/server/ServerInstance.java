package ch.get.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.get.contoller.ComponentController;
import ch.get.model.Client;
import ch.get.model.ClientBucket;
import ch.get.util.LoggerUtil;
import ch.get.util.UuidUtil;
import ch.get.view.RootLayoutController;
import javafx.scene.control.TextArea;

public class ServerInstance extends Thread {
	private ServerSocket serverSocket;
	private RootLayoutController rootLayoutController;
	private TextArea mainLog;
	private boolean serverStatus;
	
	public ServerInstance() {
		rootLayoutController = RootLayoutController.getInstance();
		mainLog = rootLayoutController.getMainLogTextArea();
		serverStatus = true;
	}
	
	public void serverStop() {
		serverStatus = false;
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			LoggerUtil.error(e.getMessage());
		}
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(10000);
			
			while (serverStatus) {
				ComponentController.printServerLog(mainLog, "클라이언트를 기다리는 중...");
				Socket socket = serverSocket.accept();
				
				String clientId = UuidUtil.getUuid();
				Client client = new Client(socket, clientId);
				ClientBucket.getClientBucket().put(clientId, client); // 명시적 자료형 반환
				client.start();
				ComponentController.printServerLog(mainLog, "클라이언트 접속...!");
			}
		} catch (IOException e) {
			ComponentController.printServerLog(mainLog, "서버 종료...");
			LoggerUtil.info(e.getMessage());
		} catch (Exception e) {
			ComponentController.printServerLog(mainLog, "서버 비정상 종료...");
			LoggerUtil.error(e.getMessage());
		}
	}
}