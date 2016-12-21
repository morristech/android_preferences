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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Manager that may be used to simplify management (storing + obtaining) of values presented within
 * a {@link SharedPreferences} of an Android application.
 *
 * <h3>Primitive types</h3>
 * The following methods can be used to store primitive types within shared preferences:
 * <ul>
 * <li>{@link #putInt(String, int)} - {@link #obtainInt(String, int)}</li>
 * <li>{@link #putFloat(String, float)} - {@link #obtainInt(String, int)}</li>
 * <li>{@link #putLong(String, long)} - {@link #obtainLong(String, long)}</li>
 * <li>{@link #putBoolean(String, boolean)} - {@link #obtainBoolean(String, boolean)}</li>
 * <li>{@link #putString(String, String)} - {@link #obtainString(String, String)}</li>
 * </ul>
 *
 * <h3>Arrays + Lists</h3>
 * See {@code ArrayPreference} and {@code ListPreference}.
 *
 * <h3>Shared preferences</h3>
 * The PreferencesManager is closely related with {@link SharedPreference}. You can use any implementation
 * of SharedPreference class to represent any of your shared preference values. The following methods
 * can be used to store/obtain actual values of these preferences:
 * <ul>
 * <li>{@link #putPreference(SharedPreference, Object)} - {@link #obtainPreference(SharedPreference)}</li>
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
 *          return obtainBoolean(mPrefsName + KEY_APP_FIRST_LAUNCH, true);
 *      }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 */
public abstract class PreferencesManager {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "PreferencesManager";

	/**
	 * Defines an annotation for determining set of allowed flags for {@link #setMode(int)} method.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({MODE_PRIVATE, MODE_MULTI_PROCESS, MODE_ENABLE_WRITE_AHEAD_LOGGING, MODE_APPEND})
	public @interface Mode {
	}

	/**
	 * Wrapped mode: {@link Context#MODE_PRIVATE}
	 */
	public static final int MODE_PRIVATE = Context.MODE_PRIVATE;

	/**
	 * Wrapped mode: {@link Context#MODE_MULTI_PROCESS}
	 */
	public static final int MODE_MULTI_PROCESS = 0x0004;

	/**
	 * Wrapped mode: {@link Context#MODE_ENABLE_WRITE_AHEAD_LOGGING}
	 */
	public static final int MODE_ENABLE_WRITE_AHEAD_LOGGING = 0x0008;

	/**
	 * Wrapped mode: {@link Context#MODE_APPEND}
	 */
	public static final int MODE_APPEND = Context.MODE_APPEND;

	/**
	 * Flag for generic preference type.
	 */
	private static final int GENERIC = 0x00;

	/**
	 * Flag for integer preference type.
	 */
	private static final int INTEGER = 0x01;

	/**
	 * Flag for float preference type.
	 */
	private static final int FLOAT = 0x02;

	/**
	 * Flag for long preference type.
	 */
	private static final int LONG = 0x03;

	/**
	 * Flag for boolean preference type.
	 */
	private static final int BOOLEAN = 0x04;

	/**
	 * Flag for string preference type.
	 */
	private static final int STRING = 0x05;

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Application context used to access preferences.
	 */
	private Context mContext;

	/**
	 * Name of the current context's shared preferences into which will be data saved.
	 * Can be set during runtime or can depends on the type of the saving data.
	 */
	private String mSharedPreferencesName = "";

	/**
	 * Mode for the current context's shared preferences.
	 */
	private int mMode = MODE_PRIVATE;

	/**
	 * Flag indicating whether caching of the actual values of each shared preference is enabled.
	 * This means that, if shared preference holds actual value which is same as in shared preferences,
	 * parsing of that value will be not performed, instead actual value will be obtained from that
	 * preference object.
	 */
	private boolean mCachingEnabled = false;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #PreferencesManager(Context, String)} with default name for
	 * preferences (like the one provided by Android framework).
	 */
	protected PreferencesManager(@NonNull Context context) {
		this(context, defaultSharedPreferencesName(context));
	}

	/**
	 * Same as {@link #PreferencesManager(Context, String, int)} with {@link Context#MODE_PRIVATE}
	 * creation mode.
	 */
	protected PreferencesManager(@NonNull Context context, @NonNull String preferencesName) {
		this(context, preferencesName, MODE_PRIVATE);
	}

	/**
	 * Creates a new instance of PreferencesManager with the given shared preferences name and
	 * preferences file creation mode.
	 *
	 * @param context         Valid context to access shared preferences.
	 * @param preferencesName Name for shared preferences file.
	 * @param mode            Shared preferences will be created in this mode.
	 */
	protected PreferencesManager(@NonNull Context context, @NonNull String preferencesName, @Mode int mode) {
		this.mContext = context.getApplicationContext();
		setSharedPreferencesName(preferencesName);
		setMode(mode);
	}

	/**
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
	 * Same as {@link SharedPreferences#registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)}.
	 *
	 * @param listener Listener callback to register up on the current shared preferences.
	 * @see #getSharedPreferences()
	 * @see #unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	public void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		obtainPreferences(mContext).registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Same as {@link SharedPreferences#unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)}.
	 *
	 * @param listener Listener callback to un-register up on the current shared preferences.
	 * @see #getSharedPreferences()
	 * @see #registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	public void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		obtainPreferences(mContext).unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Returns application context.
	 *
	 * @return Application context.
	 */
	@NonNull
	public final Context getApplicationContext() {
		return mContext;
	}

	/**
	 * Same as {@link #put(String, Object)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final <T> boolean put(@StringRes int keyResId, @NonNull T value) {
		return put(getKey(keyResId), value);
	}

	/**
	 * Saves the given {@code value} into shared preferences.
	 * <p>
	 * <b>Note, that this method can be used to store values of primitive types only.</b>
	 *
	 * @param key   The key under which will be the specified value mapped in the shared preferences.
	 * @param value Value to save.
	 * @return {@code True} if saving succeeded, {@code false} otherwise.
	 */
	public final <T> boolean put(@NonNull String key, @NonNull T value) {
		return this.putElement(key, value, GENERIC, mContext);
	}

	/**
	 * Same as {@link #obtain(String, Object)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final <T> T obtain(@StringRes int keyResId, @NonNull T defValue) {
		return obtain(getKey(keyResId), defValue);
	}

	/**
	 * Returns the requested value mapped in the shared preferences under the given key.
	 * <p>
	 * <b>Note, that this method can be used to obtain values of primitive types only.</b>
	 *
	 * @param key      The key under which is the requested value mapped in the shared preferences.
	 * @param defValue Default value to return if there is no mapping for the specified <var>key</var>
	 *                 yet.
	 * @return Requested value obtained from the shared preferences or <var>defValue</var> if there
	 * is no mapping for the given key.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T obtain(@NonNull String key, @NonNull T defValue) {
		return (T) this.obtainElement(key, defValue, GENERIC, mContext);
	}

	/**
	 * Same as {@link #putInt(String, int)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean putInt(@StringRes int keyResId, int value) {
		return putInt(getKey(keyResId), value);
	}

	/**
	 * Saves the given {@code int} value into shared preferences.
	 *
	 * @param key   The key under which will be the specified value mapped in the shared preferences.
	 * @param value Value to save.
	 * @return {@code True} if saving succeeded, {@code false} otherwise.
	 * @see #putInt(int, int)
	 */
	public final boolean putInt(@NonNull String key, int value) {
		return this.putElement(key, value, INTEGER, mContext);
	}

	/**
	 * Sane as {@link #obtainInt(String, int)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final int obtainInt(@StringRes int keyResId, int defValue) {
		return obtainInt(getKey(keyResId), defValue);
	}

	/**
	 * Returns the {@code int} value mapped in the shared preferences under the given key.
	 *
	 * @param key      The key under which is the requested value mapped in the shared preferences.
	 * @param defValue Default value to return if there is no mapping for the specified <var>key</var> yet.
	 * @return {@code int} value obtained from the shared preferences or <var>defValue</var> if
	 * there is no mapping for the given key.
	 * @see #obtainInt(int, int)
	 */
	public final int obtainInt(@NonNull String key, int defValue) {
		return (int) this.obtainElement(key, defValue, INTEGER, mContext);
	}

	/**
	 * Same as {@link #putFloat(String, float)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean putFloat(@StringRes int keyResId, float value) {
		return putFloat(getKey(keyResId), value);
	}

	/**
	 * Saves the given {@code float} value into shared preferences.
	 *
	 * @param key   The key under which will be the specified value mapped in the shared preferences.
	 * @param value Value to save.
	 * @return {@code True} if saving succeeded, {@code false} otherwise.
	 * @see #putFloat(int, float)
	 */
	public final boolean putFloat(@NonNull String key, float value) {
		return this.putElement(key, value, FLOAT, mContext);
	}

	/**
	 * Same as {@link #obtainFloat(String, float)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final float obtainFloat(@StringRes int keyResId, float defValue) {
		return obtainFloat(getKey(keyResId), defValue);
	}

	/**
	 * Returns the {@code float} value mapped in the shared preferences under the given key.
	 *
	 * @param key      The key under which is the requested value mapped in the shared preferences.
	 * @param defValue Default value to return if there is no mapping for the specified <var>key</var> yet.
	 * @return {@code float} value obtained from the shared preferences or
	 * <var>defValue</var> if there is no mapping for the specified key.
	 * @see #obtainFloat(int, float)
	 */
	public final float obtainFloat(@NonNull String key, float defValue) {
		return (float) this.obtainElement(key, defValue, FLOAT, mContext);
	}

	/**
	 * Same as {@link #putLong(String, long)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean putLong(@StringRes int keyResId, long value) {
		return putLong(getKey(keyResId), value);
	}

	/**
	 * Saves the given {@code long} value into shared preferences.
	 *
	 * @param key   The key under which will be the specified value mapped in the shared preferences.
	 * @param value Value to save.
	 * @return {@code True} if saving succeeded, {@code false} otherwise.
	 * @see #putLong(int, long)
	 */
	public final boolean putLong(@NonNull String key, long value) {
		return this.putElement(key, value, LONG, mContext);
	}

	/**
	 * Same as {@link #obtainLong(String, long)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final long obtainLong(@StringRes int keyResId, long defValue) {
		return obtainLong(getKey(keyResId), defValue);
	}

	/**
	 * Returns the {@code long} value mapped in the shared preferences under the given key.
	 *
	 * @param key      The key under which is the requested value mapped in the shared preferences.
	 * @param defValue Default value to return if there is no mapping for the specified <var>key</var> yet.
	 * @return {@code long} value obtained from the shared preferences or
	 * <var>defValue</var> if there is no mapping for the specified key.
	 * @see #obtainLong(int, long)
	 */
	public final long obtainLong(@NonNull String key, long defValue) {
		return (long) this.obtainElement(key, defValue, LONG, mContext);
	}

	/**
	 * Same as {@link #putBoolean(String, boolean)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean putBoolean(@StringRes int keyResId, boolean value) {
		return putBoolean(getKey(keyResId), value);
	}

	/**
	 * Saves the given {@code boolean} value into shared preferences.
	 *
	 * @param key   The key under which will be the specified value mapped in the shared preferences.
	 * @param value Value to save.
	 * @return {@code True} if saving succeeded, {@code false} otherwise.
	 * @see #putBoolean(int, boolean)
	 */
	public final boolean putBoolean(@NonNull String key, boolean value) {
		return this.putElement(key, value, BOOLEAN, mContext);
	}

	/**
	 * Same as {@link #obtainBoolean(String, boolean)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean obtainBoolean(@StringRes int keyResId, boolean defValue) {
		return obtainBoolean(getKey(keyResId), defValue);
	}

	/**
	 * Returns the {@code boolean} value mapped in the shared preferences under the given key.
	 *
	 * @param key      The key under which is the requested value mapped in the shared preferences.
	 * @param defValue Default value to return if there is no mapping for the specified <var>key</var> yet.
	 * @return {@code boolean} value obtained from the shared preferences
	 * or <var>defValue</var> if there is no mapping for the specified key.
	 * @see #obtainBoolean(int, boolean)
	 */
	public final boolean obtainBoolean(@NonNull String key, boolean defValue) {
		return (boolean) this.obtainElement(key, defValue, BOOLEAN, mContext);
	}

	/**
	 * Same as {@link #putString(String, String)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean putString(@StringRes int keyResId, @Nullable String value) {
		return putString(getKey(keyResId), value);
	}

	/**
	 * Saves the given {@code String} value into shared preferences.
	 *
	 * @param key   The key under which will be the specified value mapped in the shared preferences.
	 * @param value Value to save.
	 * @return {@code True} if saving succeeded, {@code false} otherwise.
	 * @see #putString(int, String)
	 */
	public final boolean putString(@NonNull String key, @Nullable String value) {
		return this.putElement(key, value, STRING, mContext);
	}

	/**
	 * Same as {@link #obtainString(String, String)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final String obtainString(@StringRes int keyResId, @Nullable String defValue) {
		return obtainString(getKey(keyResId), defValue);
	}

	/**
	 * Returns the {@code String} value mapped in the shared preferences under the given key.
	 *
	 * @param key      The key under which is the requested value mapped in the shared preferences.
	 * @param defValue Default value to return if there is no mapping for the specified <var>key</var> yet.
	 * @return {@code String} value obtained from the shared preferences or
	 * <var>defValue</var> if there is no mapping for the specified key.
	 * @see #obtainString(int, String)
	 */
	public final String obtainString(@NonNull String key, @Nullable String defValue) {
		return (String) this.obtainElement(key, defValue, STRING, mContext);
	}

	/**
	 * Same as {@link #contains(String)} for key resource id.
	 *
	 * @param keyResId Resource id of the desired preference key.
	 */
	public final boolean contains(@StringRes int keyResId) {
		return contains(getKey(keyResId));
	}

	/**
	 * Checks whether there is contained some value for the specified <var>key</var> in the share
	 * preferences.
	 *
	 * @param key Key of the desired preference of which value to check.
	 * @return {@code True} if some value for the requested key is stored, {@code false} otherwise.
	 * @see #contains(int)
	 */
	public final boolean contains(@NonNull String key) {
		return obtainPreferences(mContext).contains(key);
	}

	/**
	 * Same as {@link #remove(String)} for resource id.
	 */
	public final boolean remove(@StringRes int resId) {
		return remove(getKey(resId));
	}

	/**
	 * Removes current value of a preference with the specified <var>key</var> from the shared preferences.
	 *
	 * @param key Key of the desired preference of which value (entry) should be removed.
	 * @return {@code True} if current value of shared preference has been removed, {@code false} if
	 * some error occurred.
	 */
	public final boolean remove(@NonNull String key) {
		final SharedPreferences prefs = obtainPreferences(mContext);
		final SharedPreferences.Editor editor = prefs.edit();
		return editor.remove(key).commit();
	}

	/**
	 * Saves the given <var>value</var> using the specified <var>preference</var> into shared preferences.
	 *
	 * @param preference Preference of which value should be saved.
	 * @param value      The value of preference to save into shared preferences.
	 * @return {@code True} if save operation succeeded, {@code false} otherwise.
	 */
	public final <Type> boolean putPreference(@NonNull SharedPreference<Type> preference, @Nullable Type value) {
		preference.setUpKey(mContext.getResources()).updateValue(value);
		final boolean succeed = preference.putIntoPreferences(obtainPreferences(mContext));
		if (!mCachingEnabled) {
			preference.clear();
		}
		return succeed;
	}

	/**
	 * Obtains the value of the given <var>preference</var> from shared preferences.
	 *
	 * @param preference Preference of which value should be obtained.
	 * @return Value of the given shared preference.
	 */
	public final <Type> Type obtainPreference(@NonNull SharedPreference<Type> preference) {
		preference.setUpKey(mContext.getResources());
		final Type value = preference.obtainFromPreferences(obtainPreferences(mContext));
		if (!mCachingEnabled) {
			preference.clear();
		}
		return value;
	}

	/**
	 * Checks whether value of the given <var>preference</var> is contained within the shared preferences.
	 *
 	 * @param preference The desired preference of which value to check.
	 * @return {@code True} if some value for the specified preference is stored, {@code false} otherwise.
	 */
	public final boolean containsPreference(@NonNull SharedPreference preference) {
		preference.setUpKey(mContext.getResources());
		return contains(preference.getKey());
	}

	/**
	 * Removes current value of the given <var>preference</var> from the shared preferences.
	 *
	 * @param preference The desired preference of which value (entry) should be removed.
	 * @return {@code True} if current value of shared preference has been removed, {@code false} if
	 * some error occurred.
	 */
	public final boolean removePreference(@NonNull SharedPreference preference) {
		preference.setUpKey(mContext.getResources());
		return remove(preference.getKey());
	}

	/**
	 * Returns the instance of SharedPreferences depends on the current file creation mode and
	 * preferences name. If there is not currently provided name for shared preferences, the default
	 * one will be used.
	 *
	 * @return Instance of shared preferences.
	 * @see #setMode(int)
	 * @see #setSharedPreferencesName(String)
	 */
	@NonNull
	public SharedPreferences getSharedPreferences() {
		return obtainPreferences(mContext);
	}

	/**
	 * Returns the actual file creation mode for shared preferences.
	 *
	 * @return The shared preferences file creation mode. {@link #MODE_PRIVATE} by default.
	 * @see Context
	 */
	@Mode
	protected final int getMode() {
		return mMode;
	}

	/**
	 * Sets the actual file creation mode for shared preferences. This mode is used whenever the
	 * instance of shared preferences is obtained to put/obtain data to/from them.
	 *
	 * @param mode Preferences creation mode.
	 */
	protected final void setMode(@Mode int mode) {
		// Check valid mode.
		switch (mode) {
			case MODE_APPEND:
			case MODE_ENABLE_WRITE_AHEAD_LOGGING:
			case MODE_MULTI_PROCESS:
			case MODE_PRIVATE:
				this.mMode = mode;
				break;
			default:
				Log.e(TAG, "Invalid file creation mode(" + mode + ") for shared preferences.");
		}
	}

	/**
	 * Sets the actual name for shared preferences. This name is used whenever the instance of shared
	 * preferences is needed to put/obtain data to/from it.
	 * <p>
	 * <b>Note</b>, that you can set this to {@code null} to obtain always default shared preferences.
	 *
	 * @param name The desired name for shared preferences.
	 */
	protected final void setSharedPreferencesName(@Nullable String name) {
		this.mSharedPreferencesName = name;
	}

	/**
	 * Returns the actual name for shared preferences.
	 *
	 * @return Name used to obtain shared preferences.
	 */
	@Nullable
	protected final String getSharedPreferencesName() {
		return mSharedPreferencesName;
	}

	/**
	 * Enables/disables the caching {@link universum.studios.android.preference.SharedPreference}'s values.
	 *
	 * @param enabled {@code True} to enable caching, {@code false} to disable.
	 * @see #isCachingEnabled()
	 */
	protected final void setCachingEnabled(boolean enabled) {
		this.mCachingEnabled = enabled;
	}

	/**
	 * Returns flag indicating whether the caching of {@link universum.studios.android.preference.SharedPreference}'s
	 * values is enabled or not.
	 *
	 * @return {@code True} if caching is enabled, {@code false} otherwise.
	 * @see #setCachingEnabled(boolean)
	 */
	protected final boolean isCachingEnabled() {
		return mCachingEnabled;
	}

	/**
	 * Returns a string key for the specified <var>resId</var>.
	 *
	 * @param resId Resource id of the desired preference key.
	 * @return String key obtained from the current context.
	 */
	@NonNull
	protected final String getKey(@StringRes int resId) {
		return mContext.getString(resId);
	}

	/**
	 * Returns the instance of shared preferences for the actual preferences name and mode. If there
	 * isn't currently provided name for shared preferences, package name of the given context will
	 * be used as name for shared preferences.
	 *
	 * @param context Actual context.
	 * @return Shared preferences instance.
	 */
	private SharedPreferences obtainPreferences(Context context) {
		if (!TextUtils.isEmpty(mSharedPreferencesName)) {
			return context.getSharedPreferences(mSharedPreferencesName, mMode);
		}
		// Return default shared preferences for the given context.
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Saves the given value into shared preferences which will be mapped under the the given key.
	 *
	 * @param key     The preference key.
	 * @param value   The preference value to save.
	 * @param type    Preference type by which is the given value represented.
	 * @param context Valid context to obtain shared preferences.
	 * @return {@code True} if operation succeeded, {@code false} otherwise.
	 */
	private boolean putElement(String key, Object value, int type, Context context) {
		if (TextUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Empty preference keys are not allowed!");
		}
		// Put element value into shared preferences.
		final SharedPreferences prefs = obtainPreferences(context);
		final SharedPreferences.Editor editor = prefs.edit();
		switch (type) {
			case GENERIC:
				if (value instanceof Integer) {
					editor.putInt(key, (Integer) value);
				} else if (value instanceof Float) {
					editor.putFloat(key, (Float) value);
				} else if (value instanceof Long) {
					editor.putLong(key, (Long) value);
				} else if (value instanceof Boolean) {
					editor.putBoolean(key, (Boolean) value);
				} else if (value instanceof String) {
					editor.putString(key, (String) value);
				} else {
					final String valueType = value.getClass().getSimpleName();
					throw new IllegalArgumentException(
							"Failed to save value of(" + valueType + ") for preference with key(" + key + "). " +
									"Value is not of supported generic (primitive) type."
					);
				}
				break;
			case BOOLEAN:
				editor.putBoolean(key, (Boolean) value);
				break;
			case INTEGER:
				editor.putInt(key, (Integer) value);
				break;
			case FLOAT:
				editor.putFloat(key, (Float) value);
				break;
			case LONG:
				editor.putLong(key, (Long) value);
				break;
			case STRING:
				editor.putString(key, (String) value);
				break;
		}
		return editor.commit();
	}

	/**
	 * Obtains the value that is mapped within shared preferences under the given key.
	 *
	 * @param key      The preference key.
	 * @param defValue Value to obtain if there is no mapping for the specified key.
	 * @param type     Preference type by which is the obtaining value represented.
	 * @param context  Valid context to obtain shared preferences.
	 * @return Value of the element or {@code null} if operation failed (invalid key or context).
	 */
	private Object obtainElement(String key, Object defValue, int type, Context context) {
		if (TextUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Empty preference keys are not allowed!");
		}

		Object value = null;

		// Obtain element value from preferences.
		final SharedPreferences prefs = obtainPreferences(context);
		switch (type) {
			case GENERIC:
				if (defValue != null) {
					if (defValue instanceof Integer) {
						value = prefs.getInt(key, (Integer) defValue);
					} else if (defValue instanceof Float) {
						value = prefs.getFloat(key, (Float) defValue);
					} else if (defValue instanceof Long) {
						value = prefs.getLong(key, (Long) defValue);
					} else if (defValue instanceof Boolean) {
						value = prefs.getBoolean(key, (Boolean) defValue);
					} else if (defValue instanceof String) {
						value = prefs.getString(key, (String) defValue);
					} else {
						throw new IllegalArgumentException(
								"Failed to obtain value for preference with key(" + key + "). " +
										"Value is not of supported generic (primitive) type."
						);
					}
				}
				break;
			case BOOLEAN:
				value = prefs.getBoolean(key, (Boolean) defValue);
				break;
			case INTEGER:
				value = prefs.getInt(key, (Integer) defValue);
				break;
			case FLOAT:
				value = prefs.getFloat(key, (Float) defValue);
				break;
			case LONG:
				value = prefs.getLong(key, (Long) defValue);
				break;
			case STRING:
				value = prefs.getString(key, (String) defValue);
				break;
		}
		return value;
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
