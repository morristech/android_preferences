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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Interface declaring layer for caches that may be used to store values of {@code SharedPreferences}
 * in a memory.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesCache {

	/**
	 * Checks whether this cache is empty or not.
	 *
	 * @return {@code True} if this cache does not have any values stored, {@code false} if there
	 * is at least one value stored.
	 */
	boolean isEmpty();

	/**
	 * Checks whether there is stored value for the specified <var>key</var> in this cache.
	 *
	 * @param key The key of which value presence to check.
	 * @return {@code True} if there is value presented for the specified key, {@code false} otherwise.
	 */
	boolean contains(@NonNull String key);

	/**
	 * Puts the given string <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
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
	 */
	int getInt(@NonNull String key, int defValue);

	/**
	 * Puts the given float <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
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
	 */
	float getFloat(@NonNull String key, float defValue);

	/**
	 * Puts the given long <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
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
	 */
	long getLong(@NonNull String key, long defValue);

	/**
	 * Puts the given boolean <var>value</var> for the specified <var>key</var> into {@code SharedPreferences}
	 * hidden behind this facade.
	 *
	 * @param key   The key for which to put the value into preferences.
	 * @param value The desired value to put into preferences.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
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
	 */
	boolean getBoolean(@NonNull String key, boolean defValue);

	/**
	 * Evicts a value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to evict its associated value.
	 * @return {@code True} if eviction has been successful, {@code false} otherwise.
	 */
	boolean evict(@NonNull String key);

	/**
	 * Evicts all values stored in this cache.
	 *
	 * @return The number of values that has been evicted. May be {@code 0} if this cache is empty.
	 */
	int evictAll();
}
