package com.gmail.hexragonat.clockGadget.controller;

import com.gmail.hexragonat.clockGadget.ClockApp;
import com.gmail.hexragonat.clockGadget.WordEnum;
import com.jfoenix.controls.JFXDrawer;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Controller class for the JavaFX program.
 * @see ClockApp
 */
public class MainController
{
	private final ClockApp app;
	private final Stage stage;


	@FXML
	protected GridPane root;
	@FXML
	protected GridPane letterGrid;
	@FXML
	protected Text closeLabel;
	@FXML
	protected Text settingsLabel;
	@FXML
	public JFXDrawer sidebar;


	private String offColor = "#333333";
	private String onColor = "#1a75ff";

	private double xOffset = 0;
	private double yOffset = 0;

	/*
	 * Dependency injection.
	 */
	public MainController(ClockApp app)
	{
		this.app = app;
		this.stage = app.stage;
	}

	public void setOnColor(String onColor)
	{
		this.onColor = onColor;
	}

	public void setOffColor(String offColor)
	{
		this.offColor = offColor;
	}

	public void setBackgroundColor(String color)
	{
		root.setStyle("-fx-background-color: "+color+";");
	}

	public void setup()
	{
		/*
		 * Stage dragging property.
		 */
		root.setOnMousePressed(event ->
		{
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		final Stage finalStage = stage;
		root.setOnMouseDragged(event ->
		{
			finalStage.setX(event.getScreenX() - xOffset);
			finalStage.setY(event.getScreenY() - yOffset);
		});

		root.setOnMouseEntered(event -> setClockField(null));
		root.setOnMouseExited(event -> resetClockField());

		/*
		 * Close button handling.
		 */
		closeLabel.setOnMouseEntered(event ->
		{
			FillTransition ft = new FillTransition(Duration.millis(200), closeLabel, Color.valueOf(offColor), Color.valueOf(onColor).brighter());
			ft.play();

			setClockField(" CLOSE ");
		});
		closeLabel.setOnMouseExited(event ->
		{
			FillTransition ft = new FillTransition(Duration.millis(200), closeLabel, Color.valueOf(onColor).brighter(), Color.valueOf(offColor));
			ft.play();

			setClockField(null);
		});
		closeLabel.setOnMouseClicked(event ->
		{
			app.heartbeat.stop();
			stage.close();
			app.settingsManager.save();
			System.exit(0);
		});

		settingsLabel.setOnMouseEntered(event ->
		{
			FillTransition ft = new FillTransition(Duration.millis(200), settingsLabel, Color.valueOf(offColor), Color.valueOf(onColor).brighter());
			ft.play();
			setClockField("OPTIONS");
		});
		settingsLabel.setOnMouseExited(event ->
		{
			FillTransition ft = new FillTransition(Duration.millis(200), settingsLabel, Color.valueOf(onColor).brighter(), Color.valueOf(offColor));
			ft.play();

			if (WordEnum.P_OCLOCK.isActive())
			{
				FillTransition ft0 = new FillTransition(Duration.millis(200), settingsLabel, Color.valueOf(offColor), Color.valueOf(onColor));
				ft0.play();
			}

			setClockField(null);
		});
		settingsLabel.setOnMouseClicked(event ->
		{
			System.out.println(sidebar.isHidden());
			if (sidebar.isHidden()) sidebar.open();
			else sidebar.close();
		});


		resetAll();
	}

	public void setClockField(String string)
	{
		if (string == null || string.isEmpty())
		{
			SimpleDateFormat formatter = new SimpleDateFormat("hh:mma");
			string = formatter.format(new Date());
		}

		for (int i = 0; i < 7; i++)
		{
			final int finalI = i;
			final String finalString = string;
			app.heartbeat.exec.schedule(() ->
					Platform.runLater(() ->
					{
						Text node = letterAt(finalI +2,5);

						if (node != null)
						{
							node.setStyle("-fx-font-weight: bold");
							node.setFill(Paint.valueOf(Color.valueOf(onColor).brighter().toString()));
							node.setText(String.valueOf(finalString.charAt(finalI)));
						}
					}), 50 * (i+1), TimeUnit.MILLISECONDS);
		}
	}

	public void resetClockField()
	{
		String original = "IRTHWDA";

		for (int i = 0; i < 7; i++)
		{
			final int finalI = i;
			app.heartbeat.exec.schedule(() ->
					Platform.runLater(() ->
					{
						Text node = letterAt(finalI+2,5);

						if (node != null)
						{
							node.setStyle(null);
							node.setFill(Paint.valueOf(offColor));
							node.setText(String.valueOf(original.charAt(finalI)));
						}
					}), 50 * (i+1), TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Grab a letter from the letter grid.
	 */
	private Text letterAt(Integer x, Integer y)
	{
		for (Node node : letterGrid.getChildren())
		{
			Integer columnIndex = GridPane.getColumnIndex(node);
			if (columnIndex == null) columnIndex = 0;

			Integer rowIndex = GridPane.getRowIndex(node);
			if (rowIndex == null) rowIndex = 0;

			if (columnIndex.equals(x) && rowIndex.equals(y))
			{
				return ((Text) node);
			}
		}
		return null;
	}

	/**
	 * Toggle the clock's letters by the words.
	 * @see WordEnum
	 */
	public void toggle(WordEnum wordEnum, boolean bool)
	{
		if (wordEnum != null)
		{
			try
			{
				int i = 0;
				int max = wordEnum.size;

				/*
				 * Overlapping words.
				 */
				if (wordEnum == WordEnum.M_TWENTYFIVE && (WordEnum.M_TWENTY.isActive() || WordEnum.M_FIVE.isActive()))
				{
					if (WordEnum.M_TWENTY.isActive()) i = 6;
					else if (WordEnum.M_FIVE.isActive()) max = 6;
				}
				else if ((wordEnum == WordEnum.M_TWENTY || wordEnum == WordEnum.M_FIVE) && WordEnum.M_TWENTYFIVE.isActive())
				{
					return;
				}

				for (; i < max; i++)
				{
					Text node = letterAt(wordEnum.xOf[i], wordEnum.yOf[i]);

					if (node != null)
					{
						if (!bool)
						{
							if (wordEnum.isActive())
							{
								FillTransition ft = new FillTransition(Duration.millis(1000), node, Color.valueOf(onColor), Color.valueOf(offColor));
								ft.play();
							}

							node.setFill(Paint.valueOf(offColor));
							node.setEffect(null);
						}
						else
						{
							if (!wordEnum.isActive())
							{
								FillTransition ft = new FillTransition(Duration.millis(1000), node, Color.valueOf(offColor), Color.valueOf(onColor));
								ft.play();
							}

							node.setFill(Paint.valueOf(onColor));
							node.setEffect(new Glow(5.0));
						}
					}
				}
				wordEnum.setActive(bool);
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * Turn all letters off.
	 */
	protected void clearAll()
	{
		for (WordEnum enum0 : WordEnum.values())
		{
			toggle(enum0, false);
		}
	}

	protected void resetAll()
	{
		// set all of letters color to off color
		for (int x = 0; x <= 10; x++)
		{
			for (int y = 0; y <= 10; y++)
			{
				Text node = letterAt(x, y);
				if (node != null)
				{
					node.setFill(Paint.valueOf(offColor));
					node.setEffect(null);
				}
			}
		}
	}

}
