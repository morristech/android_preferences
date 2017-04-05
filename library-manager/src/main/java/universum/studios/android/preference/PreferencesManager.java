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
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

/**
 * Manager which implements {@link SharedPreferencesFacade} along with {@link SharedPreferencesHost}
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
public class PreferencesManager implements SharedPreferencesFacade, SharedPreferencesHost {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "PreferencesManager";

	/**
	 * <b>This constant has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_PRIVATE}
	 *
	 * @deprecated Use {@link SharedPreferencesPolicy#MODE_PRIVATE} instead.
	 */
	@Deprecated
	public static final int MODE_PRIVATE = Context.MODE_PRIVATE;

	/**
	 * <b>This constant has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_MULTI_PROCESS}
	 *
	 * @deprecated Deprecated due to deprecation in the framework. See {@link Context#MODE_MULTI_PROCESS}.
	 */
	@Deprecated
	public static final int MODE_MULTI_PROCESS = 0x0004;

	/**
	 * <b>This constant has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_ENABLE_WRITE_AHEAD_LOGGING}
	 *
	 * @deprecated This flag is appropriate only for database files.
	 */
	@Deprecated
	public static final int MODE_ENABLE_WRITE_AHEAD_LOGGING = 0x0008;

	/**
	 * <b>This constant has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Wrapped mode: {@link Context#MODE_APPEND}
	 *
	 * @deprecated Use {@link SharedPreferencesPolicy#MODE_APPEND} instead.
	 */
	@Deprecated
	public static final int MODE_APPEND = Context.MODE_APPEND;

	/**
	 * <b>This annotation has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Defines an annotation for determining set of allowed flags for {@link #setMode(int)} method.
	 *
	 * @deprecated Use {@link SharedPreferencesPolicy.Mode @SharedPreferencesPolicy.FileMode} instead.
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
	 * Context with which has been this manager created.
	 *
	 * @deprecated Use {@link #getContext()} instead.
	 */
	@NonNull
	@Deprecated
	protected final Context mContext;

	/**
	 * File name of shared preferences managed by this manager.
	 */
	private String mPreferencesName;

	/**
	 * File mode of shared preferences managed by this manager.
	 */
	@SharedPreferencesPolicy.Mode
	private int mPreferencesMode;

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
		this(new Builder(context)
				.preferences(context.getSharedPreferences(
						SharedPreferencesPolicy.defaultPreferencesName(context),
						SharedPreferencesPolicy.MODE_PRIVATE
				))
				.preferencesName(SharedPreferencesPolicy.defaultPreferencesName(context))
		);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Same as {@link #PreferencesManager(Context, String, int)} with {@link Context#MODE_PRIVATE}
	 * creation mode.
	 *
	 * @deprecated Use {@link #PreferencesManager(Context)} instead.
	 */
	@Deprecated
	public PreferencesManager(@NonNull Context context, @NonNull String preferencesName) {
		this(new Builder(context)
				.preferences(context.getSharedPreferences(preferencesName, SharedPreferencesPolicy.MODE_PRIVATE))
				.preferencesName(preferencesName)
		);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Creates a new instance of PreferencesManager with the given shared preferences name and
	 * preferences file creation mode.
	 *
	 * @param context             Valid context to access shared preferences.
	 * @param preferencesName     Name for shared preferences file.
	 * @param preferencesFileMode Shared preferences will be created in this mode.
	 * @deprecated Use {@link #PreferencesManager(Context)} instead.
	 */
	@Deprecated
	public PreferencesManager(@NonNull Context context, @NonNull String preferencesName, @SharedPreferencesPolicy.FileMode int preferencesFileMode) {
		this(new Builder(context)
				.preferences(context.getSharedPreferences(preferencesName, preferencesFileMode))
				.preferencesName(preferencesName)
				.preferencesFileMode(preferencesFileMode)
		);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Creates a new instance of PreferencesManager with configuration provided by the specified
	 * <var>builder</var>.
	 *
	 * @param builder The builder used to configure the new preferences manager.
	 * @deprecated Use {@link #PreferencesManager(Context)} instead.
	 */
	@Deprecated
	protected PreferencesManager(@NonNull final Builder builder) {
		this.mContext = builder.context;
		this.mPreferencesName = builder.preferencesName;
		this.mPreferencesMode = builder.preferencesFileMode;
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

	// fixme: deprecated methods that should be removed in the next none-beta release.

	/**
	 * <b>This method has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Creates a default name for shared preferences (like the one created by {@link PreferenceManager}).
	 *
	 * @param context Context for which the name should be created.
	 * @return Default name for shared preferences.
	 * @deprecated Use {@link SharedPreferencesPolicy#defaultPreferencesName(Context)} instead.
	 */
	@NonNull
	@Deprecated
	public static String defaultSharedPreferencesName(@NonNull Context context) {
		return SharedPreferencesPolicy.defaultPreferencesName(context);
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Returns the name of {@link SharedPreferences} that are managed by this manager.
	 *
	 * @return The associated preference's name.
	 * @deprecated Use {@link #getSharedPreferencesName()} instead.
	 */
	@NonNull
	@Deprecated
	public final String getPreferencesName() {
		return getSharedPreferencesName();
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Returns the file mode of {@link SharedPreferences} that are managed by this manager.
	 *
	 * @return The associated preference's file mode.
	 * @deprecated Use {@link #getSharedPreferencesMode()}
	 */
	@Deprecated
	public final int getPreferencesFileMode() {
		return getSharedPreferencesMode();
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next none-beta release.</b>
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
	 * <b>This method has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Returns the actual file creation mode for shared preferences.
	 *
	 * @return The shared preferences file creation mode. {@link #MODE_PRIVATE} by default.
	 * @see Context
	 * @see #setMode(int)
	 * @deprecated Use {@link #getPreferencesFileMode()} instead.
	 */
	@Deprecated
	protected final int getMode() {
		return getPreferencesFileMode();
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next none-beta release.</b>
	 * <p>
	 * Returns the {@link SharedPreferences} that are hidden behind this facade.
	 *
	 * @return The associated preferences.
	 * @deprecated Use {@link #getSharedPreferences()} instead.
	 */
	@Deprecated
	public final SharedPreferences getPreferences() {
		return getSharedPreferences();
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Builder that may be used to creates new instances of PreferencesManager with a desired configuration.
	 *
	 * <h3>Required parameters</h3>
	 * <ul>
	 * <li>{@code none}</li>
	 * </ul>
	 *
	 * @author Martin Albedinsky
	 * @deprecated Use {@link #PreferencesManager(Context)} instead along with {@link #setSharedPreferencesName(String)}
	 * and {@link #setSharedPreferencesMode(int)}.
	 */
	@Deprecated
	public static class Builder {

		/**
		 * See {@link PreferencesManager#mContext}.
		 */
		final Context context;

		/**
		 * See {@link PreferencesManager#mPreferencesName}.
		 */
		String preferencesName;

		/**
		 * See {@link PreferencesManager#mPreferencesMode}.
		 */
		@SharedPreferencesPolicy.Mode
		int preferencesFileMode = SharedPreferencesPolicy.MODE_PRIVATE;

		/**
		 * See {@link PreferencesManager#mPreferencesFacade}.
		 */
		SharedPreferences preferences;

		/**
		 * Creates a new Builder with the specified <var>context</var>.
		 *
		 * @param context The context that is used to create default preferences instance for this
		 *                builder if there is no explicit instance of preferences specified.
		 */
		public Builder(@NonNull final Context context) {
			super();
			this.context = context;
			this.preferencesName = SharedPreferencesPolicy.defaultPreferencesName(context);
		}

		/**
		 * Specifies a name for shared preferences that is used to create default preferences instance
		 * in case when there is no explicit instance of preferences specified for this builder via
		 * {@link #preferences(SharedPreferences)}.
		 * <p>
		 * Also the specified name may be used later in order to identify preferences instance via
		 * {@link PreferencesManager#getPreferencesName()}.
		 *
		 * @param name The desired name for preferences. May be {@code null} in order to use default
		 *             name for preferences.
		 * @return This builder to allow methods chaining.
		 * @see SharedPreferencesPolicy#defaultPreferencesName(Context)
		 */
		public Builder preferencesName(@Nullable final String name) {
			this.preferencesName = name;
			return this;
		}

		/**
		 * Specifies a file mode for shared preferences that is used to create default preferences
		 * instance in case when there is no explicit instance of preferences specified for this builder
		 * via {@link #preferences(SharedPreferences)}.
		 * <p>
		 * Also the specified mode may be used later in order to identify preferences file mode via
		 * {@link PreferencesManager#getPreferencesFileMode()}.
		 *
		 * @param fileMode The desired file mode for preferences. Should be one of modes defined by
		 *                 {@link SharedPreferencesPolicy.Mode @SharedPreferencesPolicy.FileMode}
		 *                 annotation.
		 * @return This builder to allow methods chaining.
		 */
		public Builder preferencesFileMode(@SharedPreferencesPolicy.FileMode final int fileMode) {
			this.preferencesFileMode = fileMode;
			return this;
		}


		/**
		 * Specifies a shared preferences instance that should be used by the new preferences manager.
		 *
		 * @param preferences The shared preferences for which to create new manager.
		 * @return This builder to allow methods chaining.
		 */
		@SuppressWarnings("unchecked")
		public Builder preferences(@NonNull final SharedPreferences preferences) {
			this.preferences = preferences;
			return this;
		}

		/**
		 * Builds a new instance of PreferencesManager with the configuration specified for this builder.
		 *
		 * @return Instance of preferences manager ready to be used.
		 * @throws IllegalArgumentException If some of the required parameters is missing.
		 */
		@NonNull
		public PreferencesManager build() {
			if (TextUtils.isEmpty(preferencesName)) {
				throw new IllegalArgumentException("No preferences name specified.");
			}
			if (preferences == null) {
				this.preferences = context.getSharedPreferences(preferencesName, preferencesFileMode);
			}
			return new PreferencesManager(this);
		}
	}
}
