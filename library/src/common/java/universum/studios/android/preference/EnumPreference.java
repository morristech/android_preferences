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
import android.text.TextUtils;

/**
 * A {@link SharedPreference} implementation that can be used to manage (store + obtain) an
 * {@link Enum} preference value within {@link SharedPreferences}.
 *
 * @param <E> A type of the enum implementation of which value will this preference manage.
 * @author Martin Albedinsky
 * @see BooleanPreference
 * @see IntegerPreference
 * @see LongPreference
 * @see StringPreference
 * @see FloatPreference
 */
public final class EnumPreference<E extends Enum> extends SharedPreference<E> {

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of EnumPreference.
	 *
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public EnumPreference(@NonNull String key, @Nullable E defValue) {
		super(key, defValue);
	}

	/**
	 * Creates a new instance of EnumPreference.
	 *
	 * @see SharedPreference#SharedPreference(int, Object)
	 */
	public EnumPreference(@StringRes int keyResId, @Nullable E defValue) {
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
		return preferences.edit().putString(mKey, mValue.name()).commit();
	}

	/**
	 */
	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	protected E onObtainFromPreferences(@NonNull SharedPreferences preferences) {
		final String enumName = preferences.getString(mKey, mDefaultValue != null ? mDefaultValue.name() : "");
		return !TextUtils.isEmpty(enumName) ? (E) E.valueOf(mDefaultValue.getClass(), enumName) : null;
	}
}
