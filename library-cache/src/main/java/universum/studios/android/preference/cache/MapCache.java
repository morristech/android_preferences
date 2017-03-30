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
 * Class that todo:
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
	 * todo:
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
		// todo:
		return false;
	}

	/**
	 */
	@Nullable
	@Override
	public String getString(@NonNull String key, @Nullable String defValue) {
		// todo:
		return null;
	}

	/**
	 */
	@Override
	public boolean putStringSet(@NonNull String key, @Nullable Set<String> values) {
		// todo:
		return false;
	}

	/**
	 */
	@Nullable
	@Override
	public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defValues) {
		// todo:
		return null;
	}

	/**
	 */
	@Override
	public boolean putInt(@NonNull String key, int value) {
		// todo:
		return false;
	}

	/**
	 */
	@Override
	public int getInt(@NonNull String key, int defValue) {
		// todo:
		return 0;
	}

	/**
	 */
	@Override
	public boolean putFloat(@NonNull String key, float value) {
		// todo:
		return false;
	}

	/**
	 */
	@Override
	public float getFloat(@NonNull String key, float defValue) {
		// todo:
		return 0;
	}

	/**
	 */
	@Override
	public boolean putLong(@NonNull String key, long value) {
		// todo:
		return false;
	}

	/**
	 */
	@Override
	public long getLong(@NonNull String key, long defValue) {
		// todo:
		return 0;
	}

	/**
	 */
	@Override
	public boolean putBoolean(@NonNull String key, boolean value) {
		// todo:
		return false;
	}

	/**
	 */
	@Override
	public boolean getBoolean(@NonNull String key, boolean defValue) {
		// todo:
		return false;
	}

	/**
	 */
	@Override
	public boolean remove(@NonNull String key) {
		return mMap.remove(key) != null;
	}

	/**
	 */
	@Override
	public int removeAll() {
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
