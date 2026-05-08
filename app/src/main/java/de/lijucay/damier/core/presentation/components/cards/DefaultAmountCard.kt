package de.lijucay.damier.core.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.models.ActivityFormState
import de.lijucay.damier.core.presentation.components.NumberTextField
import de.lijucay.damier.core.presentation.components.SwitchPreference
import de.lijucay.damier.design.components.TitleText

@Composable
fun DefaultAmountCard(
    state: ActivityFormState,
    focusRequester: FocusRequester,
    onUseDefaultAmountToggle: () -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusRequested: () -> Unit
) {
    ConditionalOptionsCard(
        showOption = state.useDefaultAmount,
        parentLayout = {
            SwitchPreference(
                checked = state.useDefaultAmount,
                title = stringResource(R.string.override_default_amount),
                subTitle = stringResource(R.string.use_default_amount_explanation),
                columnPadding = PaddingValues(16.dp),
                onCheckedChange = { onUseDefaultAmountToggle() }
            )
        },
        optionLayout = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TitleText(text = stringResource(R.string.default_amount))
                NumberTextField(
                    value = state.defaultAmount,
                    onValueChange = onValueChange,
                    focusRequester = focusRequester
                )
            }
        },
        optionPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
        onParentClicked = onUseDefaultAmountToggle,
        onOptionClicked = onFocusRequested
    )
}