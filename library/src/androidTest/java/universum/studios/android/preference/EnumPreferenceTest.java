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
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class EnumPreferenceTest extends SharedPreferenceBaseTest<EnumPreferenceTest.TestEnum> {

	@SuppressWarnings("unused")
	private static final String TAG = "EnumPreferenceTest";

	enum TestEnum {
		ONE, TWO, THREE
	}

	public EnumPreferenceTest() {
		super("PREFERENCE.Enum", TestEnum.TWO);
	}

	@Nullable
	@Override
	SharedPreference<TestEnum> onCreatePreference(String key, TestEnum defValue) {
		return new EnumPreference<>(key, defValue);
	}

	@Test
	public void testInstantiation() {
		new EnumPreference<>(DEF_PREF_KEY, null);
		new EnumPreference<>(R.string.test_preference_key, null);
	}

	@Test
	public void testOnPutObtainIntoFromPreferences() {
		assertThat(preference.getValue(), is(PREF_DEF_VALUE));
		preference.updateValue(TestEnum.THREE);
		assertThat(preference.onPutIntoPreferences(sharedPreferences), is(true));
		preference.clear();
		assertThat(preference.onGetFromPreferences(sharedPreferences), is(TestEnum.THREE));
	}

	@Test
	public void testObtainFromPreferencesUnsaved() {
		final EnumPreference<TestEnum> enumPreference = new EnumPreference<>(PREF_KEY + "Unsaved", null);
		assertThat(enumPreference.onGetFromPreferences(sharedPreferences), is(nullValue()));
	}
}