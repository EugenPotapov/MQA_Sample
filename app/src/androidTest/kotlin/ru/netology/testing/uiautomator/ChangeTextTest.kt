package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


//const val SETTINGS_PACKAGE = "com.android.settings"
const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"

const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ChangeTextTest {

    private lateinit var device: UiDevice

    private val textHead = "Hello UiAutomator!"
    private val textToSetSpaces = "   "
    private val textToAnotherActivity = "Another"


    private fun waitForPackage(packageName: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }

    @Before
    fun beforeEachTest() {
        // Press home
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        // Wait for launcher
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)
    }

    @Test
    fun testChangeTextToSpaces() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        device.findObject(By.res(packageName, "userInput")).text = textToSetSpaces
        device.findObject(By.res(packageName, "buttonChange")).click()

        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        assertEquals(result, textHead)
    }

    @Test
    fun testChangeTextToBlankLine() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        device.findObject(By.res(packageName, "buttonChange")).click()

        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        assertEquals(result, textHead)
    }

    @Test
    fun testOpenTextInAnotherActivity() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        device.findObject(By.res(packageName, "userInput")).text = textToAnotherActivity
        device.findObject(By.res(packageName, "buttonActivity")).click()

        Thread.sleep(2000)
        val result = device.findObject(By.res(packageName, "text")).text
        assertEquals(result, textToAnotherActivity)
    }
}