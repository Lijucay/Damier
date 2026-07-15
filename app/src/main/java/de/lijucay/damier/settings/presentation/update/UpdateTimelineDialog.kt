package de.lijucay.damier.settings.presentation.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.BrandGooglePlay
import compose.icons.tablericons.Planet
import compose.icons.tablericons.User
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import de.lijucay.damier.design.components.LargeTitleText
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTimelineDialog(
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        contentWindowInsets = {
            BottomSheetDefaults
                .modalWindowInsets
                .exclude(WindowInsets.navigationBars)
        },
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
                LargeTitleText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.update_timeline)
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = bottomPadding())
        ) {
            item {
                val date = LocalDate.of(2026, 6, 25)

                UpdateItem(
                    version = "1.2.1",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v1_2_1_notes).toList()
                )
            }
            item {
                val date = LocalDate.of(2026, 6, 24)

                UpdateItem(
                    version = "1.1.1",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v1_1_0_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 6, 20)

                UpdateItem(
                    version = "1.1.0",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v1_1_0_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 6, 3)

                UpdateItem(
                    version = "1.0.4",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v1_0_4_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 24)

                UpdateItem(
                    highlightUpdate = HighlightUpdate(
                        title = stringResource(R.string.play_store_release),
                        text = stringResource(R.string.play_store_release_text),
                        icon = TablerIcons.BrandGooglePlay
                    ),
                    version = "1.0.2",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v1_0_2_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 19)

                UpdateItem(
                    version = "0.7.1-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_7_1_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 14)

                UpdateItem(
                    version = "0.7.0-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_7_0_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 14)

                UpdateItem(
                    version = "0.6.1-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_6_1_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 12)

                UpdateItem(
                    version = "0.6.0-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_6_0_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 10)

                UpdateItem(
                    version = "0.5.2-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_5_2_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 10)

                UpdateItem(
                    version = "0.4.3-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_4_3_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 8)

                UpdateItem(
                    highlightUpdate = HighlightUpdate(
                        title = stringResource(R.string.closed_beta_2),
                        text = stringResource(R.string.closed_beta_2_started),
                        icon = TablerIcons.Planet
                    ),
                    version = "0.4.2-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_4_2_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 5, 7)

                UpdateItem(
                    version = "0.4.1-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_4_1_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 30)

                UpdateItem(
                    version = "0.3.2-alpha/0.3.3-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_3_2_a_0_3_3_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 30)

                UpdateItem(
                    version = "0.3.1-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_3_1_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 30)

                UpdateItem(
                    version = "0.3.0-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_3_0_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 14)

                UpdateItem(
                    version = "0.2.1-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_2_1_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 11)

                UpdateItem(
                    version = "0.2.0-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_2_0_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 8)

                UpdateItem(
                    version = "0.1.1-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = stringArrayResource(R.array.v0_1_1_notes).toList()
                )
            }

            item {
                val date = LocalDate.of(2026, 4, 7)

                UpdateItem(
                    highlightUpdate = HighlightUpdate(
                        title = stringResource(R.string.closed_beta),
                        text = stringResource(R.string.closed_beta_started),
                        icon = TablerIcons.User
                    ),
                    version = "0.1.0-alpha",
                    date = date.toDisplayableDate().formatted,
                    releaseNotes = emptyList()
                )
            }
        }
    }
}