package com.example.testecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testecompose.ui.theme.TesteComposeTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ExpandableListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TesteComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: ExpandableListViewModel
) {

    val itemIds by viewModel.itemsIds.collectAsState()

    Scaffold(
        topBar = { TopBar() }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            itemsIndexed(viewModel.items.value) { index, item ->
                ExpandableContainerView(
                    itemModel = item,
                    isExpanded = itemIds.contains(index),
                    onClickItem = { viewModel.onItemClicked(index) }
                )/* {
                    viewModel.onItemClicked(index)
                }*/
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(text = "Exapandable List Jetpack Compose Example", fontSize = 18.sp)
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.purple_700),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun HeaderView(
    questionText: String,
    isExpanded: Boolean = false,
    onClickItem: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "")

    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.purple_200))
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = onClickItem
            )
            .padding(8.dp)
    ) {
        Text(
            text = questionText,
            fontSize = 17.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.chevron),
            contentDescription = "chevron",
            colorFilter = ColorFilter.tint(
                color = Color.White,
                blendMode = BlendMode.SrcIn
            ),
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterEnd)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing
                    )
                )
                .rotate(rotation)
        )
    }
}

@Composable
fun ExpandableView(
    modifier: Modifier = Modifier,
    answerText: String,
    isExpanded: Boolean = false
) {
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Box(
            modifier = Modifier
                .background(color = colorResource(id = R.color.purple_500))
                .padding(15.dp)
        ) {
            Text(
                text = answerText,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ExpandableContainerView(
    itemModel: DataModel,
    isExpanded: Boolean = false,
    onClickItem: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.purple_500))
    ) {
        Column {
            HeaderView(
                questionText = itemModel.question,
                isExpanded = isExpanded,
                onClickItem = onClickItem
            )
            ExpandableView(answerText = itemModel.answer, isExpanded = isExpanded)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HeaderViewPreview() {
    TesteComposeTheme {
        HeaderView(questionText = "Android", isExpanded = false) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableViewPreview() {
    TesteComposeTheme {
        ExpandableView(answerText = "Android", isExpanded = true)
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableContainerViewPreview() {
    TesteComposeTheme {
        ExpandableContainerView(
            itemModel = DataModel("Question", "Answer"),
            isExpanded = true,
            onClickItem = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TesteComposeTheme {
        val viewModel = ExpandableListViewModel()
        MainScreen(viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TesteComposeTheme {
        TopBar()
    }
}

