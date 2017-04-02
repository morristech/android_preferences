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

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.crypto.Crypto;

import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class CryptoSharedPreferencesHelperTest extends BaseCryptoPreferencesTest {

	@SuppressWarnings("unused")
	private static final String TAG = "CryptoSharedPreferencesHelperTest";

	private Crypto mMockCrypto;
	private CryptoSharedPreferences.CryptoHelper mEmptyHelper;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockCrypto = mock(Crypto.class);
		this.mEmptyHelper = new CryptoSharedPreferences.CryptoHelper(null, null);
	}

	@Test
	public void testAreValuesEqual() {
		// todo:: implement test
	}

	@Test
	public void testEncryptKey() {
		// todo:: implement test
	}

	@Test
	public void testEncryptKeyOnEmptyHelper() {
		// todo:: implement test
	}

	@Test
	public void testDecryptKey() {
		// todo:: implement test
	}

	@Test
	public void testDecryptKeyOnEmptyHelper() {
		// todo:: implement test
	}

	@Test
	public void testEncryptValue() {
		// todo:: implement test
	}

	@Test
	public void testEncryptValueOnEmptyHelper() {
		// todo:: implement test
	}

	@Test
	public void testDecryptValue() {
		// todo: implement test
	}

	@Test
	public void testDecryptValueOnEmptyHelper() {
		// todo: implement test
	}

	@Test
	public void testEncryptValuesSet() {
		// todo:: implement test
	}

	@Test
	public void testEncryptValuesSetOnEmptyHelper() {
		// todo:: implement test
	}

	@Test
	public void testDecryptValuesSet() {
		// todo: implement test
	}

	@Test
	public void testDecryptValuesSetOnEmptyHelper() {
		// todo: implement test
	}
}
