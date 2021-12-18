package com.corrot.kwiatonomousapp.presentation.dasboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.corrot.kwiatonomousapp.R
import com.corrot.kwiatonomousapp.common.components.*
import com.corrot.kwiatonomousapp.presentation.Screen
import com.corrot.kwiatonomousapp.presentation.dasboard.components.DeviceConfigurationItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.ZoneOffset

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            if (!state.error.isNullOrBlank()) {
                Text(
                    text = state.error,
                    textAlign = TextAlign.Center,
                )
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isLoading),
                onRefresh = {
                    viewModel.refreshDevice()
                }
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    // Device
                    if (state.device != null) {
                        item {
                            DeviceItem(
                                device = state.device,
                                onItemClick = {}
                            )
                            Divider(
                                color = MaterialTheme.colors.primaryVariant, thickness = 1.dp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        // Device configuration
                        if (state.deviceConfiguration != null)
                            item {
                                Row(Modifier.fillMaxWidth()) {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.device_configuration),
                                            style = MaterialTheme.typography.h2,
                                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        IconButton(onClick = {
                                            navController.navigate(
                                                Screen.DeviceSettings.withArgs(
                                                    state.device.id
                                                )
                                            )
                                        }) {
                                            Icon(Icons.Filled.Edit, "")
                                        }
                                    }
                                }
                                DeviceConfigurationItem(
                                    deviceConfiguration = state.deviceConfiguration
                                )
                                Divider(
                                    color = MaterialTheme.colors.primaryVariant, thickness = 1.dp,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }
                    }


                    // Device updates
                    if (state.deviceUpdates.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.last_updates),
                                style = MaterialTheme.typography.h2,
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(vertical = 8.dp)
                            ) {
                                DateLineChart(
                                    xData = state.deviceUpdates.map {
                                        it.updateTime.toEpochSecond(
                                            ZoneOffset.UTC
                                        )
                                    },
                                    yData = when (viewModel.selectedChartDataTypeState.value) {
                                        LineChartDataType.TEMPERATURE -> state.deviceUpdates.map { it.temperature }
                                        LineChartDataType.HUMIDITY -> state.deviceUpdates.map { it.humidity }
                                        LineChartDataType.BATTERY -> state.deviceUpdates.map { it.batteryVoltage }
                                    },
                                    isLoading = state.isLoading,
                                    fromDate = viewModel.currentDateRangeState.value.first,
                                    toDate = viewModel.currentDateRangeState.value.second,
                                    dateType = viewModel.selectedChartDateTypeState.value,
                                    title = viewModel.selectedChartDataTypeState.value.name,
                                    yAxisUnit = when (viewModel.selectedChartDataTypeState.value) {
                                        LineChartDataType.TEMPERATURE -> "°C"
                                        LineChartDataType.HUMIDITY -> "%"
                                        LineChartDataType.BATTERY -> "V"
                                    },
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                CustomRadioGroup(
                                    options = LineChartDateType.values().map { it.name },
                                    selectedOption = viewModel.selectedChartDateTypeState.value.name,
                                    onOptionSelected = {
                                        viewModel.onChartDateTypeSelected(
                                            LineChartDateType.valueOf(
                                                it
                                            )
                                        )
                                    }
                                )
                                CustomRadioGroup(
                                    options = LineChartDataType.values().map { it.name },
                                    selectedOption = viewModel.selectedChartDataTypeState.value.name,
                                    onOptionSelected = {
                                        viewModel.onChartDataTypeSelected(
                                            LineChartDataType.valueOf(
                                                it
                                            )
                                        )
                                    }
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}


