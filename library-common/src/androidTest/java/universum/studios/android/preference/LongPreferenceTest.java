/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.PreferencesTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class LongPreferenceTest extends PreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "LongPreferenceTest";
	private static final String PREF_KEY = "PREFERENCE.Long";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have a clean slate before each test.
		mPreferences.edit().remove(PREF_KEY).commit();
	}

	@Test
	public void testInstantiation() {
		final LongPreference preference = new LongPreference(PREF_KEY, 0L);
		assertThat(preference.getKey(), is(PREF_KEY));
		assertThat(preference.getValue(), is(0L));
		assertThat(preference.getDefaultValue(), is(0L));
	}

	@Test
	public void testPutAndGet() {
		final LongPreference preference = new LongPreference(PREF_KEY, 0L);
		assertThat(preference.getFromPreferences(mPreferences), is(0L));
		preference.updateValue(100000L);
		assertThat(preference.putIntoPreferences(mPreferences), is(true));
		preference.clear();
		assertThat(preference.getFromPreferences(mPreferences), is(100000L));
	}
}
