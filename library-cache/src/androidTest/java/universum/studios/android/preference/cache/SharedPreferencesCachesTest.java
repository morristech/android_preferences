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
package universum.studios.android.preference.cache;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SharedPreferencesCachesTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "SharedPreferencesCachesTest";

	@Test
	public void testMapCache() {
		// Only test that the factory provides valid instance as the MapCache implementation should
		// be tested by MapCacheTest.
		assertThat(SharedPreferencesCaches.mapCache(), is(not(nullValue())));
	}
}