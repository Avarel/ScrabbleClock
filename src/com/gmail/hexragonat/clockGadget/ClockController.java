package com.gmail.hexragonat.clockGadget;

import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller class for the JavaFX program.
 * @see ClockApp
 */
public class ClockController
{
	private static ClockController instance;
	private final Stage stage;

	@FXML
	protected GridPane root;
	@FXML
	protected GridPane letterGrid;
	@FXML
	protected Text closeLabel;

	private final String offColor = "#333333ff";
	private final String onColor = "#1a75ffff";

	private double xOffset = 0;
	private double yOffset = 0;

	/*
	 * Dependency injection.
	 */
	public ClockController(Stage stage)
	{
		this.stage = stage;
		instance = this;
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

		/*
		 * Close button handling.
		 */
		closeLabel.setOnMouseEntered(event ->
		{
			FillTransition ft = new FillTransition(Duration.millis(200), closeLabel, Color.valueOf("#333333"), Color.valueOf("#990000"));
			ft.play();

			closeLabel.setFill(Paint.valueOf("#990000"));
		});
		closeLabel.setOnMouseExited(event ->
		{
			FillTransition ft = new FillTransition(Duration.millis(200), closeLabel, Color.valueOf("#990000"), Color.valueOf("#333333"));
			ft.play();

			closeLabel.setFill(Paint.valueOf("#333333"));
		});
		closeLabel.setOnMouseClicked(event ->
		{
			ClockApp.heartbeat.stop();
			clearAll();
			System.exit(0);
		});
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
					Text node = ClockController.instance.letterAt(wordEnum.xOf[i], wordEnum.yOf[i]);

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
	private void clearAll()
	{
		for (WordEnum enum0 : WordEnum.values())
		{
			toggle(enum0, false);
		}
	}

}
