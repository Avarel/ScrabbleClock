package com.gmail.hexragonat.clockGadget.controller

import com.gmail.hexragonat.clockGadget.ClockApp
import com.gmail.hexragonat.clockGadget.WordEnum
import com.jfoenix.controls.JFXDrawer
import javafx.animation.FillTransition
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.effect.Glow
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.util.Duration
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Controller class for the JavaFX program.
 * @see ClockApp
 */
class MainController
(private val app : ClockApp)
{
    private val stage : Stage
    
    @FXML @JvmField
    var root : GridPane? = null
    @FXML @JvmField
    var letterGrid : GridPane? = null
    @FXML @JvmField
    var closeLabel : Text? = null
    @FXML @JvmField
    var settingsLabel : Text? = null
    @FXML @JvmField
    var sidebar : JFXDrawer? = null
    
    private var offColor = "#333333"
    private var onColor = "#1a75ff"
    
    private var xOffset = 0.0
    private var yOffset = 0.0
    
    init
    {
        this.stage = app.stage
    }
    
    fun setOnColor(onColor : String)
    {
        this.onColor = onColor
    }
    
    fun setOffColor(offColor : String)
    {
        this.offColor = offColor
    }
    
    fun setBackgroundColor(color : String)
    {
        root!!.style = "-fx-background-color: $color;"
    }
    
    fun setup()
    {
        /*
		 * Stage dragging property.
		 */
        root!!.setOnMousePressed { event ->
            xOffset = event.sceneX
            yOffset = event.sceneY
        }
        
        val finalStage = stage
        root!!.setOnMouseDragged { event ->
            finalStage.x = event.screenX - xOffset
            finalStage.y = event.screenY - yOffset
        }
        
        root!!.setOnMouseEntered { event -> setClockField(null) }
        root!!.setOnMouseExited { event -> resetClockField() }
        
        setupCloseLabel()
        setupSettingsLabel()
    }
    
    private fun setupCloseLabel()
    {
        /*
		 * Close button handling.
		 */
        closeLabel!!.setOnMouseEntered { event ->
            val ft = FillTransition(Duration.millis(200.0), closeLabel, Color.valueOf(offColor), Color.valueOf(onColor).brighter())
            ft.play()
            closeLabel!!.effect = Glow(5.0)
            setClockField(" CLOSE ")
        }
        closeLabel!!.setOnMouseExited { event ->
            val ft = FillTransition(Duration.millis(200.0), closeLabel, Color.valueOf(onColor).brighter(), Color.valueOf(offColor))
            ft.play()
            closeLabel!!.effect = null
            setClockField(null)
        }
        closeLabel!!.setOnMouseClicked { event ->
            app.heartbeat.stop()
            stage.close()
            app.settingsManager.save()
            System.exit(0)
        }
    }
    
    private fun setupSettingsLabel()
    {
        settingsLabel!!.setOnMouseEntered { event ->
            val ft = FillTransition(Duration.millis(200.0), settingsLabel, Color.valueOf(offColor), Color.valueOf(onColor).brighter())
            ft.play()
            settingsLabel!!.effect = Glow(5.0)
            setClockField("OPTIONS")
        }
        settingsLabel!!.setOnMouseExited { event ->
            val ft = FillTransition(Duration.millis(200.0), settingsLabel, Color.valueOf(onColor).brighter(), Color.valueOf(offColor))
            ft.play()
            
            if (WordEnum.P_OCLOCK.isActive)
            {
                val ft0 = FillTransition(Duration.millis(200.0), settingsLabel, Color.valueOf(offColor), Color.valueOf(onColor))
                ft0.play()
            }
            else
            {
                settingsLabel!!.effect = null
            }
            
            setClockField(null)
        }
        settingsLabel!!.setOnMouseClicked { event ->
            if (sidebar!!.isHidden())
                sidebar!!.open()
            else
                sidebar!!.close()
        }
    }
    
    fun setClockField(string : String?)
    {
        var string0 = string
        if (string0 == null || string0.isEmpty())
        {
            val formatter = SimpleDateFormat("hh:mma")
            string0 = formatter.format(Date())
        }
        
        for (i in 0 .. 6)
        {
            val finalI = i
            val finalString = string0
            app.heartbeat.executor.schedule({
                Platform.runLater {
                    val node = letterAt(finalI + 2, 5)
                    
                    if (node != null)
                    {
                        node.style = "-fx-font-weight: bold"
                        node.fill = Paint.valueOf(Color.valueOf(onColor).brighter().toString())
                        node.text = finalString!![finalI].toString()
                        node.effect = Glow(1.0)
                    }
                }
            }, (50 * (i + 1)).toLong(), TimeUnit.MILLISECONDS)
        }
    }
    
    fun resetClockField()
    {
        val original = "EXAVALO"
        
        for (i in 0 .. 6)
        {
            val finalI = i
            app.heartbeat.executor.schedule({
                Platform.runLater {
                    val node = letterAt(finalI + 2, 5)
                    
                    if (node != null)
                    {
                        node.style = null
                        node.fill = Paint.valueOf(offColor)
                        node.text = original[finalI].toString()
                        node.effect = null
                    }
                }
            }, (50 * (i + 1)).toLong(), TimeUnit.MILLISECONDS)
        }
    }
    
    /**
     * Grab a letter from the letter grid.
     */
    private fun letterAt(x : Int?, y : Int?) : Text?
    {
        for (node in letterGrid!!.children)
        {
            var columnIndex = GridPane.getColumnIndex(node)
            if (columnIndex == null) columnIndex = 0
            
            var rowIndex = GridPane.getRowIndex(node)
            if (rowIndex == null) rowIndex = 0
            
            if (columnIndex == x && rowIndex == y)
            {
                return node as Text
            }
        }
        return null
    }
    
    /**
     * Toggle the clock's letters by the words.
     * @see WordEnum
     */
    fun toggle(wordEnum : WordEnum?, bool : Boolean)
    {
        if (wordEnum != null)
        {
            try
            {
                var i = 0
                var max = wordEnum.size
                
                /*
				 * Overlapping words.
				 */
                if (wordEnum == WordEnum.M_TWENTYFIVE && (WordEnum.M_TWENTY.isActive || WordEnum.M_FIVE.isActive))
                {
                    if (WordEnum.M_TWENTY.isActive)
                        i = 6
                    else if (WordEnum.M_FIVE.isActive) max = 6
                }
                else if ((wordEnum == WordEnum.M_TWENTY || wordEnum == WordEnum.M_FIVE) && WordEnum.M_TWENTYFIVE.isActive)
                {
                    return
                }
                
                while (i < max)
                {
                    val node = letterAt(wordEnum.xOf[i], wordEnum.yOf[i])
                    
                    if (node != null)
                    {
                        if (!bool)
                        {
                            if (wordEnum.isActive)
                            {
                                val ft = FillTransition(Duration.millis(1000.0), node, Color.valueOf(onColor), Color.valueOf(offColor))
                                ft.play()
                            }
                            
                            node.fill = Paint.valueOf(offColor)
                            node.effect = null
                        }
                        else
                        {
                            if (!wordEnum.isActive)
                            {
                                val ft = FillTransition(Duration.millis(1000.0), node, Color.valueOf(offColor), Color.valueOf(onColor))
                                ft.play()
                            }
                            
                            node.fill = Paint.valueOf(onColor)
                            node.effect = Glow(5.0)
                        }
                    }
                    i++
                }
                wordEnum.isActive = bool
            }
            catch (e : NullPointerException)
            {
                e.printStackTrace()
            }
            
        }
    }
    
    fun getSidebar() = sidebar
    
    /*
	 * Turn all letters off.
	 */
    fun clearAll()
    {
        for (enum0 in WordEnum.values())
        {
            toggle(enum0, false)
        }
    }
    
    fun resetAll()
    {
        // set all of letters color to off color
        for (x in 0 .. 10)
        {
            for (y in 0 .. 10)
            {
                val node = letterAt(x, y)
                if (node != null)
                {
                    node.fill = Paint.valueOf(offColor)
                    node.effect = null
                }
            }
        }
    }
}
