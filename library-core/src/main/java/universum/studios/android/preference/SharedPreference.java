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
import android.text.TextUtils;

/**
 * Base implementation of shared preference object which may be used to persist a desired value in
 * {@link SharedPreferences} and later obtain such value.
 * <p>
 * This class provides API necessary to persist a desired preference value in {@link SharedPreferences}
 * via {@link #updateValue(Object)} followed by {@link #putIntoPreferences(SharedPreferences)} and
 * to obtain such persisted value from preferences via {@link #getFromPreferences(SharedPreferences)}.
 * The current value that has been obtained from shared preferences for a particular preference may
 * be obtained via {@link #getValue()}.
 * <p>
 * The key for each shared preference is required to be specified via {@link #SharedPreference(String, Object)}
 * constructor and may be later obtained via {@link #getKey()}.
 *
 * @param <T> Type of the value that can be persisted by this preference implementation.
 * @author Martin Albedinsky
 */
public abstract class SharedPreference<T> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SharedPreference";

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Callback that may be used to listen for changes in value of a specific {@link SharedPreference}
	 * implementation.
	 *
	 * @param <P> Type of the preference to which will be this callback attached.
	 * @author Martin Albedinsky
	 * @see #createOnChangeListener(PreferenceChangeCallback)
	 */
	public interface PreferenceChangeCallback<P> {

		/**
		 * Invoked whenever a value of the specified <var>preference</var> has been changed. The passed
		 * preference already contains new value parsed from shared preferences which may be obtained
		 * via {@link SharedPreference#getValue()}.
		 * <p>
		 * The preference can be identified by {@link SharedPreference#getKey()}.
		 *
		 * @param preference The preference of which value has changed.
		 */
		void onPreferenceChanged(@NonNull SharedPreference<P> preference);
	}

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * The key for which will be the value of this preference persisted within shared preferences.
	 */
	final String mKey;

	/**
	 * Default value of this preference for case when there is no value persisted within shared
	 * preference yet.
	 */
	T mDefaultValue;

	/**
	 * Actual value of this shared preference. This is either already persisted value or yet to be
	 * persisted if the value has been updated via {@link #updateValue(Object)} but not put into
	 * preferences via {@link #putIntoPreferences(SharedPreferences)} yet.
	 */
	T mValue;

	/**
	 * Boolean flag indicating whether the {@link #mValue} is the same as value persisted within
	 * shared preferences.
	 */
	boolean mValueIsActual;

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
	protected SharedPreference(@NonNull final String key, @Nullable final T defValue) {
		if (TextUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Preference key cannot be empty.");
		}
		this.mKey = key;
		this.mDefaultValue = mValue = defValue;
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
	 * Creates a new instance of {@link SharedPreferences.OnSharedPreferenceChangeListener} which
	 * may be used to listen for changes in value of this preference within shared preferences.
	 * <p>
	 * <b>Note</b>, that the created listener will delegate the change event the given callback only
	 * in case when the key of changed value matches the key of this preference.
	 *
	 * @param callback Callback to be invoked when the value of this preference changes.
	 * @return OnSharedPreferenceListener ready to be used.
	 * @see SharedPreferences#registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 * @see SharedPreferences#unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	@NonNull
	public SharedPreferences.OnSharedPreferenceChangeListener createOnChangeListener(@NonNull final PreferenceChangeCallback<T> callback) {
		return new SharedPreferences.OnSharedPreferenceChangeListener() {

			/**
			 */
			@Override
			public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String key) {
				if (mKey.equals(key)) {
					// Ensure that we have always the actual value.
					mValueIsActual = false;
					getFromPreferences(sharedPreferences);
					callback.onPreferenceChanged(SharedPreference.this);
				}
			}
		};
	}

	/**
	 * Returns the default value of this preference.
	 *
	 * @return This preference's default value.
	 * @see #SharedPreference(String, Object)
	 */
	@Nullable
	public final T getDefaultValue() {
		return mDefaultValue;
	}

	/**
	 * Updates the actual value of this preference to the given one.
	 * <p>
	 * <b>Note</b>, that calling this method does not trigger persisting of the given value into
	 * shared preferences.
	 * <p>
	 * In order to immediately persist the given new value of this preference into shared preferences,
	 * this call need to be followed by {@link #putIntoPreferences(SharedPreferences)}.
	 *
	 * @param newValue New value for this preference.
	 * @return This preference to allow methods chaining.
	 * @see #getValue()
	 */
	public SharedPreference<T> updateValue(@Nullable final T newValue) {
		if (mValue == null || !mValue.equals(newValue)) {
			this.mValue = newValue;
			this.mValueIsActual = true;
		}
		return this;
	}

	/**
	 * Persists the actual value of this preference into the given shared <var>preferences</var>.
	 *
	 * @param preferences The instance of shared preferences where should be the actual value of
	 *                    this preference persisted.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see #getFromPreferences(SharedPreferences)
	 * @see #updateValue(Object)
	 * @see #getValue()
	 */
	public final boolean putIntoPreferences(@NonNull final SharedPreferences preferences) {
		this.mValueIsActual = onPutIntoPreferences(preferences);
		return mValueIsActual;
	}

	/**
	 * Invoked to persist the actual value into the given shared <var>preferences</var>.
	 *
	 * @param preferences The instance of shared preferences into which should be the current value
	 *                    of this preference persisted.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	@CheckResult
	protected abstract boolean onPutIntoPreferences(@NonNull SharedPreferences preferences);

	/**
	 * Returns the value which is at this time being hold by this preference object.
	 * <p>
	 * <b>Note</b>, that this does not need to be necessarily the same value as persisted (if) within
	 * shared preferences for this preference if the actual value has been just updated via
	 * {@link #updateValue(Object)} and not persisted via {@link #putIntoPreferences(SharedPreferences)}
	 * or {@link #getFromPreferences(SharedPreferences)} was not called for this preference yet.
	 * <p>
	 * In order to always obtain the actual value of this preference from shared preferences use
	 * {@link #getFromPreferences(SharedPreferences)} instead.
	 *
	 * @return The actual value of this preference.
	 */
	@Nullable
	public final T getValue() {
		return mValue;
	}

	/**
	 * Obtains the actual value of this preference that is persisted within the given shared
	 * <var>preferences</var>.
	 * <p>
	 * <b>Note</b>, that the value is obtained only in case when this preference does not already
	 * hold the actual value which may be obtained via {@link #getValue()}.
	 *
	 * @param preferences The instance of shared preferences from which to obtain the actual value
	 *                    if needed.
	 * @return The actual value either already hold by this preference or obtained from the preferences.
	 * May be also the default value is there is no value persisted yet.
	 * @see #getDefaultValue()
	 * @see #putIntoPreferences(SharedPreferences)
	 */
	final T getFromPreferences(final SharedPreferences preferences) {
		if (!mValueIsActual) {
			this.mValue = onGetFromPreferences(preferences);
			this.mValueIsActual = true;
		}
		return mValue;
	}

	/**
	 * Invoked to retrieve the actual value of this preference from the given shared preferences.
	 *
	 * @param preferences The instance of shared preferences into which was the value of this preference
	 *                    before saved.
	 * @return The actual value of this preference from the given shared preferences.
	 */
	@Nullable
	protected abstract T onGetFromPreferences(@NonNull SharedPreferences preferences);

	/**
	 * Invalidates the actual value of this preference so next call to {@link #getFromPreferences(SharedPreferences)}
	 * will obtain the actual value from the specified preferences in order to refresh it.
	 */
	final void invalidate() {
		this.mValue = null;
		this.mValueIsActual = false;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
