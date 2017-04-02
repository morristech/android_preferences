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
import android.support.annotation.StringRes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A {@link SharedPreference} implementation that may be used to persist a {@link Collection} of values
 * via {@link SharedPreferences}.
 *
 * @param <T> Type of items within a collection of which values should be persisted by CollectionPreference.
 * @author Martin Albedinsky
 * @see ArrayPreference
 */
public final class CollectionPreference<T> extends SharedPreference<Collection<T>> {

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Class of components stored within a collection persisted by this preference.
	 */
	private final Class<T> mComponentType;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ListPreference with the specified <var>key</var> and <var>defValue</var>.
	 *
	 * @param componentType Class of components that can be presented within a collection that is to
	 *                      by persisted by the new collection preference.
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public CollectionPreference(@NonNull final String key, @NonNull final Class<T> componentType, @Nullable final Collection<T> defValue) {
		super(key, defValue);
		this.mComponentType = componentType;
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Creates a new instance of ListPreference.
	 *
	 * @param componentType Class of components that will be presented within a collection that may
	 *                      by persisted by the new preference.
	 * @see SharedPreference#SharedPreference(int, Object)
	 * @deprecated Use {@link #CollectionPreference(String, Class, Collection)} instead.
	 */
	@Deprecated
	CollectionPreference(@StringRes int keyResId, @NonNull Class<T> componentType, @Nullable List<T> defValue) {
		super(keyResId, defValue);
		this.mComponentType = componentType;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * @see #putIntoPreferences(SharedPreferences, String, Collection, Class)
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull final SharedPreferences preferences) {
		return putIntoPreferences(preferences, mKey, mValue, mComponentType);
	}

	/**
	 * Persists the given collection <var>value</var> for the specified <var>key</var> into the given
	 * shared <var>preferences</var>.
	 *
	 * @param preferences   The instance of shared preferences into which should be the given collection
	 *                      persisted.
	 * @param key           The key for which should be the collection mapped in the shared preferences.
	 * @param value         The desired collection value to be persisted.
	 * @param componentType Class of components presented within the given collection.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	@CheckResult
	@SuppressWarnings("unchecked")
	public static <T> boolean putIntoPreferences(@NonNull final SharedPreferences preferences, @NonNull final String key, @Nullable final Collection<T> value, @NonNull final Class<T> componentType) {
		final SharedPreferences.Editor editor = preferences.edit();
		if (value == null) {
			editor.putString(key, null);
		} else {
			final T[] array = (T[]) ArrayPreference.createArrayInSize(componentType, value.size());
			if (array == null) {
				final String componentName = componentType.getSimpleName();
				throw new IllegalArgumentException(
						"Failed to put collection of(" + componentName + ") into shared preferences. " +
								"Only collections of primitive types or theirs boxed representations including String are supported."
				);
			}
			value.toArray(array);
			return ArrayPreference.putIntoPreferences(preferences, key, array);
		}
		return editor.commit();
	}

	/**
	 * @see #getFromPreferences(SharedPreferences, String, Collection)
	 */
	@Nullable
	@Override
	protected Collection<T> onGetFromPreferences(@NonNull final SharedPreferences preferences) {
		return getFromPreferences(preferences, mKey, mDefaultValue);
	}

	/**
	 * Obtains the <b>array</b> persisted within the given shared <var>preferences</var> for the
	 * specified <var>key</var>.
	 *
	 * @param preferences The instance of shared preferences where is the desired collection persisted.
	 * @param key         The key for which is the desired collection mapped in the shared preferences.
	 * @param defValue    Default collection value to return in case when there is no collection value
	 *                    persisted for the specified <var>key</var> yet.
	 * @param <T>         Type of items of the collection to obtain.
	 * @return Instance of the requested collection or <var>defValue</var> if there is no mapping for
	 * the specified key.
	 * @throws ClassCastException       If value stored for the specified key does not represent a
	 *                                  collection (more specifically an array).
	 * @throws IllegalArgumentException If type of items of the requested collection is not supported
	 *                                  by this library.
	 * @throws IllegalStateException    If the requested collection was not stored by means of this
	 *                                  library.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> getFromPreferences(@NonNull final SharedPreferences preferences, @NonNull final String key, @Nullable final Collection<T> defValue) {
		final String value = preferences.getString(key, null);
		if (value != null) {
			try {
				final T[] array = ArrayPreference.getFromPreferences(preferences, key, null);
				return Arrays.asList(array);
			} catch (ClassCastException e) {
				final String arrayValue = ArrayPreference.extractArrayValueFromPreferenceValue(value);
				throw new ClassCastException(
						"Cannot obtain a collection for the key(" + key + ") from shared preferences. " +
								"Value(" + arrayValue + ") is not a collection!"
				);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Failed to obtain collection for the key(" + key + ") from shared preferences. " +
								"Only collections of primitive types or theirs boxed representations including String are supported."
				);
			} catch (IllegalStateException e) {
				throw new IllegalStateException(
						"Trying to obtain a collection for the key(" + key + ") from shared preferences not saved by the Preferences library."
				);
			}
		}
		return defValue;
	}
}
