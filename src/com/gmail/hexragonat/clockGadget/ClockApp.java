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
	private Stage stage;

	private ClockHeartbeat heartbeat;
	private MainController mainController;
	private SettingsController settingsController;
	private PropertiesManager settingsManager;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		this.stage = stage;

		// loading main stuff
		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
		mainController = new MainController(this);
		loader.setController(getMainController());

		Parent root = loader.load();

		// meta stuff
		stage.setTitle("Clock Gadget");
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("files/icon.png")));


		// loading and setting sidebar
		FXMLLoader loader0 = new FXMLLoader(getClass().getResource("fxml/settings.fxml"));
		settingsController = new SettingsController(this);
		loader0.setController(getSettingsController());

		GridPane drawerPane = loader0.load();
		getMainController().getSidebar().setSidePane(drawerPane);
		getMainController().getSidebar().close();



		// properties file
		settingsManager = new PropertiesManager(this, "files/settings.properties", "ClockAppData/settings.properties");
		//settingsManager.load(settingsManager.file, getClass().getResourceAsStream(settingsManager.path)); //getClass().getResourceAsStream(settingsManager.path)
		getSettingsManager().load();
		getSettingsManager().setDefault(getClass().getResourceAsStream(getSettingsManager().path));

		// settings
		getSettingsController().setup();
		getMainController().setup();

		// start app heartbeat
		heartbeat = new ClockHeartbeat(this);
		getHeartbeat().start();

		stage.show();
	}

	public Stage getStage()
	{
		return stage;
	}

	public ClockHeartbeat getHeartbeat()
	{
		return heartbeat;
	}

	public MainController getMainController()
	{
		return mainController;
	}

	public SettingsController getSettingsController()
	{
		return settingsController;
	}

	public PropertiesManager getSettingsManager()
	{
		return settingsManager;
	}
}
