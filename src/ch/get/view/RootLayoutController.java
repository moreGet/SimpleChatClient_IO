package ch.get.view;

import java.net.URL;
import java.util.ResourceBundle;

import ch.get.contoller.ComponentController;
import ch.get.server.ServerInstance;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class RootLayoutController implements Initializable {
	
	@FXML
	private TextArea mainLogTextArea;
	
	private boolean sevInitSw;
	private static RootLayoutController instance;
	private ServerInstance serverInstance;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sevInitSw = false; // 기본 서버 중지 상태
		instance = this;
	}
	
	@FXML
	private void runServer() {
		if (!sevInitSw) { // 서버가 동작 중이지 않으면
			sevInitSw = true;
			
			serverInstance = new ServerInstance();
			serverInstance.setDaemon(true);
			serverInstance.start();
			
			ComponentController.changeBtnText(runBtn, "Stop");
			ComponentController.printServerLog(mainLogTextArea, "서버 실행...");
		} else {
			sevInitSw = false;
			
			ComponentController.changeBtnText(runBtn, "Run");
			serverInstance.serverStop();
			serverInstance = null;
		}
	}
	
	public TextArea getMainLogTextArea() {
		return mainLogTextArea;
	}
	
	public static RootLayoutController getInstance() {
		return instance;
	}
}