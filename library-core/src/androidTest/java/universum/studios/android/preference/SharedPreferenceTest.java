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

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.PreferencesTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SharedPreferenceTest extends PreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "SharedPreferenceTest";
	private static final String PREF_KEY = "PREFERENCE.Impl";
	private static final String PREF_DEF_VALUE = "defValue";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have a clean slate before each test.
		mPreferences.edit().remove(PREF_KEY).commit();
	}

	@Test
	public void testInstantiation() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		assertThat(preference.getKey(), is(PREF_KEY));
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
		assertThat(preference.getDefaultValue(), is(PREF_DEF_VALUE));
	}

	@Test
	public void testInstantiationWithInvalidKeyString() {
		try {
			new PreferenceImpl("", "defValue");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Preference key cannot be empty."));
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testUpdateAndGetValue() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		// Test case 1.
		assertThat(preference.updateValue("newValue"), is(preference));
		assertThat(preference.getValue(), is("newValue"));
		// Test case 2.
		assertThat(preference.updateValue("newValue"), is(preference));
		assertThat(preference.getValue(), is("newValue"));
		// Test case 3.
		assertThat(preference.updateValue(null), is(preference));
		assertThat(preference.getValue(), is(nullValue()));
	}

	@Test
	public void testClear() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		assertThat(preference.updateValue("newValue"), is(preference));
		preference.invalidate();
		assertThat(preference.getValue(), is(nullValue()));
	}

	@Test
	public void testGetFromPreferences() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		assertThat(preference.getFromPreferences(mPreferences), is(PREF_DEF_VALUE));
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
		// Test again to cover case when the value of preference has been already obtained.
		assertThat(preference.getFromPreferences(mPreferences), is(PREF_DEF_VALUE));
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
	}

	@Test
	public void testPutIntoPreferences() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		assertThat(preference.updateValue("newValue"), is(preference));
		assertThat(preference.putIntoPreferences(mPreferences), is(true));
		assertThat(preference.getValue(), is("newValue"));
	}

	@Test
	public void testCreateOnChangeListener() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		final SharedPreferences.OnSharedPreferenceChangeListener listener = preference.createOnChangeListener(
				new SharedPreference.PreferenceChangeCallback<String>() {

					@Override
					public void onPreferenceChanged(@NonNull SharedPreference<String> preference) {
					}
				}
		);
		assertThat(listener, is(not(nullValue())));
	}

	private static final class PreferenceImpl extends SharedPreference<String> {

		private PreferenceImpl(@NonNull String key, @Nullable String defValue) {
			super(key, defValue);
		}

		@CheckResult
		@Override
		protected boolean onPutIntoPreferences(@NonNull SharedPreferences preferences) {
			return preferences.edit().putString(mKey, mValue).commit();
		}

		@Nullable
		@Override
		protected String onGetFromPreferences(@NonNull SharedPreferences preferences) {
			return preferences.getString(mKey, mDefaultValue);
		}
	}
}
