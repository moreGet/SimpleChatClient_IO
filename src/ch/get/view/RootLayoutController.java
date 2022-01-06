package ch.get.view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import ch.get.MainApp;
import ch.get.common.ServerFlag;
import ch.get.contoller.ComponentController;
import ch.get.model.Client;
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		
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
		client = new Client();
		clientThread = new Thread(client);
		clientThread.setDaemon(true);
		clientThread.start();
	}
	
	@FXML
	private void doSendMessage() {
		ComponentController.printServerLog(
				mainLogTextArea, chatMsgInputForm.getText());
	
		Platform.runLater(() -> {
			client.doSendMessage(ServerFlag.SEND, chatMsgInputForm.getText());
			chatMsgInputForm.clear();
		});
	}
	
	@FXML
	private void doShowInfo() {
		mainApp.showInfoWindow();
	}
	
	/*
	 * GETTER
	 */
	public TextArea getMainLogTextArea() {
		return mainLogTextArea;
	}
	
	public Button getConnectBtn() {
		return connectBtn;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public boolean isConnected() {
		if(Optional.ofNullable(client).isPresent()) {
			return client.isConnected();
		}
		
		return false;
	}
	
	/*
	 * SINGLETON
	 */
	public static RootLayoutController getInstance() {
		return instance;
	}
}