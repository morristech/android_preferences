/*
 * =================================================================================================
 *                             Copyright (C) 2015 Martin Albedinsky
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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.preference.inner.ContextBaseTest;
import universum.studios.android.preference.test.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class PreferencesManagerTest extends ContextBaseTest {

	@SuppressWarnings("unused")
	private static final String TAG = "PreferencesManagerTest";

	private final SharedPreferences.OnSharedPreferenceChangeListener SHARED_PREFERENCE_LISTENER = new SharedPreferences.OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		}
	};

	private PreferencesManager preferencesManager;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.preferencesManager = new ManagerImpl(mContext);
		this.preferencesManager.remove(mContext.getString(R.string.test_preference_key));
	}

	@Test
	public void testInstantiation() {
		final PreferencesManager manager = new ManagerImpl(mContext);
		assertThat(manager.getMode(), is(Context.MODE_PRIVATE));
		assertThat(manager.getSharedPreferencesName(), is("universum.studios.android.preference.test_preferences"));
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						"universum.studios.android.preference.test_preferences",
						Context.MODE_PRIVATE
				))
		);
	}

	@Test
	public void testInstantiationWithName() {
		final PreferencesManager manager = new ManagerImpl(mContext, "test_preferences_name");
		assertThat(manager.getSharedPreferencesName(), is("test_preferences_name"));
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						"test_preferences_name",
						Context.MODE_PRIVATE
				))
		);
	}

	@Test
	public void testInitializationWithNameAndMode() {
		final PreferencesManager manager = new ManagerImpl(mContext, "test_preferences_name", PreferencesManager.MODE_APPEND);
		assertThat(manager.getMode(), is(Context.MODE_APPEND));
		assertThat(manager.getSharedPreferencesName(), is("test_preferences_name"));
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						"test_preferences_name",
						Context.MODE_APPEND
				))
		);
	}

	@Test
	public void testInitializationWithEmptyName() {
		final PreferencesManager manager = new ManagerImpl(mContext, "", PreferencesManager.MODE_PRIVATE);
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						"universum.studios.android.preference.test_preferences",
						Context.MODE_PRIVATE
				))
		);
	}

	@Test
	@SuppressWarnings("WrongConstant")
	public void testInitializationWithInvalidMode() {
		final PreferencesManager manager = new ManagerImpl(mContext, "test_preferences_name", 15);
		assertThat(manager.getMode(), is(Context.MODE_PRIVATE));
	}

	@Test
	public void testRegisterOnPreferenceChangeListener() {
		preferencesManager.registerOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
	}

	@Test
	public void testUnregisterOnSharedPreferenceChangeListener() {
		preferencesManager.registerOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
		preferencesManager.unregisterOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
	}

	@Test
	public void testKey() {
		final String key = preferencesManager.key(R.string.test_preference_key);
		assertThat(TextUtils.isEmpty(key), is(false));
	}

	@Test
	public void testObtainDefaultBoolean() {
		assertThat(preferencesManager.getBoolean(mContext.getString(R.string.test_preference_key), true), is(true));
	}

	@Test
	public void testPutObtainBoolean() {
		assertThat(preferencesManager.putBoolean(mContext.getString(R.string.test_preference_key), false), is(true));
		assertThat(preferencesManager.getBoolean(mContext.getString(R.string.test_preference_key), true), is(false));
	}

	@Test
	public void testObtainDefaultInt() {
		assertThat(preferencesManager.getInt(mContext.getString(R.string.test_preference_key), 100), is(100));
	}

	@Test
	public void testPutObtainInt() {
		assertThat(preferencesManager.putInt(mContext.getString(R.string.test_preference_key), 14), is(true));
		assertThat(preferencesManager.getInt(mContext.getString(R.string.test_preference_key), 0), is(14));
	}

	@Test
	public void testObtainDefaultLong() {
		assertThat(preferencesManager.getLong(mContext.getString(R.string.test_preference_key), 124589L), is(124589L));
	}

	@Test
	public void testPutObtainLong() {
		assertThat(preferencesManager.putLong(mContext.getString(R.string.test_preference_key), 1545454L), is(true));
		assertThat(preferencesManager.getLong(mContext.getString(R.string.test_preference_key), 0), is(1545454L));
	}

	@Test
	public void testObtainDefaultFloat() {
		assertThat(preferencesManager.getFloat(mContext.getString(R.string.test_preference_key), 0.5f), is(0.5f));
	}

	@Test
	public void testPutObtainFloat() {
		assertThat(preferencesManager.putFloat(mContext.getString(R.string.test_preference_key), 0.324234f), is(true));
		assertThat(preferencesManager.getFloat(mContext.getString(R.string.test_preference_key), 0), is(0.324234f));
	}

	@Test
	public void testObtainDefaultString() {
		assertThat(preferencesManager.getString(mContext.getString(R.string.test_preference_key), "default"), is("default"));
	}

	@Test
	public void testPutObtainString() {
		assertThat(preferencesManager.putString(mContext.getString(R.string.test_preference_key), "new value"), is(true));
		assertThat(preferencesManager.getString(mContext.getString(R.string.test_preference_key), null), is("new value"));
	}

	@Test
	public void testContains() {
		// todo:
	}

	@Test
	public void testRemove() {
		// todo:
	}

	@Test
	public void testPutObtainPreference() {
		final StringPreference preference = new StringPreference("PREFERENCE.String", "default value");
		assertThat(preferencesManager.putPreference(preference, "new value"), is(true));
		assertThat(preferencesManager.getPreference(preference), is("new value"));
	}

	@Test
	public void testContainsPreference() {
		// todo:
	}

	@Test
	public void testRemovePreference() {
		final StringPreference preference = new StringPreference("PREFERENCE.String", "default value");
		assertThat(preferencesManager.putPreference(preference, "new value"), is(true));
		assertThat(preferencesManager.getPreference(preference), is("new value"));
		assertThat(preferencesManager.removePreference(preference), is(true));
		assertThat(preferencesManager.getPreference(preference), is("default value"));
	}

	@Test
	public void testCaching() {
		final ManagerImpl manager = new ManagerImpl(mContext);
		assertThat(manager.isCachingEnabled(), is(false));
		manager.setCachingEnabled(true);
		assertThat(manager.isCachingEnabled(), is(true));
		manager.putPreference(manager.booleanPreference, false);
		assertThat(manager.getPreference(manager.booleanPreference), is(false));
		manager.setCachingEnabled(false);
		manager.putPreference(manager.booleanPreference, true);
		assertThat(manager.getPreference(manager.booleanPreference), is(true));
	}

	static final class ManagerImpl extends PreferencesManager {

		final BooleanPreference booleanPreference = new BooleanPreference(R.string.test_preference_key, true);

		ManagerImpl(@NonNull Context context) {
			super(context);
		}

		private ManagerImpl(@NonNull Context context, @NonNull String preferencesName) {
			super(context, preferencesName);
		}

		private ManagerImpl(@NonNull Context context, @NonNull String preferencesName, @Mode int mode) {
			super(context, preferencesName, mode);
		}
	}
}