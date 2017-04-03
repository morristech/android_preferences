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

import universum.studios.android.preference.SharedPreferencesFacade;
import universum.studios.android.preference.SharedPreferencesPolicy;
import universum.studios.android.preference.SimpleSharedPreferencesFacade;
import universum.studios.android.test.BaseInstrumentedTest;

/**
 * @author Martin Albedinsky
 */
abstract class BaseCryptoPreferencesTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "BaseCryptoPreferencesTest";

	SharedPreferences mPreferences;
	SharedPreferencesFacade mPreferencesFacade;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mPreferences = mContext.getSharedPreferences(
				mContext.getPackageName() + ":test_crypto_preferences",
				SharedPreferencesPolicy.FILE_MODE_PRIVATE
		);
		this.mPreferencesFacade = new SimpleSharedPreferencesFacade.Builder<SimpleSharedPreferencesFacade.Builder>()
				.preferences(mPreferences)
				.build();
		// Ensure that we have a clean slate before each test.
		this.mPreferencesFacade.removeAll();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mPreferences = null;
		this.mPreferencesFacade = null;
	}
}
