package ch.get.contoller;

import java.lang.ref.WeakReference;
import java.util.Optional;

import ch.get.util.DateUtil;
import ch.get.view.RootLayoutController;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ComponentController {
	
	/**
	 * 싱글톤
	 */
	private ComponentController() {}
	
	private static class LazyHolder {
		public static final ComponentController instance = new ComponentController();
	}
	
	public static ComponentController getInstance() {
		return LazyHolder.instance;
	}
	
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
}