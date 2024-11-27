package com.langsapp.android.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.langsapp.android.app.R
import com.langsapp.android.ui.ButtonOutlined
import com.langsapp.android.ui.ButtonPrimary
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.home.HomeAction
import com.langsapp.home.HomeState
import com.langsapp.home.welcome.WelcomeSlide
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    actionSender: ActionSender<Action>,
    welcomeState: HomeState.Welcome,
) {
    val slides = listOf(
        WelcomeSlide("Welcome slide 1"),
        WelcomeSlide("Welcome slide 2"),
        WelcomeSlide("Welcome slide 3")
    )

    val slideImage = remember { mutableStateOf(R.drawable.image_welcome_example) }
    val slideTitle = remember { mutableStateOf("Slide title") }
    val slideText = remember { mutableStateOf("Slide text") }
    val pagerState = rememberPagerState(pageCount = { 3 })

    Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxSize().padding(dimensionResource(R.dimen.margin_8x))) {
        if (pagerState.currentPage == slides.size - 1) {
            SkipButton { actionSender.sendAction(HomeAction.SkipTapped) }
        }
    }
    Box(contentAlignment = Alignment.Center) {
        Column {
            HorizontalPager(state = pagerState) { page ->
                slideImage.value = R.drawable.image_welcome_example
                slideTitle.value = slides[page].text
                slideText.value = slides[page].text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.margin_8x),
                    )
                ) {
                    SlideImage(drawableResId = slideImage.value)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_2x)))
                    SlideTitleText(slideTitle.value)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_2x)))
                    SlideText(slideText.value)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_8x)))
                }
            }
        }
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_1x)))
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.margin_4x)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SlideButton(
                    pagerState = pagerState,
                    slidesSize = slides.size,
                    onStartButtonClicked = {
                        // TODO
                    }
                )
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_4x)))
                CircleIndicator(
                    circlesNumber = slides.size,
                    selectedIndex = pagerState.currentPage
                )
            }
        }
    }
}

@Composable
fun SlideImage(drawableResId: Int) {
    Box(
        modifier = Modifier
            .aspectRatio(1.0F)
            .padding(dimensionResource(id = R.dimen.margin_4x))
    ) {
        Image(
            painterResource(id = drawableResId),
            contentDescription = ""
        )
    }
}

@Composable
fun SlideTitleText(text: String) {
    Text(
        textAlign = TextAlign.Center,
        text = text,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun SlideText(text: String) {
    Text(
        textAlign = TextAlign.Center,
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideButton(pagerState: PagerState, slidesSize: Int, onStartButtonClicked: () -> Unit) {
    val scope = rememberCoroutineScope()
    ButtonPrimary(
        onClick = {
            scope.launch {
                if (pagerState.currentPage == slidesSize - 1) {
                    onStartButtonClicked()
                } else {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.margin_8x)),
        text = if (pagerState.currentPage == slidesSize - 1) stringResource(R.string.button_start) else stringResource(R.string.button_next)
    )
}

@Composable
fun CircleIndicator(circlesNumber: Int, selectedIndex: Int) {
    LazyRow {
        items(circlesNumber) { index ->
            Circle(isSelected = index == selectedIndex)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}

@Composable
fun Circle(isSelected: Boolean) {
    Image(
        painter = painterResource(id = if (isSelected) R.drawable.ic_circle else R.drawable.ic_outline_circle),
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.circle_indicator_size))
            .fillMaxSize(),
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
        contentDescription = null
    )
}

@Composable
fun SkipButton(onSkipButtonClicked: () -> Unit) {
    ButtonOutlined(
        onClick = {
            onSkipButtonClicked()
        },
        text = stringResource(R.string.button_skip)
    )
}