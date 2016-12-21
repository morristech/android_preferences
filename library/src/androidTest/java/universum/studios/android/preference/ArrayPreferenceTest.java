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

import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;

import universum.studios.android.preference.test.R;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType")
public final class ArrayPreferenceTest extends SharedPreferenceBaseTest<Object> {

	@SuppressWarnings("unused")
	private static final String TAG = "ArrayPreferenceTest";

	public ArrayPreferenceTest() {
		super("PREFERENCE.Array", null);
	}

	@Nullable
	@Override
	SharedPreference<Object> onCreatePreference(String key, Object defValue) {
		return new ArrayPreference<>(key, defValue);
	}

	@Test
	public void testInstantiation() {
		new ArrayPreference<>(PREF_KEY, null);
		new ArrayPreference<>(R.string.test_preference_key, null);
	}

	@Test
	public void testInstantiationWithUnsupportedType() {
		try {
			new ArrayPreference<>(PREF_KEY, new Date());
		} catch (IllegalArgumentException e) {
			assertThat(TextUtils.isEmpty(e.getMessage()), is(false));
			assertThat(e.getMessage(), is("Not an array(Date)."));
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testResolveArrayClass() {
		assertEquals(boolean[].class, ArrayPreference.resolveArrayClass(new boolean[]{true}));
		assertEquals(byte[].class, ArrayPreference.resolveArrayClass(new byte[]{0}));
		assertEquals(char[].class, ArrayPreference.resolveArrayClass(new char[]{0}));
		assertEquals(short[].class, ArrayPreference.resolveArrayClass(new short[]{2}));
		assertEquals(int[].class, ArrayPreference.resolveArrayClass(new int[]{1}));
		assertEquals(float[].class, ArrayPreference.resolveArrayClass(new float[]{1.5f}));
		assertEquals(long[].class, ArrayPreference.resolveArrayClass(new long[]{198L}));
		assertEquals(double[].class, ArrayPreference.resolveArrayClass(new double[]{15D}));
		assertEquals(Boolean[].class, ArrayPreference.resolveArrayClass(new Boolean[]{true, null}));
		assertEquals(Byte[].class, ArrayPreference.resolveArrayClass(new Byte[]{0, null}));
		assertEquals(Short[].class, ArrayPreference.resolveArrayClass(new Short[]{2, null}));
		assertEquals(Integer[].class, ArrayPreference.resolveArrayClass(new Integer[]{1, null}));
		assertEquals(Float[].class, ArrayPreference.resolveArrayClass(new Float[]{1.5f, null}));
		assertEquals(Long[].class, ArrayPreference.resolveArrayClass(new Long[]{198L, null}));
		assertEquals(Double[].class, ArrayPreference.resolveArrayClass(new Double[]{15D, null}));
		assertEquals(String[].class, ArrayPreference.resolveArrayClass(new String[]{"", null}));
		assertEquals(null, ArrayPreference.resolveArrayClass(new Date[0]));
	}

	@Test
	public void testResolveArrayClassByName() {
		assertEquals(boolean[].class, ArrayPreference.resolveArrayClassByName("boolean[]"));
		assertEquals(byte[].class, ArrayPreference.resolveArrayClassByName("byte[]"));
		assertEquals(char[].class, ArrayPreference.resolveArrayClassByName("char[]"));
		assertEquals(short[].class, ArrayPreference.resolveArrayClassByName("short[]"));
		assertEquals(int[].class, ArrayPreference.resolveArrayClassByName("int[]"));
		assertEquals(float[].class, ArrayPreference.resolveArrayClassByName("float[]"));
		assertEquals(long[].class, ArrayPreference.resolveArrayClassByName("long[]"));
		assertEquals(double[].class, ArrayPreference.resolveArrayClassByName("double[]"));
		assertEquals(Boolean[].class, ArrayPreference.resolveArrayClassByName("Boolean[]"));
		assertEquals(Byte[].class, ArrayPreference.resolveArrayClassByName("Byte[]"));
		assertEquals(Short[].class, ArrayPreference.resolveArrayClassByName("Short[]"));
		assertEquals(Integer[].class, ArrayPreference.resolveArrayClassByName("Integer[]"));
		assertEquals(Float[].class, ArrayPreference.resolveArrayClassByName("Float[]"));
		assertEquals(Long[].class, ArrayPreference.resolveArrayClassByName("Long[]"));
		assertEquals(Double[].class, ArrayPreference.resolveArrayClassByName("Double[]"));
		assertEquals(String[].class, ArrayPreference.resolveArrayClassByName("String[]"));
		assertEquals(null, ArrayPreference.resolveArrayClassByName("Date[]"));
	}

	@Test
	public void testPutObtainBooleanArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		boolean[] firstPrimitiveArray = (boolean[]) createTestableArrayOf(boolean.class);
		boolean[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Boolean.class);
	}

	@Test
	public void testPutObtainByteArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		byte[] firstPrimitiveArray = (byte[]) createTestableArrayOf(byte.class);
		byte[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Byte.class);
	}

	@Test
	public void testPutObtainCharArrayIntoFromPreferences() {
		// fixme: not working
		/*// Test storing and obtaining of primitive array.
		char[] firstPrimitiveArray = (char[]) createTestableArrayOf(char.class);
		char[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Character[].class, Character.class);*/
	}

	@Test
	public void testPutObtainShortArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		short[] firstPrimitiveArray = (short[]) createTestableArrayOf(short.class);
		short[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Short.class);
	}

	@Test
	public void testPutObtainIntegerArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		int[] firstPrimitiveArray = (int[]) createTestableArrayOf(int.class);
		int[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Integer.class);
	}

	@Test
	public void testPutObtainFloatArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		float[] firstPrimitiveArray = (float[]) createTestableArrayOf(float.class);
		float[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Float.class);
	}

	@Test
	public void testPutObtainLongArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		long[] firstPrimitiveArray = (long[]) createTestableArrayOf(long.class);
		long[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Long.class);
	}

	@Test
	public void testPutObtainDoubleArrayIntoFromPreferences() {
		// Test storing and obtaining of primitive array.
		double[] firstPrimitiveArray = (double[]) createTestableArrayOf(double.class);
		double[] secondPrimitiveArray = putObtainArrayIntoFromPreferences(firstPrimitiveArray);
		assertThat(secondPrimitiveArray, is(firstPrimitiveArray));
		// Test storing and obtaining of boxed array.
		innerTestPutObtainArrayIntoFromPreferences(Double.class);
	}

	@Test
	public void testPutObtainStringArrayIntoFromPreferences() {
		innerTestPutObtainArrayIntoFromPreferences(String.class);
	}

	@Test
	public void testPutObtainArrayWithNullItemIntoFromPreferences() {
		// fixme: not working
		/*final String[] firstArray = new String[] {"text", null, null};
		final String[] secondArray = putObtainArrayIntoFromPreferences(firstArray);
		assertArraysEquals(String[].class, firstArray, secondArray);*/
	}

	@SuppressWarnings("unchecked")
	private <A, T> void innerTestPutObtainArrayIntoFromPreferences(Class<T> itemClass) {
		final T[] firstArray = (T[]) createTestableArrayOf(itemClass);
		assertThat(putObtainArrayIntoFromPreferences(firstArray), is(firstArray));
	}

	@SuppressWarnings("unchecked")
	private <A> A putObtainArrayIntoFromPreferences(A array) {
		final ArrayPreference<A> preference = new ArrayPreference<>(PREF_KEY, null);
		preference.updateValue(array);
		preference.onPutIntoPreferences(sharedPreferences);
		preference.clear();
		return preference.onGetFromPreferences(sharedPreferences);
	}

	@Test
	public void testObtainNotSavedArrayFromPreferences() {
		assertThat(ArrayPreference.getFromPreferences(sharedPreferences, PREF_KEY, null), is(nullValue()));
	}

	@Test
	public void testPutObtainNullArrayIntoFromPreferences() {
		ArrayPreference.putIntoPreferences(sharedPreferences, PREF_KEY, null);
		assertThat(ArrayPreference.getFromPreferences(sharedPreferences, PREF_KEY, null), is(nullValue()));
	}

	@Test
	public void testPutObtainUnsupportedArrayIntoFromPreferences() {
		try {
			ArrayPreference.putIntoPreferences(sharedPreferences, PREF_KEY, new Date[]{new Date(0)});
			throw new AssertionError("No exception thrown.");
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("Failed to put array of(Date) into shared preferences. " +
							"Only arrays of primitive types or theirs boxed representations including String are supported.")
			);
		}

		final StringPreference tmpPreference = new StringPreference(PREF_KEY, null);
		tmpPreference.updateValue("<Date[]>[]");
		tmpPreference.putIntoPreferences(sharedPreferences);
		try {
			ArrayPreference.getFromPreferences(sharedPreferences, PREF_KEY, null);
			throw new AssertionError();
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("Failed to obtain an array of(Date) for the key(" + PREF_KEY + ") from shared preferences. " +
							"Only arrays of primitive types or theirs boxed representations including String are supported.")
			);
		}
	}

	@Test
	public void testObtainNotArrayFromPreferences() {
		final StringPreference tmpPreference = new StringPreference(PREF_KEY, null);
		tmpPreference.updateValue("<String[]>text,text");
		tmpPreference.putIntoPreferences(sharedPreferences);

		try {
			ArrayPreference.getFromPreferences(sharedPreferences, PREF_KEY, null);
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Trying to obtain an array for the key(" + PREF_KEY + ") from shared preferences not saved by the Preferences library.")
			);
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@Test
	public void testObtainArrayNotStoredByLibraryFromPreferences() {
		final StringPreference tmpPreference = new StringPreference(PREF_KEY, null);
		tmpPreference.updateValue("[text,text,text]");
		tmpPreference.putIntoPreferences(sharedPreferences);

		try {
			ArrayPreference.getFromPreferences(sharedPreferences, PREF_KEY, null);
		} catch (IllegalStateException e) {
			assertThat(
					e.getMessage(),
					is("Trying to obtain an array for the key(" + PREF_KEY + ") from shared preferences not saved by the Preferences library.")
			);
			return;
		}
		throw new AssertionError("No exception thrown.");
	}

	@SuppressWarnings("unchecked")
	private static <T> Object createTestableArrayOf(Class<T> itemClass) {
		if (boolean.class.equals(itemClass)) {
			return new boolean[]{true, false, true};
		} else if (byte.class.equals(itemClass)) {
			return new byte[]{48, 16};
		} else if (char.class.equals(itemClass)) {
			return new char[]{0, 1};
		} else if (short.class.equals(itemClass)) {
			return new short[]{3, 12};
		} else if (int.class.equals(itemClass)) {
			return new int[]{0, 10007, 12, 432342};
		} else if (float.class.equals(itemClass)) {
			return new float[]{15798.12f, 0.14578f};
		} else if (long.class.equals(itemClass)) {
			return new long[]{1654678987L, 11491465L};
		} else if (double.class.equals(itemClass)) {
			return new double[]{15498.161645578D, 12.1234971947D};
		} else if (Boolean.class.equals(itemClass)) {
			return new Boolean[]{true, false, true};
		} else if (Byte.class.equals(itemClass)) {
			return new Byte[]{48, 16};
		} else if (Character.class.equals(itemClass)) {
			return new Character[]{0, 1};
		} else if (Short.class.equals(itemClass)) {
			return new Short[]{3, 12};
		} else if (Integer.class.equals(itemClass)) {
			return new Integer[]{0, 10007, 12, 432342};
		} else if (Float.class.equals(itemClass)) {
			return new Float[]{15798.12f, 0.14578f};
		} else if (Long.class.equals(itemClass)) {
			return new Long[]{1654678987L, 11491465L};
		} else if (Double.class.equals(itemClass)) {
			return new Double[]{15498.161645578D, 12.1234971947D};
		} else if (String.class.equals(itemClass)) {
			return new String[]{"text", "text text", "text text"};
		} else if (Enum.class.equals(itemClass)) {
			return new Enum[]{EnumPreferenceTest.TestEnum.ONE, EnumPreferenceTest.TestEnum.THREE};
		}
		return null;
	}

	private static void assertArraysEquals(Class<?> arrayClass, Object[] first, Object[] second) {
		assertThatArraysAreEqual(arrayClass, Arrays.toString(first), Arrays.toString(second), Arrays.equals(first, second));
	}

	private static void assertThatArraysAreEqual(Class<?> arrayClass, String firstArray, String secondArray, boolean arraysEquals) {
		Assert.assertTrue("expected " + arrayClass.getSimpleName() + " array:<" + firstArray + "> but was:<" + secondArray + ">", arraysEquals);
	}
}