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
package universum.studios.android.preference.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import universum.studios.android.preference.SharedPreferencesCache;

/**
 * A {@link SharedPreferencesCache} implementation backed by implementation of {@link Map}.
 *
 * @author Martin Albedinsky
 */
final class MapCache implements SharedPreferencesCache {

    /*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "MapCache";

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
	 * Map used to store all values available through this cache.
	 */
	private final Map<String, Object> mMap = new HashMap<>();
	 
	/*
	 * Constructors ================================================================================
	 */
	 
	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public boolean isEmpty() {
		return mMap.isEmpty();
	}

	/**
	 */
	@Override
	public boolean contains(@NonNull String key) {
		return mMap.containsKey(key);
	}

	/**
	 */
	@Override
	public boolean putString(@NonNull String key, @Nullable String value) {
		mMap.put(key, value);
		return true;
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull String key) {
		assertContainsOrThrow(key);
		return (String) mMap.get(key);
	}

	/**
	 */
	@Override
	public boolean putStringSet(@NonNull String key, @Nullable Set<String> values) {
		mMap.put(key, values);
		return true;
	}

	/**
	 */
	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public Set<String> getStringSet(@NonNull String key) {
		assertContainsOrThrow(key);
		return (Set<String>) mMap.get(key);
	}

	/**
	 */
	@Override
	public boolean putInt(@NonNull String key, int value) {
		mMap.put(key, value);
		return true;
	}

	/**
	 */
	@Override
	public int getInt(@NonNull String key) {
		assertContainsOrThrow(key);
		return (int) mMap.get(key);
	}

	/**
	 */
	@Override
	public boolean putFloat(@NonNull String key, float value) {
		mMap.put(key, value);
		return true;
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull String key) {
		assertContainsOrThrow(key);
		return (float) mMap.get(key);
	}

	/**
	 */
	@Override
	public boolean putLong(@NonNull String key, long value) {
		mMap.put(key, value);
		return true;
	}

	/**
	 */
	@Override
	public long getLong(@NonNull String key) {
		assertContainsOrThrow(key);
		return (long) mMap.get(key);
	}

	/**
	 */
	@Override
	public boolean putBoolean(@NonNull String key, boolean value) {
		mMap.put(key, value);
		return true;
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull String key) {
		assertContainsOrThrow(key);
		return (boolean) mMap.get(key);
	}

	/**
	 * Asserts that this cache contains value for the specified <var>key</var>. If there is no value
	 * for the key stored in this cache, a new {@link NotInCacheException} is thrown.
	 *
	 * @param key The of which value's presence to check.
	 */
	private void assertContainsOrThrow(String key) {
		if (!mMap.containsKey(key)) throw new NotInCacheException(key);
	}

	/**
	 */
	@Override
	public boolean evict(@NonNull String key) {
		return mMap.remove(key) != null;
	}

	/**
	 */
	@Override
	public int evictAll() {
		final int size = mMap.size();
		if (size > 0) {
			mMap.clear();
		}
		return size;
	}
	 
	/*
	 * Inner classes ===============================================================================
	 */
}
