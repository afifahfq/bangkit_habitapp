package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.*

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        viewModel.setInitialTime(habit.minutesFocus)
        val timeObserver = Observer<String> { aTime ->
            findViewById<TextView>(R.id.tv_count_down).text = aTime
        }
        viewModel.currentTimeString.observe(this, timeObserver)

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
        val data = Data.Builder()
            .putString(NOTIFICATION_CHANNEL_ID, getString(R.string.notify_channel_name))
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_TITLE, habit.title)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .addTag(NOTIF_UNIQUE_WORK)
            .build()

        val workManager = WorkManager.getInstance(this)
        val finishObserver = Observer<Boolean> { aFinish ->
            if (aFinish == true) {
                workManager.enqueue(oneTimeWorkRequest)
                updateButtonState(false)
            }
        }
        viewModel.eventCountDownFinish.observe(this, finishObserver)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            updateButtonState(true)
            viewModel.startTimer()
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            updateButtonState(false)
            viewModel.resetTimer()
            workManager.cancelUniqueWork(NOTIF_UNIQUE_WORK)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}