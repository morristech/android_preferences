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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import universum.studios.android.preference.SharedPreferencesCache;
import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class MapCacheTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "MapCacheTest";

	private static final String PREF_KEY_STRING = "PREFERENCE.Key.String";
	private static final String PREF_KEY_STRING_SET = "PREFERENCE.Key.StringSet";
	private static final String PREF_KEY_INTEGER = "PREFERENCE.Key.Integer";
	private static final String PREF_KEY_FLOAT = "PREFERENCE.Key.Float";
	private static final String PREF_KEY_LONG = "PREFERENCE.Key.Long";
	private static final String PREF_KEY_BOOLEAN = "PREFERENCE.Key.Boolean";

	private static final String[] PREF_KEYS = {
			PREF_KEY_STRING,
			PREF_KEY_STRING_SET,
			PREF_KEY_INTEGER,
			PREF_KEY_FLOAT,
			PREF_KEY_LONG,
			PREF_KEY_BOOLEAN
	};

	private SharedPreferencesCache mCache;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mCache = new MapCache();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mCache = null;
	}

	@Test
	public void testIsEmpty() {
		assertThat(mCache.isEmpty(), is(true));
		mCache.putBoolean(PREF_KEY_BOOLEAN, false);
		assertThat(mCache.isEmpty(), is(false));
		mCache.evictAll();
		assertThat(mCache.isEmpty(), is(true));
	}

	@Test
	public void testContains() {
		// Test continuous addition of values into cache and that they really are stored in the cache.
		final Collection<String> exceptKeys = TestUtils.mutableCollectionFrom(PREF_KEYS);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
		// Round #1:
		assertThat(mCache.putString(PREF_KEY_STRING, "pref.value"), is(true));
		exceptKeys.remove(PREF_KEY_STRING);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
		// Round #2:
		assertThat(mCache.putStringSet(PREF_KEY_STRING_SET, new HashSet<String>(0)), is(true));
		exceptKeys.remove(PREF_KEY_STRING_SET);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
		// Round #3:
		assertThat(mCache.putInt(PREF_KEY_INTEGER, 11), is(true));
		exceptKeys.remove(PREF_KEY_INTEGER);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
		// Round #4:
		assertThat(mCache.putFloat(PREF_KEY_FLOAT, 0.25f), is(true));
		exceptKeys.remove(PREF_KEY_FLOAT);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
		// Round #5:
		assertThat(mCache.putLong(PREF_KEY_LONG, 100L), is(true));
		exceptKeys.remove(PREF_KEY_LONG);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
		// Round #6:
		assertThat(mCache.putBoolean(PREF_KEY_BOOLEAN, true), is(true));
		exceptKeys.remove(PREF_KEY_BOOLEAN);
		assertThatCacheContainsValueForKeysExcept(exceptKeys);
	}

	private void assertThatCacheContainsValueForKeysExcept(Collection<String> exceptKeys) {
		for (final String prefKey : PREF_KEYS) {
			if (exceptKeys.contains(prefKey)) {
				continue;
			}
			assertThat("No value for key(" + prefKey + ") is stored in the cache!", mCache.contains(prefKey), is(true));
		}
	}

	@Test
	public void testContainsOnEmptyCache() {
		for (final String PREF_KEY : PREF_KEYS) {
			assertThat(mCache.contains(PREF_KEY), is(false));
		}
	}

	@Test
	public void testPutGetString() {
		assertThat(mCache.putString(PREF_KEY_STRING, "pref.value"), is(true));
		assertThat(mCache.getString(PREF_KEY_STRING), is("pref.value"));
		assertThat(mCache.putString(PREF_KEY_STRING, "pref.value"), is(true));
		assertThat(mCache.getString(PREF_KEY_STRING), is("pref.value"));
		// Test that putting value for another key into cache does not change value for the current key.
		for (int i = 0; i < 10; i++) {
			assertThat(mCache.putString(PREF_KEY_STRING + "." + i, "pref.value." + i), is(true));
		}
		assertThat(mCache.getString(PREF_KEY_STRING), is("pref.value"));
	}

	@Test(expected = SharedPreferencesCache.NotInCacheException.class)
	public void testPutGetStringWithoutStoredValue() {
		mCache.getBoolean(PREF_KEY_STRING);
	}

	@Test
	public void testPutGetStringSet() {
		final Set<String> prefValue1 = new HashSet<>(0);
		final Set<String> prefValue2 = new HashSet<>(1);
		prefValue2.add("pref.value");
		assertThat(mCache.putStringSet(PREF_KEY_STRING_SET, prefValue1), is(true));
		assertThat(mCache.getStringSet(PREF_KEY_STRING_SET), is(prefValue1));
		assertThat(mCache.putStringSet(PREF_KEY_STRING_SET, prefValue2), is(true));
		assertThat(mCache.getStringSet(PREF_KEY_STRING_SET), is(prefValue2));
		// Test that putting value for another key into cache does not change value for the current key.
		for (int i = 0; i < 10; i++) {
			assertThat(mCache.putStringSet(PREF_KEY_STRING_SET + "." + i, null), is(true));
		}
		assertThat(mCache.getStringSet(PREF_KEY_STRING_SET), is(prefValue2));
	}

	@Test(expected = SharedPreferencesCache.NotInCacheException.class)
	public void testPutGetStringSetWithoutStoredValue() {
		mCache.getBoolean(PREF_KEY_STRING_SET);
	}

	@Test
	public void testPutGetInt() {
		assertThat(mCache.putInt(PREF_KEY_INTEGER, 14), is(true));
		assertThat(mCache.getInt(PREF_KEY_INTEGER), is(14));
		assertThat(mCache.putInt(PREF_KEY_INTEGER, 99), is(true));
		assertThat(mCache.getInt(PREF_KEY_INTEGER), is(99));
		// Test that putting value for another key into cache does not change value for the current key.
		for (int i = 0; i < 10; i++) {
			assertThat(mCache.putInt(PREF_KEY_INTEGER + "." + i, -100 - i), is(true));
		}
		assertThat(mCache.getInt(PREF_KEY_INTEGER), is(99));
	}

	@Test(expected = SharedPreferencesCache.NotInCacheException.class)
	public void testPutGetIntWithoutStoredValue() {
		mCache.getBoolean(PREF_KEY_INTEGER);
	}

	@Test
	public void testPutGetFloat() {
		assertThat(mCache.putFloat(PREF_KEY_FLOAT, 0.5f), is(true));
		assertThat(mCache.getFloat(PREF_KEY_FLOAT), is(0.5f));
		assertThat(mCache.putFloat(PREF_KEY_FLOAT, 1.5f), is(true));
		assertThat(mCache.getFloat(PREF_KEY_FLOAT), is(1.5f));
		// Test that putting value for another key into cache does not change value for the current key.
		for (int i = 0; i < 10; i++) {
			assertThat(mCache.putFloat(PREF_KEY_FLOAT + "." + i, -0.5f - i), is(true));
		}
		assertThat(mCache.getFloat(PREF_KEY_FLOAT), is(1.5f));
	}

	@Test(expected = SharedPreferencesCache.NotInCacheException.class)
	public void testPutGetFloatWithoutStoredValue() {
		mCache.getBoolean(PREF_KEY_FLOAT);
	}

	@Test
	public void testPutGetLong() {
		assertThat(mCache.putLong(PREF_KEY_LONG, 1000L), is(true));
		assertThat(mCache.getLong(PREF_KEY_LONG), is(1000L));
		assertThat(mCache.putLong(PREF_KEY_LONG, 2000L), is(true));
		assertThat(mCache.getLong(PREF_KEY_LONG), is(2000L));
		// Test that putting value for another key into cache does not change value for the current key.
		for (int i = 0; i < 10; i++) {
			assertThat(mCache.putLong(PREF_KEY_LONG + "." + i, -1000L - i), is(true));
		}
		assertThat(mCache.getLong(PREF_KEY_LONG), is(2000L));
	}

	@Test(expected = SharedPreferencesCache.NotInCacheException.class)
	public void testPutGetLongWithoutStoredValue() {
		mCache.getLong(PREF_KEY_LONG);
	}

	@Test
	public void testPutGetBoolean() {
		assertThat(mCache.putBoolean(PREF_KEY_BOOLEAN, false), is(true));
		assertThat(mCache.getBoolean(PREF_KEY_BOOLEAN), is(false));
		assertThat(mCache.putBoolean(PREF_KEY_BOOLEAN, true), is(true));
		assertThat(mCache.getBoolean(PREF_KEY_BOOLEAN), is(true));
		// Test that putting value for another key into cache does not change value for the current key.
		for (int i = 0; i < 10; i++) {
			assertThat(mCache.putBoolean(PREF_KEY_BOOLEAN + "." + i, false), is(true));
		}
		assertThat(mCache.getBoolean(PREF_KEY_BOOLEAN), is(true));
	}

	@Test(expected = SharedPreferencesCache.NotInCacheException.class)
	public void testPutGetBooleanWithoutStoredValue() {
		mCache.getBoolean(PREF_KEY_BOOLEAN);
	}

	@Test
	public void testEvict() {
		// Test that any value put into cache may be also evicted.
		mCache.putString(PREF_KEY_STRING, "pref.value");
		assertThat(mCache.evict(PREF_KEY_STRING), is(true));
		assertThat(mCache.contains(PREF_KEY_STRING), is(false));
		mCache.putStringSet(PREF_KEY_STRING_SET, null);
		assertThat(mCache.evict(PREF_KEY_STRING_SET), is(true));
		assertThat(mCache.contains(PREF_KEY_STRING_SET), is(false));
		mCache.putInt(PREF_KEY_INTEGER, 1);
		assertThat(mCache.evict(PREF_KEY_INTEGER), is(true));
		assertThat(mCache.contains(PREF_KEY_INTEGER), is(false));
		mCache.putFloat(PREF_KEY_FLOAT, 0.5f);
		assertThat(mCache.evict(PREF_KEY_FLOAT), is(true));
		assertThat(mCache.contains(PREF_KEY_FLOAT), is(false));
		mCache.putLong(PREF_KEY_LONG, 1000L);
		assertThat(mCache.evict(PREF_KEY_LONG), is(true));
		assertThat(mCache.contains(PREF_KEY_LONG), is(false));
		mCache.putBoolean(PREF_KEY_BOOLEAN, false);
		assertThat(mCache.evict(PREF_KEY_BOOLEAN), is(true));
		assertThat(mCache.contains(PREF_KEY_BOOLEAN), is(false));
	}

	@Test
	public void testEvictOnEmptyCache() {
		assertThat(mCache.evict(PREF_KEY_BOOLEAN), is(false));
		mCache.putBoolean(PREF_KEY_BOOLEAN, true);
		mCache.evict(PREF_KEY_BOOLEAN);
		assertThat(mCache.evict(PREF_KEY_BOOLEAN), is(false));
	}

	@Test
	public void testEvictAll() {
		// Test that any value put into cache may be also evicted.
		mCache.putString(PREF_KEY_STRING, "pref.value");
		assertThat(mCache.evictAll(), is(1));
		assertThat(mCache.contains(PREF_KEY_STRING), is(false));
		mCache.putStringSet(PREF_KEY_STRING_SET, null);
		assertThat(mCache.evictAll(), is(1));
		assertThat(mCache.contains(PREF_KEY_STRING_SET), is(false));
		mCache.putInt(PREF_KEY_INTEGER, 1);
		assertThat(mCache.evictAll(), is(1));
		assertThat(mCache.contains(PREF_KEY_INTEGER), is(false));
		mCache.putFloat(PREF_KEY_FLOAT, 0.5f);
		assertThat(mCache.evictAll(), is(1));
		assertThat(mCache.contains(PREF_KEY_FLOAT), is(false));
		mCache.putLong(PREF_KEY_LONG, 1000L);
		assertThat(mCache.evictAll(), is(1));
		assertThat(mCache.contains(PREF_KEY_LONG), is(false));
		assertThat(mCache.putBoolean(PREF_KEY_BOOLEAN, false), is(true));
		assertThat(mCache.evictAll(), is(1));
		assertThat(mCache.contains(PREF_KEY_BOOLEAN), is(false));
	}

	@Test
	public void testEvictAllOnEmptyCache() {
		assertThat(mCache.evictAll(), is(0));
		assertThat(mCache.putBoolean(PREF_KEY_BOOLEAN, true), is(true));
		assertThat(mCache.evict(PREF_KEY_BOOLEAN), is(true));
		assertThat(mCache.evictAll(), is(0));
	}
}
