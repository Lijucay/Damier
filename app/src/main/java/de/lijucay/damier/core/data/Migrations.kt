package de.lijucay.damier.core.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val Migration_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            val cursor = db.query("PRAGMA table_info(CheckInInfo)")
            val columns = mutableListOf<String>()
            while (cursor.moveToNext()) {
                columns.add(cursor.getString(cursor.getColumnIndexOrThrow("name")))
            }
            cursor.close()

            if ("checkInCount" in columns) {
                db.execSQL("ALTER TABLE CheckInInfo RENAME COLUMN checkInCount to amount")
            }
        }
    }

    val Migration_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE ActivityInfo ADD COLUMN nfcChipId TEXT DEFAULT NULL")
        }
    }

    val Migration_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            /* no-op */
        }
    }

    val Migration_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS `NfcChipInfo` (
                        `chipId` BLOB NOT NULL,
                        `activityId` BLOB NOT NULL,
                        `linkedAt` TEXT NOT NULL,
                        `label` TEXT,
                        PRIMARY KEY(`chipId`),
                        FOREIGN KEY(`activityId`) REFERENCES `ActivityInfo`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent()
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_NfcChipInfo_activityId` ON `NfcChipInfo` (`activityId`)"
            )

            db.execSQL(
                """
                    INSERT INTO NfcChipInfo (chipId, activityId, linkedAt, label)
                    SELECT id, id, strftime('%Y-%m-%dT%H:%M:%S', 'now'), NULL
                    FROM ActivityInfo
                    WHERE nfcChipId IS NOT NULL
                """.trimIndent()
            )

            db.execSQL("PRAGMA foreign_keys=OFF")

            db.execSQL(
                """
                CREATE TABLE `ActivityInfo_new` (
                    `id` BLOB NOT NULL,
                    `activityName` TEXT NOT NULL,
                    `unit` TEXT NOT NULL,
                    `reference` INTEGER NOT NULL,
                    `referenceType` TEXT NOT NULL,
                    `defaultAmount` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                INSERT INTO ActivityInfo_new (id, activityName, unit, reference, referenceType, defaultAmount)
                SELECT id, activityName, unit, reference, referenceType, defaultAmount
                FROM ActivityInfo
                """.trimIndent()
            )
            db.execSQL("DROP TABLE ActivityInfo")
            db.execSQL("ALTER TABLE ActivityInfo_new RENAME TO ActivityInfo")

            db.execSQL("PRAGMA foreign_keys=ON")
        }
    }
}