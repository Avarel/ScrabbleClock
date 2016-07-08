package com.gmail.hexragonat.clockGadget;

import com.gmail.hexragonat.clockGadget.controller.MainController;
import com.gmail.hexragonat.clockGadget.controller.SettingsController;
import com.gmail.hexragonat.clockGadget.utils.PropertiesManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main program.
 */
public class ClockApp extends Application
{
	public MainController controller;
	public ClockHeartbeat heartbeat;
	public Stage stage;
	public PropertiesManager settingsManager;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		this.stage = stage;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
		controller = new MainController(this);
		loader.setController(controller);

		Parent root = loader.load();

		stage.setTitle("Clock Gadget");
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("files/icon.png")));



		FXMLLoader loader0 = new FXMLLoader(getClass().getResource("fxml/settings.fxml"));
		SettingsController settingsController = new SettingsController(this);
		loader0.setController(settingsController);

		GridPane drawerPane = loader0.load();
		controller.sidebar.setSidePane(drawerPane);
		controller.sidebar.close();



		// properties file
		settingsManager = new PropertiesManager(this, "files/settings.properties", "ClockAppData/settings.properties");
		//settingsManager.load(settingsManager.file, getClass().getResourceAsStream(settingsManager.path)); //getClass().getResourceAsStream(settingsManager.path)
		settingsManager.load();
		settingsManager.setDefault(getClass().getResourceAsStream(settingsManager.path));

		// settings
		settingsController.setup();
		controller.setup();

		heartbeat = new ClockHeartbeat(this);
		heartbeat.start();

		stage.show();
	}
}
