package com.gmail.hexragonat.clockGadget;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClockApp extends Application
{
	public static ClockHeartbeat heartbeat;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));

		ClockController controller = new ClockController(stage);
		loader.setController(controller);

		Parent root = loader.load();

		stage.setTitle("Clock Gadget");
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();

		controller.setup();

		heartbeat = new ClockHeartbeat(controller);
		heartbeat.start();
	}
}
