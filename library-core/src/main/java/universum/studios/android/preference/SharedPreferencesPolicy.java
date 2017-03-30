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

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Simple class that declares basic policies for {@code SharedPreferences}.
 *
 * @author Martin Albedinsky
 */
public final class SharedPreferencesPolicy {

	/**
	 * Copied flag from {@link Context#MODE_PRIVATE}.
	 */
	public static final int FILE_MODE_PRIVATE = Context.MODE_PRIVATE;

	/**
	 * Copied flag from {@link Context#MODE_APPEND}.
	 */
	public static final int FILE_MODE_APPEND = Context.MODE_APPEND;

	/**
	 * Defines an annotation for determining allowed file creation modes for {@code SharedPreferences}
	 * file.
	 */
	@IntDef(flag = true, value = {
			FILE_MODE_PRIVATE,
			FILE_MODE_APPEND
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface FileMode {
	}

	/**
	 */
	private SharedPreferencesPolicy() {
		// Creation of instances of this class is not publicly allowed.
	}
}
