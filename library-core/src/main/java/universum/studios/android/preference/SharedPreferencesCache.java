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

/**
 * Interface declaring layer for caches that may be used to store values of {@code SharedPreferences}
 * in a memory.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesCache extends SharedPreferencesFacade {

	/**
	 * Checks whether this cache is empty or not.
	 *
	 * @return {@code True} if this cache does not have any values stored, {@code false} if there
	 * is at least one value stored.
	 */
	boolean isEmpty();

	/**
	 * Removes all values stored in this cache.
	 *
	 * @return The number of values that has been removed. May be {@code 0} if this cache is empty.
	 */
	int removeAll();
}
