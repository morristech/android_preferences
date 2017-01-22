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
import android.support.annotation.StringRes;

/**
 * A {@link SharedPreference} implementation that may be used to manage (store/retrieve) a {@link String}
 * preference value within {@link SharedPreferences}.
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
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of StringPreference.
	 *
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public StringPreference(@NonNull String key, @Nullable String defValue) {
		super(key, defValue);
	}

	/**
	 * Creates a new instance of StringPreference.
	 *
	 * @see SharedPreference#SharedPreference(int, Object)
	 */
	public StringPreference(@StringRes int keyResId, @Nullable String defValue) {
		super(keyResId, defValue);
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull SharedPreferences preferences) {
		return preferences.edit().putString(mKey, mValue).commit();
	}

	/**
	 */
	@Nullable
	@Override
	protected String onGetFromPreferences(@NonNull SharedPreferences preferences) {
		return preferences.getString(mKey, mDefaultValue);
	}
}