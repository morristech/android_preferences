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

import universum.studios.android.preference.SharedPreferencesCache;

/**
 * Factory that provides common implementations of {@link SharedPreferencesCache}.
 *
 * <h3>Provided caches</h3>
 * <ul>
 * <li>{@link #mapCache()}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 */
public final class SharedPreferencesCaches {

	/**
	 */
	private SharedPreferencesCaches() {
		// Creation of instances of this class is not publicly allowed.
	}

	/**
	 * Returns a new instance of {@link SharedPreferencesCache} that is backed by {@link java.util.Map Map}.
	 *
	 * @return Preferences cache ready to be used.
	 */
	@NonNull
	public static SharedPreferencesCache mapCache() {
		return new MapCache();
	}
}
