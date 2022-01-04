package ch.get;

import ch.get.util.LoggerUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
	
	private Stage primaryStage; 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("간단한 채팅 클라이언트 v1.1");
		primaryStage.setWidth(675);
		primaryStage.setHeight(420);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UTILITY);
		
		// 메인 레이아웃
		initMain();
		
		// 스테이지 보이기
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void initMain() {
		FXMLLoader loader = new FXMLLoader();
		
		try {
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			BorderPane borderPane = (BorderPane) loader.load();
			Scene scene = new Scene(borderPane);
			this.primaryStage.setScene(scene);
			
			LoggerUtil.info("클라...");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage());
		}
	}
}
