package dev.lijucay.damier.data.local.model

import java.time.LocalDate

data class DateCount(val date: LocalDate, val totalCount: Int)
data class DateCountNullable(val date: String?, val totalCount: Int?)
