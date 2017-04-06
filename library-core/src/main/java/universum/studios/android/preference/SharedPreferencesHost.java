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

/**
 * <b>This interface has been deprecated and will be removed in the next release.</b>
 * <p>
 * Interface for simple host which provides access to {@link SharedPreferences} for specified
 * {@link #setSharedPreferencesName(String) name} and {@link #setSharedPreferencesMode(int) mode}.
 * Instance of the corresponding shared preferences may be obtained via {@link #getSharedPreferences()}.
 *
 * @author Martin Albedinsky
 * @deprecated Use {@link SharedPreferencesProvider} instead.
 */
@Deprecated
public interface SharedPreferencesHost extends SharedPreferencesProvider {
}
