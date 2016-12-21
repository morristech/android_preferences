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
import android.support.annotation.StringRes;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.preference.test.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SharedPreferenceTest extends SharedPreferenceBaseTest<String> {

	@SuppressWarnings("unused")
	private static final String TAG = "SharedPreferenceTest";

	public SharedPreferenceTest() {
		super("defaultValue");
	}

	@Nullable
	@Override
	SharedPreference<String> onCreatePreference(String key, String defValue) {
		return new PreferenceImpl(key, defValue);
	}

	@Test
	public void testInstantiationWithKeyString() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, PREF_DEF_VALUE);
		// This will not change value of key, because there was no key resource specified.
		preference.attachKey(mContext.getResources());
		assertThat(preference.getKey(), is(PREF_KEY));
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
	public void testInstantiationWithKeyResource() {
		final SharedPreference preference = new PreferenceImpl(R.string.test_preference_key, PREF_DEF_VALUE);
		preference.attachKey(mContext.getResources());
		assertThat(preference.getKeyRes(), is(R.string.test_preference_key));
		assertThat(preference.getKey(), is("universum.studios.android.preference.test.PREFERENCE.Key"));
	}

	@Test
	public void testInstantiationWithEmptyKeyResource() {
		final SharedPreference preference = new PreferenceImpl(R.string.test_preference_key_empty, PREF_DEF_VALUE);
		try {
			preference.attachKey(mContext.getResources());
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Preference key cannot be empty."));
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testInstantiationWithInvalidKeyResource() {
		try {
			new PreferenceImpl(0, "defValue");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Resource id(0) for preference key is not valid."));
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testGetActualValue() {
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
	}

	@Test
	public void testUpdateActualValue() {
		final SharedPreference<String> preference = new PreferenceImpl(PREF_KEY, null);
		// Test case 1.
		preference.updateValue("newValue");
		assertThat(preference.getValue(), is("newValue"));
		// Test case 2.
		preference.updateValue("newValue");
		assertThat(preference.getValue(), is("newValue"));
		// Test case 3.
		preference.updateValue(null);
		assertThat(preference.getValue(), is(nullValue()));
	}

	@Test
	public void testClear() {
		preference.clear();
		assertThat(preference.getValue(), is(nullValue()));
	}

	@Test
	public void testObtainFromPreferences() {
		assertThat(preference.getFromPreferences(sharedPreferences), is(PREF_DEF_VALUE));
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
		// Test again to cover case when the value of preference has been already obtained.
		preference.getFromPreferences(sharedPreferences);
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
	}

	@Test
	public void testObtainFromPreferencesWithoutSetUppedKey() {
		final SharedPreference<String> preference = new PreferenceImpl(R.string.test_preference_key, PREF_DEF_VALUE);
		try {
			preference.getFromPreferences(sharedPreferences);
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Key for preference(PreferenceImpl) is not properly initialized. " +
							"Didn't you forget to set it up via SharedPreference.attachKey(Resources)?")
			);
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testPutIntoPreferences() {
		preference.updateValue("newValue");
		assertThat(preference.putIntoPreferences(sharedPreferences), is(true));
		assertThat(preference.getValue(), is("newValue"));
	}

	@Test
	public void testPutIntoPreferencesWithoutSetUppedKey() {
		final SharedPreference<String> preference = new PreferenceImpl(R.string.test_preference_key, PREF_DEF_VALUE);
		try {
			preference.putIntoPreferences(sharedPreferences);
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Key for preference(PreferenceImpl) is not properly initialized. " +
							"Didn't you forget to set it up via SharedPreference.attachKey(Resources)?")
			);
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testParseUsingSharedPreferences() {
		assertThat(preference.retrieve(sharedPreferences), is(not(nullValue())));
	}

	@Test
	public void testSaveUsingSharedPreferences() {
		assertThat(preference.save(sharedPreferences), is(true));
	}

	@Test
	public void testCreateOnChangeListener() {
		final SharedPreferences.OnSharedPreferenceChangeListener listener = preference.createOnChangeListener(
				new SharedPreference.PreferenceChangeCallback<String>() {
					@Override
					public void onPreferenceChanged(@NonNull SharedPreference<String> preference) {
					}
				}
		);
		assertThat(listener, is(not(nullValue())));
	}

	static final class PreferenceImpl extends SharedPreference<String> {

		private PreferenceImpl(@NonNull String key, @Nullable String defValue) {
			super(key, defValue);
		}

		private PreferenceImpl(@StringRes int keyResId, @Nullable String defaultValue) {
			super(keyResId, defaultValue);
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