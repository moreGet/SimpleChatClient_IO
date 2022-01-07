package ch.get;

import ch.get.common.WindowProperties;
import ch.get.util.LoggerUtil;
import ch.get.view.InfoLayoutController;
import ch.get.view.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
	
	private Stage primaryStage; 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("간단한 채팅 클라이언트 v1.2");
		primaryStage.setWidth(WindowProperties.ROOT_LAYOUT_WIDTH.getValue());
		primaryStage.setHeight(WindowProperties.ROOT_LAYOUT_HEIGHT.getValue());
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
			
			RootLayoutController rootCont = loader.getController();
			rootCont.setMainApp(this);
			
			LoggerUtil.info("클라...");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage());
		}
	}
	
	public void showInfoWindow() {
		FXMLLoader loader = new FXMLLoader();
		
		try {
			loader.setLocation(MainApp.class.getResource("view/InfoLayout.fxml"));
			AnchorPane anchorPane = (AnchorPane) loader.load();
			Scene scene = new Scene(anchorPane);
			
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UTILITY);
			stage.setTitle("설정");
			stage.initOwner(primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setWidth(WindowProperties.INFO_LAYOUT_WIDTH.getValue());
			stage.setHeight(WindowProperties.INFO_LAYOUT_HEIGHT.getValue());
			stage.show();
			
			InfoLayoutController controller = loader.getController();
			controller.setMyStage(stage);
			
			LoggerUtil.info("정보 창 불러오기...");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage());
		}
	}
}