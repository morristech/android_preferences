/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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

import android.util.Log;

/**
 * <b>This class has been deprecated and will be removed in the next none-beta release.</b>
 * <p>
 * Configuration options for the Preferences library.
 *
 * @author Martin Albedinsky
 * @deprecated To control library logs use {@link PreferencesLogging} utility class.
 */
@Deprecated
public final class PreferencesConfig {

	/**
	 * Flag indicating whether the <b>verbose</b> output for the Preferences library trough log-cat
	 * is enabled or not.
	 *
	 * @see Log#v(String, String)
	 */
	public static boolean LOG_ENABLED = true;

	/**
	 * Flag indicating whether the <b>debug</b> output for the Preferences library trough log-cat is
	 * enabled or not.
	 *
	 * @see Log#d(String, String)
	 */
	public static boolean DEBUG_LOG_ENABLED = false;

	/**
	 */
	private PreferencesConfig() {
		// Not allowed to be instantiated publicly.
	}
}
