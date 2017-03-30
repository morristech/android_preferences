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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import universum.studios.android.test.PreferencesTest;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */

public final class ListPreferenceTest extends PreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "ListPreferenceTest";
	private static final String PREF_KEY = "PREFERENCE.List";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have a clean slate before each test.
		mPreferences.edit().remove(PREF_KEY).commit();
	}

	@Test
	public void testInstantiation() {
		final ListPreference<String> preference = new ListPreference<>(PREF_KEY, String.class, null);
		assertThat(preference.getKey(), is(PREF_KEY));
		assertThat(preference.getDefaultValue(), is(nullValue()));
	}

	@Test
	public void testPutAndGetBooleanList() {
		testPutAndGetListTypeOf(Boolean.class);
	}

	@Test
	public void testPutAndGetByteList() {
		testPutAndGetListTypeOf(Byte.class);
	}

	@Test
	public void testPutAndGetShortList() {
		testPutAndGetListTypeOf(Short.class);
	}

	@Test
	public void testPutAndGetIntegerList() {
		testPutAndGetListTypeOf(Integer.class);
	}

	@Test
	public void testPutAndGetFloatList() {
		testPutAndGetListTypeOf(Float.class);
	}

	@Test
	public void testPutAndGetLongList() {
		testPutAndGetListTypeOf(Long.class);
	}

	@Test
	public void testPutAndGetDoubleList() {
		testPutAndGetListTypeOf(Double.class);
	}

	@Test
	public void testPutAndGetEmptyList() {
		innerTestPutAndGetList(new ArrayList<Integer>(0), Integer.class);
	}

	@Test
	public void testGetNotPersistedList() {
		assertThat(ListPreference.getFromPreferences(mPreferences, PREF_KEY, null), is(nullValue()));
	}

	@Test
	public void testPutAndGetNullList() {
		innerTestPutAndGetList(null, null);
	}

	private <T> void testPutAndGetListTypeOf(Class<T> componentType) {
		innerTestPutAndGetList(createTestableListOf(componentType), componentType);
	}

	private <T> void innerTestPutAndGetList(List<T> list, Class<T> componentType) {
		final ListPreference<T> preference = new ListPreference<>(PREF_KEY, componentType, null);
		preference.updateValue(list);
		assertThat(preference.onPutIntoPreferences(mPreferences), is(true));
		preference.clear();
		assertThat(preference.onGetFromPreferences(mPreferences), is(list));
	}

	@Test
	public void testPutAndGetUnsupportedList() {
		try {
			final List<Date> list = new ArrayList<>(1);
			list.add(new Date(0));
			assertTrue(ListPreference.putIntoPreferences(mPreferences, PREF_KEY, list, Date.class));
			throw new AssertionError("No exception thrown.");
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("Failed to put list of(Date) into shared preferences. " +
							"Only lists of primitive types or theirs boxed representations including String are supported.")
			);
		}
		mPreferences.edit().putString(PREF_KEY, "<Date[]>[]").commit();
		try {
			ListPreference.getFromPreferences(mPreferences, PREF_KEY, null);
			throw new AssertionError("No exception thrown.");
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("Failed to obtain list for the key(" + PREF_KEY + ") from shared preferences. " +
							"Only lists of primitive types or theirs boxed representations including String are supported.")
			);
		}
	}

	@Test
	public void testGetListNotPersistedByLibrary1() {
		mPreferences.edit().putString(PREF_KEY, "<String[]>text,text").commit();
		try {
			ListPreference.getFromPreferences(mPreferences, PREF_KEY, null);
			throw new AssertionError("No exception thrown.");
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Trying to obtain a list for the key(" + PREF_KEY + ") from shared preferences not saved by the Preferences library.")
			);
		}
	}

	@Test
	public void testGetListNotPersistedByLibrary2() {
		mPreferences.edit().putString(PREF_KEY, "[text, text, text]").commit();
		try {
			ListPreference.getFromPreferences(mPreferences, PREF_KEY, null);
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Trying to obtain a list for the key(" + PREF_KEY + ") from shared preferences not saved by the Preferences library.")
			);
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> createTestableListOf(Class<T> itemClass) {
		final List list = new ArrayList<>();
		if (Boolean.class.equals(itemClass)) {
			list.add(true);
			list.add(true);
			list.add(false);
		} else if (Byte.class.equals(itemClass)) {
			list.add((byte) 10);
			list.add((byte) 15);
			list.add((byte) 124);
			list.add((byte) 2);
		} else if (Short.class.equals(itemClass)) {
			list.add((short) 26);
			list.add((short) 64);
		} else if (Integer.class.equals(itemClass)) {
			list.add(45);
			list.add(9784);
			list.add(15498798);
			list.add(1);
			list.add(13241657);
		} else if (Float.class.equals(itemClass)) {
			list.add(15.2f);
			list.add(1657.154f);
			list.add(13487.155614f);
		} else if (Long.class.equals(itemClass)) {
			list.add(1645L);
			list.add(1L);
			list.add(6749841687984L);
		} else if (Double.class.equals(itemClass)) {
			list.add(15648979465.1634616D);
			list.add(654777.22D);
		} else if (String.class.equals(itemClass)) {
			list.add("text text");
			list.add("text text text text");
			list.add("text text");
			list.add("text text text");
		}
		return (List<T>) list;
	}
}
