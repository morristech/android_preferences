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
 * A {@link SharedPreference} implementation that may be used to persist a {@link Boolean} value via
 * {@link SharedPreferences}.
 *
 * @author Martin Albedinsky
 * @see StringPreference
 * @see IntegerPreference
 * @see LongPreference
 * @see FloatPreference
 * @see EnumPreference
 */
public final class BooleanPreference extends SharedPreference<Boolean> {

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of BooleanPreference.
	 *
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public BooleanPreference(@NonNull String key, @NonNull Boolean defValue) {
		super(key, defValue);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Creates a new instance of BooleanPreference.
	 *
	 * @see SharedPreference#SharedPreference(int, Object)
	 * @deprecated Use {@link #BooleanPreference(String, Boolean)} instead.
	 */
	@Deprecated
	public BooleanPreference(@StringRes int keyResId, @NonNull Boolean defValue) {
		super(keyResId, defValue);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull SharedPreferences preferences) {
		return preferences.edit().putBoolean(mKey, mValue).commit();
	}

	/**
	 */
	@Nullable
	@Override
	protected Boolean onGetFromPreferences(@NonNull SharedPreferences preferences) {
		return preferences.getBoolean(mKey, mDefaultValue);
	}
}
