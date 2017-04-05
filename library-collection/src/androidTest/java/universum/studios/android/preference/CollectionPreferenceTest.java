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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import universum.studios.android.test.PreferencesTest;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class CollectionPreferenceTest extends PreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "CollectionPreferenceTest";
	private static final String PREF_KEY = "PREFERENCE.Collection";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have a clean slate before each test.
		mPreferences.edit().remove(PREF_KEY).commit();
	}

	@Test
	public void testInstantiation() {
		final CollectionPreference<String> preference = new CollectionPreference<>(PREF_KEY, String.class, null);
		assertThat(preference.getKey(), is(PREF_KEY));
		assertThat(preference.getDefaultValue(), is(nullValue()));
	}

	@Test
	public void testPutAndGetBooleanCollection() {
		testPutAndGetCollectionTypeOf(Boolean.class);
	}

	@Test
	public void testPutAndGetByteCollection() {
		testPutAndGetCollectionTypeOf(Byte.class);
	}

	@Test
	public void testPutAndGetShortCollection() {
		testPutAndGetCollectionTypeOf(Short.class);
	}

	@Test
	public void testPutAndGetIntegerCollection() {
		testPutAndGetCollectionTypeOf(Integer.class);
	}

	@Test
	public void testPutAndGetFloatCollection() {
		testPutAndGetCollectionTypeOf(Float.class);
	}

	@Test
	public void testPutAndGetLongCollection() {
		testPutAndGetCollectionTypeOf(Long.class);
	}

	@Test
	public void testPutAndGetDoubleCollection() {
		testPutAndGetCollectionTypeOf(Double.class);
	}

	@Test
	public void testPutAndGetEmptyCollection() {
		innerTestPutAndGetCollection(new ArrayList<Integer>(0), Integer.class);
	}

	@Test
	public void testGetNotPersistedCollection() {
		assertThat(CollectionPreference.getFromPreferences(mPreferences, PREF_KEY, null), is(nullValue()));
	}

	@Test
	public void testPutAndGetNullCollection() {
		innerTestPutAndGetCollection(null, null);
	}

	private <T> void testPutAndGetCollectionTypeOf(Class<T> componentType) {
		innerTestPutAndGetCollection(createTestCollectionOf(componentType), componentType);
	}

	private <T> void innerTestPutAndGetCollection(Collection<T> collection, Class<T> componentType) {
		final CollectionPreference<T> preference = new CollectionPreference<>(PREF_KEY, componentType, null);
		preference.updateValue(collection);
		assertThat(preference.onPutIntoPreferences(mPreferences), is(true));
		preference.invalidate();
		assertThat(preference.onGetFromPreferences(mPreferences), is(collection));
	}

	@Test
	public void testPutAndGetUnsupportedCollection() {
		try {
			final Collection<Date> collection = new ArrayList<>(1);
			collection.add(new Date(0));
			assertTrue(CollectionPreference.putIntoPreferences(mPreferences, PREF_KEY, collection, Date.class));
			throw new AssertionError("No exception thrown.");
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("Failed to put collection of(Date) into shared preferences. " +
							"Only collections of primitive types or theirs boxed representations including String are supported.")
			);
		}
		mPreferences.edit().putString(PREF_KEY, "<Date[]>[]").commit();
		try {
			CollectionPreference.getFromPreferences(mPreferences, PREF_KEY, null);
			throw new AssertionError("No exception thrown.");
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("Failed to obtain collection for the key(" + PREF_KEY + ") from shared preferences. " +
							"Only collections of primitive types or theirs boxed representations including String are supported.")
			);
		}
	}

	@Test
	public void testGetCollectionNotPersistedByLibrary1() {
		mPreferences.edit().putString(PREF_KEY, "<String[]>text,text").commit();
		try {
			CollectionPreference.getFromPreferences(mPreferences, PREF_KEY, null);
			throw new AssertionError("No exception thrown.");
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Trying to obtain a collection for the key(" + PREF_KEY + ") from shared preferences not saved by the Preferences library.")
			);
		}
	}

	@Test
	public void testGetCollectionNotPersistedByLibrary2() {
		mPreferences.edit().putString(PREF_KEY, "[text, text, text]").commit();
		try {
			CollectionPreference.getFromPreferences(mPreferences, PREF_KEY, null);
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Trying to obtain a collection for the key(" + PREF_KEY + ") from shared preferences not saved by the Preferences library.")
			);
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@SuppressWarnings("unchecked")
	private static <T> Collection<T> createTestCollectionOf(Class<T> itemClass) {
		final Collection collection = new ArrayList<>();
		if (Boolean.class.equals(itemClass)) {
			collection.add(true);
			collection.add(true);
			collection.add(false);
		} else if (Byte.class.equals(itemClass)) {
			collection.add((byte) 10);
			collection.add((byte) 15);
			collection.add((byte) 124);
			collection.add((byte) 2);
		} else if (Short.class.equals(itemClass)) {
			collection.add((short) 26);
			collection.add((short) 64);
		} else if (Integer.class.equals(itemClass)) {
			collection.add(45);
			collection.add(9784);
			collection.add(15498798);
			collection.add(1);
			collection.add(13241657);
		} else if (Float.class.equals(itemClass)) {
			collection.add(15.2f);
			collection.add(1657.154f);
			collection.add(13487.155614f);
		} else if (Long.class.equals(itemClass)) {
			collection.add(1645L);
			collection.add(1L);
			collection.add(6749841687984L);
		} else if (Double.class.equals(itemClass)) {
			collection.add(15648979465.1634616D);
			collection.add(654777.22D);
		} else if (String.class.equals(itemClass)) {
			collection.add("text text");
			collection.add("text text text text");
			collection.add("text text");
			collection.add("text text text");
		}
		return (Collection<T>) collection;
	}
}
