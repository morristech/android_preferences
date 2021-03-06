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

import java.util.Map;
import java.util.Set;

/**
 * Simple implementation of {@link SharedPreferencesFacade} which supports simple <var>obtaining</var>
 * and <var>putting</var> of values persisted in {@link SharedPreferences} with which is facade created
 * via {@link #SimpleSharedPreferencesFacade(SharedPreferences)} constructor.
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
	private final SharedPreferences mPreferences;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SimpleSharedPreferencesFacade for the specified <var>preferences</var>.
	 *
	 * @param preferences The instance of shared preferences to hide behind the new facade.
	 */
	public SimpleSharedPreferencesFacade(@NonNull final SharedPreferences preferences) {
		this.mPreferences = preferences;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the {@link SharedPreferences} that are hidden behind this facade.
	 *
	 * @return The associated preferences.
	 * @see #SimpleSharedPreferencesFacade(SharedPreferences)
	 */
	@NonNull
	public final SharedPreferences getPreferences() {
		return mPreferences;
	}

	/**
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(@NonNull final SharedPreferences.OnSharedPreferenceChangeListener listener) {
		mPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(@NonNull final SharedPreferences.OnSharedPreferenceChangeListener listener) {
		mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull final String key) {
		return mPreferences.contains(key);
	}

	/**
	 */
	@Override
	public boolean putString(@NonNull final String key, @Nullable final String value) {
		return mPreferences.edit().putString(key, value).commit();
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull final String key, @Nullable final String defValue) {
		return mPreferences.getString(key, defValue);
	}

	/**
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#HONEYCOMB HONEYCOMB} Android versions
	 * this method does nothing and always returns {@code false}.
	 */
	@Override
	public boolean putStringSet(@NonNull final String key, @Nullable final Set<String> values) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mPreferences.edit().putStringSet(key, values).commit();
	}

	/**
	 * <b>Note</b>, that for pre {@link Build.VERSION_CODES#HONEYCOMB HONEYCOMB} Android versions
	 * this method does nothing and always returns {@code null}.
	 */
	@Nullable
	@Override
	public Set<String> getStringSet(@NonNull final String key, @Nullable final Set<String> defValues) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? mPreferences.getStringSet(key, defValues) : null;
	}

	/**
	 */
	@Override
	public boolean putInt(@NonNull final String key, final int value) {
		return mPreferences.edit().putInt(key, value).commit();
	}

	/**
	 */
	@Override
	public int getInt(@NonNull final String key, final int defValue) {
		return mPreferences.getInt(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putFloat(@NonNull final String key, final float value) {
		return mPreferences.edit().putFloat(key, value).commit();
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull final String key, final float defValue) {
		return mPreferences.getFloat(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putLong(@NonNull final String key, final long value) {
		return mPreferences.edit().putLong(key, value).commit();
	}

	/**
	 */
	@Override
	public long getLong(@NonNull final String key, final long defValue) {
		return mPreferences.getLong(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putBoolean(@NonNull final String key, final boolean value) {
		return mPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull final String key, final boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	/**
	 */
	@Override
	public boolean remove(@NonNull final String key) {
		return mPreferences.edit().remove(key).commit();
	}

	/**
	 */
	@Override
	public int removeAll() {
		final Map<String, ?> values = mPreferences.getAll();
		int result = 0;
		if (!values.isEmpty()) {
			for (final Map.Entry<String, ?> entry : values.entrySet()) {
				if (mPreferences.edit().remove(entry.getKey()).commit()) result++;
			}
		}
		return result;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
