import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.gson.Gson
import java.io.File
import javax.swing.JFileChooser

@Composable
@Preview
fun App(
    selectedFile: String,
    onSelect: (String) -> Unit
) {
    MaterialTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Selected File:")
            Text(selectedFile)
            Button(onClick = {
                openFileDialog(onSelect)
            }) {
                Text("파일 열기")
            }
            Button(onClick = {
                convertFile(selectedFile)
            }) {
                Text("변환하기")
            }
        }
    }
}

data class KickEvents(
    val kickedIds: List<Long>,
)

data class CreateInfo(
    val members: List<Long>,
    val pg_members: List<Long>,
)

data class ChatInfo(
    val eventInfo: KickEvents,
    val inviteUsers: List<Long>,
    val createInfo: CreateInfo,
)

data class MessageContent(
    val body: String,
    val connectInfo: List<Long?>,
)

data class MessageInfo(
    val mention: List<Long?>
)

class LinkPreview()
data class Message(
    val content: MessageContent,
    val info: MessageInfo,
    val from: String?,
    val deleterId: Long?,
    val updateTime: Long?,
    val linkPreviewId: Long?,
    val status: String?,
    val shareEntities: List<Long?>,
    val feedbackId: Long?,
    val pollId: Long?,
    val postId: Long?,
    val todoId: Long?,
    val isFormatted: Boolean,
    val formatKey: String?,
    val commentCount: Int,
    val attachments: List<Long?>,
    val sharedMessageIds: List<Long?>,
    val isEdited: Boolean,
    val isThreaded: Boolean,
    val updatedAt: String?,
    val id: Long,
    val teamId: Long,
    val writerId: Long,
    val contentType: String,
    val permission: Int,
    val createdAt: String,
    val mentions: List<Long?>,
    val likedCount: Int,
    val createTime: Long,
    val linkPreview: LinkPreview,
    val isStarred: Boolean,
)

data class ChatRecord(
    val id: Long,
    val fromEntity: Long,
    val teamId: Long,
    val info: ChatInfo,
    val pollId: Long?,
    val feedbackType: Long?,
    val feedbackId: Long?,
    val status: String?,
    val messageId: Long,
    val time: Long,
    val toEntity: List<Long>,
    val message: Message
)

data class JsonData(
    val entityId: Long,
    val globalLastLinkId: Long,
    val firstLinkId: Long,
    val lastLinkId: Long,
    val isLimited: Boolean,
    val records: List<ChatRecord>
)

fun convertFile(selectedFile: String) {
    println("convertFile: $selectedFile")
    // read the json and convert it to a list of objects
    val gson = Gson()
    // read the json file
    val text = File(selectedFile).readText(Charsets.UTF_8)
//    print("Text: $text")
    val json = gson.fromJson(text, JsonData::class.java)
    print(json.entityId)
    print(json.globalLastLinkId)
    print(json.firstLinkId)
    print(json.lastLinkId)
    print(json.isLimited)
    for (record in json.records) {
        print(record.id)
        print(record.fromEntity)
        print(record.teamId)
        print(record.info)
        print(record.pollId)
        print(record.feedbackType)
        print(record.feedbackId)
        print(record.status)
        print(record.messageId)
        print(record.time)
        print(record.toEntity)
        print(record.message)
        println()
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
            }
        )
    }
}
