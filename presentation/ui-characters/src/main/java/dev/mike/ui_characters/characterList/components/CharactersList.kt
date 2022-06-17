package dev.mike.ui_characters.characterList.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import dev.mike.domain.model.Character

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharactersListColumn(
    items: LazyPagingItems<Character>,
    listState: LazyListState = rememberLazyListState(),
    navigate: (Int) -> Unit = {}
) {
    LazyColumn(
        state = listState,
        modifier = Modifier

    ) {

        items(items) { character ->
            CharacterUI(character = character!!,modifier = Modifier.animateItemPlacement()) { id ->

                navigate(id)
            }
        }

        items.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 50.dp,
                                    bottom = 50.dp
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(30.dp)
                            )
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {

                    item {
                        Box(
                            modifier =  Modifier.fillMaxWidth()
                                .padding(bottom = 56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.height(30.dp))
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val errorMessage = items.loadState.refresh as LoadState.Error
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 56.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val errorText =
                                    if (errorMessage.error.localizedMessage!!
                                        .contains("404")
                                    ) "Character not Found"
                                    else
                                        errorMessage.error.localizedMessage
                                Text(errorText)
                               /* Button(onClick = { retry() }) {
                                    Text(text = "Try Again")
                                }*/
                            }
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    val errorMessage = items.loadState.append as LoadState.Error

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 56.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = errorMessage.error.localizedMessage!!)
                                Button(onClick = { retry() }) {
                                    Text(text = "Try Again")
                                }
                            }
                        }
                    }
                }
            }
        }
/**  WARNING adding this to a  lazy column makes iit lose state
        item {
            Box(modifier = Modifier.height(60.dp)
                .padding(56.dp))
        }*/
    }
}
