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
package universum.studios.android.preference.crypto;

import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class CryptoSharedPreferencesChangeListenersTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "CryptoSharedPreferencesChangeListenersTest";
	private static final String PREF_KEY = "PREFERENCE.Key";

	private SharedPreferences mMockPreferences;
	private CryptoSharedPreferences.ChangeListeners mListeners;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockPreferences = mock(SharedPreferences.class);
		this.mListeners = new CryptoSharedPreferences.ChangeListeners(
				new CryptoSharedPreferences.CryptoHelper(null, null),
				mMockPreferences
		);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mMockPreferences = null;
		this.mListeners = null;
	}

	@Test
	public void testRegisterUnregister() {
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mListeners.register(mockListener);
		mListeners.register(mockListener);
		mListeners.unregister(mockListener);
		assertThat(mListeners.isEmpty(), is(true));
		mListeners.register(mockListener);
		mListeners.register(mock(SharedPreferences.OnSharedPreferenceChangeListener.class));
		mListeners.unregister(mockListener);
		mListeners.unregister(mockListener);
		assertThat(mListeners.isEmpty(), is(false));
	}

	@Test
	public void testIsEmptyWithoutRegisteredAnyListeners() {
		assertThat(mListeners.isEmpty(), is(true));
	}

	@Test
	public void testOnSharedPreferenceChangedWithRegisteredListener() {
		final SharedPreferences mockPreferences = mock(SharedPreferences.class);
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mListeners.register(mockListener);
		mListeners.onSharedPreferenceChanged(mockPreferences, PREF_KEY + ".First");
		mListeners.onSharedPreferenceChanged(mockPreferences, PREF_KEY + ".Second");
		verify(mockListener, times(1)).onSharedPreferenceChanged(mMockPreferences, PREF_KEY + ".First");
		verify(mockListener, times(1)).onSharedPreferenceChanged(mMockPreferences, PREF_KEY + ".Second");
		verify(mockListener, times(0)).onSharedPreferenceChanged(mockPreferences, PREF_KEY);
	}

	@Test
	public void testOnSharedPreferenceChangedWithUnregisteredListener() {
		final SharedPreferences mockPreferences = mock(SharedPreferences.class);
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mListeners.register(mockListener);
		mListeners.unregister(mockListener);
		mListeners.onSharedPreferenceChanged(mockPreferences, PREF_KEY);
		verify(mockListener, times(0)).onSharedPreferenceChanged(mMockPreferences, PREF_KEY);
	}


	@Test
	public void testOnSharedPreferenceChangedWithoutRegisteredAnyListeners() {
		mListeners.onSharedPreferenceChanged(
				mock(SharedPreferences.class),
				PREF_KEY
		);
	}
}
