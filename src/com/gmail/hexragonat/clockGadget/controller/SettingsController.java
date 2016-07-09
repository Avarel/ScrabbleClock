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
		onLettersCP.setValue(Color.valueOf(app.getSettingsManager().get("letter-enabled-color")));
		offLettersCP.setValue(Color.valueOf(app.getSettingsManager().get("letter-disabled-color")));
		backgroundCP.setValue(Color.valueOf(app.getSettingsManager().get("background-color")));

		app.getMainController().setOnColor(colorToHex(onLettersCP.getValue()));
		app.getMainController().setOffColor(colorToHex(offLettersCP.getValue()));
		app.getMainController().setBackgroundColor(colorToHex(backgroundCP.getValue()));

		onLettersCP.setOnAction((event) ->
		{
			app.getMainController().clearAll();
			app.getMainController().setOnColor(colorToHex(onLettersCP.getValue()));

			app.getSettingsManager().set("letter-enabled-color", colorToHex(onLettersCP.getValue()));
		});

		offLettersCP.setOnAction((event) ->
		{
			app.getMainController().clearAll();
			app.getMainController().setOffColor(colorToHex(offLettersCP.getValue()));
			app.getMainController().resetAll();

			app.getSettingsManager().set("letter-disabled-color", colorToHex(offLettersCP.getValue()));
		});

		backgroundCP.setOnAction((event) ->
		{
			app.getMainController().setBackgroundColor(colorToHex(backgroundCP.getValue()));

			app.getSettingsManager().set("background-color", colorToHex(backgroundCP.getValue()));
		});

		app.getMainController().resetAll();
	}

	public String colorToHex(Color color)
	{
		return '#' + color.toString().substring(2,8);
	}

	public void resetColors()
	{
		app.getMainController().clearAll();

		app.getSettingsManager().set("letter-enabled-color", app.getSettingsManager().getDefault("letter-enabled-color"));
		app.getSettingsManager().set("letter-disabled-color", app.getSettingsManager().getDefault("letter-disabled-color"));
		app.getSettingsManager().set("background-color", app.getSettingsManager().getDefault("background-color"));

		onLettersCP.setValue(Color.valueOf(app.getSettingsManager().get("letter-enabled-color")));
		offLettersCP.setValue(Color.valueOf(app.getSettingsManager().get("letter-disabled-color")));
		backgroundCP.setValue(Color.valueOf(app.getSettingsManager().get("background-color")));

		app.getMainController().setOnColor(colorToHex(onLettersCP.getValue()));
		app.getMainController().setOffColor(colorToHex(offLettersCP.getValue()));
		app.getMainController().setBackgroundColor(colorToHex(backgroundCP.getValue()));

		app.getMainController().resetAll();
	}
}
