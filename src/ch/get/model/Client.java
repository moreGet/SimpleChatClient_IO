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

import ch.get.common.ServerFlag;
import ch.get.common.ServerSplitCode;
import ch.get.common.UserPropertiesKey;
import ch.get.contoller.ComponentController;
import ch.get.util.LoggerUtil;
import ch.get.util.RandomUtil;
import ch.get.util.UuidUtil;
import ch.get.view.RootLayoutController;

public class Client implements Runnable {

	public final int SOCKET_TIME_OUT = 3000;

	private Socket socket;
	private String clientId;
	private String nickName;

	private String hostIp;
	private String hostPort;

	/*
	 * INIT SOCKET I/O
	 */
	private InputStreamReader isr;
	private BufferedReader br;

	private OutputStreamWriter osw;
	private PrintWriter pw;

	private Thread msgReadThread;
	private boolean activeReadThread;

	private WeakReference<Thread> we;

	public Client() {
		// 고유값 생성
		this.clientId = UuidUtil.getUuid();

		if (UserProperties.getUserInfo().containsKey(UserPropertiesKey.NICK_NAME.name())) {
			this.nickName = UserProperties.getUserInfo().get(UserPropertiesKey.NICK_NAME.name()).toString();
		} else {
			this.nickName = String.valueOf(RandomUtil.getRandom(1000, 9999));
		}
	}

	@Override
	public void run() {
		doJoin();
	}

	public void doJoin() {
		try {
			this.socket = new Socket();

			// 1. 서버 접속
			hostIp = UserProperties.getUserInfo().get(UserPropertiesKey.DEST_ADDR.name()).toString();
			hostPort = UserProperties.getUserInfo().get(UserPropertiesKey.DEST_PORT.name()).toString();

			socket.connect(new InetSocketAddress(hostIp, Integer.parseInt(hostPort)), SOCKET_TIME_OUT);

			if (socket.isConnected()) {
				isr = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
				br = new BufferedReader(isr);

				osw = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
				pw = new PrintWriter(osw, true);

				ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(),
						"서버에 접속 하였습니다.");

				doSendMessage(ServerFlag.JOIN, this.nickName);

				// 2. 읽기 쓰레드 수행
				activeReadThread = true;
				doReadMessage();

				ComponentController.changeBtnText(RootLayoutController.getInstance().getConnectBtn(), "나가기");
			}
		} catch (IOException e) {
			ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(),
					"서버에 접속 할 수 없습니다.");
			doQuit();
		}
	}

	// 서버로 부터 오는 메세지를 읽는 클라이언트 쓰레드
	private void doReadMessage() {
		msgReadThread = new Thread(() -> {
			try {
				we = new WeakReference<Thread>(msgReadThread);
				LoggerUtil.info("READ I/O THREAD RUN...");

				while (activeReadThread) {
					String msg = br.readLine();

					if (msg == null) {
						ComponentController.changeBtnText(RootLayoutController.getInstance().getConnectBtn(), "접속");
						throw new IOException();
					}

					ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(), msg);
				}
			} catch (IOException e) {
				ComponentController.printServerLog(RootLayoutController.getInstance().getMainLogTextArea(), "종료...");

				try {
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
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
				LoggerUtil
						.info("SENDING MSG : " + sb.toString() + " SERVER CONNECTION STATUS : " + socket.isConnected());
				sb = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doQuit() {
		try {
			if (socket != null || !socket.isClosed()) {
				;
				try {
					if (socket.isConnected()) {
						StringBuffer sb = new StringBuffer();
						WeakReference<StringBuffer> wr = new WeakReference<StringBuffer>(sb);

						sb.append(ServerFlag.QUIT.getFlagValue());
						sb.append(ServerSplitCode.SPLIT.getCode());
						sb.append(this.nickName + "님이 나가셨습니다.");

						pw.println(sb.toString());
						sb = null;

						ComponentController.changeBtnText(RootLayoutController.getInstance().getConnectBtn(), "접속");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				socket.close();
				socket = null;

				activeReadThread = false;
				msgReadThread = null;
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

	public Socket getSocket() {
		return socket;
	}

	public boolean isConnected() {
		if (socket == null || socket.isClosed()) {
			return false;
		}

		return true;
	}
}