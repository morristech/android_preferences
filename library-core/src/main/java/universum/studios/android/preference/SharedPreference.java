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
import android.content.res.Resources;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;

/**
 * Base implementation of shared preference object used by {@link PreferencesManager} to manage
 * simplified storing and retrieving values from shared preferences.
 * <p>
 * This class implements API necessary to save its current value into {@link SharedPreferences}
 * via {@link #updateValue(Object)} followed by {@link #save(SharedPreferences)} and to retrieve such
 * saved value from the preferences via {@link #retrieve(SharedPreferences)} followed by {@link #getValue()}.
 * <p>
 * The key for shared preference must be passed during initialization to one of constructors and can
 * be later obtained via {@link #getKey()} or {@link #getKeyRes()}. <b>Note</b>, that if you will use
 * resource ids as keys, the raw string key need to be attached via {@link #attachKey(Resources)},
 * before any initial storing/obtaining.
 *
 * @param <Type> A type of the value that can this implementation of preference hold and manage its
 *               saving/obtaining.
 * @author Martin Albedinsky
 */
public abstract class SharedPreference<Type> {

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Callback that can be used to listen for changes in value of a specific {@link SharedPreference}
	 * implementation.
	 *
	 * @param <PreferenceType> A type of the preference, for which is this callback created.
	 * @author Martin Albedinsky
	 */
	public interface PreferenceChangeCallback<PreferenceType> {

		/**
		 * Invoked whenever a value of the specified <var>preference</var> has been changed. The passed
		 * preference already contains new value parsed from shared preferences which can be obtained
		 * via {@link SharedPreference#getValue()}.
		 * <p>
		 * The preference can be identified by {@link SharedPreference#getKey()} or
		 * {@link SharedPreference#getKeyRes()}.
		 *
		 * @param preference The preference, of which value was changed.
		 */
		void onPreferenceChanged(@NonNull SharedPreference<PreferenceType> preference);
	}

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SharedPreference";

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * The key under which will be the value of this preference mapped within shared preferences.
	 */
	String mKey = "";

	/**
	 * Default value of this preference for case, when there is no value saved withing
	 * shared preference yet.
	 */
	Type mDefaultValue;

	/**
	 * Actual value of this shared preference to save.
	 */
	Type mValue;

	/**
	 * Xml resource
	 */
	private int mKeyRes = -1;

	/**
	 * Flag indicating whether the {@link #mValue} is same as value saved within shared
	 * preferences.
	 */
	private boolean mAlreadyParsed;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SharedPreference with the given key and default value.
	 *
	 * @param key      The key under which will be the value of this preference mapped within
	 *                 shared preferences.
	 * @param defValue Default value of this preference to return in case, when there is no value
	 *                 saved withing shared preference yet.
	 * @throws IllegalArgumentException If the specified <var>key</var> is empty.
	 */
	protected SharedPreference(@NonNull String key, @Nullable Type defValue) {
		if (TextUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Preference key cannot be empty.");
		}
		this.mKey = key;
		this.mDefaultValue = mValue = defValue;
	}

	/**
	 * Creates a new instance of SharedPreference with the given key resource and default value.
	 *
	 * @param keyResId     Resource id of the desired key under which will be the value of this preference
	 *                     mapped within shared preferences.
	 * @param defaultValue Default value of this preference for case, when there is no value saved withing
	 *                     shared preference yet.
	 * @throws IllegalArgumentException If the specified <var>keyResId</var> is invalid.
	 * @see #attachKey(Resources)
	 */
	protected SharedPreference(@StringRes int keyResId, @Nullable Type defaultValue) {
		if (keyResId <= 0) {
			throw new IllegalArgumentException("Resource id(" + keyResId + ") for preference key is not valid.");
		}
		this.mKeyRes = keyResId;
		this.mDefaultValue = defaultValue;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the key of this preference.
	 *
	 * @return Same key as passed to {@link #SharedPreference(String, Object)} constructors.
	 */
	@NonNull
	public final String getKey() {
		return mKey;
	}

	/**
	 * Return the resource id of the key of this preference.
	 *
	 * @return Same resource id as passed to {@link #SharedPreference(int, Object)} constructor.
	 */
	@StringRes
	public final int getKeyRes() {
		return mKeyRes;
	}

	/**
	 * Attaches the key for this preference from the given resources using the current key's resource
	 * id specified via {@link #SharedPreference(int, Object)}.
	 *
	 * @param resources An application's resources to obtain key.
	 * @return This preference to allow methods chaining.
	 * @throws IllegalArgumentException If key obtained from the resources for the resource id is empty.
	 */
	public SharedPreference<Type> attachKey(@NonNull Resources resources) {
		if (mKeyRes != -1) {
			this.mKey = resources.getString(mKeyRes);
			if (TextUtils.isEmpty(mKey)) {
				throw new IllegalArgumentException("Preference key cannot be empty.");
			}
		}
		return this;
	}

	/**
	 * Returns the default value of this preference.
	 *
	 * @return Same value as passed to {@link #SharedPreference(String, Object)}.
	 */
	@Nullable
	public final Type getDefaultValue() {
		return mDefaultValue;
	}

	/**
	 * Retrieves the current value of this preference from the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which was the value of this preference
	 *                    before saved.
	 * @return This preference to allow methods chaining.
	 * @see #getValue()
	 */
	public SharedPreference<Type> retrieve(@NonNull SharedPreferences preferences) {
		this.mValue = getFromPreferences(preferences);
		return this;
	}

	/**
	 * Performs obtaining of the actual value of this preference from the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which was the value of this preference
	 *                    before saved.
	 * @return The actual value obtained from the given shared preferences or {@code null} if this
	 * preference's key is invalid.
	 */
	final Type getFromPreferences(SharedPreferences preferences) {
		this.ensureValidKeyOrThrow();
		if (!mAlreadyParsed) {
			this.mValue = onGetFromPreferences(preferences);
			this.mAlreadyParsed = true;
		}
		return mValue;
	}

	/**
	 * Ensures that the key of this preference is valid (not empty). If not throws an IllegalStateException.
	 */
	private void ensureValidKeyOrThrow() {
		if (TextUtils.isEmpty(mKey)) {
			final String preferenceType = getClass().getSimpleName();
			throw new IllegalStateException(
					"Key for preference(" + preferenceType + ") is not properly initialized. " +
							"Didn't you forget to set it up via SharedPreference.attachKey(Resources)?"
			);
		}
	}

	/**
	 * Invoked to retrieve the actual value of this preference from the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which was the value of this preference
	 *                    before saved.
	 * @return The actual value of this preference from the given shared preferences.
	 */
	@Nullable
	protected abstract Type onGetFromPreferences(@NonNull SharedPreferences preferences);

	/**
	 * Saves the current value of this preference into the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which will be the current value of
	 *                    this preference saved.
	 * @return {@code True} if saving operation succeed, {@code false} otherwise.
	 * @see #updateValue(Object)
	 */
	@CheckResult
	public boolean save(@NonNull SharedPreferences preferences) {
		return putIntoPreferences(preferences);
	}

	/**
	 * Performs saving of the actual value of this preference into the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which should be the current value of
	 *                    this preference saved.
	 * @return {@code True} if saving operation succeed, {@code false} otherwise.
	 */
	final boolean putIntoPreferences(SharedPreferences preferences) {
		this.ensureValidKeyOrThrow();
		return mAlreadyParsed = onPutIntoPreferences(preferences);
	}

	/**
	 * Invoked to save the actual value into the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which should be the current value of
	 *                    this preference saved.
	 * @return {@code True} if saving operation succeed, {@code false} otherwise.
	 */
	@CheckResult
	protected abstract boolean onPutIntoPreferences(@NonNull SharedPreferences preferences);

	/**
	 * Creates new instance of {@link SharedPreferences.OnSharedPreferenceChangeListener} which can
	 * be used to listen on changes provided up on the value of this preference within shared preferences.
	 * <p>
	 * <b>Note</b>, that here created listener will fire the given callback only in case, that the
	 * key received in the callback from shared preferences will match the key of this preference.
	 *
	 * @param callback Callback to be invoked when the value of this preference changes.
	 * @return New instance of OnSharedPreferenceListener.
	 * @see SharedPreferences
	 */
	@NonNull
	public SharedPreferences.OnSharedPreferenceChangeListener createOnChangeListener(@NonNull final PreferenceChangeCallback<Type> callback) {
		return new SharedPreferences.OnSharedPreferenceChangeListener() {

			/**
			 */
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				if (mKey.equals(key)) {
					// Parse actual value.
					retrieve(sharedPreferences);
					callback.onPreferenceChanged(SharedPreference.this);
				}
			}
		};
	}

	/**
	 * Returns the value, which is currently being hold by this preference object. <b>Note</b>, that
	 * this isn't current value from shared preferences if this preference wasn't parsed or
	 * {@link #retrieve(SharedPreferences)}) before this call.
	 * <p>
	 * To obtain always an actual value from shared preferences, perform this calls on the instance
	 * of SharedPreference of which value you want to obtain:
	 * <pre>
	 * {@code <b>SharedPreference.parse(SharedPreferences).getActualValue()</b>} or
	 * {@code <b>SharedPreference.parse(PreferencesManager).getActualValue()</b>}
	 * </pre>
	 *
	 * @return The actual value of this preference or {@code null} if this preference's key is
	 * invalid.
	 * @see #retrieve(SharedPreferences)
	 */
	@Nullable
	public final Type getValue() {
		return mValue;
	}

	/**
	 * Updates the actual value of this preference to the given one. <b>Note</b>, that
	 * this doesn't updates the value within shared preferences.
	 * <p>
	 * To immediately save the given new value of this preference into shared preferences, perform
	 * this calls on the instance of SharedPreference of which value you want to save:
	 * <pre>
	 * {@code <b>SharedPreference.updateValue(Type).save(SharedPreferences)</b>} or
	 * {@code <b>SharedPreference.updateValue(Type).save(PreferencesManager)</b>}
	 * </pre>
	 *
	 * @param newValue New value for this preference.
	 * @return This preference to allow methods chaining.
	 * @see #save(SharedPreferences)
	 */
	public SharedPreference<Type> updateValue(@Nullable Type newValue) {
		if (mValue == null || !mValue.equals(newValue)) {
			this.mValue = newValue;
			this.mAlreadyParsed = true;
		}
		return this;
	}

	/**
	 * Clears the actual value of this preference.
	 */
	final void clear() {
		this.mValue = null;
		this.mAlreadyParsed = false;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
