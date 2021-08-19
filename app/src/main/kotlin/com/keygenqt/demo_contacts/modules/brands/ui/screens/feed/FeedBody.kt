package com.keygenqt.demo_contacts.modules.brands.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.keygenqt.demo_contacts.R
import com.keygenqt.demo_contacts.base.LocalBaseViewModel
import com.keygenqt.demo_contacts.extensions.ListenRefresh
import com.keygenqt.demo_contacts.modules.brands.data.relations.FeedRelation
import com.keygenqt.demo_contacts.modules.brands.ui.events.BrandsEvents
import com.keygenqt.demo_contacts.modules.common.ui.compose.components.MainScaffold
import com.keygenqt.demo_contacts.modules.common.ui.compose.screens.EmptyListScreen
import com.keygenqt.demo_contacts.modules.common.ui.compose.screens.LoadingScreen

@ExperimentalCoilApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@Composable
fun FeedBody(
    feed: FeedRelation?,
    commonError: String? = null,
    loading: Boolean = true,
    onEvent: (BrandsEvents) -> Unit = {},
) {
    MainScaffold(
        title = stringResource(id = R.string.app_name),
        searchListener = { search ->

        }
    ) {

        LocalBaseViewModel.current.ListenRefresh {
            onEvent(BrandsEvents.RefreshFeed)
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = { onEvent(BrandsEvents.RefreshFeed) },
            indicator = { st, tr ->
                SwipeRefreshIndicator(
                    state = st,
                    refreshTriggerDistance = tr,
                    contentColor = MaterialTheme.colors.onPrimary,
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            feed?.let { model ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        FeedItemBanners(
                            banners = model.banners,
                            onEvent = onEvent
                        )
                    }
                    item {
                        FeedItemBrands(
                            name = model.owner.brandName,
                            brands = model.brands,
                            onEvent = onEvent
                        )
                    }
                    item {
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable(onClick = {

                                })
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .size(48.dp)
                                        .background(Color.Black)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ic_city_pin),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                                Column {
                                    Text(
                                        color = MaterialTheme.colors.onBackground,
                                        text = stringResource(id = R.string.brands_btn_shops),
                                        modifier = Modifier
                                            .padding(start = 16.dp, end = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {

                                })
                        ) {
                            Image(
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(R.drawable.ic_new_items),
                                contentDescription = stringResource(id = R.string.brands_new_items),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }
                }
            } ?: run {
                if (!loading) {
                    EmptyListScreen(
                        text = stringResource(id = R.string.brands_empty_feed),
                        painter = painterResource(id = R.drawable.ic_common_not_found)
                    )
                } else {
                    LoadingScreen(loading)
                }
            }
        }
    }
}