package de.lijucay.damier.core.presentation.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.presentation.models.ActivityFormState
import de.lijucay.damier.core.presentation.components.ExpandableLayout
import de.lijucay.damier.core.presentation.components.NumberTextField
import de.lijucay.damier.core.presentation.components.ReferenceTypeRow
import de.lijucay.damier.core.presentation.components.UnitBadge
import de.lijucay.damier.core.presentation.components.SwitchPreference

@Composable
fun ReferenceCard(
    state: ActivityFormState,
    focusRequester: FocusRequester,
    onUseReferenceToggle: () -> Unit,
    onShowReferenceTypesToggle: () -> Unit,
    onFocusRequested: () -> Unit,
    onReferenceTypeSelected: (ReferenceType) -> Unit,
    onReferenceValueChange: (TextFieldValue) -> Unit,
) {
    ConditionalOptionsCard(
        showOption = state.useReference,
        parentLayout = {
            SwitchPreference(
                checked = state.useReference,
                title = stringResource(R.string.override_reference_type),
                subTitle = stringResource(R.string.override_reference_type_explanation),
                columnPadding = PaddingValues(16.dp),
                onCheckedChange = { onUseReferenceToggle() }
            )
        },
        optionLayout = {
            ExpandableLayout(
                title = stringResource(R.string.reference_type),
                expanded = state.showReferenceTypes,
                arrowContent = {
                    UnitBadge(
                        text = "${
                            state.referenceType.name.lowercase().replaceFirstChar { it.uppercase() }
                        }: ${state.reference.text}"
                    )
                }
            ) {
                Column {
                    ReferenceType.entries.forEach { type ->
                        if (type != ReferenceType.MAX) {
                            ReferenceTypeRow(
                                type = type,
                                selected = type == state.referenceType,
                                onClick = { onReferenceTypeSelected(type) }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(shapes.extraLarge)
                            .clickable(onClick = onFocusRequested)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${
                                    state.referenceType.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                }:"
                            )
                            NumberTextField(
                                value = state.reference,
                                onValueChange = onReferenceValueChange,
                                focusRequester = focusRequester
                            )
                        }
                    }
                }
            }
        },
        onParentClicked = onUseReferenceToggle,
        onOptionClicked = onShowReferenceTypesToggle
    )
}