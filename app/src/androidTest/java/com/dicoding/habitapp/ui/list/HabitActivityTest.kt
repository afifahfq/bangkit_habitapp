package com.dicoding.habitapp.ui.list

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.add.AddHabitActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


//TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
@RunWith(AndroidJUnit4ClassRunner::class)
class HabitActivityTest {
    @Before
    fun setup(){
        ActivityScenario.launch(HabitListActivity::class.java)
    }

    @Test
    fun testAddHabit() {
        lateinit var currActivity: Activity
        Espresso.onView(withId(R.id.fab)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.fab)).perform(click())

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            run {
                currActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0)!!
            }
        }

        Assert.assertTrue(currActivity.javaClass == AddHabitActivity::class.java)
    }
}