package org.oppia.android.app.administratorcontrols.learneranalytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import javax.inject.Inject
import org.oppia.android.R
import org.oppia.android.app.translation.AppLanguageResourceHandler
import org.oppia.android.domain.clipboard.ClipboardController
import org.oppia.android.domain.clipboard.ClipboardController.CurrentClip
import org.oppia.android.domain.oppialogger.LoggingIdentifierController
import org.oppia.android.util.data.AsyncResult
import org.oppia.android.util.data.DataProviders.Companion.toLiveData

class DeviceIdItemViewModel private constructor(
  private val clipboardController: ClipboardController,
  private val resourceHandler: AppLanguageResourceHandler,
  private val loggingIdentifierController: LoggingIdentifierController
) : ProfileListViewModel.ProfileListItemViewModel(
  ProfileListViewModel.ProfileListItemViewType.DEVICE_ID
) {
  val deviceId: LiveData<String?> by lazy {
    Transformations.map(
      loggingIdentifierController.getDeviceId().toLiveData(), this::processDeviceId
    )
  }
  val currentCopiedId: LiveData<String?> by lazy {
    Transformations.map(clipboardController.getCurrentClip().toLiveData(), this::processCurrentClip)
  }

  fun computeDeviceIdLabel(deviceId: String?): String {
    return if (deviceId != null) {
      resourceHandler.getStringInLocaleWithWrapping(
        R.string.learner_analytics_device_id_label, deviceId
      )
    } else {
      resourceHandler.getStringInLocale(R.string.learner_analytics_error_retrieving_device_id_error)
    }
  }

  // TODO: add tests for copying the device ID.
  fun clickOnCopyIdButton(deviceId: String?) {
    // Only copy the device ID if it's available.
    deviceId?.let {
      val appName = resourceHandler.getStringInLocale(R.string.app_name)
      clipboardController.setCurrentClip(
        resourceHandler.getStringInLocaleWithWrapping(
          R.string.learner_analytics_device_id_clipboard_label_description, appName
        ),
        deviceId
      )
    }
  }

  private fun processDeviceId(result: AsyncResult<String>): String? =
    if (result.isSuccess()) result.getOrThrow() else null

  private fun processCurrentClip(result: AsyncResult<CurrentClip>): String? {
    return if (result.isSuccess()) {
      when (val clip = result.getOrThrow()) {
        is CurrentClip.SetWithAppText -> clip.text
        CurrentClip.SetWithOtherContent, CurrentClip.Unknown -> null
      }
    } else null
  }

  class Factory @Inject constructor(
    private val clipboardController: ClipboardController,
    private val resourceHandler: AppLanguageResourceHandler,
    private val loggingIdentifierController: LoggingIdentifierController
  ) {
    fun create(): DeviceIdItemViewModel =
      DeviceIdItemViewModel(clipboardController, resourceHandler, loggingIdentifierController)
  }
}
