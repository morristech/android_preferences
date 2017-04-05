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
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Simple class that declares basic policies for {@code SharedPreferences}.
 *
 * @author Martin Albedinsky
 */
public final class SharedPreferencesPolicy {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Suffix for default name used by the <b>Android</b> for shared preferences.
	 */
	public static final String DEFAULT_PREFERENCES_NAME_SUFFIX = "_preferences";

	/**
	 * Copied flag from {@link Context#MODE_PRIVATE}.
	 */
	public static final int MODE_PRIVATE = Context.MODE_PRIVATE;

	/**
	 * Copied flag from {@link Context#MODE_PRIVATE}.
	 *
	 * @deprecated Use {@link #MODE_PRIVATE} instead.
	 */
	@Deprecated
	public static final int FILE_MODE_PRIVATE = MODE_PRIVATE;

	/**
	 * Copied flag from {@link Context#MODE_APPEND}.
	 */
	public static final int MODE_APPEND = Context.MODE_APPEND;

	/**
	 * Copied flag from {@link Context#MODE_APPEND}.
	 *
	 * @deprecated Use {@link #MODE_APPEND} instead.
	 */
	@Deprecated
	public static final int FILE_MODE_APPEND = MODE_APPEND;

	/**
	 * Defines an annotation for determining allowed file creation modes for {@code SharedPreferences}
	 * file.
	 */
	@IntDef(flag = true, value = {
			MODE_PRIVATE,
			MODE_APPEND
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Mode {
	}

	/**
	 * Defines an annotation for determining allowed file creation modes for {@code SharedPreferences}
	 * file.
	 *
	 * @deprecated Use {@link Mode} instead.
	 */
	@Deprecated
	@IntDef(flag = true, value = {
			FILE_MODE_PRIVATE,
			FILE_MODE_APPEND
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface FileMode {
	}

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 */
	private SharedPreferencesPolicy() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a default name for shared preferences for the specified <var>context</var> that is
	 * the same one as created by default by the <b>Android</b> framework.
	 *
	 * @param context The context for which to create preferences name.
	 * @return Preferences name that may be used in association with {@link Context#getSharedPreferences(String, int)}.
	 * @see #preferencesName(Context, String)
	 * @see PreferenceManager#getDefaultSharedPreferencesName(Context)
	 */
	public static String defaultPreferencesName(@NonNull final Context context) {
		return preferencesName(context, DEFAULT_PREFERENCES_NAME_SUFFIX);
	}

	/**
	 * Creates a name for shared preferences with the given <var>nameSuffix</var> for the specified
	 * <var>context</var>.
	 *
	 * @param context    The context for which to create preferences name.
	 * @param nameSuffix Suffix to be added into preferences name.
	 * @return Preferences name that may be used in association with {@link Context#getSharedPreferences(String, int)}.
	 * @see #defaultPreferencesName(Context)
	 */
	public static String preferencesName(@NonNull final Context context, @NonNull final String nameSuffix) {
		return context.getPackageName() + nameSuffix;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
