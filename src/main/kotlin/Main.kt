import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
) {
    MaterialTheme {
        Column {
            Text("JSON 파일을 열어서 SQLite로 변환해보세요!")
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
            Text("Converted File: $outFile")
            // 실제 리스트뷰, 테이블 뷰
            // 열린파일이 SQLite 파일일 경우, 테이블 뷰 보여줌
            ChatView()
        }
    }
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
    column: (item: T, col: Int) -> String
) {
    LazyColumn(Modifier.horizontalScroll(rememberScrollState())) {
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
}

@Composable
fun ChatView() {
    TableView(
        titles = listOf(
            "이름" to 100.dp,
            "나이" to 100.dp,
            "성별" to 100.dp,
        ),
        items = listOf(
            "홍길동" to 20 to "남",
            "홍길순" to 21 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
            "홍길자" to 22 to "여",
        ),
        column = { item, col ->
            when (col) {
                0 -> item.first.first
                1 -> item.first.second.toString()
                2 -> item.second
                else -> ""
            }
        },
    )
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
        MenuBar {
            Menu("파일", mnemonic = 'F') {
                Item("열기", onClick = {
                    openFileDialog {
                        selectedFile = it
                    }
                })
                Item("저장", onClick = { /* 파일 저장 로직 */ })
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
        )
    }
}
