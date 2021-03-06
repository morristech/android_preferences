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
package universum.studios.android.preference;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Interface that specifies a layer which may be used to hide a concrete implementation of
 * {@link android.content.SharedPreferences SharedPreferences} and to simplify <b>putting</b> and
 * <var>obtaining</var> of values for such preferences implementation.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesFacade {

	/**
	 * Registers a listener in order to receive a callback whenever a value of preference persisted
	 * within the {@code SharedPreferences} hidden behind this facade changes.
	 *
	 * @param listener The desired listener to be registered.
	 * @see #unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 * @see SharedPreferences#registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener);

	/**
	 * Un-registers a previously registered listener in order to not receive further any callbacks
	 * about  preference changes.
	 *
	 * @param listener The desired listener to be un-registered.
	 * @see #registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 * @see SharedPreferences#unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener listener);

	/**
	 * Checks whether the {@code SharedPreferences} hidden behind this facade contain value for the
	 * specified <var>key</var> or not.
	 *
	 * @param key The key of which value's presence to check.
	 * @return {@code True} if there is value contained for the specified key, {@code false} otherwise.
	 * @see #remove(String)
	 * @see SharedPreferences#contains(String)
	 */
	boolean contains(@NonNull String key);

	/**
	 * Puts the given string <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#putString(String, String)
	 */
	boolean putString(@NonNull String key, @Nullable String value);

	/**
	 * Retrieves the string value for the specified <var>key</var> from {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key      The key for which to retrieve its associated value.
	 * @param defValue Default value to be returned in case when there is no value persisted for
	 *                 the specified key.
	 * @return Either persisted or default string value.
	 * @see #contains(String)
	 * @see SharedPreferences#getString(String, String)
	 */
	@Nullable
	String getString(@NonNull String key, @Nullable String defValue);

	/**
	 * Puts the given set of string <var>values</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key    The key for which to put the values into preferences.
	 * @param values The desired values to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#putStringSet(String, Set)
	 */
	boolean putStringSet(@NonNull String key, @Nullable Set<String> values);

	/**
	 * Retrieves the set of string values for the specified <var>key</var> from {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key       The key for which to retrieve its associated values.
	 * @param defValues Default values to be returned in case when there are no values persisted for
	 *                  the specified key.
	 * @return Either persisted or default string values.
	 * @see #contains(String)
	 * @see SharedPreferences#getStringSet(String, Set)
	 */
	@Nullable
	Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defValues);

	/**
	 * Puts the given integer <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#putInt(String, int)
	 */
	boolean putInt(@NonNull String key, int value);

	/**
	 * Retrieves the integer value for the specified <var>key</var> from {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key      The key for which to retrieve its associated value.
	 * @param defValue Default value to be returned in case when there is no value persisted for
	 *                 the specified key.
	 * @return Either persisted or default integer value.
	 * @see #contains(String)
	 * @see SharedPreferences#getInt(String, int)
	 */
	int getInt(@NonNull String key, int defValue);

	/**
	 * Puts the given float <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#putFloat(String, float)
	 */
	boolean putFloat(@NonNull String key, float value);

	/**
	 * Retrieves the float value for the specified <var>key</var> from {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key      The key for which to retrieve its associated value.
	 * @param defValue Default value to be returned in case when there is no value persisted for
	 *                 the specified key.
	 * @return Either persisted or default float value.
	 * @see #contains(String)
	 * @see SharedPreferences#getFloat(String, float)
	 */
	float getFloat(@NonNull String key, float defValue);

	/**
	 * Puts the given long <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#putLong(String, long)
	 */
	boolean putLong(@NonNull String key, long value);

	/**
	 * Retrieves the long value for the specified <var>key</var> from {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key      The key for which to retrieve its associated value.
	 * @param defValue Default value to be returned in case when there is no value persisted for
	 *                 the specified key.
	 * @return Either persisted or default long value.
	 * @see #contains(String)
	 * @see SharedPreferences#getLong(String, long)
	 */
	long getLong(@NonNull String key, long defValue);

	/**
	 * Puts the given boolean <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#putBoolean(String, boolean)
	 */
	boolean putBoolean(@NonNull String key, boolean value);

	/**
	 * Retrieves the boolean value for the specified <var>key</var> from {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key      The key for which to retrieve its associated value.
	 * @param defValue Default value to be returned in case when there is no value persisted for
	 *                 the specified key.
	 * @return Either persisted or default boolean value.
	 * @see #contains(String)
	 * @see SharedPreferences#getBoolean(String, boolean)
	 */
	boolean getBoolean(@NonNull String key, boolean defValue);

	/**
	 * Removes a value for the specified <var>key</var> from the {@code SharedPreferences} hidden
	 * behind this facade.
	 *
	 * @param key The key for which to remove its associated value.
	 * @return {@code True} if removal has been successful, {@code false} otherwise.
	 * @see #removeAll()
	 * @see #contains(String)
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#remove(String)
	 */
	boolean remove(@NonNull String key);

	/**
	 * Removes all values from the {@code SharedPreferences} hidden behind this facade.
	 *
	 * @return A none-negative number determining count of values that has been removed. May be {@code 0}
	 * if there are no values persisted.
	 * @see #remove(String)
	 * @see SharedPreferences#getAll()
	 * @see SharedPreferences#edit()
	 * @see SharedPreferences.Editor#remove(String)
	 */
	int removeAll();
}