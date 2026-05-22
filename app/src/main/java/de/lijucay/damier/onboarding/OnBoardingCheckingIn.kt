package de.lijucay.damier.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.CookieButton
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.design.components.TitleText

@Composable
fun OnBoardingCheckingIn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LargeTitleText(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.how_checking_in_works)
        )

        DefaultText(
            text = stringResource(R.string.how_checking_in_works_desc)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {  },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = contentColorFor(MaterialTheme.colorScheme.primaryContainer),
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = stringResource(R.string.check_in)
                    )
                }

                TitleText(
                    text = stringResource(R.string.quick_check_in)
                )

                DefaultText(
                    text = stringResource(R.string.quick_check_in_expl)
                )

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )

                        Text(
                            text = stringResource(R.string.default_amount_info)
                        )
                    }
                }

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.available_on_quick_check_in)
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CookieButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = contentColorFor(MaterialTheme.colorScheme.primaryContainer),
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) { }

                TitleText(
                    text = stringResource(R.string.check_in)
                )

                DefaultText(
                    text = stringResource(R.string.check_in_expl)
                )

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.available_on_check_in)
                    )
                }
            }
        }
    }
}