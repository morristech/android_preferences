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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class PreferencesManagerTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "PreferencesManagerTest";
	private static final String PREF_KEY = "PREFERENCE.Key";

	private final SharedPreferences.OnSharedPreferenceChangeListener SHARED_PREFERENCE_LISTENER = new SharedPreferences.OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		}
	};

	private PreferencesManager mManager;
	private BooleanPreference mPreference;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mManager = new PreferencesManager.Builder(mContext).build();
		this.mPreference = new BooleanPreference("PREFERENCE.Boolean", true);
		// Ensure that we have a clean slate before each test.
		this.mManager.remove(PREF_KEY);
		this.mManager.removePreference(mPreference);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mManager = null;
		this.mPreference = null;
	}

	@Test
	public void testDefaultInstantiation() {
		final String packageName = mContext.getPackageName();
		final PreferencesManager manager = new PreferencesManager.Builder(mContext).build();
		assertThat(
				manager.getPreferences(),
				is(mContext.getSharedPreferences(
						packageName + "_preferences",
						Context.MODE_PRIVATE
				))
		);
		assertThat(manager.getPreferencesName(), is(packageName + "_preferences"));
		assertThat(manager.getPreferencesFileMode(), is(SharedPreferencesPolicy.FILE_MODE_PRIVATE));
	}

	@Test
	public void testInstantiationWithPreferenceName() {
		final PreferencesManager manager = new PreferencesManager.Builder(mContext)
				.preferencesName("test_preferences_name")
				.build();
		assertThat(
				manager.getPreferences(),
				is(mContext.getSharedPreferences(
						"test_preferences_name",
						Context.MODE_PRIVATE
				))
		);
		assertThat(manager.getPreferencesName(), is("test_preferences_name"));
		assertThat(manager.getPreferencesFileMode(), is(SharedPreferencesPolicy.FILE_MODE_PRIVATE));
	}

	@Test
	@SuppressWarnings("PointlessBitwiseExpression")
	public void testInstantiationWithPreferencesNameAndMode() {
		final PreferencesManager manager = new PreferencesManager.Builder(mContext)
				.preferencesName("test_preferences_name")
				.preferencesFileMode(SharedPreferencesPolicy.FILE_MODE_PRIVATE | SharedPreferencesPolicy.FILE_MODE_APPEND)
				.build();
		assertThat(
				manager.getPreferences(),
				is(mContext.getSharedPreferences(
						"test_preferences_name",
						Context.MODE_APPEND | Context.MODE_APPEND
				))
		);
		assertThat(manager.getPreferencesName(), is("test_preferences_name"));
		assertThat(manager.getPreferencesFileMode(), is(SharedPreferencesPolicy.FILE_MODE_PRIVATE | SharedPreferencesPolicy.FILE_MODE_APPEND));
	}

	@Test
	public void testRegisterUnregisterOnSharedPreferenceChangeListener() {
		mManager.registerOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
		mManager.unregisterOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
	}

	@Test
	public void testKey() {
		if (TestUtils.hasLibraryRootTestPackageName(mContext)) {
			final int keyIdentifier = mContext.getResources().getIdentifier(
					"test_preference_key",
					"string",
					mContext.getPackageName()
			);
			assertThat(mManager.key(keyIdentifier), is("TEST_PREFERENCE.Key"));
		}
	}

	@Test
	public void testPutGetBoolean() {
		assertThat(mManager.getBoolean(PREF_KEY, true), is(true));
		assertThat(mManager.putBoolean(PREF_KEY, false), is(true));
		assertThat(mManager.getBoolean(PREF_KEY, true), is(false));
	}

	@Test
	public void testPutGetInt() {
		assertThat(mManager.getInt(PREF_KEY, 100), is(100));
		assertThat(mManager.putInt(PREF_KEY, 14), is(true));
		assertThat(mManager.getInt(PREF_KEY, 0), is(14));
	}

	@Test
	public void testPutGetLong() {
		assertThat(mManager.getLong(PREF_KEY, 124589L), is(124589L));
		assertThat(mManager.putLong(PREF_KEY, 1545454L), is(true));
		assertThat(mManager.getLong(PREF_KEY, 0), is(1545454L));
	}

	@Test
	public void testPutGetFloat() {
		assertThat(mManager.getFloat(PREF_KEY, 0.5f), is(0.5f));
		assertThat(mManager.putFloat(PREF_KEY, 0.324234f), is(true));
		assertThat(mManager.getFloat(PREF_KEY, 0), is(0.324234f));
	}

	@Test
	public void testPutGetString() {
		assertThat(mManager.getString(PREF_KEY, "default"), is("default"));
		assertThat(mManager.putString(PREF_KEY, "new value"), is(true));
		assertThat(mManager.getString(PREF_KEY, null), is("new value"));
	}

	@Test
	public void testContains() {
		assertThat(mManager.contains(PREF_KEY), is(false));
		mManager.putFloat(PREF_KEY, 0.324234f);
		assertThat(mManager.contains(PREF_KEY), is(true));
		mManager.remove(PREF_KEY);
		assertThat(mManager.contains(PREF_KEY), is(false));
	}

	@Test
	public void testRemove() {
		mManager.putInt(PREF_KEY, 14);
		assertThat(mManager.remove(PREF_KEY), is(true));
		assertThat(mManager.contains(PREF_KEY), is(false));
	}

	@Test
	public void testPutGetPreference() {
		final StringPreference preference = new StringPreference(PREF_KEY, "default value");
		assertThat(mManager.getPreference(preference), is("default value"));
		assertThat(mManager.putPreference(preference, "new value"), is(true));
		assertThat(mManager.getPreference(preference), is("new value"));
	}

	@Test
	public void testContainsPreference() {
		final StringPreference preference = new StringPreference(PREF_KEY, "default value");
		assertThat(mManager.containsPreference(preference), is(false));
		mManager.putPreference(preference, "some value");
		assertThat(mManager.containsPreference(preference), is(true));
		mManager.removePreference(preference);
		assertThat(mManager.containsPreference(preference), is(false));
	}

	@Test
	public void testRemovePreference() {
		final StringPreference preference = new StringPreference(PREF_KEY, "default value");
		mManager.putPreference(preference, "new value");
		assertThat(mManager.removePreference(preference), is(true));
		assertThat(mManager.getPreference(preference), is("default value"));
	}

	@Test
	public void testCaching() {
		assertThat(mManager.isCachingEnabled(), is(false));
		mManager.setCachingEnabled(true);
		assertThat(mManager.isCachingEnabled(), is(true));
		mManager.putPreference(mPreference, false);
		assertThat(mManager.getPreference(mPreference), is(false));
		mManager.setCachingEnabled(false);
		mManager.putPreference(mPreference, true);
		assertThat(mManager.getPreference(mPreference), is(true));
	}
}
