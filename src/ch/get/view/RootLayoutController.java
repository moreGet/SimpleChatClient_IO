package ch.get.view;

import java.net.URL;
import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;
import java.util.ResourceBundle;

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
	 * ��Ʈ�ѷ�
	 */
	private static RootLayoutController instance;
	private Client client;
	
	private boolean isConnected = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		client = new Client();
		
		/*
		 * �⺻ ������Ʈ ����
		 */
		ComponentController.printServerLog(mainLogTextArea, "ä���� �õ� �Ϸ��� ���� ��ư�� ���� �ּ���.");
		chatMsgInputForm.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			
			if (event.getCode().equals(KeyCode.ENTER)) {
				doSendMessage();
			}
		});
	}
	
	@FXML
	private void doConnectServer() {
		if (!isConnected) {
			client.doJoin();
			
			if (client.getSocket() != null) {
				isConnected = true;
				ComponentController.changeBtnText(connectBtn, "������");
			}
		} else {
			client.doQuit();
			isConnected = false;
			ComponentController.changeBtnText(connectBtn, "����");
		}
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
	
	public TextArea getMainLogTextArea() {
		return mainLogTextArea;
	}
	
	public Button getConnectBtn() {
		return connectBtn;
	}
	
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public static RootLayoutController getInstance() {
		return instance;
	}
}