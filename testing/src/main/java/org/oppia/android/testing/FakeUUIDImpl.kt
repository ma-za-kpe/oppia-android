package org.oppia.android.testing

import javax.inject.Inject
import javax.inject.Singleton
import org.oppia.android.util.system.UUIDWrapper

/** A test specific fake for UUID. */
@Singleton
class FakeUUIDImpl @Inject constructor(): UUIDWrapper {

  private var currentUUIDValue = "default_uuid_value"

  override fun randomUUIDString(): String = currentUUIDValue

  /**
   * Sets the current UUID string to [value].
   * This string would be returned by the [UUIDWrapper] when asked for a random UUID string.
   */
  fun setUUIDValue(value: String) {
    currentUUIDValue = value
  }

  /**
   * Returns the current UUID string which is generated by the [UUIDWrapper] when asked for a
   * random UUID string.
   */
  fun getUUIDValue(): String = currentUUIDValue
}
