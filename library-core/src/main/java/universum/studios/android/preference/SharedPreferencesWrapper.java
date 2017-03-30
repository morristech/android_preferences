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
 * A {@link SharedPreferences} implementation which may be used to wrap an instance of {@link SharedPreferences}.
 * This wrapper class simply delegates all its calls to the wrapped preferences instance.
 *
 * @author Martin Albedinsky
 */
public class SharedPreferencesWrapper implements SharedPreferences {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SharedPreferencesWrapper";

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
	 * Wrapped instance of SharedPreferences to which is this wrapper delegating all its calls.
	 */
	@NonNull
	protected final SharedPreferences mPreferences;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SharedPreferencesWrapper in order to wrap the given <var>preferences</var>
	 * instance.
	 *
	 * @param preferences The shared preferences to be wrapped.
	 */
	public SharedPreferencesWrapper(@NonNull SharedPreferences preferences) {
		this.mPreferences = preferences;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the wrapped SharedPreferences instance.
	 *
	 * @return Instance of SharedPreferences that are wrapped by this preferences wrapper.
	 */
	@NonNull
	public final SharedPreferences getWrappedPreferences() {
		return mPreferences;
	}

	/**
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(@NonNull OnSharedPreferenceChangeListener listener) {
		mPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(@NonNull OnSharedPreferenceChangeListener listener) {
		mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public Map<String, ?> getAll() {
		return mPreferences.getAll();
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull String key) {
		return mPreferences.contains(key);
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
	public int getInt(@NonNull String key, int defValue) {
		return mPreferences.getInt(key, defValue);
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
	public float getFloat(@NonNull String key, float defValue) {
		return mPreferences.getFloat(key, defValue);
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	/**
	 */
	@NonNull
	@Override
	public Editor edit() {
		return mPreferences.edit();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}