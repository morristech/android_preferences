/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License
 * you may retrieve at
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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.Set;

/**
 * Manager which implements {@link SharedPreferencesFacade} along with {@link SharedPreferencesProvider}
 * in order to provide a simple API for putting and obtaining of values persisted within {@link SharedPreferences}.
 *
 * <h3>Shared preferences</h3>
 * The PreferencesManager is closely associated with {@link SharedPreference}. Any implementation of
 * SharedPreference class may be used to represent a concrete preference value to be persisted in
 * shared preferences. Methods listed below may be used to put, get or remove a desired shared preference
 * into or from preferences managed by the preferences manager:
 * <ul>
 * <li>{@link #putPreference(SharedPreference, Object)}</li>
 * <li>{@link #getPreference(SharedPreference)}</li>
 * <li>{@link #containsPreference(SharedPreference)}</li>
 * <li>{@link #removePreference(SharedPreference)}</li>
 * </ul>
 *
 * <h3>Sample implementation</h3>
 * <pre>
 * public final class AppPreferences extends PreferencesManager {
 *
 *      private static final String KEY_PREFIX = BuildConfig.APPLICATION_ID = ".PREFERENCE.";
 *      private static final String KEY_APP_FIRST_LAUNCH = KEY_PREFIX + "AppFirstLaunch";
 *
 *      public AppPreferences(@NonNull Context context) {
 *          super(context);
 *      }
 *
 *      public void setAppFirstLaunch(boolean firstLaunch) {
 *          putBoolean(KEY_APP_FIRST_LAUNCH, firstLaunch);
 *      }
 *
 *      public boolean isAppFirstLaunch() {
 *          return getBoolean(KEY_APP_FIRST_LAUNCH, true);
 *      }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 */
public class PreferencesManager implements SharedPreferencesFacade, SharedPreferencesProvider {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "PreferencesManager";

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
	 * Context with which has been this manager created.
	 *
	 * @deprecated Use {@link #getContext()} instead.
	 */
	private final Context mContext;

	/**
	 * File name of shared preferences managed by this manager.
	 */
	private String mPreferencesName;

	/**
	 * File mode of shared preferences managed by this manager.
	 */
	@SharedPreferencesPolicy.Mode
	private int mPreferencesMode = SharedPreferencesPolicy.MODE_PRIVATE;

	/**
	 * Facade to which is this manager delegating all put/get/remove requests.
	 */
	private SimpleSharedPreferencesFacade mPreferencesFacade;

	/**
	 * Flag indicating whether caching of the actual values of each shared preference is enabled.
	 * This means that, if shared preference holds actual value which is same as in shared preferences,
	 * parsing of that value will be not performed, instead actual value will be obtained from that
	 * preference object.
	 */
	private boolean mCachingEnabled;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of PreferencesManager with the specified <var>context</var>.
	 * <p>
	 * Manager will be created with default name for shared preferences obtained via
	 * {@link SharedPreferencesPolicy#defaultPreferencesName(Context)} and mode set to
	 * {@link SharedPreferencesPolicy#MODE_PRIVATE}.
	 *
	 * @param context Context that is used to obtain instance of {@link SharedPreferences} for the
	 *                new manager via {@link Context#getSharedPreferences(String, int)} for the
	 *                {@code name} and {@code mode} specified via {@link #setSharedPreferencesName(String)}
	 *                and {@link #setSharedPreferencesMode(int)}.
	 */
	public PreferencesManager(@NonNull Context context) {
		this.mContext = context;
		this.mPreferencesName = SharedPreferencesPolicy.defaultPreferencesName(context);

	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the context with which has been this manager created.
	 *
	 * @return The associated context.
	 */
	@NonNull
	public final Context getContext() {
		return mContext;
	}

	/**
	 */
	@Override
	public void setSharedPreferencesName(@Nullable String name) {
		this.mPreferencesName = name == null ? SharedPreferencesPolicy.defaultPreferencesName(mContext) : name;
		this.mPreferencesFacade = null;
	}

	/**
	 */
	@NonNull
	@Override
	public final String getSharedPreferencesName() {
		return mPreferencesName;
	}

	/**
	 */
	@Override
	public void setSharedPreferencesMode(@SharedPreferencesPolicy.Mode int mode) {
		this.mPreferencesMode = mode;
		this.mPreferencesFacade = null;
	}

	/**
	 */
	@Override
	@SharedPreferencesPolicy.Mode
	public final int getSharedPreferencesMode() {
		return mPreferencesMode;
	}

	/**
	 */
	@NonNull
	@Override
	public final SharedPreferences getSharedPreferences() {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getPreferences();
	}

	/**
	 * Ensures that the {@link #mPreferencesFacade} for {@link SharedPreferences} managed by this
	 * manager is initialized for {@link #mPreferencesName} and {@link #mPreferencesMode}.
	 */
	private void ensurePreferencesFacade() {
		if (mPreferencesFacade == null) {
			this.mPreferencesFacade = new SimpleSharedPreferencesFacade(
					mContext.getSharedPreferences(
							mPreferencesName,
							mPreferencesMode
					)
			);
		}
	}

	/**
	 * Enables/disables the caching {@link universum.studios.android.preference.SharedPreference}'s values.
	 *
	 * @param enabled {@code True} to enable caching, {@code false} to disable.
	 * @see #isCachingEnabled()
	 */
	public final void setCachingEnabled(final boolean enabled) {
		this.mCachingEnabled = enabled;
	}

	/**
	 * Returns flag indicating whether the caching of {@link universum.studios.android.preference.SharedPreference}'s
	 * values is enabled or not.
	 *
	 * @return {@code True} if caching is enabled, {@code false} otherwise.
	 * @see #setCachingEnabled(boolean)
	 */
	public final boolean isCachingEnabled() {
		return mCachingEnabled;
	}

	/**
	 * Returns a string key for the specified <var>resId</var>.
	 *
	 * @param resId Resource id of the desired preference key.
	 * @return String key obtained from the context specified for this manager.
	 */
	@NonNull
	public final String key(@StringRes final int resId) {
		return mContext.getString(resId);
	}

	/**
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		this.ensurePreferencesFacade();
		mPreferencesFacade.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		this.ensurePreferencesFacade();
		mPreferencesFacade.unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull String key) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.contains(key);
	}

	/**
	 */
	@Override
	public boolean putString(@NonNull String key, @Nullable String value) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.putString(key, value);
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull String key, @Nullable String defValue) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getString(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putStringSet(@NonNull String key, @Nullable Set<String> values) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.putStringSet(key, values);
	}

	/**
	 */
	@Nullable
	@Override
	public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defValues) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getStringSet(key, defValues);
	}

	/**
	 */
	@Override
	public boolean putInt(@NonNull String key, int value) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.putInt(key, value);
	}

	/**
	 */
	@Override
	public int getInt(@NonNull String key, int defValue) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getInt(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putFloat(@NonNull String key, float value) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.putFloat(key, value);
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull String key, float defValue) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getFloat(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putLong(@NonNull String key, long value) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.putLong(key, value);
	}

	/**
	 */
	@Override
	public long getLong(@NonNull String key, long defValue) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getLong(key, defValue);
	}

	/**
	 */
	@Override
	public boolean putBoolean(@NonNull String key, boolean value) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.putBoolean(key, value);
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull String key, boolean defValue) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.getBoolean(key, defValue);
	}

	/**
	 */
	@Override
	public boolean remove(@NonNull String key) {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.remove(key);
	}

	/**
	 */
	@Override
	public int removeAll() {
		this.ensurePreferencesFacade();
		return mPreferencesFacade.removeAll();
	}

	/**
	 * Persists the given <var>value</var> for the specified <var>preference</var> into {@link SharedPreferences}
	 * that are managed by this manager.
	 *
	 * @param preference Preference for which to persist the value.
	 * @param value      The value to be persisted.
	 * @param <T>        Type of the value associated with the preference.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see #getPreference(SharedPreference)
	 * @see #containsPreference(SharedPreference)
	 * @see #removePreference(SharedPreference)
	 * @see SharedPreference#updateValue(Object)
	 * @see SharedPreference#putIntoPreferences(SharedPreferences)
	 */
	public final <T> boolean putPreference(@NonNull final SharedPreference<T> preference, @Nullable final T value) {
		this.ensurePreferencesFacade();
		final boolean result = preference.updateValue(value).putIntoPreferences(mPreferencesFacade.getPreferences());
		if (!mCachingEnabled) {
			preference.invalidate();
		}
		return result;
	}

	/**
	 * Obtains the value for the given <var>preference</var> from {@link SharedPreferences} that are
	 * managed by this manager.
	 *
	 * @param preference Preference for which to obtain its associated value.
	 * @param <T>        Type of the value associated with the preference.
	 * @return Value associated with the preference. May be {@code null} if there is no value persisted
	 * for the preference yet.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #contains(String)
	 * @see SharedPreference#getFromPreferences(SharedPreferences)
	 */
	@Nullable
	public final <T> T getPreference(@NonNull final SharedPreference<T> preference) {
		this.ensurePreferencesFacade();
		final T value = preference.getFromPreferences(mPreferencesFacade.getPreferences());
		if (!mCachingEnabled) {
			preference.invalidate();
		}
		return value;
	}

	/**
	 * Checks whether there is value associated with the specified <var>preference</var> contained
	 * within {@link SharedPreferences} that are managed by this manager.
	 *
	 * @param preference The desired preference of which value's presence to check.
	 * @return {@code True} if there is value contained for key of the specified preference,
	 * {@code false} otherwise.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #getPreference(SharedPreference)
	 * @see #removePreference(SharedPreference)
	 */
	public final boolean containsPreference(@NonNull final SharedPreference preference) {
		return contains(preference.getKey());
	}

	/**
	 * Removes a value for the specified <var>preference</var> from {@link SharedPreferences} that
	 * are managed by this manager.
	 *
	 * @param preference The preference for which to remove its associated value.
	 * @return {@code True} if removal has been successful, {@code false} otherwise.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #getPreference(SharedPreference)
	 * @see #containsPreference(SharedPreference)
	 */
	public final boolean removePreference(@NonNull final SharedPreference preference) {
		return remove(preference.getKey());
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
