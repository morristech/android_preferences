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

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link SharedPreference} implementation that may be used to persist an {@code array} of values
 * via {@link SharedPreferences}.
 *
 * @param <T> Type of items within an array of which values should be persisted by ArrayPreference.
 * @author Martin Albedinsky
 * @see CollectionPreference
 */
public final class ArrayPreference<T> extends SharedPreference<T> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Pattern used to validate that a string contents represent an array value.
	 */
	private static final Pattern VALUE_PATTERN = Pattern.compile("^\\<(.+)\\[\\]\\>\\[(.*)\\]$");

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ArrayPreference with the specified <var>key</var> and <var>defValue</var>.
	 *
	 * @throws IllegalArgumentException If the given <var>defValue</var> is not actually an array.
	 * @see SharedPreference#SharedPreference(String, Object)
	 */
	public ArrayPreference(@NonNull final String key, @Nullable final T defValue) {
		super(key, defValue);
		assertIsArrayOrThrow(defValue);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Asserts that the specified value is type of array (if not null). If it is not type of array
	 * an IllegalArgumentException is thrown.
	 *
	 * @param value The value to be check if it is an array.
	 */
	private static void assertIsArrayOrThrow(final Object value) {
		if (value != null && !value.getClass().isArray()) {
			throw new IllegalArgumentException("Not an array(" + value.getClass().getSimpleName() + ").");
		}
	}

	/**
	 * @see #putIntoPreferences(SharedPreferences, String, Object)
	 */
	@Override
	@CheckResult
	protected boolean onPutIntoPreferences(@NonNull final SharedPreferences preferences) {
		return putIntoPreferences(preferences, mKey, mValue);
	}

	/**
	 * Persists the given array <var>value</var> for the specified <var>key</var> into the given
	 * shared <var>preferences</var>.
	 *
	 * @param preferences The instance of shared preferences into which should be the given array persisted.
	 * @param key         The key for which should be the array mapped in the shared preferences.
	 * @param value       The desired array value to be persisted.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @throws IllegalArgumentException If the given value is not actually an array.
	 */
	@CheckResult
	public static boolean putIntoPreferences(@NonNull final SharedPreferences preferences, @NonNull final String key, @Nullable final Object value) {
		if (value == null) {
			return preferences.edit().putString(key, null).commit();
		}
		assertIsArrayOrThrow(value);
		final int n = Array.getLength(value);
		final JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < n; i++) {
			jsonArray.put(Array.get(value, i));
		}
		// Save also class of the array, so when obtaining it we will know exactly of which type it is.
		final Class<?> arrayClass = resolveArrayClass(value);
		if (arrayClass == null) {
			final String componentName = value.getClass().getComponentType().getSimpleName();
			throw new IllegalArgumentException(
					"Failed to put array of(" + componentName + ") into shared preferences. " +
							"Only arrays of primitive types or theirs boxed representations including String are supported."
			);
		}
		return preferences.edit().putString(key, "<" + arrayClass.getSimpleName() + ">" + jsonArray.toString()).commit();
	}

	/**
	 * Resolves class of the given <var>array</var>.
	 *
	 * @param array The array of which class should be resolved.
	 * @return Class of the given array or {@code null} if the given array is not supported by this
	 * preference.
	 */
	static Class<?> resolveArrayClass(final Object array) {
		if (array instanceof boolean[]) {
			return boolean[].class;
		} else if (array instanceof byte[]) {
			return byte[].class;
		} else if (array instanceof char[]) {
			return char[].class;
		} else if (array instanceof short[]) {
			return short[].class;
		} else if (array instanceof int[]) {
			return int[].class;
		} else if (array instanceof float[]) {
			return float[].class;
		} else if (array instanceof long[]) {
			return long[].class;
		} else if (array instanceof double[]) {
			return double[].class;
		} else if (array instanceof Boolean[]) {
			return Boolean[].class;
		} else if (array instanceof Byte[]) {
			return Byte[].class;
		} else if (array instanceof Short[]) {
			return Short[].class;
		} else if (array instanceof Integer[]) {
			return Integer[].class;
		} else if (array instanceof Float[]) {
			return Float[].class;
		} else if (array instanceof Long[]) {
			return Long[].class;
		} else if (array instanceof Double[]) {
			return Double[].class;
		} else if (array instanceof String[]) {
			return String[].class;
		}
		return null;
	}

	/**
	 * @see #getFromPreferences(SharedPreferences, String, Object)
	 */
	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	protected T onGetFromPreferences(@NonNull final SharedPreferences preferences) {
		return getFromPreferences(preferences, mKey, mDefaultValue);
	}

	/**
	 * Obtains the <b>array</b> persisted within the given shared <var>preferences</var> for the
	 * specified <var>key</var>.
	 *
	 * @param preferences The instance of shared preferences where is the desired array persisted.
	 * @param key         The key for which is the desired array mapped in the shared preferences.
	 * @param defValue    Default array value to return in case when there is no array value persisted
	 *                    for the specified <var>key</var> yet.
	 * @param <A>         Type of the array to obtain.
	 * @return Instance of the requested array or <var>defValue</var> if there is no mapping for the
	 * specified key.
	 * @throws ClassCastException       If value stored for the specified key does not represent an
	 *                                  array.
	 * @throws IllegalArgumentException If type of the requested array is not supported by this library.
	 * @throws IllegalStateException    If the requested array was not stored by means of this library.
	 */
	@SuppressWarnings("unchecked")
	public static <A> A getFromPreferences(@NonNull final SharedPreferences preferences, @NonNull final String key, @Nullable final Object defValue) {
		Object array = defValue;
		final String value = preferences.getString(key, null);
		if (!TextUtils.isEmpty(value)) {
			final Matcher valueMatcher = VALUE_PATTERN.matcher(value);
			if (valueMatcher.reset(value).matches()) {
				final String arrayClassName = valueMatcher.group(1) + "[]";
				final Class<?> arrayClass = resolveArrayClassByName(arrayClassName);
				if (arrayClass == null) {
					final String componentName = arrayClassName.substring(0, arrayClassName.length() - 2);
					throw new IllegalArgumentException(
							"Failed to obtain an array of(" + componentName + ") for the key(" + key + ") from shared preferences. " +
									"Only arrays of primitive types or theirs boxed representations including String are supported."
					);
				}
				final String jsonArrayValue = "[" + valueMatcher.group(2) + "]";
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(jsonArrayValue);
				} catch (JSONException e) {
					throw new ClassCastException(
							"Cannot obtain an array for the key(" + key + ") from shared preferences. " +
									"Value(" + jsonArrayValue + ") is not an array!"
					);
				}
				final int n = jsonArray.length();
				array = createArrayInSize(arrayClass.getComponentType(), n);
				for (int i = 0; i < n; i++) {
					setArrayValueAt(arrayClass, array, i, jsonArray.opt(i));
				}
			} else {
				throw new IllegalStateException(
						"Trying to obtain an array for the key(" + key + ") from shared preferences not saved by the Preferences library."
				);
			}
		}
		return (A) array;
	}

	/**
	 * Extracts part with array elements from the specified array <var>value</var>.
	 *
	 * @param value The array preference value.
	 * @return Extracted array elements as String or {@code null} if the specified value is empty
	 * or does not match array preference structure.
	 */
	@Nullable
	static String extractArrayValueFromPreferenceValue(final String value) {
		final Matcher matcher = VALUE_PATTERN.matcher(value);
		if (!TextUtils.isEmpty(value) && matcher.matches()) {
			return matcher.group(2);
		}
		return null;
	}

	/**
	 * Sets a value of the given <var>array</var> at the specified <var>index</var> to the given one.
	 *
	 * @param arrayClass Class of the given array used to resolve proper setting of the given value.
	 * @param array      The array of which specific value to update.
	 * @param index      The index at which should be the value updated.
	 * @param value      The value to update.
	 */
	private static void setArrayValueAt(final Class<?> arrayClass, final Object array, final int index, final Object value) {
		if (value == null) {
			Array.set(array, index, null);
			return;
		}
		if (byte[].class.equals(arrayClass) || Byte[].class.equals(arrayClass)) {
			Array.set(array, index, Byte.valueOf(value.toString()));
		} else if (short[].class.equals(arrayClass) || Short[].class.equals(arrayClass)) {
			Array.set(array, index, Short.valueOf(value.toString()));
		} else if (float[].class.equals(arrayClass) || Float[].class.equals(arrayClass)) {
			Array.set(array, index, Float.valueOf(value.toString()));
		} else if (long[].class.equals(arrayClass) || Long[].class.equals(arrayClass)) {
			Array.set(array, index, Long.valueOf(value.toString()));
		} else {
			Array.set(array, index, value);
		}
	}

	/**
	 * Resolves array class for the specified <var>arrayClassName</var>.
	 *
	 * @param arrayClassName Name of the array class that should be resolved.
	 * @return Array class associated with the specified name or {@code Object[].class} if the given
	 * class name is associated with an array that is not supported by this preference.
	 */
	static Class<?> resolveArrayClassByName(final String arrayClassName) {
		switch (arrayClassName) {
			case "boolean[]":
				return boolean[].class;
			case "byte[]":
				return byte[].class;
			case "char[]":
				return char[].class;
			case "short[]":
				return short[].class;
			case "int[]":
				return int[].class;
			case "float[]":
				return float[].class;
			case "long[]":
				return long[].class;
			case "double[]":
				return double[].class;
			case "Boolean[]":
				return Boolean[].class;
			case "Byte[]":
				return Byte[].class;
			case "Short[]":
				return Short[].class;
			case "Integer[]":
				return Integer[].class;
			case "Float[]":
				return Float[].class;
			case "Long[]":
				return Long[].class;
			case "Double[]":
				return Double[].class;
			case "String[]":
				return String[].class;
			default:
				return null;
		}
	}

	/**
	 * Creates a new instance of array of the type for the requested <var>componentClass</var>.
	 *
	 * @param componentClass Class of a component that can be stored within the new array.
	 * @param size           Fixed size for the new array.
	 * @return New instance of the requested array.
	 */
	static Object createArrayInSize(final Class<?> componentClass, final int size) {
		if (boolean.class.equals(componentClass)) {
			return new boolean[size];
		} else if (byte.class.equals(componentClass)) {
			return new byte[size];
		} else if (char.class.equals(componentClass)) {
			return new char[size];
		} else if (short.class.equals(componentClass)) {
			return new short[size];
		} else if (int.class.equals(componentClass)) {
			return new int[size];
		} else if (float.class.equals(componentClass)) {
			return new float[size];
		} else if (long.class.equals(componentClass)) {
			return new long[size];
		} else if (double.class.equals(componentClass)) {
			return new double[size];
		} else if (Boolean.class.equals(componentClass)) {
			return new Boolean[size];
		} else if (Byte.class.equals(componentClass)) {
			return new Byte[size];
		} else if (Short.class.equals(componentClass)) {
			return new Short[size];
		} else if (Integer.class.equals(componentClass)) {
			return new Integer[size];
		} else if (Float.class.equals(componentClass)) {
			return new Float[size];
		} else if (Long.class.equals(componentClass)) {
			return new Long[size];
		} else if (Double.class.equals(componentClass)) {
			return new Double[size];
		} else if (String.class.equals(componentClass)) {
			return new String[size];
		}
		return null;
	}
}
