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
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Manager that may be used to simplify management (storing + obtaining) of values presented within
 * a {@link SharedPreferences} of an Android application.
 *
 * <h3>Primitive types</h3>
 * The following methods can be used to store primitive types within shared preferences:
 * <ul>
 * <li>{@link #putInt(String, int)} - {@link #getInt(String, int)}</li>
 * <li>{@link #putFloat(String, float)} - {@link #getInt(String, int)}</li>
 * <li>{@link #putLong(String, long)} - {@link #getLong(String, long)}</li>
 * <li>{@link #putBoolean(String, boolean)} - {@link #getBoolean(String, boolean)}</li>
 * <li>{@link #putString(String, String)} - {@link #getString(String, String)}</li>
 * </ul>
 *
 * <h3>Arrays + Lists</h3>
 * See {@code ArrayPreference} and {@code ListPreference}.
 *
 * <h3>Shared preferences</h3>
 * The PreferencesManager is closely related with {@link SharedPreference}. You can use any implementation
 * of SharedPreference class to represent any of your shared preference values. The following methods
 * can be used to store/retrieve actual values of these preferences:
 * <ul>
 * <li>{@link #putPreference(SharedPreference, Object)} - {@link #getPreference(SharedPreference)}</li>
 * </ul>
 *
 * <h3>Sample implementation</h3>
 * <pre>
 * public class AppPreferences extends PreferencesManager {
 *
 *      // Key for the first app launch preference.
 *      private static final String KEY_APP_FIRST_LAUNCH = ".AppFirstLaunch";
 *
 *      // Name of the shared preferences.
 *      private final String mPrefsName;
 *
 *      // Creates a new instance of AppPreferences manager.
 *      public AppPreferences(Context context) {
 *          super(context);
 *          this.mPrefsName = getSharedPreferencesName();
 *      }
 *
 *      // Stores a boolean flag into shared preferences indicating whether this is a first launch
 *      // of this app or not.
 *      public void saveIsAppFirstLaunch(boolean first) {
 *          putBoolean(mPrefsName + KEY_APP_FIRST_LAUNCH, first);
 *      }
 *
 *      // Returns a boolean flag from shared preferences indicating whether this is a first launch
 *      // of this app or not.
 *      public boolean isAppFirstLaunch() {
 *          return getBoolean(mPrefsName + KEY_APP_FIRST_LAUNCH, true);
 *      }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 */
public class PreferencesManager extends SimpleSharedPreferencesFacade {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "PreferencesManager";

	/**
	 * <b>This constant has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_PRIVATE}
	 *
	 * @deprecated Use {@link SharedPreferencesPolicy#FILE_MODE_PRIVATE} instead.
	 */
	@Deprecated
	public static final int MODE_PRIVATE = Context.MODE_PRIVATE;

	/**
	 * <b>This constant has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_MULTI_PROCESS}
	 *
	 * @deprecated Deprecated due to deprecation in the framework. See {@link Context#MODE_MULTI_PROCESS}.
	 */
	@Deprecated
	public static final int MODE_MULTI_PROCESS = 0x0004;

	/**
	 * <b>This constant has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_ENABLE_WRITE_AHEAD_LOGGING}
	 *
	 * @deprecated This flag is appropriate only for database files.
	 */
	@Deprecated
	public static final int MODE_ENABLE_WRITE_AHEAD_LOGGING = 0x0008;

	/**
	 * <b>This constant has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_APPEND}
	 *
	 * @deprecated Use {@link SharedPreferencesPolicy#FILE_MODE_APPEND} instead.
	 */
	@Deprecated
	public static final int MODE_APPEND = Context.MODE_APPEND;

	/**
	 * <b>This annotation has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Defines an annotation for determining set of allowed flags for {@link #setMode(int)} method.
	 *
	 * @deprecated Use {@link SharedPreferencesPolicy.FileMode @SharedPreferencesPolicy.FileMode} instead.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({MODE_PRIVATE, MODE_MULTI_PROCESS, MODE_ENABLE_WRITE_AHEAD_LOGGING, MODE_APPEND})
	@Deprecated
	public @interface Mode {
	}

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
	 * Context that has been specified during initialization of this manager instance.
	 */
	@NonNull
	protected final Context mContext;

	/**
	 * Flag indicating whether caching of the actual values of each shared preference is enabled.
	 * This means that, if shared preference holds actual value which is same as in shared preferences,
	 * parsing of that value will be not performed, instead actual value will be obtained from that
	 * preference object.
	 */
	private boolean mCachingEnabled = false;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #PreferencesManager(Context, String)} with default name for
	 * preferences (like the one provided by Android framework).
	 */
	public PreferencesManager(@NonNull Context context) {
		this(context, defaultSharedPreferencesName(context));
	}

	/**
	 * Same as {@link #PreferencesManager(Context, String, int)} with {@link Context#MODE_PRIVATE}
	 * creation mode.
	 */
	public PreferencesManager(@NonNull Context context, @NonNull String preferencesName) {
		this(context, preferencesName, SharedPreferencesPolicy.FILE_MODE_PRIVATE);
	}

	/**
	 * Creates a new instance of PreferencesManager with the given shared preferences name and
	 * preferences file creation mode.
	 *
	 * @param context             Valid context to access shared preferences.
	 * @param preferencesName     Name for shared preferences file.
	 * @param preferencesFileMode Shared preferences will be created in this mode.
	 */
	public PreferencesManager(@NonNull Context context, @NonNull String preferencesName, @SharedPreferencesPolicy.FileMode int preferencesFileMode) {
		this(context, context.getSharedPreferences(preferencesName, preferencesFileMode), preferencesName);
	}

	/**
	 * todo:
	 *
	 * @param context
	 * @param preferences
	 * @param preferencesName
	 */
	public PreferencesManager(@NonNull Context context, @NonNull SharedPreferences preferences, @NonNull String preferencesName) {
		super(preferences, preferencesName);
		this.mContext = context;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a default name for shared preferences (like the one created by {@link PreferenceManager}).
	 *
	 * @param context Context for which the name should be created.
	 * @return Default name for shared preferences.
	 */
	@NonNull
	public static String defaultSharedPreferencesName(@NonNull Context context) {
		return context.getPackageName() + "_preferences";
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	@NonNull
	public final SharedPreferences getPreferences() {
		return mPreferences;
	}

	/**
	 * Enables/disables the caching {@link universum.studios.android.preference.SharedPreference}'s values.
	 *
	 * @param enabled {@code True} to enable caching, {@code false} to disable.
	 * @see #isCachingEnabled()
	 */
	public final void setCachingEnabled(boolean enabled) {
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
	 * Same as {@link SharedPreferences#registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)}.
	 *
	 * @param listener Listener callback to register up on the current shared preferences.
	 * @see #getSharedPreferences()
	 * @see #unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	public void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		mPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Same as {@link SharedPreferences#unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)}.
	 *
	 * @param listener Listener callback to un-register up on the current shared preferences.
	 * @see #getSharedPreferences()
	 * @see #registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	public void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Returns a string key for the specified <var>resId</var>.
	 *
	 * @param resId Resource id of the desired preference key.
	 * @return String key obtained from the current context.
	 */
	@NonNull
	protected final String key(@StringRes int resId) {
		return mContext.getString(resId);
	}

	/**
	 * Saves the given <var>value</var> using the specified <var>preference</var> into shared preferences.
	 *
	 * @param preference Preference of which value should be saved.
	 * @param value      The value of preference to save into shared preferences.
	 * @return {@code True} if save operation succeeded, {@code false} otherwise.
	 * @see #getPreference(SharedPreference)
	 * @see #containsPreference(SharedPreference)
	 * @see #removePreference(SharedPreference)
	 */
	public final <Type> boolean putPreference(@NonNull SharedPreference<Type> preference, @Nullable Type value) {
		preference.updateValue(value);
		final boolean result = preference.putIntoPreferences(mPreferences);
		if (!mCachingEnabled) {
			preference.invalidate();
		}
		return result;
	}

	/**
	 * Obtains the value of the given <var>preference</var> from shared preferences.
	 *
	 * @param preference Preference of which value should be obtained.
	 * @return Value of the given shared preference.
	 * @see #putPreference(SharedPreference, Object)
	 */
	public final <Type> Type getPreference(@NonNull SharedPreference<Type> preference) {
		final Type value = preference.getFromPreferences(mPreferences);
		if (!mCachingEnabled) {
			preference.invalidate();
		}
		return value;
	}

	/**
	 * Checks whether value of the given <var>preference</var> is contained within the shared preferences.
	 *
	 * @param preference The desired preference of which value to check.
	 * @return {@code True} if some value for the specified preference is stored, {@code false} otherwise.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #getPreference(SharedPreference)
	 * @see #removePreference(SharedPreference)
	 */
	public final boolean containsPreference(@NonNull SharedPreference preference) {
		return contains(preference.getKey());
	}

	/**
	 * Removes current value of the given <var>preference</var> from the shared preferences.
	 *
	 * @param preference The desired preference of which value (entry) should be removed.
	 * @return {@code True} if current value of shared preference has been removed, {@code false} if
	 * some error occurred.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #getPreference(SharedPreference)
	 * @see #containsPreference(SharedPreference)
	 */
	public final boolean removePreference(@NonNull SharedPreference preference) {
		return remove(preference.getKey());
	}

	// fixme: deprecated methods that should be removed in the next release

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Returns the actual file creation mode for shared preferences.
	 *
	 * @return The shared preferences file creation mode. {@link #MODE_PRIVATE} by default.
	 * @see Context
	 * @see #setMode(int)
	 * @deprecated The new implementation of preferences manager is created with already initialized
	 * instance of {@link SharedPreferences} and thus its file mode cannot be changed.
	 */
	@Deprecated
	protected final int getMode() {
		return SharedPreferencesPolicy.FILE_MODE_PRIVATE;
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Sets the actual file creation mode for shared preferences. This mode is used whenever the
	 * instance of shared preferences is obtained to put/retrieve data to/from them.
	 *
	 * @param mode Preferences creation mode.
	 * @see #getMode()
	 * @deprecated The new implementation of preferences manager does not support changing of preferences
	 * file mode after the manager has been initialized.
	 */
	@Deprecated
	protected final void setMode(@Mode int mode) {
		// Actual implementation of this manager does not support changing of preferences file mode.
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Sets the actual name for shared preferences. This name is used whenever the instance of shared
	 * preferences is needed to put/retrieve data to/from it.
	 * <p>
	 * <b>Note</b>, that you can set this to {@code null} to retrieve always default shared preferences.
	 *
	 * @param name The desired name for shared preferences.
	 * @see #getSharedPreferencesName()
	 * @see #getSharedPreferences()
	 * @deprecated The new implementation of preferences manager does not support changing of preferences
	 * name after the manager has been initialized.
	 */
	@Deprecated
	protected final void setSharedPreferencesName(@Nullable String name) {
		// Actual implementation of this manager does not support changing of preferences name.
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Returns the actual name for shared preferences.
	 *
	 * @return Name used to retrieve shared preferences.
	 * @see #setSharedPreferencesName(String)
	 * @deprecated Use {@link #getPreferencesName()} instead.
	 */
	@Nullable
	@Deprecated
	protected final String getSharedPreferencesName() {
		return getPreferencesName();
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Returns the instance of SharedPreferences depends on the current file creation mode and
	 * preferences name. If there is not currently provided name for shared preferences, the default
	 * one will be used.
	 *
	 * @return Instance of shared preferences.
	 * @see #setMode(int)
	 * @see #setSharedPreferencesName(String)
	 * @deprecated Use {@link #getPreferences()} instead.
	 */
	@NonNull
	@Deprecated
	public SharedPreferences getSharedPreferences() {
		return getPreferences();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
