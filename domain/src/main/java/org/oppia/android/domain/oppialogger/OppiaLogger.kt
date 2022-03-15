package org.oppia.android.domain.oppialogger

import org.oppia.android.app.model.EventLog
import org.oppia.android.domain.oppialogger.analytics.AnalyticsController
import org.oppia.android.util.logging.ConsoleLogger
import org.oppia.android.util.platformparameter.LearnerStudyAnalytics
import org.oppia.android.util.platformparameter.PlatformParameterValue
import javax.inject.Inject
import org.oppia.android.app.model.HelpIndex
import org.oppia.android.util.platformparameter.LearnerStudyAnalytics
import org.oppia.android.util.platformparameter.PlatformParameterValue

/** Logger that handles event logging. */
class OppiaLogger @Inject constructor(
  private val analyticsController: AnalyticsController,
  private val consoleLogger: ConsoleLogger,
  @LearnerStudyAnalytics private val learnerStudyAnalytics: PlatformParameterValue<Boolean>,
  private val loggingIdentifierController: LoggingIdentifierController
) {
  // TODO: Clean up this file (remove extras post-merge).
  /** Logs transition events. See [AnalyticsController.logTransitionEvent] for more context. */
  fun logTransitionEvent(
    timestamp: Long,
    eventContext: EventLog.Context
  ) {
    analyticsController.logTransitionEvent(timestamp, eventContext)
  }

  /** Logs transition events which are specifically related to Learner Study Analytics. These events
   * will only get logged if the value of [LearnerStudyAnalytics] platform parameter is set to true.
   * See [AnalyticsController.logTransitionEvent] for more context.
   */
  fun logLearnerAnalyticsEvent(
    timestamp: Long,
    eventAction: EventAction,
    eventContext: EventLog.Context?
  ) {
    if (learnerStudyAnalytics.value) {
      analyticsController.logTransitionEvent(timestamp, eventAction, eventContext)
    }
  }

  /** Logs click events. See [AnalyticsController.logClickEvent] for more context. */
  fun logClickEvent(
    timestamp: Long,
    eventContext: EventLog.Context
  ) {
    analyticsController.logClickEvent(timestamp, eventContext)
  }

  /** Logs transition events which are specifically related to Learner Study Analytics. These events
   * will only get logged if the value of [LearnerStudyAnalytics] platform parameter is set to true.
   * See [AnalyticsController.logTransitionEvent] for more context.
   */
  fun logLearnerAnalyticsEvent(
    timestamp: Long,
    eventContext: EventLog.Context
  ) {
    if (learnerStudyAnalytics.value) {
      analyticsController.logTransitionEvent(timestamp, eventContext)
    }
  }

  /** Logs a verbose message with the specified tag. See [ConsoleLogger.v] for more context */
  fun v(tag: String, msg: String) {
    consoleLogger.v(tag, msg)
  }

  /**
   * Logs a verbose message with the specified tag, message and exception. See [ConsoleLogger.v]
   * for more context
   */
  fun v(tag: String, msg: String, tr: Throwable) {
    consoleLogger.v(tag, msg, tr)
  }

  /** Logs a debug message with the specified tag. See [ConsoleLogger.d] for more context */
  fun d(tag: String, msg: String) {
    consoleLogger.d(tag, msg)
  }

  /**
   * Logs a debug message with the specified tag, message and exception. See [ConsoleLogger.d] for
   * more context
   */
  fun d(tag: String, msg: String, tr: Throwable) {
    consoleLogger.d(tag, msg, tr)
  }

  /** Logs an info message with the specified tag. See [ConsoleLogger.i] for more context */
  fun i(tag: String, msg: String) {
    consoleLogger.i(tag, msg)
  }

  /**
   * Logs an info message with the specified tag, message and exception. See [ConsoleLogger.i] for
   * more context
   */
  fun i(tag: String, msg: String, tr: Throwable) {
    consoleLogger.i(tag, msg, tr)
  }

  /** Logs a warn message with the specified tag. See [ConsoleLogger.w] for more context */
  fun w(tag: String, msg: String) {
    consoleLogger.w(tag, msg)
  }

  /**
   * Logs a warn message with the specified tag, message and exception. See [ConsoleLogger.w] for
   * more context
   */
  fun w(tag: String, msg: String, tr: Throwable) {
    consoleLogger.w(tag, msg, tr)
  }

  /** Logs an error message with the specified tag. See [ConsoleLogger.e] for more context */
  fun e(tag: String, msg: String) {
    consoleLogger.e(tag, msg)
  }

  /**
   * Logs an error message with the specified tag, message and exception. See [ConsoleLogger.e] for
   * more context
   */
  fun e(tag: String, msg: String, tr: Throwable?) {
    consoleLogger.e(tag, msg, tr)
  }

  /**
   * Returns a generic data object that contains [deviceId] and [learnerId]. These identifiers are
   * logged across all Learner Study Analytics events.
   *
   * @param deviceId: device-specific identifier which is unique to each device.
   * @param learnerId: profile-specific identifier which is unique to each profile on a device.
   * */
  fun createGenericData(
    deviceId: String,
    learnerId: String
  ): EventLog.GenericData {
    return EventLog.GenericData.newBuilder()
      .setDeviceId(loggingIdentifierController.deviceId)
      .setLearnerId(learnerId)
      .build()
  }

  /**
   * Returns an exploration-specific data object that contains [sessionId], [explorationId],
   * [explorationVersion] and [stateName].
   *
   * @param sessionId: session-specific identifier which is unique to each session.
   * @param explorationId: id of the exploration.
   * @param explorationVersion: version of the exploration.
   * @param stateName: name of the current state.
   */
  fun createExplorationData(
    sessionId: String,
    explorationId: String,
    explorationVersion: String,
    stateName: String
  ): EventLog.ExplorationData {
    return EventLog.ExplorationData.newBuilder()
      .setSessionId(sessionId)
      .setExplorationId(explorationId)
      .setExplorationVersion(explorationVersion)
      .setStateName(stateName)
      .build()
  }

  /** Returns the context of an event related to exploration. */
  fun createExplorationContext(
    topicId: String,
    storyId: String,
    explorationId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenExplorationActivity(
        EventLog.ExplorationContext.newBuilder()
          .setTopicId(topicId)
          .setStoryId(storyId)
          .setExplorationId(explorationId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the home activity. */
  fun createOpenHomeContext(): EventLog.Context {
    return EventLog.Context.newBuilder().setOpenHome(true).build()
  }

  /** Returns the context of the event indicating that the user opened the profile chooser activity. */
  fun createOpenProfileChooserContext(): EventLog.Context {
    return EventLog.Context.newBuilder().setOpenProfileChooser(true).build()
  }

  /** Returns the context of the event indicating that the user opened the exploration activity. */
  fun createOpenExplorationActivityContext(
    topicId: String,
    storyId: String,
    explorationId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenExplorationActivity(
        EventLog.ExplorationContext.newBuilder()
          .setTopicId(topicId)
          .setStoryId(storyId)
          .setExplorationId(explorationId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the question player. */
  fun createOpenQuestionPlayerContext(
    questionId: String,
    skillId: List<String>
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenQuestionPlayer(
        EventLog.QuestionContext.newBuilder()
          .setQuestionId(questionId)
          .addAllSkillId(skillId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the practice tab. */
  fun createOpenPracticeTabContext(
    topicId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenPracticeTab(
        EventLog.TopicContext.newBuilder()
          .setTopicId(topicId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the info tab. */
  fun createOpenInfoTabContext(
    topicId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenInfoTab(
        EventLog.TopicContext.newBuilder()
          .setTopicId(topicId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the lessons tab. */
  fun createOpenLessonsTabContext(
    topicId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenLessonsTab(
        EventLog.TopicContext.newBuilder()
          .setTopicId(topicId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the revision tab. */
  fun createOpenRevisionTabContext(
    topicId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenRevisionTab(
        EventLog.TopicContext.newBuilder()
          .setTopicId(topicId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the story activity. */
  fun createOpenStoryActivityContext(
    topicId: String,
    storyId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenStoryActivity(
        EventLog.StoryContext.newBuilder()
          .setTopicId(topicId)
          .setStoryId(storyId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the concept card. */
  fun createOpenConceptCardContext(
    skillId: String
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenConceptCard(
        EventLog.ConceptCardContext.newBuilder()
          .setSkillId(skillId)
          .build()
      )
      .build()
  }

  /** Returns the context of the event indicating that the user opened the revision card. */
  fun createOpenRevisionCardContext(
    topicId: String,
    subTopicId: Int
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setOpenRevisionCard(
        EventLog.RevisionCardContext.newBuilder()
          .setTopicId(topicId)
          .setSubTopicId(subTopicId)
          .build()
      )
      .build()
  }

  /** Returns the context of an event related to starting an exploration card. */
  fun createStartCardContext(
    skillId: String,
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setStartCardContext(
        EventLog.StartCardContext.newBuilder()
          .setSkillId(skillId)
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /**
   * Returns a learner details data object that contains [deviceId] and [learnerId]. These
   * identifiers are logged across all Learner Study Analytics events.
   *
   * @param deviceId: device-specific identifier which is unique to each device.
   * @param learnerId: profile-specific identifier which is unique to each profile on a device.
   * */
  fun createLearnerDetailsContext(
    learnerId: String
  ): EventLog.LearnerDetailsContext {
    return EventLog.LearnerDetailsContext.newBuilder()
      .setDeviceId(loggingIdentifierController.deviceId)
      .setLearnerId(learnerId)
      .build()
  }

  /**
   * Returns an exploration-specific data object that contains [sessionId], [explorationId],
   * [explorationVersion] and [stateName].
   *
   * @param sessionId: session-specific identifier which is unique to each session.
   * @param explorationId: id of the exploration.
   * @param explorationVersion: version of the exploration.
   * @param stateName: name of the current state.
   * @param learnerDetails:
   */
  fun createExplorationDetailsContext(
    sessionId: String,
    explorationId: String,
    explorationVersion: String,
    stateName: String,
    learnerDetails: EventLog.LearnerDetailsContext
  ): EventLog.ExplorationContext {
    return EventLog.ExplorationContext.newBuilder()
      .setSessionId(sessionId)
      .setExplorationId(explorationId)
      .setExplorationVersion(explorationVersion)
      .setStateName(stateName)
      .setLearnerDetails(learnerDetails)
      .build()
  }

  /** Returns the context of an event related to starting an exploration card. */
  fun createStartCardContext(
    skillId: String,
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setStartCardContext(
        EventLog.CardContext.newBuilder()
          .setSkillId(skillId)
          .setExplorationDetails(explorationContext)
          .build()
      ).build()
  }

  /** Returns the context of an event related to ending an exploration card. */
  fun createEndCardContext(
    skillId: String,
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setEndCardContext(
        EventLog.EndCardContext.newBuilder()
          .setSkillId(skillId)
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to ending an exploration card. */
  fun createEndCardContext(
    skillId: String,
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setEndCardContext(
        EventLog.CardContext.newBuilder()
          .setSkillId(skillId)
          .setExplorationDetails(explorationContext)
          .build()
      ).build()
  }

  /** Returns the context of an event related to offering a hint when it becomes available. */
  fun createHintOfferedContext(
    hintIndex: String,
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setHintOfferedContext(
        EventLog.HintOfferedContext.newBuilder()
          .setHintIndex(hintIndex)
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }
  
  /** Returns the context of an event related to offering a hint when it becomes available. */
  fun createHintOfferedContext(
    hintIndex: HelpIndex,
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setHintOfferedContext(
        EventLog.HintContext.newBuilder()
          .setHelpIndex(hintIndex)
          .setExplorationDetails(explorationContext)
          .build()
      ).build()
  }

  /** Returns the context of an event related to accessing a hint. */
  fun createAccessHintContext(
    hintIndex: String,
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setAccessHintContext(
        EventLog.AccessHintContext.newBuilder()
          .setHintIndex(hintIndex)
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to accessing a hint. */
  fun createAccessHintContext(
    hintIndex: HelpIndex,
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setAccessHintContext(
        EventLog.HintContext.newBuilder()
          .setHelpIndex(hintIndex)
          .setExplorationDetails(explorationContext)
          .build()
      ).build()
  }

  /** Returns the context of an event related to offering a solution when it becomes available. */
  fun createSolutionOfferedContext(
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setSolutionOfferedContext(
        EventLog.SolutionOfferedContext.newBuilder()
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to offering a solution when it becomes available. */
  fun createSolutionOfferedContext(
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setSolutionOfferedContext(explorationContext).build()
  }

  /** Returns the context of an event related to accessing a solution. */
  fun createAccessSolutionContext(
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setAccessSolutionContext(
        EventLog.AccessSolutionContext.newBuilder()
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to accessing a solution. */
  fun createAccessSolutionContext(
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setAccessSolutionContext(explorationContext).build()
  }

  /** Returns the context of an event related to submitting an answer. */
  fun createSubmitAnswerContext(
    isAnswerCorrect: Boolean,
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setSubmitAnswerContext(
        EventLog.SubmitAnswerContext.newBuilder()
          .setIsAnswerCorrect(isAnswerCorrect)
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to submitting an answer. */
  fun createSubmitAnswerContext(
    isAnswerCorrect: Boolean,
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setSubmitAnswerContext(
        EventLog.SubmitAnswerContext.newBuilder()
          .setIsAnswerCorrect(isAnswerCorrect)
          .setExplorationDetails(explorationContext)
          .build()
      ).build()
  }

  /** Returns the context of an event related to playing a voice over. */
  fun createPlayVoiceOverContext(
    contentId: String,
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setPlayVoiceOverContext(
        EventLog.PlayVoiceOverContext.newBuilder()
          .setContentId(contentId)
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to playing a voice over. */
  fun createPlayVoiceOverContext(
    contentId: String,
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setPlayVoiceOverContext(
        EventLog.PlayVoiceOverContext.newBuilder()
          .setContentId(contentId)
          .setExplorationDetails(explorationContext)
          .build()
      ).build()
  }

  /** Returns the context of an event related to backgrounding of the application. */
  fun createAppInBackgroundContext(
    genericData: EventLog.GenericData,
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setAppInBackgroundContext(
        EventLog.AppInBackgroundContext.newBuilder()
          .setGenericData(genericData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to backgrounding of the application. */
  fun createAppInBackgroundContext(
    learnerDetails: EventLog.LearnerDetailsContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setAppInBackgroundContext(learnerDetails).build()
  }

  /** Returns the context of an event related to foregrounding of the application. */
  fun createAppInForegroundContext(
    genericData: EventLog.GenericData,
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setAppInForegroundContext(
        EventLog.AppInForegroundContext.newBuilder()
          .setGenericData(genericData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to foregrounding of the application. */
  fun createAppInForegroundContext(
    learnerDetails: EventLog.LearnerDetailsContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setAppInForegroundContext(learnerDetails).build()
  }

  /** Returns the context of an event related to exiting an exploration. */
  fun createExitExplorationContext(
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setExitExplorationContext(
        EventLog.ExitExplorationContext.newBuilder()
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to exiting an exploration. */
  fun createExitExplorationContext(
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setExitExplorationContext(explorationContext).build()
  }

  /** Returns the context of an event related to finishing an exploration. */
  fun createFinishExplorationContext(
    genericData: EventLog.GenericData,
    explorationData: EventLog.ExplorationData
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setFinishExplorationContext(
        EventLog.FinishExplorationContext.newBuilder()
          .setGenericData(genericData)
          .setExplorationData(explorationData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to finishing an exploration. */
  fun createFinishExplorationContext(
    explorationContext: EventLog.ExplorationContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setFinishExplorationContext(explorationContext).build()
  }

  /** Returns the context of an event related to resuming an exploration. */
  fun createResumeExplorationContext(
    genericData: EventLog.GenericData,
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setResumeExplorationContext(
        EventLog.ResumeExplorationContext.newBuilder()
          .setGenericData(genericData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to resuming an exploration. */
  fun createResumeExplorationContext(
    learnerDetails: EventLog.LearnerDetailsContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setResumeExplorationContext(learnerDetails).build()
  }

  /** Returns the context of an event related to starting over an exploration. */
  fun createStartOverExplorationContext(
    genericData: EventLog.GenericData,
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setStartOverExplorationContext(
        EventLog.StartOverExplorationContext.newBuilder()
          .setGenericData(genericData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to starting over an exploration. */
  fun createStartOverExplorationContext(
    learnerDetails: EventLog.LearnerDetailsContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setStartOverExplorationContext(learnerDetails).build()
  }

  /** Returns the context of an event related to deleting a profile. */
  fun createDeleteProfileContext(
    genericData: EventLog.GenericData,
  ): EventLog.Context {
    return EventLog.Context.newBuilder()
      .setDeleteProfileContext(
        EventLog.DeleteProfileContext.newBuilder()
          .setGenericData(genericData)
          .build()
      ).build()
  }

  /** Returns the context of an event related to deleting a profile. */
  fun createDeleteProfileContext(
    learnerDetails: EventLog.LearnerDetailsContext
  ): EventLog.Context {
    return EventLog.Context.newBuilder().setDeleteProfileContext(learnerDetails).build()
  }
}
