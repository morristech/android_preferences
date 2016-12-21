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
package universum.studios.android.preference.inner;

import android.support.annotation.NonNull;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;

/**
 * @author Martin Albedinsky
 */
public abstract class BaseTest {

	@SuppressWarnings("unused")
	private static final String TAG = "BaseTest";

	public static final long WAIT_MAX_DURATION = 10000;

	@Before
	public void beforeTest() throws Exception {
	}

	@After
	public void afterTest() throws Exception {
	}

	public static void waitUntil(@NonNull Object item, @NonNull Matcher matches) throws InterruptedException {
		waitUntil(item, matches, WAIT_MAX_DURATION);
	}

	public static void waitUntil(@NonNull Object item, @NonNull Matcher matcher, long maxDuration) throws InterruptedException {
		long duration = 0;
		while (duration < maxDuration) {
			if (matcher.matches(item)) break;
			Thread.sleep(100);
			duration += 100;
		}
	}
}
