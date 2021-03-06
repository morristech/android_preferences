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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;

/**
 * Utility class used by the Adapters library for logging purpose.
 * <p>
 * Custom {@link Logger} may be specified via {@link #setLogger(Logger)} which may be used to control
 * logging outputs of the library.
 * <p>
 * Default logger used by this class has specified {@link Log#ASSERT} log level which means the the
 * library by default does not print out any logs.
 *
 * @author Martin Albedinsky
 */
public final class PreferencesLogging {

	/**
	 * Default logger used by the library for logging purpose.
	 */
	private static final Logger LOGGER = new SimpleLogger(Log.ASSERT);

	/**
	 * Logger to which is this logging utility class delegating all log related requests.
	 */
	@NonNull
	private static Logger sLogger = LOGGER;

	/**
	 */
	private PreferencesLogging() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets a logger to be used by this logging class to print out logs into console.
	 *
	 * @param logger The desired logger. May by {@code null} to use default logger.
	 * @see #getLogger()
	 */
	public static void setLogger(@Nullable final Logger logger) {
		sLogger = logger == null ? LOGGER : logger;
	}

	/**
	 * Returns the logger used by this logging class.
	 *
	 * @return Either default or custom logger.
	 * @see #setLogger(Logger)
	 */
	@NonNull
	public static Logger getLogger() {
		return sLogger;
	}

	/**
	 * Delegates to {@link Logger#d(String, String)}.
	 */
	public static void d(@NonNull final String tag, @NonNull final String msg) {
		sLogger.d(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#d(String, String, Throwable)}.
	 */
	public static void d(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.d(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#v(String, String)}.
	 */
	public static void v(@NonNull final String tag, @NonNull final String msg) {
		sLogger.d(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#v(String, String, Throwable)}.
	 */
	public static void v(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.v(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#i(String, String)}.
	 */
	public static void i(@NonNull final String tag, @NonNull final String msg) {
		sLogger.i(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#i(String, String, Throwable)}.
	 */
	public static void i(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.i(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#w(String, String)}.
	 */
	public static void w(@NonNull final String tag, @NonNull final String msg) {
		sLogger.w(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#w(String, Throwable)}.
	 */
	public static void w(@NonNull final String tag, @Nullable final Throwable tr) {
		sLogger.w(tag, tr);
	}

	/**
	 * Delegates to {@link Logger#w(String, String, Throwable)}.
	 */
	public static void w(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.w(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#e(String, String)}.
	 */
	public static void e(@NonNull final String tag, @NonNull final String msg) {
		sLogger.e(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#e(String, String, Throwable)}.
	 */
	public static void e(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.e(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, String)}.
	 */
	public static void wtf(@NonNull final String tag, @NonNull final String msg) {
		sLogger.wtf(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, Throwable)}.
	 */
	public static void wtf(@NonNull final String tag, @Nullable final Throwable tr) {
		sLogger.wtf(tag, tr);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, String, Throwable)}.
	 */
	public static void wtf(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.wtf(tag, msg, tr);
	}
}
