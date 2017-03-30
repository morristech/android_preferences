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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Simple interface for factories that may be used to create instances of {@link SharedPreferences}.
 *
 * @author Martin Albedinsky
 */
public interface SharedPreferencesFactory {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * A {@link SharedPreferencesFactory} which may be used to create default shared preferences.
	 *
	 * @see PreferenceManager#getDefaultSharedPreferences(Context)
	 */
	SharedPreferencesFactory DEFAULT = new SharedPreferencesFactory() {

		/**
		 */
		@NonNull
		@Override
		public SharedPreferences createPreferences(@NonNull Context context) {
			return PreferenceManager.getDefaultSharedPreferences(context);
		}
	};

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of SharedPreferences for the given <var>context</var>.
	 *
	 * @param context Context used to create shared preferences.
	 * @return Instance of SharedPreferences with name and file creation mode specific for implementation
	 * of this factory.
	 * @see Context#getSharedPreferences(String, int)
	 */
	@NonNull
	SharedPreferences createPreferences(@NonNull Context context);

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A simple implementation of {@link SharedPreferencesFactory} which may be used to create
	 * instances of {@link SharedPreferences} with a desired name and file creation mode via either
	 * {@link #SimpleFactory(String)} or {@link #SimpleFactory(String, int)}.
	 *
	 * @author Martin Albedinsky
	 */
	class SimpleFactory implements SharedPreferencesFactory {

		/**
		 * Name for the preferences file.
		 */
		private final String fileName;

		/**
		 * File creation mode for the preferences file.
		 */
		private final int fileMode;

		/**
		 * Creates a new instance of SimpleFactory with the specified <var>fileName</var> and
		 * {@link SharedPreferencesPolicy#FILE_MODE_PRIVATE)} as file mode for preferences file.
		 *
		 * @param fileName The desired name for preferences file.
		 * @see #SimpleFactory(String, int)
		 */
		public SimpleFactory(@NonNull String fileName) {
			this(fileName, SharedPreferencesPolicy.FILE_MODE_PRIVATE);
		}

		/**
		 * Creates a new instance of SimpleFactory with the specified <var>fileName</var> and
		 * <var>fileMode</var> for preferences file.
		 *
		 * @param fileName The desired name for preferences file.
		 * @param fileMode The desired creation mode for preferences file.
		 */
		public SimpleFactory(@NonNull String fileName, @SharedPreferencesPolicy.FileMode int fileMode) {
			this.fileName = fileName;
			this.fileMode = fileMode;
		}

		/**
		 */
		@NonNull
		@Override
		public SharedPreferences createPreferences(@NonNull Context context) {
			return context.getSharedPreferences(fileName, fileMode);
		}
	}
}