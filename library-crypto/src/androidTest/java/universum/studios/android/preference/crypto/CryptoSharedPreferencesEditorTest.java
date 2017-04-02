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
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class CryptoSharedPreferencesEditorTest extends BaseCryptoPreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "CryptoSharedPreferencesEditorTest";
	private static final String PREF_KEY = "PREFERENCE.Key";

	private CryptoSharedPreferences.CryptoHelper mMockHelper;
	private SharedPreferences.Editor mMockDelegate;
	private CryptoSharedPreferences.CryptoEditor mEditor;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockHelper = mock(CryptoSharedPreferences.CryptoHelper.class);
		this.mMockDelegate = mock(SharedPreferences.Editor.class);
		this.mEditor = new CryptoSharedPreferences.CryptoEditor(mMockHelper, mMockDelegate);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mMockHelper = null;
		this.mMockDelegate = null;
		this.mEditor = null;
	}

	@Test
	public void testSetCache() {
		// todo:: implement test
	}

	@Test
	public void testPutString() {
		final String prefValue = "pref.value";
		when(mMockHelper.encryptKey(PREF_KEY)).thenReturn(PREF_KEY);
		when(mMockHelper.encryptValue(prefValue)).thenReturn(prefValue);
		mEditor.putString(PREF_KEY, prefValue);
		verify(mMockDelegate, times(1)).putString(PREF_KEY, prefValue);
		verify(mMockDelegate, times(0)).commit();
		verify(mMockHelper, times(1)).encryptKey(PREF_KEY);
		verify(mMockHelper, times(1)).encryptValue(prefValue);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	public void testPutStringSet() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);
		final Set<String> prefValues = new HashSet<>();
		when(mMockHelper.encryptKey(PREF_KEY)).thenReturn(PREF_KEY);
		mEditor.putStringSet(PREF_KEY, prefValues);
		verify(mMockDelegate, times(1)).putStringSet(PREF_KEY, prefValues);
		verify(mMockDelegate, times(0)).commit();
		verify(mMockHelper, times(1)).encryptKey(PREF_KEY);
		verify(mMockHelper, times(1)).encryptValuesSet(prefValues);
	}

	@Test
	public void testPutInt() {
		final int prefValue = 1;
		when(mMockHelper.encryptKey(PREF_KEY)).thenReturn(PREF_KEY);
		when(mMockHelper.encryptValue(Integer.toString(prefValue))).thenReturn(Integer.toString(prefValue));
		mEditor.putInt(PREF_KEY, prefValue);
		verify(mMockDelegate, times(1)).putString(PREF_KEY, Integer.toString(prefValue));
		verify(mMockDelegate, times(0)).commit();
		verify(mMockHelper, times(1)).encryptKey(PREF_KEY);
		verify(mMockHelper, times(1)).encryptValue(Integer.toString(prefValue));
	}

	@Test
	public void testPutFloat() {
		final float prefValue = 0.5f;
		when(mMockHelper.encryptKey(PREF_KEY)).thenReturn(PREF_KEY);
		when(mMockHelper.encryptValue(Float.toString(prefValue))).thenReturn(Float.toString(prefValue));
		mEditor.putFloat(PREF_KEY, prefValue);
		verify(mMockDelegate, times(1)).putString(PREF_KEY, Float.toString(prefValue));
		verify(mMockDelegate, times(0)).commit();
		verify(mMockHelper, times(1)).encryptKey(PREF_KEY);
		verify(mMockHelper, times(1)).encryptValue(Float.toString(prefValue));
	}

	@Test
	public void testPutLong() {
		final long prefValue = 1000L;
		when(mMockHelper.encryptKey(PREF_KEY)).thenReturn(PREF_KEY);
		when(mMockHelper.encryptValue(Long.toString(prefValue))).thenReturn(Long.toString(prefValue));
		mEditor.putLong(PREF_KEY, prefValue);
		verify(mMockDelegate, times(1)).putString(PREF_KEY, Long.toString(prefValue));
		verify(mMockDelegate, times(0)).commit();
		verify(mMockHelper, times(1)).encryptKey(PREF_KEY);
		verify(mMockHelper, times(1)).encryptValue(Long.toString(prefValue));
	}

	@Test
	public void testPutBoolean() {
		final boolean prefValue = true;
		when(mMockHelper.encryptKey(PREF_KEY)).thenReturn(PREF_KEY);
		when(mMockHelper.encryptValue(Boolean.toString(prefValue))).thenReturn(Boolean.toString(prefValue));
		mEditor.putBoolean(PREF_KEY, prefValue);
		verify(mMockDelegate, times(1)).putString(PREF_KEY, Boolean.toString(prefValue));
		verify(mMockDelegate, times(0)).commit();
		verify(mMockHelper, times(1)).encryptKey(PREF_KEY);
		verify(mMockHelper, times(1)).encryptValue(Boolean.toString(prefValue));
	}

	@Test
	public void testClear() {
		mEditor.clear();
		verify(mMockDelegate, times(1)).clear();
	}

	@Test
	public void testRemove() {
		mEditor.remove(PREF_KEY);
		verify(mMockDelegate, times(1)).remove(PREF_KEY);
	}

	@Test
	public void testCommit() {
		mEditor.commit();
		verify(mMockDelegate, times(1)).commit();
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
	public void testApply() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD);
		mEditor.apply();
		verify(mMockDelegate, times(1)).apply();
	}
}
