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
import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

/**
 * Class that may be used as base for <b>Android Instrumented Tests</b>.
 *
 * @author Martin Albedinsky
 */
public abstract class BaseInstrumentedTest {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "BaseInstrumentedTest";

	/**
	 * Target context obtained from the {@link InstrumentationRegistry}.
	 * <p>
	 * It is always valid between calls to {@link #beforeTest()} and {@link #afterTest()}.
	 */
	protected Context mContext;

	/**
	 * Called before execution of each test method starts.
	 */
	@Before
	@CallSuper
	public void beforeTest() throws Exception {
		// Inheritance hierarchies may for example acquire here resources needed for each test.
		this.mContext = InstrumentationRegistry.getTargetContext();
	}

	/**
	 * Called after execution of each test method finishes.
	 */
	@After
	@CallSuper
	public void afterTest() throws Exception {
		// Inheritance hierarchies may for example release here resources acquired in beforeTest() call.
		this.mContext = null;
	}
}