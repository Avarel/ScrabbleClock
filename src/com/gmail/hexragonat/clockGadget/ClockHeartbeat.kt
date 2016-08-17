package com.gmail.hexragonat.clockGadget

import com.gmail.hexragonat.clockGadget.controller.MainController
import javafx.application.Platform
import java.util.Calendar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Background heartbeat that grabs the time and
 * updates the main interface.
 * @see MainController
 */
class ClockHeartbeat internal constructor(app : ClockApp) : Runnable
{
    val executor : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val controller : MainController
    private var task : ScheduledFuture<*>? = null
    
    init
    {
        this.controller = app.mainController
    }
    
    fun start()
    {
        task = executor.scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS)
    }
    
    fun stop()
    {
        if (!task!!.isCancelled) task!!.cancel(false)
    }
    
    override fun run()
    {
        Platform.runLater {
            val now = Calendar.getInstance()
            consume(now)
        }
    }
    
    private fun consume(now : Calendar)
    {
        var hourEnum : WordEnum? = null
        var progressEnum : WordEnum?
        val minuteEnum : WordEnum?
        
        val hour = now.get(Calendar.HOUR)
        var isAM = now.get(Calendar.AM_PM) == Calendar.AM
        
        val minute = now.get(Calendar.MINUTE)
        
        var displayedHour = hour
    
        when
        {
            minute < 5 ->
            {
                minuteEnum = null
                progressEnum = WordEnum.P_OCLOCK
            }
            minute < 10 ->
            {
                minuteEnum = WordEnum.M_FIVE
                progressEnum = WordEnum.P_PAST
            }
            minute < 15 ->
            {
                minuteEnum = WordEnum.M_TEN
                progressEnum = WordEnum.P_PAST
            }
            minute < 20 ->
            {
                minuteEnum = WordEnum.M_QUARTER
                progressEnum = WordEnum.P_PAST
            }
            minute < 25 ->
            {
                minuteEnum = WordEnum.M_TWENTY
                progressEnum = WordEnum.P_PAST
            }
            minute < 30 ->
            {
                minuteEnum = WordEnum.M_TWENTYFIVE
                progressEnum = WordEnum.P_PAST
            }
            minute < 35 ->
            {
                minuteEnum = WordEnum.M_HALF
                progressEnum = WordEnum.P_PAST
            }
            minute < 40 ->
            {
                minuteEnum = WordEnum.M_TWENTYFIVE
                progressEnum = WordEnum.P_TIL
            }
            minute < 45 ->
            {
                minuteEnum = WordEnum.M_TWENTY
                progressEnum = WordEnum.P_TIL
            }
            minute < 50 ->
            {
                minuteEnum = WordEnum.M_QUARTER
                progressEnum = WordEnum.P_TIL
            }
            minute < 55 ->
            {
                minuteEnum = WordEnum.M_TEN
                progressEnum = WordEnum.P_TIL
            }
            else ->
            {
                minuteEnum = WordEnum.M_FIVE
                progressEnum = WordEnum.P_TIL
            }
        }
        
        if (progressEnum == WordEnum.P_TIL) displayedHour++
        
        if (displayedHour == 12)
        {
            isAM = !isAM
        }
        if (displayedHour > 12)
        {
            displayedHour = 1
        }
        
        when (displayedHour)
        {
            1 -> hourEnum = WordEnum.H_ONE
            2 -> hourEnum = WordEnum.H_TWO
            3 -> hourEnum = WordEnum.H_THREE
            4 -> hourEnum = WordEnum.H_FOUR
            5 -> hourEnum = WordEnum.H_FIVE
            6 -> hourEnum = WordEnum.H_SIX
            7 -> hourEnum = WordEnum.H_SEVEN
            8 -> hourEnum = WordEnum.H_EIGHT
            9 -> hourEnum = WordEnum.H_NINE
            10 -> hourEnum = WordEnum.H_TEN
            11 -> hourEnum = WordEnum.H_ELEVEN
            0, 12 ->
            {
                if (!isAM) hourEnum = WordEnum.H_NOON
                else hourEnum = WordEnum.H_MIDNIGHT
                if (minuteEnum == null) progressEnum = null
            }
        }
        
        //disable all stuff that will be disabled
        WordEnum.values()
                .filter { it.isActive }
                .filter { !(it equalsAny arrayOf(minuteEnum, hourEnum, progressEnum, WordEnum.`$_ITS`)) }
                .forEach { controller.toggle(it, false) }
        
        // handle one second delay as delayed transition plays out

        
        executor.schedule({
            Platform.runLater {
                WordEnum.values()
                        .filter { !it.isActive }
                        .filter { it equalsAny arrayOf(minuteEnum, hourEnum, progressEnum, WordEnum.`$_ITS`) }
                        .forEach { controller.toggle(it, true) }
            }
        }, 1, TimeUnit.SECONDS) //so animations won't overlap and cause weird stuff
        
        //System.out.println(minuteEnum +"\t\t"+ progressEnum +"\t\t"+ hourEnum);
    }
}
