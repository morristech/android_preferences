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
import android.util.Base64;

import universum.studios.android.crypto.Crypto;
import universum.studios.android.crypto.CryptographyException;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("unused") final class CryptoTests {

	/**
	 * Creates a new {@link Crypto} implementation for purpose of tests of <b>crypto</b> package.
	 *
	 * @return Crypto that is ready to be used.
	 */
	@NonNull
	static Crypto testCrypto() {
		return new Crypto() {

			/**
			 */
			@NonNull
			@Override
			public byte[] decrypt(@NonNull byte[] data) throws CryptographyException {
				return Base64.decode(data, Base64.DEFAULT);
			}

			/**
			 */
			@NonNull
			@Override
			public byte[] encrypt(@NonNull byte[] data) throws CryptographyException {
				return Base64.encode(data, Base64.DEFAULT);
			}
		};
	}
}