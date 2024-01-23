import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
            // 실제 리스트뷰, 테이블 뷰

        }

    }
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
