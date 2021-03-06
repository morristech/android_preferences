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
package universum.studios.android.test;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Class providing simple utility methods for tests.
 *
 * @author Martin Albedinsky
 */
public final class TestUtils {

	/**
	 * Name of the library's root package.
	 */
	private static final String LIBRARY_ROOT_PACKAGE_NAME = "universum.studios.android.preference.test";

	/**
	 */
	private TestUtils() {
		// Not allowed to be instantiated publicly.
	}

	/**
	 * Checks whether the given <var>context</var> has package name equal to the root test package
	 * name of the library.
	 *
	 * @param context The context of which package name to check.
	 * @return {@code True} if the context's package name is the same as the library's root one,
	 * {@code false} otherwise.
	 * @see #isLibraryRootTestPackageName(String)
	 */
	public static boolean hasLibraryRootTestPackageName(@NonNull Context context) {
		return isLibraryRootTestPackageName(context.getPackageName());
	}

	/**
	 * Checks whether the given <var>packageName</var> is equal to the root test package name of
	 * the library.
	 * <p>
	 * <b>Note</b>, that this method will return {@code false} also for library's subpackages.
	 *
	 * @param packageName The package name to check if it is the library's root one.
	 * @return {@code True} if the package name is the same as the library's root one,
	 * {@code false} otherwise.
	 */
	public static boolean isLibraryRootTestPackageName(@NonNull String packageName) {
		return LIBRARY_ROOT_PACKAGE_NAME.equals(packageName);
	}

	/**
	 * Creates a new {@link Collection} with contents of the given <var>array</var>.
	 *
	 * @param array The desired array from which to create a new collection.
	 * @return Mutable collection with the array's contents.
	 */
	@NonNull
	public static Collection<String> mutableCollectionFrom(@NonNull String[] array) {
		final Collection<String> collection = new ArrayList<>(array.length);
		Collections.addAll(collection, array);
		return collection;
	}
}
