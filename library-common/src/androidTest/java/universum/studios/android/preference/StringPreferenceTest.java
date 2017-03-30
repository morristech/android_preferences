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
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class StringPreferenceTest extends PreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "StringPreferenceTest";
	private static final String PREF_KEY = "PREFERENCE.String";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have a clean slate before each test.
		mPreferences.edit().remove(PREF_KEY).commit();
	}

	@Test
	public void testInstantiation() {
		final StringPreference preference = new StringPreference(PREF_KEY, null);
		assertThat(preference.getKey(), is(PREF_KEY));
		assertThat(preference.getValue(), is(nullValue()));
		assertThat(preference.getDefaultValue(), is(nullValue()));
	}

	@Test
	public void testPutAndGet() {
		final StringPreference preference = new StringPreference(PREF_KEY, null);
		assertThat(preference.getFromPreferences(mPreferences), is(nullValue()));
		preference.updateValue("New Year");
		assertThat(preference.putIntoPreferences(mPreferences), is(true));
		preference.invalidate();
		assertThat(preference.getFromPreferences(mPreferences), is("New Year"));
	}
}
