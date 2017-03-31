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
 * A {@link SharedPreference} implementation that may be used to persist a {@link Integer} value via
 * {@link SharedPreferences}.
 *
 * @author Martin Albedinsky
 * @see BooleanPreference
 * @see StringPreference
 * @see LongPreference
 * @see FloatPreference
 * @see EnumPreference
 */
public final class IntegerPreference extends SharedPreference<Integer> {

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of IntegerPreference.
	 *
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public IntegerPreference(@NonNull final String key, @NonNull final Integer defValue) {
		super(key, defValue);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Creates a new instance of IntegerPreference.
	 *
	 * @see SharedPreference#SharedPreference(int, Object)
	 * @deprecated Use {@link #IntegerPreference(String, Integer)} instead.
	 */
	@Deprecated
	public IntegerPreference(@StringRes int keyResId, @NonNull Integer defValue) {
		super(keyResId, defValue);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull final SharedPreferences preferences) {
		return preferences.edit().putInt(mKey, mValue).commit();
	}

	/**
	 */
	@Nullable
	@Override
	protected Integer onGetFromPreferences(@NonNull final SharedPreferences preferences) {
		return preferences.getInt(mKey, mDefaultValue);
	}
}
