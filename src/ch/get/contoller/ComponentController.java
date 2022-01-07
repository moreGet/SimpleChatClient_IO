package ch.get.contoller;

import java.lang.ref.WeakReference;

import ch.get.util.DateUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Window;

public class ComponentController {

	// 서버로그 출력
	public static void printServerLog(TextArea textArea, String msg) {
		Platform.runLater(() -> {
			StringBuffer sb = new StringBuffer();
			WeakReference<StringBuffer> weakReference = new WeakReference<StringBuffer>(sb);

			sb.append("[ ");
			sb.append(DateUtil.getDate());
			sb.append(" ] ");
			sb.append(msg);
			sb.append("\n");

			textArea.appendText(sb.toString());
			sb = null;
		});
	}

	// 버튼 텍스트 변경
	public static void changeBtnText(Button button, String text) {
		Platform.runLater(() -> {
			button.setText(text);
		});
	}

	// 컴포넌트 비활성화
	public static void disableComponent(Object component) {
		if (component instanceof Control) {
			Control control = (Control) component;
			control.setDisable(true);
		}
	}

	// 확인창 출력
	public static ButtonType showAlert(String title, String content, AlertType alertType, Window onwer) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.initModality(Modality.WINDOW_MODAL);

		if (alertType == AlertType.INFORMATION) {
			alert.setResultConverter(result -> {
				return ButtonType.CANCEL;
			});
		}

		alert.initOwner(onwer);
		alert.showAndWait();
		return alert.getResult();
	}
}