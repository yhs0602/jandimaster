import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javax.swing.JFileChooser

@Composable
@Preview
fun App(
    selectedFile: String,
    onSelect: (String) -> Unit,
    outFile: String,
    isShowCurlDialog: Boolean,
    showCurlDialog: () -> Unit,
    onCloseDialog: () -> Unit,
) {
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var scrollToIndex by remember { mutableStateOf<Int?>(null) }
    var errorString by remember { mutableStateOf("") }
    MaterialTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Error: $errorString")
            Text(
                "JSON 파일을 열어서 SQLite로 변환해보세요!",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.h5,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("잔디에서 대화 불러오기: 저장 위치: $outFile")
                Button(onClick = {
                    showCurlDialog()
                }) {
                    Text("잔디에서 JSON 다운로드하기 (작동안함: curl 직접 사용하세요)")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Selected File:")
                Text(selectedFile)
                Button(onClick = {
                    openFileDialog(onSelect)
                }) {
                    Text("파일 열기")
                }
                Button(onClick = {
                    convertFile(
                        selectedFile,
                        outFile
                    )
                }) {
                    Text("변환하기")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("SQLite File: $outFile")
                Button(onClick = {
                    try {
                        messages = getChatData(outFile)
                    } catch (e: Exception) {
                        errorString = e.stackTraceToString()
                    }
                }) {
                    Text("파일 로드하기")
                }
            }
            Row {
                var searchText by remember { mutableStateOf("") }
                SearchView(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    onSearch = { searchText ->
                        val index = messages.indexOfFirst {
                            it.content.body.contains(searchText)
                        }
                        scrollToIndex = index
                    })
                Button(onClick = {
                    val currentSearchIndex = scrollToIndex ?: 0
                    val nextIndex = messages.subList(currentSearchIndex + 1, messages.size).indexOfFirst {
                        it.content.body.contains(searchText)
                    }

                    if (nextIndex != -1) {
                        // 실제 인덱스는 현재 인덱스 + 찾은 인덱스 + 1
                        scrollToIndex = currentSearchIndex + nextIndex + 1
                    } else {
                        // 더 이상 일치하는 항목이 없을 경우 처리
                        // TODO: 메시지 박스로 처리
                        println("더 이상 일치하는 항목이 없습니다.")
                    }
                }) {
                    Text("다음 찾기")
                }
            }
            // 실제 리스트뷰, 테이블 뷰
            // 열린파일이 SQLite 파일일 경우, 테이블 뷰 보여줌
            ChatView(messages, scrollToIndex)
        }

        if (isShowCurlDialog) {
            CurlDialog(onExecute = { curlCommand, outputPath ->
                executeCurlCommand(curlCommand, outputPath)
                onCloseDialog()
            }, closeDialog = {
                onCloseDialog()
            })
        }
    }
}

@Composable
fun CurlDialog(
    onExecute: (String, String) -> Unit,
    closeDialog: () -> Unit
) {
    var curlCommand by remember { mutableStateOf("") }
    var outputPath by remember { mutableStateOf("") }

    Dialog(
        onCloseRequest = { closeDialog() }
    ) {
        Column {
            TextField(
                value = curlCommand,
                onValueChange = { curlCommand = it },
                label = { Text("Curl 명령어") }
            )
            Text("출력 파일: $outputPath")
            Button(onClick = {
                openSaveDialog {
                    outputPath = it
                }
            }) {
                Text("저장 위치 선택")
            }
            Button(onClick = { onExecute(curlCommand, outputPath) }) {
                Text("실행")
            }
            Button(onClick = { closeDialog() }) {
                Text("취소")
            }
        }
    }
}


@Composable
fun SearchView(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = { onSearchTextChange(it) },
        label = { Text("검색") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(searchText) })
    )
}


@Composable
fun TableViewHeader(titles: List<Pair<String, Dp>>) {
    Row(Modifier.height(IntrinsicSize.Min)) {
        for (t in titles) {
            CellText(t.first, Modifier.width(t.second))
        }
    }
}

@Composable
fun CellText(
    content: String,
    modifier: Modifier,
    color: Color = Color.Black,
    bkColor: Color = Color.White,
    borderColor: Color = Color.Cyan
) {
    Box(
        modifier = modifier
            .background(bkColor)
            .border(1.dp, borderColor)
            .padding(8.dp)
            .fillMaxHeight()
    ) {
        Text(text = content, color = color, modifier = Modifier.background(bkColor))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> TableView(
    titles: List<Pair<String, Dp>>,
    items: List<T>,
    modifiers: (T, Int) -> Modifier = { _, _ -> Modifier },
    onItemLongClick: (T) -> Unit = {},
    onItemClick: (T) -> Unit = {},
    column: (item: T, col: Int) -> String,
    scrollToIndex: Int? = null,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        Modifier.horizontalScroll(rememberScrollState()),
        state = listState,
    ) {
        stickyHeader {
            TableViewHeader(titles)
        }
        items(items) { item ->
            Row(
                Modifier
                    .height(IntrinsicSize.Min)
                    .combinedClickable(
                        onLongClick = { onItemLongClick(item) },
                        onClick = { onItemClick(item) }
                    )
            ) {
                for ((i, t) in titles.withIndex()) {
                    CellText(
                        content = column(item, i),
                        modifier = Modifier
                            .width(t.second)
                            .then(modifiers(item, i))
                    )
                }
            }
        }
    }
    LaunchedEffect(scrollToIndex) {
        scrollToIndex?.let {
            listState.animateScrollToItem(it)
        }
    }
}

@Composable
fun ChatView(messages: List<Message>, scrollToIndex: Int? = null) {
    TableView(
        titles = listOf(
            "writerId" to 100.dp,
            "content" to 1000.dp,
            "createdAt" to 100.dp,
            "likedCount" to 100.dp,
        ),
        items = messages,
        column = { item, col ->
            when (col) {
                0 -> item.writerId.toString()
                1 -> item.content.body
                2 -> item.createdAt?.toString() ?: ""
                3 -> item.likedCount.toString()
                // 추가 컬럼 정의
                else -> ""
            }
        },
        scrollToIndex = scrollToIndex,
    )
}

fun openSaveDialog(onSelect: (String) -> Unit) {
    val f = JFileChooser()
    f.fileSelectionMode = JFileChooser.FILES_ONLY
    f.addActionListener { e ->
        println(f.selectedFile.absolutePath)
        onSelect(f.selectedFile.absolutePath)
    }
    f.showSaveDialog(null)
}

fun openFileDialog(onSelect: (String) -> Unit) {
    val f = JFileChooser()
    f.fileSelectionMode = JFileChooser.FILES_ONLY
    f.addActionListener { e ->
        println(f.selectedFile.absolutePath)
        onSelect(f.selectedFile.absolutePath)
    }
    f.showOpenDialog(null)
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        var selectedFile by remember { mutableStateOf("") }
        // change .json to .db
        val outFile = selectedFile.replace(".json", ".db")
        var isShowCurlDialog by remember { mutableStateOf(false) }
        MenuBar {
            Menu("파일", mnemonic = 'F') {
                Item("열기", onClick = {
                    openFileDialog {
                        selectedFile = it
                    }
                })
                Item("저장", onClick = { /* 파일 저장 로직 */ })
                Item("잔디에서 대화 불러오기", onClick = {
                    isShowCurlDialog = true
                })
                Separator()
                Item("종료", onClick = { /* 애플리케이션 종료 로직 */ })
            }
            Menu("편집", mnemonic = 'E') {
                Item("복사", onClick = { /* 복사 로직 */ })
                Item("붙여넣기", onClick = { /* 붙여넣기 로직 */ })
            }
        }
        App(
            selectedFile,
            onSelect = {
                selectedFile = it
            },
            outFile = outFile,
            isShowCurlDialog = isShowCurlDialog,
            onCloseDialog = {
                isShowCurlDialog = false
            },
            showCurlDialog = {
                isShowCurlDialog = true
            }
        )
    }
}

