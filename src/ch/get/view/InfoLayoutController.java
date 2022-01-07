package ch.get.view;

import java.net.URL;
import java.util.ResourceBundle;

import ch.get.common.ServerFlag;
import ch.get.common.UserPropertiesKey;
import ch.get.contoller.ComponentController;
import ch.get.model.UserProperties;
import ch.get.util.LoggerUtil;
import ch.get.util.StringUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
		boolean isNameChange = false;
		String name = nickNameField.getText();
		String host = hostAddrField.getText();
		String port = hostPortField.getText();

		StringBuffer sb = new StringBuffer("");

		if (StringUtil.isNotNull(name) && !UserProperties.getUserInfo().containsValue(name)) {
			UserProperties.getUserInfo().put(UserPropertiesKey.NICK_NAME.name(), name);
			sb.append("닉네임 : ");
			sb.append(UserProperties.getUserInfo().get(UserPropertiesKey.NICK_NAME.name()));
			sb.append("\n");

			isNameChange = true;
		}

		if (StringUtil.isNotNull(host) && !UserProperties.getUserInfo().containsValue(host)) {
			UserProperties.getUserInfo().put(UserPropertiesKey.DEST_ADDR.name(), host);
			sb.append("서버 아이피 : ");
			sb.append(UserProperties.getUserInfo().get(UserPropertiesKey.DEST_ADDR.name()));
			sb.append("\n");
		}

		if (StringUtil.isNotNull(port) && !UserProperties.getUserInfo().containsValue(port)) {
			UserProperties.getUserInfo().put(UserPropertiesKey.DEST_PORT.name(), port);
			sb.append("서버 포트 : ");
			sb.append(UserProperties.getUserInfo().get(UserPropertiesKey.DEST_PORT.name()));
		}

		ButtonType buttonType = ButtonType.CANCEL;
		if (StringUtil.isNotNull(sb.toString())) {
			buttonType = ComponentController.showAlert("※ 값 변경 확인 ※", sb.toString(), AlertType.CONFIRMATION, myStage);
			LoggerUtil.info(sb.toString());

		} else {
			buttonType = ComponentController.showAlert("※ 필드 확인 요망 ※", "변경할 값이 없습니다.", AlertType.INFORMATION, myStage);
		}

		if (buttonType == ButtonType.OK) {
			myStage.close();

			if (isNameChange && RootLayoutController.getInstance().isConnected()) {
				RootLayoutController.getInstance().getClient().doSendMessage(ServerFlag.NICK,
						UserProperties.getUserInfo().get(UserPropertiesKey.NICK_NAME.name()).toString());
			}
		}
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

	public TextField getHostAddrField() {
		return hostAddrField;
	}

	public TextField getHostPortField() {
		return hostPortField;
	}

	/*
	 * SETTER
	 */
	public void setMyStage(Stage myStage) {
		this.myStage = myStage;
	}
}