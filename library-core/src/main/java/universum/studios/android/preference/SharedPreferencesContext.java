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
import android.support.annotation.Nullable;

/**
 * Interface for contexts that depend on {@link SharedPreferences}.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesContext {

	/**
	 * Sets an instance of shared preferences to be used by this context.
	 *
	 * @param preferences The desired instance of preferences to be used. May be {@code null} to no
	 *                    use shared preferences.
	 * @see #getSharedPreferences()
	 */
	void setSharedPreferences(@Nullable SharedPreferences preferences);

	/**
	 * Returns the shared preferences instance used by this context.
	 *
	 * @return The associated shared preferences. May be {@code null} if no preferences has been
	 * associated with this context.
	 * @see #setSharedPreferences(SharedPreferences)
	 */
	@Nullable
	SharedPreferences getSharedPreferences();
}
