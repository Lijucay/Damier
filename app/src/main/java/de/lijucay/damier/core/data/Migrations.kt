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
}