/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License 
 * you may obtain at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You can redistribute, modify or publish any part of the code written within this file but as it 
 * is described in the License, the software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.preference;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class PreferencesLoggingTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "PreferencesLoggingTest";

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the logging class has default logger.
		PreferencesLogging.setLogger(null);
	}

	@Test
	public void testGetDefaultLogger() {
		final Logger logger = PreferencesLogging.getLogger();
		assertThat(logger, is(not(nullValue())));
		assertThat(logger.getLogLevel(), is(Log.ASSERT));
	}

	@Test
	public void testSetLogger() {
		PreferencesLogging.setLogger(null);
		assertThat(PreferencesLogging.getLogger(), is(not(nullValue())));
		final Logger logger = new SimpleLogger(Log.DEBUG);
		PreferencesLogging.setLogger(logger);
		assertThat(PreferencesLogging.getLogger(), is(logger));
	}

	@Test
	public void testLogging() {
		// At least test that the delegate methods does not throw any exception.
		PreferencesLogging.v(TAG, "");
		PreferencesLogging.v(TAG, "", null);
		PreferencesLogging.d(TAG, "");
		PreferencesLogging.d(TAG, "", null);
		PreferencesLogging.i(TAG, "");
		PreferencesLogging.i(TAG, "", null);
		PreferencesLogging.w(TAG, "");
		PreferencesLogging.w(TAG, "", null);
		PreferencesLogging.w(TAG, (Throwable) null);
		PreferencesLogging.e(TAG, "");
		PreferencesLogging.e(TAG, "", null);
		PreferencesLogging.wtf(TAG, "");
		PreferencesLogging.wtf(TAG, "", null);
		PreferencesLogging.wtf(TAG, (Throwable) null);
	}
}
