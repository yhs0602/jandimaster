import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun exportChatData(exportFileName: String, messages: List<Message>) {
    // serialize the message list to a csv file
    val file = File(exportFileName)
// Define the CSV header
    val header = "writer,content,created at\n"

    // Write the header to the file
    file.writeText(header)

    // Define a formatter for KST (Korea Standard Time)
    val kstFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val kstZone = ZoneId.of("Asia/Seoul")

    // Iterate over the messages and write each as a CSV row
    messages.forEach { message ->
        val writer = message.writerId.toString()
        val content = message.content.body

        // Parse the createdAt to ZonedDateTime and convert to KST
        val createdAt = message.createdAt?.let {
            ZonedDateTime.parse(it).withZoneSameInstant(kstZone).format(kstFormatter)
        } ?: ""

        // Create the CSV row
        val csvRow = "$writer,\"$content\",$createdAt\n"

        // Append the row to the file
        file.appendText(csvRow)
    }
}