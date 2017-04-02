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
package universum.studios.android.preference.crypto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;

import universum.studios.android.crypto.Cryptography;
import universum.studios.android.crypto.CryptographyException;
import universum.studios.android.crypto.Decrypto;
import universum.studios.android.crypto.Encrypto;

/**
 * Utility class used by {@link CryptoSharedPreferences} to perform encryption and decryption related
 * operations.
 *
 * @author Martin Albedinsky
 */
final class CryptoUtils {

	/**
	 */
	private CryptoUtils() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs encryption operation for the specified {@link String} <var>value</var> using the
	 * given <var>encrypto</var>.
	 * <p>
	 * <b>Note</b>, that data of the specified value will be obtained as
	 * {@link String#getBytes(String) value.getBytes(Cryptography.CHARSET_NAME)}.
	 * <p>
	 * Also note, that this method may additionally throw a {@link CryptographyException} thrown by
	 * the provided decrypto implementation.
	 *
	 * @param value    The desired value to be encrypted. May be {@code null} in which case this method
	 *                 does nothing.
	 * @param encrypto Encrypto implementation to be used to perform the desired encryption.
	 * @return Encrypted value or the same value if the value is {@code null} or empty.
	 * @see #decrypt(String, Decrypto)
	 * @see Cryptography#CHARSET_NAME
	 */
	@Nullable
	static String encrypt(@Nullable final String value, @NonNull final Encrypto encrypto) {
		if (value == null || value.length() == 0) {
			return value;
		}
		try {
			return new String(encrypto.encrypt(value.getBytes(Cryptography.CHARSET_NAME)), Cryptography.CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new AssertionError(Cryptography.CHARSET_NAME + " encoding is not supported!");
		}
	}

	/**
	 * Performs decryption operation for the specified {@link String} <var>value</var> using the
	 * given <var>decrypto</var>.
	 * <p>
	 * <b>Note</b>, that data of the specified value will be obtained as
	 * {@link String#getBytes(String) value.getBytes(Cryptography.CHARSET_NAME)}.
	 * <p>
	 * Also note, that this method may additionally throw a {@link CryptographyException} thrown by
	 * the provided decrypto implementation.
	 *
	 * @param value    The desired value to be decrypted. May be {@code null} in which case this method
	 *                 does nothing.
	 * @param decrypto Decrypto implementation to be used to perform the desired decryption.
	 * @return Decrypted value or the same value if the value is {@code null} or empty.
	 * @see #encrypt(String, Encrypto)
	 * @see Cryptography#CHARSET_NAME
	 */
	@Nullable
	static String decrypt(@Nullable final String value, @NonNull final Decrypto decrypto) {
		if (value == null || value.length() == 0) {
			return value;
		}
		try {
			return new String(decrypto.decrypt(value.getBytes(Cryptography.CHARSET_NAME)), Cryptography.CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new AssertionError(Cryptography.CHARSET_NAME + " encoding is not supported!");
		}
	}
}
