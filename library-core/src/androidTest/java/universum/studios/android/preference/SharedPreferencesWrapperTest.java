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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests only delegation for all provided methods by {@link SharedPreferencesWrapper} to the
 * {@link SharedPreferences} instance wrapped by instance of such wrapper.
 *
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressLint("CommitPrefEdits")
public final class SharedPreferencesWrapperTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "SharedPreferencesWrapperTest";
	private static final String PREF_KEY = "PREFERENCE.Key";

	private SharedPreferences mMockPreferences;
	private SharedPreferencesWrapper mWrapper;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockPreferences = mock(SharedPreferences.class);
		this.mWrapper = new SharedPreferencesWrapper(mMockPreferences);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mMockPreferences = null;
	}

	@Test
	public void testGetWrappedPreferences() {
		assertThat(mWrapper.getWrappedPreferences(), is(mMockPreferences));
	}

	@Test
	public void testRegisterOnSharedPreferenceChangeListener() {
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mWrapper.registerOnSharedPreferenceChangeListener(mockListener);
		verify(mMockPreferences, times(1)).registerOnSharedPreferenceChangeListener(mockListener);
	}

	@Test
	public void testUnregisterOnSharedPreferenceChangeListener() {
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mWrapper.unregisterOnSharedPreferenceChangeListener(mockListener);
		verify(mMockPreferences, times(1)).unregisterOnSharedPreferenceChangeListener(mockListener);
	}

	@Test
	public void testGetAll() {
		mWrapper.getAll();
		verify(mMockPreferences, times(1)).getAll();
	}

	@Test
	public void testContains() {
		mWrapper.contains(PREF_KEY);
		verify(mMockPreferences, times(1)).contains(PREF_KEY);
	}

	@Test
	public void testGetString() {
		mWrapper.getString(PREF_KEY, "Slovakia");
		verify(mMockPreferences, times(1)).getString(PREF_KEY, "Slovakia");
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	public void testGetStringSet() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);
		mWrapper.getStringSet(PREF_KEY, null);
		verify(mMockPreferences, times(1)).getStringSet(PREF_KEY, null);
	}

	@Test
	public void testGetInt() {
		mWrapper.getInt(PREF_KEY, 12);
		verify(mMockPreferences, times(1)).getInt(PREF_KEY, 12);
	}

	@Test
	public void testGetLong() {
		mWrapper.getLong(PREF_KEY, 1000L);
		verify(mMockPreferences, times(1)).getLong(PREF_KEY, 1000L);
	}

	@Test
	public void testGetFloat() {
		mWrapper.getFloat(PREF_KEY, 0.55f);
		verify(mMockPreferences, times(1)).getFloat(PREF_KEY, 0.55f);
	}

	@Test
	public void testGetBoolean() {
		mWrapper.getBoolean(PREF_KEY, false);
		verify(mMockPreferences, times(1)).getBoolean(PREF_KEY, false);
	}

	@Test
	public void testEdit() {
		mWrapper.edit();
		verify(mMockPreferences, times(1)).edit();
	}
}
