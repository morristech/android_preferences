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
package universum.studios.android.preference.secure;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import universum.studios.android.crypto.Crypto;
import universum.studios.android.crypto.Encrypto;

/**
 * todo: description
 *
 * @author Martin Albedinsky
 */
public final class CryptoSharedPreferences implements SharedPreferences {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "CryptoSharedPreferences";

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * todo:
	 */
	private final SharedPreferences mDelegate;

	/**
	 * todo:
	 */
	private final SecureHelper mHelper;

	/**
	 * todo:
	 */
	private SecureEditor mEditor;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * todo:
	 *
	 * @param builder
	 */
	private CryptoSharedPreferences(Builder builder) {
		this.mDelegate = builder.preferences;
		this.mHelper = new SecureHelper(builder.crypto, builder.keyEncrypto);
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		// todo: do we need to wrap the registering listener and notify it with encrypted key ???
		mDelegate.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener) {
		mDelegate.unregisterOnSharedPreferenceChangeListener(listener);
	}

	/**
	 */
	@Override
	public Map<String, String> getAll() {
		final Map<String, ?> encryptedValues = mDelegate.getAll();
		final Map<String, String> decryptedValues = new HashMap<>(encryptedValues.size());
		for (final String key : encryptedValues.keySet()) {
			final Object value = encryptedValues.get(key);
			// todo: what to do with String set value ???
			decryptedValues.put(
					mHelper.encryptKey(key),
					value == null ? null : mHelper.decrypt(value.toString())
			);
		}
		return decryptedValues;
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull String key) {
		return mDelegate.contains(mHelper.encryptKey(key));
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull String key, @Nullable String defValue) {
		final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), defValue);
		return SecureHelper.areValuesEqual(encryptedValue, defValue) ? defValue : mHelper.decrypt(encryptedValue);
	}

	/**
	 */
	@Nullable
	@Override
	public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defValues) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return defValues;
		}
		final Set<String> encryptedValue = mDelegate.getStringSet(mHelper.encryptKey(key), defValues);
		return SecureHelper.areValuesEqual(encryptedValue, defValues) ? defValues : mHelper.decryptSet(encryptedValue);
	}

	/**
	 */
	@Override
	public int getInt(@NonNull String key, int defValue) {
		final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Integer.toString(defValue));
		return SecureHelper.areValuesEqual(encryptedValue, defValue) ? defValue : Integer.parseInt(mHelper.decrypt(encryptedValue));
	}

	/**
	 */
	@Override
	public long getLong(@NonNull String key, long defValue) {
		final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Long.toString(defValue));
		return SecureHelper.areValuesEqual(encryptedValue, defValue) ? defValue : Long.parseLong(mHelper.decrypt(encryptedValue));
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull String key, float defValue) {
		final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Float.toString(defValue));
		return SecureHelper.areValuesEqual(encryptedValue, defValue) ? defValue : Float.parseFloat(mHelper.decrypt(encryptedValue));
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull String key, boolean defValue) {
		final String encryptedValue = mDelegate.getString(mHelper.encryptKey(key), Boolean.toString(defValue));
		return SecureHelper.areValuesEqual(encryptedValue, defValue) ? defValue : Boolean.parseBoolean(mHelper.decrypt(encryptedValue));
	}

	/**
	 */
	@Override
	@SuppressLint("CommitPrefEdits")
	public SharedPreferences.Editor edit() {
		if (mEditor == null) {
			this.mEditor = new SecureEditor(mDelegate.edit(), mHelper);
		}
		return mEditor;
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * todo:
	 *
	 * @author Martin Albedinsky
	 */
	public static final class Builder {

		/**
		 * todo:
		 */
		final SharedPreferences preferences;

		/**
		 * todo:
		 */
		Crypto crypto;

		/**
		 * todo:
		 */
		Encrypto keyEncrypto;

		/**
		 * todo:
		 *
		 * @param preferences
		 */
		public Builder(@NonNull SharedPreferences preferences) {
			this.preferences = preferences;
		}

		/**
		 * todo:
		 *
		 * @param crypto
		 * @return This builder to allow methods chaining.
		 */
		public Builder crypto(@Nullable Crypto crypto) {
			this.crypto = crypto;
			return this;
		}

		/**
		 * todo:
		 *
		 * @param encrypto
		 * @return This builder to allow methods chaining.
		 */
		public Builder keyEncrypto(@Nullable Encrypto encrypto) {
			this.keyEncrypto = encrypto;
			return this;
		}

		/**
		 * todo:
		 *
		 * @return
		 */
		@NonNull
		public CryptoSharedPreferences build() {
			return new CryptoSharedPreferences(this);
		}
	}

	/**
	 * todo:
	 */
	private static final class SecureEditor implements SharedPreferences.Editor {

		/**
		 * todo:
		 */
		private final SharedPreferences.Editor delegate;

		/**
		 * todo:
		 */
		private final SecureHelper helper;

		/**
		 * todo:
		 *
		 * @param delegate
		 */
		SecureEditor(SharedPreferences.Editor delegate, SecureHelper helper) {
			this.delegate = delegate;
			this.helper = helper;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putString(String key, @Nullable String value) {
			delegate.putString(helper.encryptKey(key), helper.encrypt(value));
			return this;
		}

		/**
		 */
		@Override
		public Editor putStringSet(String key, @Nullable Set<String> values) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				delegate.putStringSet(helper.encryptKey(key), helper.encryptSet(values));
			}
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putInt(String key, int value) {
			delegate.putString(helper.encryptKey(key), helper.encrypt(Integer.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putLong(String key, long value) {
			delegate.putString(helper.encryptKey(key), helper.encrypt(Long.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putFloat(String key, float value) {
			delegate.putString(helper.encryptKey(key), helper.encrypt(Float.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor putBoolean(String key, boolean value) {
			delegate.putString(helper.encryptKey(key), helper.encrypt(Boolean.toString(value)));
			return this;
		}

		/**
		 */
		@Override
		public SharedPreferences.Editor remove(String key) {
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
	 * todo:
	 */
	private static final class SecureHelper {

		/**
		 * todo:
		 */
		private final Crypto crypto;

		/**
		 * todo:
		 */
		private final Encrypto keyEncrypto;

		/**
		 * todo:
		 *
		 * @param crypto
		 * @param keyEncrypto
		 */
		SecureHelper(Crypto crypto, Encrypto keyEncrypto) {
			this.crypto = crypto;
			this.keyEncrypto = keyEncrypto;
		}

		/**
		 * todo:
		 *
		 * @param firstValue
		 * @param secondValue
		 * @return
		 */
		static boolean areValuesEqual(Object firstValue, Object secondValue) {
			return firstValue != null && firstValue.equals(secondValue);
		}

		/**
		 * todo:
		 *
		 * @param key
		 * @return
		 */
		String encryptKey(String key) {
			return keyEncrypto == null ? key : CryptoUtils.encrypt(key, keyEncrypto);
		}

		/**
		 * todo:
		 *
		 * @param value
		 * @return
		 */
		String encrypt(String value) {
			return crypto == null ? value : CryptoUtils.encrypt(value, crypto);
		}

		/**
		 * todo:
		 *
		 * @param value
		 * @return
		 */
		String decrypt(String value) {
			return crypto == null ? value : CryptoUtils.decrypt(value, crypto);
		}

		/**
		 * todo:
		 *
		 * @param values
		 * @return
		 */
		Set<String> encryptSet(Set<String> values) {
			if (crypto == null || values == null) {
				return null;
			}
			final Set<String> encryptedValues = new HashSet<>(values.size());
			for (final String value : values) {
				encryptedValues.add(encrypt(value));
			}
			return encryptedValues;
		}

		/**
		 * todo:
		 *
		 * @param values
		 * @return
		 */
		Set<String> decryptSet(Set<String> values) {
			if (crypto == null || values == null) {
				return null;
			}
			final Set<String> decryptedValues = new HashSet<>(values.size());
			for (final String value : values) {
				decryptedValues.add(decrypt(value));
			}
			return decryptedValues;
		}
	}
}
