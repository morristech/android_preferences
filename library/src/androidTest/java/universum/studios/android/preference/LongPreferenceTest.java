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

import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.preference.test.R;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class LongPreferenceTest extends SharedPreferenceBaseTest<Long> {

	@SuppressWarnings("unused")
	private static final String TAG = "LongPreferenceTest";

	public LongPreferenceTest() {
		super("PREFERENCE.Long", 15568L);
	}

	@Nullable
	@Override
	SharedPreference<Long> onCreatePreference(String key, Long defValue) {
		return new LongPreference(key, defValue);
	}

	@Test
	public void testInstantiation() {
		new LongPreference(DEF_PREF_KEY, 0L);
		new LongPreference(R.string.test_preference_key, 0L);
	}

	@Test
	public void testOnPutObtainIntoFromPreferences() {
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
		preference.updateValue(15000L);
		assertThat(preference.onPutIntoPreferences(sharedPreferences), is(true));
		preference.clear();
		assertThat(preference.onGetFromPreferences(sharedPreferences), is(15000L));
	}
}