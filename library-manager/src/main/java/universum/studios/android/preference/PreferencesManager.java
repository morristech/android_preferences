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
	 * Name of the shared preferences managed by this manager.
	 */
	private final String mPreferencesName;

	/**
	 * File mode of the shared preferences managed by this manager.
	 */
	@SharedPreferencesPolicy.FileMode
	private final int mPreferencesFileMode;

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
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Same as {@link #PreferencesManager(Context, String)} with default name for
	 * preferences (like the one provided by Android framework).
	 *
	 * @deprecated Use {@link #PreferencesManager(Builder)} instead.
	 */
	@Deprecated
	public PreferencesManager(@NonNull Context context) {
		this(new Builder(context)
				.preferences(context.getSharedPreferences(
						SharedPreferencesPolicy.defaultPreferencesName(context),
						SharedPreferencesPolicy.FILE_MODE_PRIVATE
				))
				.preferencesName(SharedPreferencesPolicy.defaultPreferencesName(context))
		);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Same as {@link #PreferencesManager(Context, String, int)} with {@link Context#MODE_PRIVATE}
	 * creation mode.
	 *
	 * @deprecated Use {@link #PreferencesManager(Builder)} instead.
	 */
	@Deprecated
	public PreferencesManager(@NonNull Context context, @NonNull String preferencesName) {
		this(new Builder(context)
				.preferences(context.getSharedPreferences(preferencesName, SharedPreferencesPolicy.FILE_MODE_PRIVATE))
				.preferencesName(preferencesName)
		);
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Creates a new instance of PreferencesManager with the given shared preferences name and
	 * preferences file creation mode.
	 *
	 * @param context             Valid context to access shared preferences.
	 * @param preferencesName     Name for shared preferences file.
	 * @param preferencesFileMode Shared preferences will be created in this mode.
	 * @deprecated Use {@link #PreferencesManager(Builder)} instead.
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
	 * todo:
	 *
	 * @param builder
	 */
	protected PreferencesManager(@NonNull Builder builder) {
		super(builder);
		this.mContext = builder.context;
		this.mPreferencesName = builder.preferencesName;
		this.mPreferencesFileMode = builder.preferencesFileMode;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the {@link SharedPreferences} that are managed by this manager.
	 *
	 * @return The associated preferences.
	 * @see Builder#preferences(SharedPreferences)
	 * @see #getPreferencesName()
	 * @see #getPreferencesFileMode()
	 */
	@NonNull
	public final SharedPreferences getPreferences() {
		return mPreferences;
	}

	/**
	 * Returns the name of {@link SharedPreferences} that are managed by this manager.
	 *
	 * @return The associated preference's name.
	 * @see Builder#preferencesName(String)
	 * @see #getPreferences()
	 * @see #getPreferencesFileMode()
	 */
	@NonNull
	public final String getPreferencesName() {
		return mPreferencesName;
	}

	/**
	 * Returns the file mode of {@link SharedPreferences} that are managed by this manager.
	 *
	 * @return The associated preference's file mode.
	 * @see Builder#preferencesFileMode(int)
	 * @see #getPreferences()
	 * @see #getPreferencesName()
	 */
	@SharedPreferencesPolicy.FileMode
	public final int getPreferencesFileMode() {
		return mPreferencesFileMode;
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
	 * Returns a string key for the specified <var>resId</var>.
	 *
	 * @param resId Resource id of the desired preference key.
	 * @return String key obtained from the context specified for this manager.
	 */
	@NonNull
	protected final String key(@StringRes int resId) {
		return mContext.getString(resId);
	}

	/**
	 * Persists the given <var>value</var> for the specified <var>preference</var> into {@link SharedPreferences}
	 * that are managed by this manager.
	 *
	 * @param preference Preference for which to persist the value.
	 * @param value      The value to be persisted.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see #getPreference(SharedPreference)
	 * @see #containsPreference(SharedPreference)
	 * @see #removePreference(SharedPreference)
	 * @see SharedPreference#updateValue(Object)
	 * @see SharedPreference#putIntoPreferences(SharedPreferences)
	 */
	public final <Type> boolean putPreference(@NonNull SharedPreference<Type> preference, @Nullable Type value) {
		final boolean result = preference.updateValue(value).putIntoPreferences(mPreferences);
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
	 * @return Value associated with the preference. May be {@code null} if there is no value persisted
	 * for the preference yet.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #contains(String)
	 * @see SharedPreference#getFromPreferences(SharedPreferences)
	 */
	@Nullable
	public final <Type> Type getPreference(@NonNull SharedPreference<Type> preference) {
		final Type value = preference.getFromPreferences(mPreferences);
		if (!mCachingEnabled) {
			preference.invalidate();
		}
		return value;
	}

	/**
	 * Checks whether there is value associated with the specified <var>preference</var> contained
	 * within {@link SharedPreferences} that are managed by this manager.
	 *
	 * @param preference The desired preference of which value's existence to check.
	 * @return {@code True} if there is value contained for key of the specified preference,
	 * {@code false} otherwise.
	 * @see #putPreference(SharedPreference, Object)
	 * @see #getPreference(SharedPreference)
	 * @see #removePreference(SharedPreference)
	 */
	public final boolean containsPreference(@NonNull SharedPreference preference) {
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
	public final boolean removePreference(@NonNull SharedPreference preference) {
		return remove(preference.getKey());
	}

	// fixme: deprecated methods that should be removed in the next release

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
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

	/**
	 * Builder that may be used to creates new instances of PreferencesManager with a desired configuration.
	 *
	 * @author Martin Albedinsky
	 */
	public static class Builder extends SimpleSharedPreferencesFacade.Builder<Builder> {

		/**
		 * See {@link PreferencesManager#mContext}.
		 */
		final Context context;

		/**
		 * See {@link PreferencesManager#mPreferencesName}.
		 */
		String preferencesName;

		/**
		 * See {@link PreferencesManager#mPreferencesFileMode}.
		 */
		@SharedPreferencesPolicy.FileMode
		int preferencesFileMode = SharedPreferencesPolicy.FILE_MODE_PRIVATE;

		/**
		 * Creates a new Builder with the specified <var>context</var>.
		 *
		 * @param context The context that is used to create default preferences instance for this
		 *                builder if there is no explicit instance of preferences specified.
		 */
		public Builder(@NonNull Context context) {
			this.context = context;
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
		public Builder preferencesName(@Nullable String name) {
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
		 *                 {@link SharedPreferencesPolicy.FileMode @SharedPreferencesPolicy.FileMode}
		 *                 annotation.
		 * @return This builder to allow methods chaining.
		 */
		public Builder preferencesFileMode(@SharedPreferencesPolicy.FileMode int fileMode) {
			this.preferencesFileMode = fileMode;
			return this;
		}

		/**
		 * Builds a new instance of PreferencesManager from the configuration specified for this builder.
		 *
		 * @return Instance of preferences manager ready to be used.
		 * @throws IllegalArgumentException If some of the required parameters are missing.
		 */
		@NonNull
		@Override
		public PreferencesManager build() {
			if (TextUtils.isEmpty(preferencesName)) {
				this.preferencesName = SharedPreferencesPolicy.defaultPreferencesName(context);
			}
			if (preferences == null) {
				this.preferences = context.getSharedPreferences(preferencesName, preferencesFileMode);
			}
			return new PreferencesManager(this);
		}
	}
}
