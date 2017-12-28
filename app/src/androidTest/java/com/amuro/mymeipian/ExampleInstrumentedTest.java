package com.amuro.mymeipian;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.amuro.corelib.orm.annotation.OrmTable;
import com.amuro.corelib.utils.ReflectUtils;
import com.amuro.mymeipian.model.entity.ArticleEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.amuro.mymeipian", appContext.getPackageName());

        List result = ReflectUtils.scanClassesOfPkg(appContext, "com.amuro.mymeipian.model");
        System.out.println(result.size());

		List result2 = ReflectUtils.scanClassesOfPkgWithAnnotation(
				appContext, "com.amuro.mymeipian.model", OrmTable.class);
		System.out.println(result2);

		OrmTable t = ArticleEntity.class.getAnnotation(OrmTable.class);
		assertNotNull(t);
    }
}
