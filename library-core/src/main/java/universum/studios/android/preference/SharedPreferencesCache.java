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
import android.util.AndroidRuntimeException;

import java.util.Set;

/**
 * Interface that specifies a layer for caches that may be used to store values of {@code SharedPreferences}
 * in a memory.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesCache {

	/*
	 * Methods =====================================================================================
	 */

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
	 * Puts the given string <var>value</var> for the specified <var>key</var> into this cache.
	 * <p>
	 * If there is already stored value for the specified <var>key</var> in this cache it will be
	 * replaced by the new one.
	 *
	 * @param key   The key for which to put the value into cache.
	 * @param value The desired value to put into cache.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	boolean putString(@NonNull String key, @Nullable String value);

	/**
	 * Retrieves the string value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to retrieve its associated value.
	 * @return The value associated with the key.
	 * @throws NotInCacheException If there is no value stored in this cache for the requested key.
	 * @see #contains(String)
	 */
	@Nullable
	String getString(@NonNull String key);

	/**
	 * Puts the given set of string <var>values</var> for the specified <var>key</var> into this cache.
	 * <p>
	 * If there is already stored value for the specified <var>key</var> in this cache it will be
	 * replaced by the new one.
	 *
	 * @param key    The key for which to put the value into cache.
	 * @param values The desired set of values to put into cache.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	boolean putStringSet(@NonNull String key, @Nullable Set<String> values);

	/**
	 * Retrieves the set of string values for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to retrieve its associated value.
	 * @return The set of values associated with the key.
	 * @throws NotInCacheException If there is no value stored in this cache for the requested key.
	 * @see #contains(String)
	 */
	@Nullable
	Set<String> getStringSet(@NonNull String key);

	/**
	 * Puts the given integer <var>value</var> for the specified <var>key</var> into this cache.
	 * <p>
	 * If there is already stored value for the specified <var>key</var> in this cache it will be
	 * replaced by the new one.
	 *
	 * @param key   The key for which to put the value into cache.
	 * @param value The desired value to put into cache.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	boolean putInt(@NonNull String key, int value);

	/**
	 * Retrieves the integer value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to retrieve its associated value.
	 * @return The value associated with the key.
	 * @throws NotInCacheException If there is no value stored in this cache for the requested key.
	 * @see #contains(String)
	 */
	int getInt(@NonNull String key);

	/**
	 * Puts the given float <var>value</var> for the specified <var>key</var> into this cache.
	 * <p>
	 * If there is already stored value for the specified <var>key</var> in this cache it will be
	 * replaced by the new one.
	 *
	 * @param key   The key for which to put the value into cache.
	 * @param value The desired value to put into cache.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	boolean putFloat(@NonNull String key, float value);

	/**
	 * Retrieves the float value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to retrieve its associated value.
	 * @return The value associated with the key.
	 * @throws NotInCacheException If there is no value stored in this cache for the requested key.
	 * @see #contains(String)
	 */
	float getFloat(@NonNull String key);

	/**
	 * Puts the given long <var>value</var> for the specified <var>key</var> into this cache.
	 * <p>
	 * If there is already stored value for the specified <var>key</var> in this cache it will be
	 * replaced by the new one.
	 *
	 * @param key   The key for which to put the value into cache.
	 * @param value The desired value to put into cache.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	boolean putLong(@NonNull String key, long value);

	/**
	 * Retrieves the long value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to retrieve its associated value.
	 * @return The value associated with the key.
	 * @throws NotInCacheException If there is no value stored in this cache for the requested key.
	 * @see #contains(String)
	 */
	long getLong(@NonNull String key);

	/**
	 * Puts the given boolean <var>value</var> for the specified <var>key</var> into this cache.
	 * <p>
	 * If there is already stored value for the specified <var>key</var> in this cache it will be
	 * replaced by the new one.
	 *
	 * @param key   The key for which to put the value into cache.
	 * @param value The desired value to put into cache.
	 * @return {@code True} if put has been successful, {@code false} otherwise.
	 */
	boolean putBoolean(@NonNull String key, boolean value);

	/**
	 * Retrieves the boolean value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to retrieve its associated value.
	 * @return The value associated with the key.
	 * @throws NotInCacheException If there is no value stored in this cache for the requested key.
	 * @see #contains(String)
	 */
	boolean getBoolean(@NonNull String key);

	/**
	 * Evicts a value for the specified <var>key</var> stored in this cache.
	 *
	 * @param key The key for which to evict its associated value.
	 * @return {@code True} if value associated with the key has been evicted, {@code false} if there
	 * was no value stored in this cache for the specified key.
	 */
	boolean evict(@NonNull String key);

	/**
	 * Evicts all values stored in this cache.
	 *
	 * @return The number of values that has been evicted. May be {@code 0} if this cache is empty.
	 */
	int evictAll();

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * An exception that may be used by implementations of {@link SharedPreferencesCache} to indicate
	 * that they have no value stored for a requested key.
	 * <p>
	 * A client that uses a {@link SharedPreferencesCache} to store and retrieve preference values
	 * should always check whether the cache contains the desired value via {@link #contains(String)}
	 * to avoid this exception.
	 *
	 * @author Martin Albedinsky
	 */
	class NotInCacheException extends AndroidRuntimeException {

		/**
		 * Creates a new instance of NotInCacheException for the specified <var>key</var>.
		 *
		 * @param key The key for which to inform that it has no value associated within the cache.
		 */
		public NotInCacheException(@NonNull final String key) {
			super("There is no value associated with the key(" + key + ") in the cache.");
		}
	}
}
