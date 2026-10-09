package de.lijucay.damier.core.domain

object DamierConstants {
    private const val PACKAGE = "de.lijucay.damier"
    const val LOG_FILE_NAME = "damier_logs.txt"
    const val DATABASE_SCHEME_VERSION = 7

    object Extras {
        private const val PREFIX = "extras"
        const val ACTIVITY_ID = "${PACKAGE}.${PREFIX}.activity_id"
        const val ACTIVITY_TITLE = "${PACKAGE}.${PREFIX}.activity_title"
        const val REMINDER_TIME = "${PACKAGE}.${PREFIX}.reminder_time"
    }

    object Channels {
        private const val PREFIX = "channels"
        const val ACTIVITY_REMINDER = "${PACKAGE}.${PREFIX}.activity_reminder"
    }

    object Workers {
        private const val PREFIX = "workers"
        const val ACTIVITY_REMINDER = "${PACKAGE}.${PREFIX}.activity_reminder"
    }

    object Actions {
        private const val PREFIX = "actions"
        const val ADD_ACTIVITY = "${PACKAGE}.${PREFIX}.add_activity"
    }
}