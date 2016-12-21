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
import android.support.annotation.Nullable;

import universum.studios.android.preference.inner.ContextBaseTest;

/**
 * @author Martin Albedinsky
 */
class SharedPreferenceBaseTest<T> extends ContextBaseTest {

	@SuppressWarnings("unused")
	private static final String TAG = "PreferenceTestCase";

	static final String DEF_PREF_KEY = "key";

	final String PREF_KEY;
	final T PREF_DEF_VALUE;
	PreferencesManager preferencesManager;
	SharedPreferences sharedPreferences;
	SharedPreference<T> preference;

	SharedPreferenceBaseTest() {
		this(DEF_PREF_KEY, null);
	}

	SharedPreferenceBaseTest(T defValue) {
		this(DEF_PREF_KEY, defValue);
	}

	SharedPreferenceBaseTest(String key, T defValue) {
		this.PREF_KEY = key;
		this.PREF_DEF_VALUE = defValue;
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.preferencesManager = new PreferencesManagerTest.ManagerImpl(mContext);
		this.sharedPreferences = preferencesManager.getSharedPreferences();
		this.preference = onCreatePreference(PREF_KEY, PREF_DEF_VALUE);
		if (preference != null) {
			// Update the current value within shared preferences so we can always test with the clear 'account'.
			preference.putIntoPreferences(sharedPreferences);
		}
	}

	@Nullable
	SharedPreference<T> onCreatePreference(String key, T defValue) {
		return null;
	}
}
