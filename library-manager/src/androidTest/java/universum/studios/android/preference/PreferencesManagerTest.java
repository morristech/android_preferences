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
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assume.assumeTrue;

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
		this.mManager = new PreferencesManager(mContext);
		this.mPreference = new BooleanPreference("PREFERENCE.Boolean", true);
		// Ensure that we have a clean slate before each test.
		this.mManager.removeAll();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mManager = null;
		this.mPreference = null;
	}

	@Test
	public void testInstantiation() {
		final String packageName = mContext.getPackageName();
		final PreferencesManager manager = new PreferencesManager(mContext);
		assertThat(manager.getContext(), is(not(nullValue())));
		assertThat(manager.getSharedPreferencesName(), is(packageName + "_preferences"));
		assertThat(manager.getSharedPreferencesMode(), is(SharedPreferencesPolicy.MODE_PRIVATE));
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						packageName + "_preferences",
						SharedPreferencesPolicy.MODE_PRIVATE
				))
		);
	}

	@Test
	public void testSetGetSharedPreferencesName() {
		final String packageName = mContext.getPackageName();
		final PreferencesManager manager = new PreferencesManager(mContext);
		manager.setSharedPreferencesName(packageName + ":test_preferences");
		assertThat(manager.getSharedPreferencesName(), is(packageName + ":test_preferences"));
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						packageName + ":test_preferences",
						SharedPreferencesPolicy.MODE_PRIVATE
				))
		);
	}

	@Test
	@SuppressWarnings("PointlessBitwiseExpression")
	public void testSetGetSharedPreferencesMode() {
		final String packageName = mContext.getPackageName();
		final PreferencesManager manager = new PreferencesManager(mContext);
		manager.setSharedPreferencesMode(SharedPreferencesPolicy.MODE_PRIVATE | SharedPreferencesPolicy.MODE_APPEND);
		assertThat(manager.getSharedPreferencesMode(), is(SharedPreferencesPolicy.MODE_PRIVATE | SharedPreferencesPolicy.MODE_APPEND));
		assertThat(
				manager.getSharedPreferences(),
				is(mContext.getSharedPreferences(
						packageName + "_preferences",
						SharedPreferencesPolicy.MODE_PRIVATE | SharedPreferencesPolicy.MODE_APPEND
				))
		);
	}

	@Test
	public void testRegisterUnregisterOnSharedPreferenceChangeListener() {
		mManager.registerOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
		mManager.unregisterOnSharedPreferenceChangeListener(SHARED_PREFERENCE_LISTENER);
	}

	@Test
	public void testKey() {
		assumeTrue(TestUtils.hasLibraryRootTestPackageName(mContext));
		final int keyIdentifier = mContext.getResources().getIdentifier(
				"test_preference_key",
				"string",
				mContext.getPackageName()
		);
		assertThat(mManager.key(keyIdentifier), is("TEST_PREFERENCE.Key"));
	}

	@Test
	public void testContains() {
		assertThat(mManager.contains(PREF_KEY), is(false));
		assertThat(mManager.putString(PREF_KEY, "NewValue"), is(true));
		assertThat(mManager.contains(PREF_KEY), is(true));
	}

	@Test
	public void testPutGetString() {
		assertThat(mManager.getString(PREF_KEY, "DefaultValue"), is("DefaultValue"));
		assertThat(mManager.putString(PREF_KEY, "NewValue"), is(true));
		assertThat(mManager.getString(PREF_KEY, "DefaultValue"), is("NewValue"));
	}

	@Test
	public void testPutGetStringSet() {
		assertThat(mManager.getStringSet(PREF_KEY, null), is(nullValue()));
		final Set<String> set = new HashSet<>(1);
		set.add("NewValue");
		assertThat(mManager.putStringSet(PREF_KEY, set), is(true));
		assertThat(mManager.getStringSet(PREF_KEY, null), is(set));
	}

	@Test
	public void testPutGetInt() {
		assertThat(mManager.getInt(PREF_KEY, -1), is(-1));
		assertThat(mManager.putInt(PREF_KEY, 1), is(true));
		assertThat(mManager.getInt(PREF_KEY, -1), is(1));
	}

	@Test
	public void testPutGetFloat() {
		assertThat(mManager.getFloat(PREF_KEY, 0.0f), is(0.0f));
		assertThat(mManager.putFloat(PREF_KEY, 0.05f), is(true));
		assertThat(mManager.getFloat(PREF_KEY, 0.0f), is(0.05f));
	}

	@Test
	public void testPutGetLong() {
		assertThat(mManager.getLong(PREF_KEY, 100L), is(100L));
		assertThat(mManager.putLong(PREF_KEY, -2000L), is(true));
		assertThat(mManager.getLong(PREF_KEY, 0L), is(-2000L));
	}

	@Test
	public void testPutGetBoolean() {
		assertThat(mManager.getBoolean(PREF_KEY, true), is(true));
		assertThat(mManager.putBoolean(PREF_KEY, false), is(true));
		assertThat(mManager.getBoolean(PREF_KEY, true), is(false));
	}

	@Test
	public void testRemove() {
		assertThat(mManager.putString(PREF_KEY, "NewValue"), is(true));
		assertThat(mManager.remove(PREF_KEY), is(true));
		assertThat(mManager.contains(PREF_KEY), is(false));
	}

	@Test
	public void testRemoveAll() {
		assertThat(mManager.removeAll(), is(0));
		assertThat(mManager.putString(PREF_KEY, "NewValue"), is(true));
		assertThat(mManager.removeAll(), is(1));
		assertThat(mManager.contains(PREF_KEY), is(false));
	}

	@Test
	public void testPutGetPreference() {
		final StringPreference preference = new StringPreference(PREF_KEY, "DefaultValue");
		assertThat(mManager.getPreference(preference), is("DefaultValue"));
		assertThat(mManager.putPreference(preference, "UpdatedValue"), is(true));
		assertThat(mManager.getPreference(preference), is("UpdatedValue"));
	}

	@Test
	public void testContainsPreference() {
		final StringPreference preference = new StringPreference(PREF_KEY, "DefaultValue");
		assertThat(mManager.containsPreference(preference), is(false));
		assertThat(mManager.putPreference(preference, "UpdatedValue"), is(true));
		assertThat(mManager.containsPreference(preference), is(true));
		assertThat(mManager.removePreference(preference), is(true));
		assertThat(mManager.containsPreference(preference), is(false));
	}

	@Test
	public void testRemovePreference() {
		final StringPreference preference = new StringPreference(PREF_KEY, "DefaultValue");
		assertThat(mManager.putPreference(preference, "UpdatedValue"), is(true));
		assertThat(mManager.removePreference(preference), is(true));
		assertThat(mManager.getPreference(preference), is("DefaultValue"));
	}

	@Test
	public void testCaching() {
		assertThat(mManager.isCachingEnabled(), is(false));
		mManager.setCachingEnabled(true);
		assertThat(mManager.isCachingEnabled(), is(true));
		assertThat(mManager.putPreference(mPreference, false), is(true));
		assertThat(mManager.getPreference(mPreference), is(false));
		mManager.setCachingEnabled(false);
		assertThat(mManager.putPreference(mPreference, true), is(true));
		assertThat(mManager.getPreference(mPreference), is(true));
	}
}
