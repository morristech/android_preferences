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
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A {@link SharedPreference} implementation that may be used to persist a {@link String} value via
 * {@link SharedPreferences}.
 *
 * @author Martin Albedinsky
 * @see BooleanPreference
 * @see IntegerPreference
 * @see LongPreference
 * @see FloatPreference
 * @see EnumPreference
 */
public final class StringPreference extends SharedPreference<String> {

	/**
	 * Creates a new instance of StringPreference with the specified <var>key</var> and <var>defValue</var>.
	 *
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public StringPreference(@NonNull final String key, @Nullable final String defValue) {
		super(key, defValue);
	}

	/**
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull final SharedPreferences preferences) {
		return preferences.edit().putString(mKey, mValue).commit();
	}

	/**
	 */
	@Nullable
	@Override
	protected String onGetFromPreferences(@NonNull final SharedPreferences preferences) {
		return preferences.getString(mKey, mDefaultValue);
	}
}
