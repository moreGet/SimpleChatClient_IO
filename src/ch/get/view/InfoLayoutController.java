package ch.get.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InfoLayoutController implements Initializable {

	/*
	 * 컨트롤러
	 */
	private static InfoLayoutController instance;
	private Stage myStage;
	
	@FXML
	private TextField nickNameField;
	@FXML
	private TextField hostAddrField;
	@FXML
	private TextField hostPortField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
	}
	
	@FXML
	private void confirmInfo() {
		
	}
	
	@FXML
	private void closeWindow() {
		myStage.close();
	}
	
	/*
	 * GETTER
	 */
	public static InfoLayoutController getInstance() {
		return instance;
	}
	
	/*
	 * SETTER
	 */
	public void setMyStage(Stage myStage) {
		this.myStage = myStage;
	}
}