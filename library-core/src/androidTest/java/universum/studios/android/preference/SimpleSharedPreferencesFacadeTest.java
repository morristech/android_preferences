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

import java.util.HashMap;
import java.util.Map;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests only delegation for all provided methods by {@link SimpleSharedPreferencesFacade} to the
 * {@link SharedPreferences} instance hidden behind instance of such facade.
 *
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressLint("CommitPrefEdits")
public final class SimpleSharedPreferencesFacadeTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "SimpleSharedPreferencesFacadeTest";
	private static final String PREF_KEY = "PREFERENCE.Key";

	private SharedPreferences mMockPreferences;
	private SharedPreferences.Editor mMockPreferencesEditor;
	private SimpleSharedPreferencesFacade mFacade;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockPreferences = mock(SharedPreferences.class);
		this.mMockPreferencesEditor = mock(SharedPreferences.Editor.class);
		when(mMockPreferences.edit()).thenReturn(mMockPreferencesEditor);
		this.mFacade = new SimpleSharedPreferencesFacade.Builder<SimpleSharedPreferencesFacade.Builder>().preferences(mMockPreferences).build();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mFacade = null;
	}

	@Test
	public void testGetPreferences() {
		assertThat(mFacade.getPreferences(), is(mMockPreferences));
	}

	@Test
	public void testRegisterOnSharedPreferenceChangeListener() {
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mFacade.registerOnSharedPreferenceChangeListener(mockListener);
		verify(mMockPreferences, times(1)).registerOnSharedPreferenceChangeListener(mockListener);
	}

	@Test
	public void testUnregisterOnSharedPreferenceChangeListener() {
		final SharedPreferences.OnSharedPreferenceChangeListener mockListener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
		mFacade.unregisterOnSharedPreferenceChangeListener(mockListener);
		verify(mMockPreferences, times(1)).unregisterOnSharedPreferenceChangeListener(mockListener);
	}


	@Test
	public void testContains() {
		mFacade.contains(PREF_KEY);
		verify(mMockPreferences, times(1)).contains(PREF_KEY);
	}

	@Test
	public void testPutString() {
		when(mMockPreferencesEditor.putString(PREF_KEY, "Universe")).thenReturn(mMockPreferencesEditor);
		mFacade.putString(PREF_KEY, "Universe");
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).putString(PREF_KEY, "Universe");
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	public void testGetString() {
		mFacade.getString(PREF_KEY, "Slovakia");
		verify(mMockPreferences, times(1)).getString(PREF_KEY, "Slovakia");
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	public void testPutStringSet() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);
		when(mMockPreferencesEditor.putStringSet(PREF_KEY, null)).thenReturn(mMockPreferencesEditor);
		mFacade.putStringSet(PREF_KEY, null);
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).putStringSet(PREF_KEY, null);
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	public void testGetStringSet() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);
		mFacade.getStringSet(PREF_KEY, null);
		verify(mMockPreferences, times(1)).getStringSet(PREF_KEY, null);
	}

	@Test
	public void testPutInt() {
		when(mMockPreferencesEditor.putInt(PREF_KEY, 14)).thenReturn(mMockPreferencesEditor);
		mFacade.putInt(PREF_KEY, 14);
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).putInt(PREF_KEY, 14);
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	public void testGetInt() {
		mFacade.getInt(PREF_KEY, 12);
		verify(mMockPreferences, times(1)).getInt(PREF_KEY, 12);
	}

	@Test
	public void testPutLong() {
		when(mMockPreferencesEditor.putLong(PREF_KEY, 20000L)).thenReturn(mMockPreferencesEditor);
		mFacade.putLong(PREF_KEY, 20000L);
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).putLong(PREF_KEY, 20000L);
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	public void testGetLong() {
		mFacade.getLong(PREF_KEY, 1000L);
		verify(mMockPreferences, times(1)).getLong(PREF_KEY, 1000L);
	}

	@Test
	public void testPutFloat() {
		when(mMockPreferencesEditor.putFloat(PREF_KEY, -14.56f)).thenReturn(mMockPreferencesEditor);
		mFacade.putFloat(PREF_KEY, -14.56f);
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).putFloat(PREF_KEY, -14.56f);
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	public void testGetFloat() {
		mFacade.getFloat(PREF_KEY, 0.55f);
		verify(mMockPreferences, times(1)).getFloat(PREF_KEY, 0.55f);
	}

	@Test
	public void testPutBoolean() {
		when(mMockPreferencesEditor.putBoolean(PREF_KEY, true)).thenReturn(mMockPreferencesEditor);
		mFacade.putBoolean(PREF_KEY, true);
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).putBoolean(PREF_KEY, true);
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	public void testGetBoolean() {
		mFacade.getBoolean(PREF_KEY, false);
		verify(mMockPreferences, times(1)).getBoolean(PREF_KEY, false);
	}

	@Test
	public void testRemove() {
		when(mMockPreferencesEditor.remove(PREF_KEY)).thenReturn(mMockPreferencesEditor);
		mFacade.remove(PREF_KEY);
		verify(mMockPreferences, times(1)).edit();
		verify(mMockPreferencesEditor, times(1)).remove(PREF_KEY);
		verify(mMockPreferencesEditor, times(1)).commit();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testRemoveAll() {
		final Map<String, Object> values = new HashMap<>(5);
		for (int i = 0; i < 5; i++) {
			final String key = PREF_KEY + "." + i;
			values.put(key, Integer.toString(i));
			when(mMockPreferencesEditor.remove(key)).thenReturn(mMockPreferencesEditor);
		}
		when(mMockPreferences.getAll()).thenReturn((Map) values);
		mFacade.removeAll();
		verify(mMockPreferences, times(values.size())).edit();
		for (int i = 0; i < values.size(); i++) {
			final String key = PREF_KEY + "." + i;
			verify(mMockPreferencesEditor, times(1)).remove(key);
		}
		verify(mMockPreferencesEditor, times(values.size())).commit();
	}

	@Test
	public void testRemoveAllWithoutPersistedValues() {
		mFacade.removeAll();
		verify(mMockPreferences, times(0)).edit();
	}
}
