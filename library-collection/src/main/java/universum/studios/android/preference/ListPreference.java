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
import java.util.List;

/**
 * A {@link SharedPreference} implementation that may be used to persist a {@link List} of values
 * via {@link SharedPreferences}.
 *
 * @param <T> Type of items within a list of which values should be persisted.
 * @author Martin Albedinsky
 * @see ArrayPreference
 */
public final class ListPreference<T> extends SharedPreference<List<T>> {

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Class of components stored within a list managed by this preference.
	 */
	private final Class<T> mComponentType;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ListPreference.
	 *
	 * @param componentType Class of components that will be presented within a list managed by the
	 *                      new list preference.
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public ListPreference(@NonNull final String key, @NonNull final Class<T> componentType, @Nullable final List<T> defValue) {
		super(key, defValue);
		this.mComponentType = componentType;
	}

	/**
	 * <b>This constructor has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Creates a new instance of ListPreference.
	 *
	 * @param componentType Class of components that will be presented within a list managed by the
	 *                      new list preference.
	 * @see SharedPreference#SharedPreference(int, Object)
	 * @deprecated Use {@link #ListPreference(String, Class, List)} instead.
	 */
	@Deprecated
	public ListPreference(@StringRes int keyResId, @NonNull Class<T> componentType, @Nullable List<T> defValue) {
		super(keyResId, defValue);
		this.mComponentType = componentType;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * @see #putIntoPreferences(SharedPreferences, String, List, Class)
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull final SharedPreferences preferences) {
		return putIntoPreferences(preferences, mKey, mValue, mComponentType);
	}

	/**
	 * Saves the given <var>list</var> into the given shared <var>preferences</var>.
	 *
	 * @param preferences   The instance of shared preferences into which will be the given list saved.
	 * @param key           The key under which will be the saved list mapped in the shared preferences.
	 * @param list          List to save into preferences.
	 * @param componentType Class of components presented within the given list.
	 * @param <T>           Type of items which are presented within the given list.
	 * @return {@code True} if saving has succeeded, {@code false} otherwise.
	 */
	@CheckResult
	@SuppressWarnings("unchecked")
	public static <T> boolean putIntoPreferences(@NonNull final SharedPreferences preferences, @NonNull final String key, @Nullable final List<T> list, @NonNull final Class<T> componentType) {
		final SharedPreferences.Editor editor = preferences.edit();
		if (list == null) {
			editor.putString(key, null);
		} else {
			final T[] array = (T[]) ArrayPreference.createArrayInSize(componentType, list.size());
			if (array == null) {
				final String componentName = componentType.getSimpleName();
				throw new IllegalArgumentException(
						"Failed to put list of(" + componentName + ") into shared preferences. " +
								"Only lists of primitive types or theirs boxed representations including String are supported."
				);
			}
			list.toArray(array);
			return ArrayPreference.putIntoPreferences(preferences, key, array);
		}
		return editor.commit();
	}

	/**
	 * @see #getFromPreferences(SharedPreferences, String, List)
	 */
	@Nullable
	@Override
	protected List<T> onGetFromPreferences(@NonNull final SharedPreferences preferences) {
		return getFromPreferences(preferences, mKey, mDefaultValue);
	}

	/**
	 * Returns a {@link List} mapped in the given shared <var>preferences</var> under the specified
	 * <var>key</var>.
	 *
	 * @param preferences The instance of shared preferences into which was the requested list
	 *                    before saved.
	 * @param key         The key under which is the saved list mapped in the shared preferences.
	 * @param defValue    Default list to return if there is no mapping for the specified <var>key</var>
	 *                    yet.
	 * @param <T>         Type of an items which should be presented within the obtained list.
	 * @return An instance of the requested list or <var>defValue</var> if there is no mapping
	 * for the specified key.
	 * @throws ClassCastException       If value stored under the specified key does not represents
	 *                                  an array/a list.
	 * @throws IllegalArgumentException If type of the requested list is not supported by the
	 *                                  Preferences library.
	 * @throws IllegalStateException    If the requested list was not stored by the Preferences  library.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getFromPreferences(@NonNull final SharedPreferences preferences, @NonNull final String key, @Nullable final List<T> defValue) {
		final String value = preferences.getString(key, null);
		if (value != null) {
			try {
				final T[] array = ArrayPreference.getFromPreferences(preferences, key, null);
				return Arrays.asList(array);
			} catch (ClassCastException e) {
				final String arrayValue = ArrayPreference.extractArrayValueFromPreferenceValue(value);
				throw new ClassCastException(
						"Cannot obtain a list for the key(" + key + ") from shared preferences. " +
								"Value(" + arrayValue + ") is not a list!"
				);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Failed to obtain list for the key(" + key + ") from shared preferences. " +
								"Only lists of primitive types or theirs boxed representations including String are supported."
				);
			} catch (IllegalStateException e) {
				throw new IllegalStateException(
						"Trying to obtain a list for the key(" + key + ") from shared preferences not saved by the Preferences library."
				);
			}
		}
		return defValue;
	}
}
