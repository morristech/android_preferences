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
package universum.studios.android.preference;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Simple implementation of {@link SharedPreferencesFacade} which supports simple <var>obtaining</var>
 * and <var>putting</var> of values stored in {@link SharedPreferences} with which is facade created
 * via {@link #SimpleSharedPreferencesFacade(SharedPreferences, String)} constructor.
 *
 * @author Martin Albedinsky
 */
public class SimpleSharedPreferencesFacade implements SharedPreferencesFacade {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SimpleSharedPreferencesFacade";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Instance of SharedPreferences that is used by this facade implementation to manage putting and
	 * obtaining of values into/from preferences in a simple way.
	 */
	@NonNull
	protected final SharedPreferences mPreferences;

	/**
	 * Name of the shared preferences wrapped by this facade.
	 */
	@NonNull
	protected final String mPreferencesName;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SimpleSharedPreferencesFacade that wraps the given <var>preferences</var>
	 * instance in order to support simple <var>putting</var> and <var>obtaining</var> of values
	 * stored in such preferences.
	 *
	 * @param preferences     The shared preferences for which to create new facade.
	 * @param preferencesName The name of the given shared preferences.
	 */
	public SimpleSharedPreferencesFacade(@NonNull SharedPreferences preferences, @NonNull String preferencesName) {
		this.mPreferences = preferences;
		this.mPreferencesName = preferencesName;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@NonNull
	@Override
	public final String getPreferencesName() {
		return mPreferencesName;
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull String key) {
		return mPreferences.contains(key);
	}

	/**
	 */
	@Override
	public boolean putString(@NonNull String key, @Nullable String value) {
		return mPreferences.edit().putString(key, value).commit();
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull String key, @Nullable String defValue) {
		return mPreferences.getString(key, defValue);
	}

	/**
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#HONEYCOMB HONEYCOMB} Android versions
	 * this method does nothing and always returns {@code false}.
	 */
	@Override
	public boolean putStringSet(@NonNull String key, @Nullable Set<String> values) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mPreferences.edit().putStringSet(key, values).commit();
	}

	/**
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#HONEYCOMB HONEYCOMB} Android versions
	 * this method does nothing and always returns {@code null}.
	 */
	@Nullable
	@Override
	public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defValues) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? mPreferences.getStringSet(key, defValues) : null;
	}

	/**
	 */
	@Override
	public boolean putInt(@NonNull String key, int value) {
		return mPreferences.edit().putInt(key, value).commit();
	}

	/**
	 */
	@Override
	public int getInt(@NonNull String key, int defValue) {
		return mPreferences.getInt(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putFloat(@NonNull String key, float value) {
		return mPreferences.edit().putFloat(key, value).commit();
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull String key, float defValue) {
		return mPreferences.getFloat(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putLong(@NonNull String key, long value) {
		return mPreferences.edit().putLong(key, value).commit();
	}

	/**
	 */
	@Override
	public long getLong(@NonNull String key, long defValue) {
		return mPreferences.getLong(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putBoolean(@NonNull String key, boolean value) {
		return mPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	/**
	 */
	@Override
	public boolean remove(@NonNull String key) {
		return mPreferences.edit().remove(key).commit();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}