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
 * A {@link SharedPreference} implementation that may be used to persist an {@link Enum} value via
 * {@link SharedPreferences}.
 *
 * @param <E> Type of the enum implementation of which value should be persisted.
 * @author Martin Albedinsky
 * @see BooleanPreference
 * @see IntegerPreference
 * @see LongPreference
 * @see StringPreference
 * @see FloatPreference
 */
public final class EnumPreference<E extends Enum> extends SharedPreference<E> {

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of EnumPreference with the specified <var>key</var> and <var>defValue</var>.
	 *
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public EnumPreference(@NonNull final String key, @Nullable final E defValue) {
		super(key, defValue);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Creates a new instance of EnumPreference.
	 *
	 * @see SharedPreference#SharedPreference(int, Object)
	 * @deprecated Use {@link #EnumPreference(String, Enum)} instead.
	 */
	@Deprecated
	public EnumPreference(@StringRes int keyResId, @Nullable E defValue) {
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
		return preferences.edit().putString(mKey, mValue.name()).commit();
	}

	/**
	 */
	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	protected E onGetFromPreferences(@NonNull final SharedPreferences preferences) {
		final String enumName = preferences.getString(mKey, mDefaultValue == null ? "" : mDefaultValue.name());
		return TextUtils.isEmpty(enumName) ? null : (E) E.valueOf(mDefaultValue.getClass(), enumName);
	}
}
