package ch.get.view;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import ch.get.MainApp;
import ch.get.common.ServerFlag;
import ch.get.common.UserPropertiesKey;
import ch.get.contoller.ComponentController;
import ch.get.model.Client;
import ch.get.model.UserProperties;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RootLayoutController implements Initializable {
	
	@FXML
	private TextArea mainLogTextArea;
	@FXML
	private TextField chatMsgInputForm;
	@FXML
	private Button connectBtn;
	
	/*
	 * 컨트롤러
	 */
	private static RootLayoutController instance;
	private Client client;
	private Thread clientThread;
	private MainApp mainApp;
	
	private TextField hostField;
	private TextField portField;
	private TextField nickNameField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		
		/*
		 * 기본 서버 정보 수정
		 */
		UserProperties.getUserInfo().put(UserPropertiesKey.DEST_ADDR.name(), "127.0.0.1");
		UserProperties.getUserInfo().put(UserPropertiesKey.DEST_PORT.name(), "10000");
		
		/*
		 * 기본 컴포넌트 셋팅
		 */
		ComponentController.printServerLog(mainLogTextArea, "채팅을 시도 하려면 접속 버튼을 눌러 주세요.");
		chatMsgInputForm.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				if (isConnected()) {
					doSendMessage();
				}
			}
		});
	}
	
	@FXML
	private void doConnectServer() {
		if (!isConnected()) {
			client = new Client();
			clientThread = new Thread(client);
			WeakReference<Thread> wr = new WeakReference<Thread>(clientThread);
			clientThread.setDaemon(true);
			clientThread.start();
		} else {
			client.doQuit();
			clientThread = null;
		}
	}
	
	@FXML
	private void doSendMessage() {	
		Platform.runLater(() -> {
			String str = chatMsgInputForm.getText().trim();
			
			boolean checkStr = 
					Optional.ofNullable(str)
							.filter(elem -> elem.length() >= 1)
							.isPresent();
			
			if (checkStr) {
				ComponentController.printServerLog(
						mainLogTextArea, 
						str);
				
				client.doSendMessage(ServerFlag.SEND, chatMsgInputForm.getText());
				chatMsgInputForm.clear();	
			}
		});
	}
	
	@FXML
	private void doShowInfo() {
		mainApp.showInfoWindow();
		
		ConcurrentHashMap<String, Object> userInfo = UserProperties.getUserInfo();
		hostField = InfoLayoutController.getInstance().getHostAddrField();
		portField = InfoLayoutController.getInstance().getHostPortField();
		nickNameField = InfoLayoutController.getInstance().getNickNameField();
		
		hostField.setText(userInfo.get(UserPropertiesKey.DEST_ADDR.name()).toString());
		portField.setText(userInfo.get(UserPropertiesKey.DEST_PORT.name()).toString());		
		if (userInfo.containsKey(UserPropertiesKey.NICK_NAME.name())) {
			nickNameField.setText(userInfo.get(UserPropertiesKey.NICK_NAME.name()).toString());
		}
		if (isConnected()) {
			hostField.setDisable(true);
			portField.setDisable(true);
		} else {
			hostField.setDisable(false);
			portField.setDisable(false);
		}
	}
	
	/*
	 * GETTER
	 */
	public Client getClient() {
		return client;
	}
	
	public TextArea getMainLogTextArea() {
		return mainLogTextArea;
	}
	
	public TextField getChatMsgInputForm() {
		return chatMsgInputForm;
	}
	
	public Button getConnectBtn() {
		return connectBtn;
	}
	
	public boolean isConnected() {
		if(Optional.ofNullable(client).isPresent()) {
			return client.isConnected();
		}
		
		return false;
	}
	
	/*
	 * SETTER
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	/*
	 * SINGLETON
	 */
	public static RootLayoutController getInstance() {
		return instance;
	}
}