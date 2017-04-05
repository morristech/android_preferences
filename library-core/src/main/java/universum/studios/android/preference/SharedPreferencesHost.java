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

/**
 * Interface for simple host which provides access to {@link SharedPreferences} for specified
 * {@link #setSharedPreferencesName(String) name} and {@link #setSharedPreferencesMode(int) mode}.
 * Instance of the corresponding shared preferences may be obtained via {@link #getSharedPreferences()}.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesHost {

	/**
	 * Sets a name for preferences file that is used by this host to access instance of
	 * {@link SharedPreferences} via {@link android.content.Context#getSharedPreferences(String, int)}.
	 *
	 * @param name The desired preferences name. May be {@code null} if this host should use default
	 *             name provide by the framework.
	 * @see #getSharedPreferencesName()
	 * @see #setSharedPreferencesMode(int)
	 * @see #getSharedPreferences()
	 */
	void setSharedPreferencesName(@Nullable String name);

	/**
	 * Returns the name for preferences file specified for this host.
	 * <p>
	 * Default value: {@link android.preference.PreferenceManager#getDefaultSharedPreferencesName(android.content.Context)
	 * PreferenceManager#getDefaultSharedPreferencesName(Context)}
	 *
	 * @return Preferences name. If no name has been specified a default one will be returned.
	 * @see #setSharedPreferencesName(String)
	 * @see #getSharedPreferencesMode()
	 * @see #getSharedPreferences()
	 */
	@NonNull
	String getSharedPreferencesName();

	/**
	 * Sets a mode for preferences file that is used by this host to access instance of
	 * {@link SharedPreferences} via {@link android.content.Context#getSharedPreferences(String, int) Context.getSharedPreferences(String, int)}.
	 *
	 * @param mode The desired preferences mode.
	 * @see #getSharedPreferencesMode()
	 * @see #setSharedPreferencesName(String)
	 * @see #getSharedPreferences()
	 */
	void setSharedPreferencesMode(@SharedPreferencesPolicy.Mode int mode);

	/**
	 * Returns the mode for preferences file specified for this host.
	 * <p>
	 * Default value: {@link SharedPreferencesPolicy#MODE_PRIVATE MODE_PRIVATE}
	 *
	 * @return Preferences mode. If no mode has been specified a default one will be returned.
	 * @see #setSharedPreferencesMode(int)
	 * @see #getSharedPreferencesName()
	 * @see #getSharedPreferences()
	 */
	@SharedPreferencesPolicy.Mode
	int getSharedPreferencesMode();

	/**
	 * Returns the instance of SharedPreferences managed by this host.
	 *
	 * @return The associated shared preferences.
	 * @see #setSharedPreferencesName(String)
	 * @see #setSharedPreferencesMode(int)
	 */
	@NonNull
	SharedPreferences getSharedPreferences();
}
