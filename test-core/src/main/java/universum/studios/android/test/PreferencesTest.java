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
package universum.studios.android.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public abstract class PreferencesTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "PreferencesTest";

	protected SharedPreferences mPreferences;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		final String preferencesName = mContext.getPackageName() + ":test_preferences";
		this.mPreferences = mContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mPreferences = null;
	}
}
