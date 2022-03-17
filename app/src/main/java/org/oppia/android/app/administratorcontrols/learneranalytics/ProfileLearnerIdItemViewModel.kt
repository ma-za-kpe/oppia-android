package org.oppia.android.app.administratorcontrols.learneranalytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import javax.inject.Inject
import org.oppia.android.R
import org.oppia.android.app.model.Profile
import org.oppia.android.app.translation.AppLanguageResourceHandler
import org.oppia.android.domain.clipboard.ClipboardController
import org.oppia.android.domain.clipboard.ClipboardController.CurrentClip
import org.oppia.android.util.data.AsyncResult
import org.oppia.android.util.data.DataProviders.Companion.toLiveData

class ProfileLearnerIdItemViewModel private constructor(
  val profile: Profile,
  private val clipboardController: ClipboardController,
  private val resourceHandler: AppLanguageResourceHandler
) : ProfileListViewModel.ProfileListItemViewModel(
  ProfileListViewModel.ProfileListItemViewType.LEARNER_ID
) {
  val currentCopiedId: LiveData<String?> by lazy {
    Transformations.map(clipboardController.getCurrentClip().toLiveData(), this::processCurrentClip)
  }

  fun clickOnCopyIdButton() {
    clipboardController.setCurrentClip(
      resourceHandler.getStringInLocaleWithWrapping(
        R.string.learner_analytics_learner_id_clipboard_label_description, profile.name
      ),
      profile.learnerId
    )
  }

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
    private val resourceHandler: AppLanguageResourceHandler
  ) {
    fun create(profile: Profile): ProfileLearnerIdItemViewModel =
      ProfileLearnerIdItemViewModel(profile, clipboardController, resourceHandler)
  }
}
