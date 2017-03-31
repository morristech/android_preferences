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
package universum.studios.android.preference.crypto;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import universum.studios.android.crypto.Crypto;
import universum.studios.android.preference.PreferencesLogging;
import universum.studios.android.preference.SharedPreferencesCache;

/**
 * A {@link SharedPreferences} implementation that supports <b>encryption</b> for both <b>keys</b>
 * and <b>values</b> that are persisted in shared preferences.
 *
 * <h3>Keys Encryption</h3>
 * todo:
 *
 * <h3>Values Encryption</h3>
 * todo:
 *
 * <h3>Caching</h3>
 * todo:
 *
 * @author Martin Albedinsky
 */
public final class CryptoSharedPreferences implements SharedPreferences {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "CryptoSharedPreferences";

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
	 * Instance of shared preferences to which is this crypto preferences implementation delegating
	 * requests to put or get encrypted values mapped to encrypted keys.
	 */
	private final SharedPreferences mDelegate;

	/**
	 * Helper used by crypto preferences to support cryptographic (encryption & decryption) operations.
	 */
	private final CryptoHelper mHelper;

	/**
	 * Editor that supports encryption for keys and values that are delegated to it in order to be
	 * persisted in shared preferences.
	 */
	private CryptoEditor mEditor;

	/**
	 * Registry containing registered {@link OnSharedPreferenceChangeListener OnSharedPreferenceChangeListeners}.
	 * This registry is used to properly dispatch preference change callbacks with encrypted key to
	 * all registered listeners.
	 */
	private final ChangeListeners mChangeListeners;

	/**
	 * Boolean flag indicating whether {@link #mChangeListeners} registry is registered as
	 * {@link OnSharedPreferenceChangeListener} upon {@link #mDelegate} preferences or not.
	 */
	private final AtomicBoolean mChangeListenersRegistered = new AtomicBoolean(false);

	/**
	 * Cache that is used by this crypto preferences for storing of already decrypted preference values.
	 */
	private final SharedPreferencesCache mCache;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of CryptoSharedPreferences with cryptography related configuration
	 * provided by the specified <var>builder</var>.
	 *
	 * @param builder The builder used to configure the new crypto preferences.
	 */
	private CryptoSharedPreferences(final Builder builder) {
		this.mDelegate = builder.preferences;
		this.mHelper = new CryptoHelper(builder.keyCrypto, builder.valueCrypto);
		this.mChangeListeners = new ChangeListeners(mHelper);
		this.mCache = builder.cache;

	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(@NonNull final OnSharedPreferenceChangeListener listener) {
		synchronized (mChangeListeners) {
			mChangeListeners.register(listener);
			if (!mChangeListenersRegistered.get()) {
				mDelegate.registerOnSharedPreferenceChangeListener(mChangeListeners);
				mChangeListenersRegistered.set(true);
			}
		}
	}

	/**
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(@NonNull final OnSharedPreferenceChangeListener listener) {
		synchronized (mChangeListeners) {
			mChangeListeners.unregister(listener);
			if (mChangeListenersRegistered.get() && mChangeListeners.isEmpty()) {
				mDelegate.unregisterOnSharedPreferenceChangeListener(mChangeListeners);
				mChangeListenersRegistered.set(false);
			}
		}
	}

	/**
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, ?> getAll() {
		final Map<String, ?> encryptedValues = mDelegate.getAll();
		final Map<String, Object> decryptedValues = new HashMap<>(encryptedValues.size());
		for (final String key : encryptedValues.keySet()) {
			final Object value = encryptedValues.get(key);
			final String decryptedKey = mHelper.decryptKey(key);
			if (value == null) {
				decryptedValues.put(decryptedKey, null);
			} else if (value instanceof String) {
				decryptedValues.put(decryptedKey, mHelper.decryptValue(value.toString()));
			} else if (value instanceof Set) {
				decryptedValues.put(decryptedKey, mHelper.decryptSetValues((Set<String>) value));
			} else {
				throw new IllegalStateException("Found encrypted value of unsupported type!");
			}
		}
		return decryptedValues;
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull final String key) {
		return mDelegate.contains(mHelper.encryptKey(key));
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull final String key, @Nullable final String defValue) {
		if (mCache == null || !mCache.contains(key)) {
			final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), defValue);
			if (CryptoHelper.areValuesEqual(encryptedValue, defValue)) {
				return defValue;
			}
			final String decryptedValue = mHelper.decryptValue(encryptedValue);
			if (mCache != null) {
				mCache.putString(key, decryptedValue);
			}
			return decryptedValue;
		}
		PreferencesLogging.d(TAG, "Retrieving String value for key(" + key + ") from the cache.");
		return mCache.getString(key);
	}

	/**
	 */
	@Nullable
	@Override
	public Set<String> getStringSet(@NonNull final String key, @Nullable final Set<String> defValues) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return defValues;
		}
		if (mCache == null || !mCache.contains(key)) {
			final Set<String> encryptedValues = mDelegate.getStringSet(mHelper.encryptKey(key), defValues);
			if (CryptoHelper.areValuesEqual(encryptedValues, defValues)) {
				return defValues;
			}
			final Set<String> decryptedValues = mHelper.decryptSetValues(encryptedValues);
			if (mCache != null) {
				mCache.putStringSet(key, decryptedValues);
			}
			return decryptedValues;
		}
		PreferencesLogging.d(TAG, "Retrieving Set<String> value for key(" + key + ") from the cache.");
		return mCache.getStringSet(key);
	}

	/**
	 */
	@Override
	public int getInt(@NonNull final String key, final int defValue) {
		if (mCache == null || !mCache.contains(key)) {
			final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Integer.toString(defValue));
			if (CryptoHelper.areValuesEqual(encryptedValue, defValue)) {
				return defValue;
			}
			final int decryptedValue = Integer.parseInt(mHelper.decryptValue(encryptedValue));
			if (mCache != null) {
				mCache.putInt(key, decryptedValue);
			}
			return decryptedValue;
		}
		PreferencesLogging.d(TAG, "Retrieving int value for key(" + key + ") from the cache.");
		return mCache.getInt(key);
	}

	/**
	 */
	@Override
	public long getLong(@NonNull final String key, final long defValue) {
		if (mCache == null || !mCache.contains(key)) {
			final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Long.toString(defValue));
			if (CryptoHelper.areValuesEqual(encryptedValue, defValue)) {
				return defValue;
			}
			final long decryptedValue = Long.parseLong(mHelper.decryptValue(encryptedValue));
			if (mCache != null) {
				mCache.putLong(key, decryptedValue);
			}
			return decryptedValue;
		}
		PreferencesLogging.d(TAG, "Retrieving long value for key(" + key + ") from the cache.");
		return mCache.getLong(key);
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull final String key, final float defValue) {
		if (mCache == null || !mCache.contains(key)) {
			final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Float.toString(defValue));
			if (CryptoHelper.areValuesEqual(encryptedValue, defValue)) {
				return defValue;
			}
			final float decryptedValue = Float.parseFloat(mHelper.decryptValue(encryptedValue));
			if (mCache != null) {
				mCache.putFloat(key, decryptedValue);
			}
			return decryptedValue;
		}
		PreferencesLogging.d(TAG, "Retrieving float value for key(" + key + ") from the cache.");
		return mCache.getFloat(key);
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull final String key, final boolean defValue) {
		if (mCache == null || !mCache.contains(key)) {
			final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Boolean.toString(defValue));
			if (CryptoHelper.areValuesEqual(encryptedValue, defValue)) {
				return defValue;
			}
			final boolean decryptedValue = Boolean.parseBoolean(mHelper.decryptValue(encryptedValue));
			if (mCache != null) {
				mCache.putBoolean(key, decryptedValue);
			}
			return decryptedValue;
		}
		PreferencesLogging.d(TAG, "Retrieving boolean value for key(" + key + ") from the cache.");
		return mCache.getBoolean(key);
	}

	/**
	 */
	@Override
	@SuppressLint("CommitPrefEdits")
	public SharedPreferences.Editor edit() {
		if (mEditor == null) {
			this.mEditor = new CryptoEditor(mHelper, mDelegate.edit());
			this.mEditor.setCache(mCache);
		}
		return mEditor;
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Builder that may be used to create instances of {@link CryptoSharedPreferences} with a desired
	 * configuration.
	 *
	 * <h3>Required parameters</h3>
	 * Parameters specified below are required in order to create a new instance of
	 * {@link CryptoSharedPreferences} via {@link Builder#build()} successfully.
	 * <ul>
	 * <li>{@link #Builder(SharedPreferences)}</li>
	 * <li>{@link #valueCrypto(Crypto)}</li>
	 * </ul>
	 *
	 * @author Martin Albedinsky
	 */
	public static final class Builder {

		/**
		 * Instance of shared preferences where should be persisted <b>encrypted</b> preference values.
		 */
		final SharedPreferences preferences;

		/**
		 * Crypto implementation that should be used for <b>encryption</b> and <b>decryption</b> of
		 * preference keys.
		 */
		Crypto keyCrypto;

		/**
		 * Crypto implementation that should be used for <b>encryption</b> and <b>decryption</b> of
		 * preference values.
		 */
		Crypto valueCrypto;

		/**
		 * Cache implementation that should be used to store <b>decrypted</b> preference values.
		 */
		SharedPreferencesCache cache;

		/**
		 * Creates a new instance of Builder with the given shared <var>preferences</var> instance.
		 *
		 * @param preferences The preferences instance that will be used by {@link CryptoSharedPreferences}
		 *                    for persistence of <b>encrypted</b> values.
		 */
		public Builder(@NonNull final SharedPreferences preferences) {
			this.preferences = preferences;
		}

		/**
		 * Specifies an implementation of {@link Crypto} that should be used by {@link CryptoSharedPreferences}
		 * for <b>encryption</b> and <b>decryption</b> of preference keys for which are values persisted
		 * in preferences specified via {@link #Builder(SharedPreferences)}.
		 * <p>
		 * <b>Note</b>, that when key crypto is specified all {@link OnSharedPreferenceChangeListener}
		 * listeners that are registered via {@link #registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener)}
		 * will be notified with already <b>decrypted</b> key of the changed preference. However any
		 * change listener that is registered directly on the encrypted preferences instance will
		 * be notified with <b>encrypted</b> key of the changed preference. It is recommended to
		 * use registration method of {@link CryptoSharedPreferences} so the listeners do not need
		 * to deal with decryption of the preference keys.
		 * <p>
		 * See <b>Keys Encryption</b> section in description of {@link CryptoSharedPreferences} for
		 * more information.
		 *
		 * @param crypto The desired crypto implementation. May be {@code null} to not encrypt nor
		 *               decrypt preference keys.
		 * @return This builder to allow methods chaining.
		 * @see #valueCrypto(Crypto)
		 */
		public Builder keyCrypto(@Nullablefinal  Crypto crypto) {
			this.keyCrypto = crypto;
			return this;
		}

		// todo: If encryption/decryption of preference keys shows up as a time consuming operation
		// todo: allow to specify also cache for keys which would map decrypted keys to theirs
		// todo: encrypted representation so we could possibly encrypt each key only once.

		/**
		 * Specifies an implementation of {@link Crypto} that should be used by {@link CryptoSharedPreferences}
		 * for <b>encryption</b> and <b>decryption</b> of preference values persisted in preferences
		 * specified via {@link #Builder(SharedPreferences)}.
		 * <p>
		 * If encryption and decryption of preference values shows up as a time consuming operation
		 * that slows up putting and obtaining of those values from shared preferences a cache may
		 * be specified via {@link #valueCache(SharedPreferencesCache)}. This cache will be then used
		 * by {@link CryptoSharedPreferences} for storing of already <b>decrypted</b> preference values
		 * so such values may be obtained in a much faster fashion.
		 * <p>
		 * See <b>Values Encryption</b> section in description of {@link CryptoSharedPreferences} for
		 * more information.
		 *
		 * @param crypto The desired crypto implementation to be used for values encryption/decryption.
		 * @return This builder to allow methods chaining.
		 * @see #keyCrypto(Crypto)
		 */
		public Builder valueCrypto(@NonNull final Crypto crypto) {
			this.valueCrypto = crypto;
			return this;
		}

		/**
		 * Specifies a cache that should be used by {@link CryptoSharedPreferences} to store already
		 * <b>decrypted</b> preference values in order to speed up obtaining process of such values
		 * from shared preferences.
		 * <p>
		 * See <b>Caching</b> section in description of {@link CryptoSharedPreferences} for more
		 * information.
		 *
		 * @param cache The desired cache for decrypted preference values. May be {@code null} to not
		 *              cache decrypted values.
		 * @return This builder to allow methods chaining.
		 * @see #valueCrypto(Crypto)
		 */
		public Builder valueCache(@Nullable final SharedPreferencesCache cache) {
			this.cache = cache;
			return this;
		}

		/**
		 * Builds a new instance of CryptoSharedPreferences with the configuration specified for
		 * this builder.
		 *
		 * @return Instance of preferences with <b>encryption</b> and <b>decryption</b> support ready
		 * to be used.
		 * @throws IllegalArgumentException If some of the required parameters is missing.
		 */
		@NonNull
		public CryptoSharedPreferences build() {
			if (valueCrypto == null) {
				throw new IllegalArgumentException("No Crypto implementation for values encryption/decryption specified.");
			}
			return new CryptoSharedPreferences(this);
		}
	}

	/**
	 * Helper used by {@link CryptoSharedPreferences} to perform <b>encryption</b> and <b>decryption</b>
	 * for preference keys and values.
	 */
	@VisibleForTesting
	@SuppressWarnings("WeakerAccess")
	static final class CryptoHelper {

		/**
		 * Crypto implementation used to <b>encrypt</b> and <b>decrypt</b> preference keys.
		 */
		private final Crypto keyCrypto;

		/**
		 * Crypto implementation used to <b>encrypt</b> and <b>decrypt</b> preference values.
		 */
		private final Crypto valueCrypto;

		/**
		 * Creates a new instance of CryptoHelper with the given crypto for values and keys.
		 *
		 * @param keyCrypto   The crypto implementation that should be used by the helper to
		 *                    <b>encrypt</b> and <b>decrypt</b> preference keys.
		 * @param valueCrypto The crypto implementation that should be used by the helper to
		 *                    <b>encrypt</b> and <b>decrypt</b> preference values.
		 * @see #encryptValue(String)
		 * @see #decryptKey(String)
		 * @see #encryptValue(String)
		 * @see #decryptValue(String)
		 */
		CryptoHelper(final Crypto keyCrypto, final Crypto valueCrypto) {
			this.valueCrypto = valueCrypto;
			this.keyCrypto = keyCrypto;
		}

		/**
		 * Checks whether the given values are equal or not.
		 *
		 * @param firstValue  The first value to be checked.
		 * @param secondValue The second value to be checked.
		 * @return {@code True} if the given values are equal, {@code false} otherwise.
		 * @see Object#equals(Object)
		 */
		static boolean areValuesEqual(final Object firstValue, final Object secondValue) {
			return firstValue != null && firstValue.equals(secondValue);
		}

		/**
		 * Performs encryption of the given <var>key</var> using the key {@link Crypto} specified
		 * for this helper.
		 *
		 * @param key The key to encrypt.
		 * @return Encrypted key or the same key if this helper does not have key Crypto specified.
		 * @see #decryptKey(String)
		 * @see #CryptoHelper(Crypto, Crypto)
		 * @see Crypto#encrypt(byte[])
		 */
		String encryptKey(final String key) {
			return keyCrypto == null ? key : CryptoUtils.encrypt(key, keyCrypto);
		}

		/**
		 * Performs decryption of the given <var>key</var> using the key {@link Crypto} specified
		 * for this helper.
		 *
		 * @param key The key to decrypt.
		 * @return Decrypted v or the same key if this helper does not have key Crypto specified.
		 * @see #encryptKey(String)
		 * @see #CryptoHelper(Crypto, Crypto)
		 * @see Crypto#decrypt(byte[])
		 */
		String decryptKey(final String key) {
			return keyCrypto == null ? key : CryptoUtils.decrypt(key, keyCrypto);
		}

		/**
		 * Performs encryption of the given string <var>value</var> using the value {@link Crypto}
		 * specified for this helper.
		 *
		 * @param value The value to encrypt.
		 * @return Encrypted value or the same value if this helper does not have value Crypto specified.
		 * @see #decryptValue(String)
		 * @see #CryptoHelper(Crypto, Crypto)
		 * @see Crypto#encrypt(byte[])
		 */
		String encryptValue(final String value) {
			return valueCrypto == null ? value : CryptoUtils.encrypt(value, valueCrypto);
		}

		/**
		 * Performs decryption of the given string <var>value</var> using the value {@link Crypto}
		 * specified for this helper.
		 *
		 * @param value The value to decrypt.
		 * @return Decrypted value or the same value if this helper does not have value Crypto specified.
		 * @see #encryptSetValues(Set)
		 * @see #CryptoHelper(Crypto, Crypto)
		 * @see Crypto#decrypt(byte[])
		 */
		String decryptValue(final String value) {
			return valueCrypto == null ? value : CryptoUtils.decrypt(value, valueCrypto);
		}

		/**
		 * Performs encryption of the given set of string <var>values</var> using the value {@link Crypto}
		 * specified for this helper.
		 *
		 * @param values The set of values to encrypt.
		 * @return Set of the same size with encrypted values or with the same values if this helper
		 * does not have value Crypto specified.
		 * @see #decryptSetValues(Set)
		 * @see #CryptoHelper(Crypto, Crypto)
		 * @see Crypto#encrypt(byte[])
		 */
		Set<String> encryptSetValues(final Set<String> values) {
			if (valueCrypto == null || values == null) {
				return null;
			}
			final Set<String> encryptedValues = new HashSet<>(values.size());
			for (final String value : values) {
				encryptedValues.add(encryptValue(value));
			}
			return encryptedValues;
		}

		/**
		 * Performs decryption of the given set of string <var>values</var> using the value {@link Crypto}
		 * specified for this helper.
		 *
		 * @param values The set of values to decrypt.
		 * @return Set of the same size with decrypted values or with the same values if this helper
		 * does not have value Crypto specified.
		 * @see #encryptSetValues(Set)
		 * @see #CryptoHelper(Crypto, Crypto)
		 * @see Crypto#decrypt(byte[])
		 */
		Set<String> decryptSetValues(final Set<String> values) {
			if (valueCrypto == null || values == null) {
				return null;
			}
			final Set<String> decryptedValues = new HashSet<>(values.size());
			for (final String value : values) {
				decryptedValues.add(decryptValue(value));
			}
			return decryptedValues;
		}
	}

	/**
	 * A {@link SharedPreferences.Editor} implementation for {@link CryptoSharedPreferences} that
	 * performs keys <b>encryption</b> along with values <b>encryption</b> whenever one of its
	 * {@code put...(...)} method is called.
	 */
	@VisibleForTesting
	@SuppressWarnings("WeakerAccess")
	static final class CryptoEditor implements SharedPreferences.Editor {

		/**
		 * Helper used by this crypto editor to perform <b>encryption</b> of keys along with
		 * <b>encryption</b> of values passed via one of {@code put...(...)} methods.
		 */
		private final CryptoHelper helper;

		/**
		 * Editor to which will this crypto editor delegate all its calls after it has performed all
		 * necessary cryptographic operations.
		 */
		private final SharedPreferences.Editor delegate;

		/**
		 * Cache that is used to store decrypted preference values.
		 */
		private SharedPreferencesCache cache;

		/**
		 * Creates a new instance of CryptoEditor with the given <var>helper</var> and preferences
		 * editor <var>delegate</var>.
		 *
		 * @param helper   Helper used for cryptographic operations.
		 * @param delegate Editor to which should the new crypto editor delegate its calls after all
		 *                 cryptographic operations has been performed.
		 */
		CryptoEditor(final CryptoHelper helper, final SharedPreferences.Editor delegate) {
			this.delegate = delegate;
			this.helper = helper;
		}

		/**
		 * Sets a cache that is used to store decrypted preference values. The given <var>cache</var>
		 * will be used by this editor to invalidate values stored in that cache for each key of which
		 * value will be updated by this editor via one of {@code put...(...)} methods.
		 *
		 * @param cache The cache used by the parent crypto preferences.
		 * @see #invalidateCachedValue(String)
		 */
		void setCache(final SharedPreferencesCache cache) {
			this.cache = cache;
		}

		/**
		 * Invalidates value associated with the specified <var>key</var> stored in the cache that
		 * is attached to this editor via {@link #setCache(SharedPreferencesCache)}.
		 *
		 * @param key The key for which to invalidate/evict its associated value from the cache.
		 */
		private void invalidateCachedValue(final String key) {
			if (cache != null) cache.evict(key);
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putString(final String key, @Nullable final String value) {
			this.invalidateCachedValue(key);
			delegate.putString(helper.encryptKey(key), helper.encryptValue(value));
			return this;
		}

		/**
		 */
		@Override
		public Editor putStringSet(final String key, @Nullable final Set<String> values) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				this.invalidateCachedValue(key);
				delegate.putStringSet(helper.encryptKey(key), helper.encryptSetValues(values));
			}
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putInt(final String key, final int value) {
			this.invalidateCachedValue(key);
			delegate.putString(helper.encryptKey(key), helper.encryptValue(Integer.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putLong(final String key, final long value) {
			this.invalidateCachedValue(key);
			delegate.putString(helper.encryptKey(key), helper.encryptValue(Long.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putFloat(final String key, final float value) {
			this.invalidateCachedValue(key);
			delegate.putString(helper.encryptKey(key), helper.encryptValue(Float.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putBoolean(final String key, final boolean value) {
			this.invalidateCachedValue(key);
			delegate.putString(helper.encryptKey(key), helper.encryptValue(Boolean.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor remove(final String key) {
			delegate.remove(key);
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor clear() {
			delegate.clear();
			return this;
		}

		/**
		 */
		@Override
		public boolean commit() {
			return delegate.commit();
		}

		/**
		 */
		@Override
		public void apply() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				delegate.apply();
			}
		}
	}

	/**
	 * Registry for {@link OnSharedPreferenceChangeListener} used to properly dispatch preference
	 * change callbacks with decrypted keys.
	 */
	@VisibleForTesting
	@SuppressWarnings("WeakerAccess")
	static final class ChangeListeners implements OnSharedPreferenceChangeListener {

		/**
		 * Helper used to decrypt encrypted keys for changed preferences.
		 */
		private final CryptoHelper helper;

		/**
		 * List of registered {@link OnSharedPreferenceChangeListener}.
		 */
		private final List<OnSharedPreferenceChangeListener> listeners = new ArrayList<>(2);

		/**
		 * Creates a new instance of ChangeListeners with the given <var>helper</var>.
		 *
		 * @param helper Helper used for cryptographic operations.
		 */
		ChangeListeners(final CryptoHelper helper) {
			this.helper = helper;
		}

		/**
		 * Adds the given <var>listener</var> into this registry of {@link OnSharedPreferenceChangeListener listeners}.
		 *
		 * @param listener The desired listener to register.
		 * @see #unregister(OnSharedPreferenceChangeListener)
		 */
		void register(final OnSharedPreferenceChangeListener listener) {
			synchronized (listeners) {
				if (!listeners.contains(listener)) listeners.add(listener);
			}
		}

		/**
		 * Removes the given <var>listener</var> from this registry of {@link OnSharedPreferenceChangeListener listeners}.
		 *
		 * @param listener The desired listener to un-register.
		 * @see #register(OnSharedPreferenceChangeListener)
		 */
		void unregister(final OnSharedPreferenceChangeListener listener) {
			synchronized (listeners) {
				listeners.remove(listener);
			}
		}

		/**
		 * Checks whether this listeners registry is empty or not.
		 *
		 * @return {@code True} if there are no listeners registered, {@code false} if there is at
		 * least one listener registered.
		 */
		boolean isEmpty() {
			return listeners.isEmpty();
		}

		/**
		 */
		@Override
		public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
			synchronized (listeners) {
				if (!listeners.isEmpty()) {
					final String decryptedKey = helper.decryptKey(key);
					for (final OnSharedPreferenceChangeListener listener : listeners) {
						listener.onSharedPreferenceChanged(sharedPreferences, decryptedKey);
					}
				}
			}
		}
	}
}
