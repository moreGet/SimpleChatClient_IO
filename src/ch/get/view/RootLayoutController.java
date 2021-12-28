package ch.get.view;

import java.net.URL;
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
		client.doJoin();
	}
	
	@FXML
	private void doSendMessage() {
		ComponentController.printServerLog(
				mainLogTextArea, chatMsgInputForm.getText());
	
		Platform.runLater(() -> {
			chatMsgInputForm.clear();
			client.doSendMessage(ServerFlag.SEND, chatMsgInputForm.getText());
		});
	}
	
	public TextArea getMainLogTextArea() {
		return mainLogTextArea;
	}
	
	public static RootLayoutController getInstance() {
		return instance;
	}
}