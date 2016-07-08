package com.gmail.hexragonat.clockGadget.controller;

import com.gmail.hexragonat.clockGadget.ClockApp;
import com.jfoenix.controls.JFXColorPicker;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

public class SettingsController
{
	private final ClockApp app;

	@FXML
	protected JFXColorPicker onLettersCP;

	@FXML
	protected JFXColorPicker offLettersCP;

	@FXML
	protected JFXColorPicker backgroundCP;

	public SettingsController(ClockApp app)
	{
		this.app = app;
	}

	public void setup()
	{
		onLettersCP.setValue(Color.valueOf(app.settingsManager.get("letter-enabled-color")));
		offLettersCP.setValue(Color.valueOf(app.settingsManager.get("letter-disabled-color")));
		backgroundCP.setValue(Color.valueOf(app.settingsManager.get("background-color")));

		app.controller.setOnColor(colorToHex(onLettersCP.getValue()));
		app.controller.setOffColor(colorToHex(offLettersCP.getValue()));
		app.controller.setBackgroundColor(colorToHex(backgroundCP.getValue()));

		onLettersCP.setOnAction((event) ->
		{
			app.controller.clearAll();
			app.controller.setOnColor(colorToHex(onLettersCP.getValue()));

			app.settingsManager.set("letter-enabled-color", colorToHex(onLettersCP.getValue()));
		});

		offLettersCP.setOnAction((event) ->
		{
			app.controller.clearAll();
			app.controller.setOffColor(colorToHex(offLettersCP.getValue()));
			app.controller.resetAll();

			app.settingsManager.set("letter-disabled-color", colorToHex(offLettersCP.getValue()));
		});

		backgroundCP.setOnAction((event) ->
		{
			app.controller.setBackgroundColor(colorToHex(backgroundCP.getValue()));

			app.settingsManager.set("background-color", colorToHex(backgroundCP.getValue()));
		});
	}

	public String colorToHex(Color color)
	{
		return '#' + color.toString().substring(2,8);
	}

	public void resetColors()
	{
		app.controller.clearAll();

		app.settingsManager.set("letter-enabled-color", app.settingsManager.getDefault("letter-enabled-color"));
		app.settingsManager.set("letter-disabled-color", app.settingsManager.getDefault("letter-disabled-color"));
		app.settingsManager.set("background-color", app.settingsManager.getDefault("background-color"));

		onLettersCP.setValue(Color.valueOf(app.settingsManager.get("letter-enabled-color")));
		offLettersCP.setValue(Color.valueOf(app.settingsManager.get("letter-disabled-color")));
		backgroundCP.setValue(Color.valueOf(app.settingsManager.get("background-color")));

		app.controller.setOnColor(colorToHex(onLettersCP.getValue()));
		app.controller.setOffColor(colorToHex(offLettersCP.getValue()));
		app.controller.setBackgroundColor(colorToHex(backgroundCP.getValue()));

		app.controller.resetAll();
	}
}
