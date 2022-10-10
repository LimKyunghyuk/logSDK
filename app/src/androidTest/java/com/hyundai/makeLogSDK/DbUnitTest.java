package com.hyundai.makeLogSDK;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.hyundai.logSDK.util.DBHelper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbUnitTest {

    Context appContext;
    @Before
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.hyundai.makeLogSDK", appContext.getPackageName());
    }
    @Test
    public void open(){
        DBHelper dbHelper = new DBHelper(appContext);
        dbHelper.open();
        dbHelper.create();
    }

}