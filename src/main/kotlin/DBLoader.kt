import java.sql.Connection
import java.sql.DriverManager

fun getChatData(databasePath: String): List<Message> {
    val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$databasePath")
    val query = "SELECT * FROM Message"
    val resultSet = connection.createStatement().executeQuery(query)
    val chatRecords = mutableListOf<Message>()

    while (resultSet.next()) {
        // resultSet에서 데이터를 읽고 ChatRecord 객체로 변환
        chatRecords.add(
            Message(
                id = resultSet.getLong("id"),
                from = resultSet.getString("from"),
                deleterId = resultSet.getLong("deleterId"),
                updateTime = resultSet.getLong("updateTime"),
                linkPreviewId = resultSet.getString("linkPreviewId"),
                status = resultSet.getString("status"),
                isFormatted = resultSet.getBoolean("isFormatted"),
                formatKey = resultSet.getString("formatKey"),
                commentCount = resultSet.getInt("commentCount"),
                isEdited = resultSet.getBoolean("isEdited"),
                isThreaded = resultSet.getBoolean("isThreaded"),
                updatedAt = resultSet.getString("updatedAt"),
                teamId = resultSet.getLong("teamId"),
                writerId = resultSet.getLong("writerId"),
                contentType = resultSet.getString("contentType"),
                permission = resultSet.getInt("permission"),
                createdAt = resultSet.getString("createdAt"),
                likedCount = resultSet.getInt("likedCount"),
                createTime = resultSet.getLong("createTime"),
                isStarred = resultSet.getBoolean("isStarred"),
                attachments = listOf(),
                content = MessageContent(
                    body = resultSet.getString("body"),
                    connectInfo = listOf(),
                ),
                feedbackId = null,
                pollId = null,
                postId = null,
                info = MessageInfo(
                    mention = listOf(),
                ),
                linkPreview = LinkPreview(),
                sharedMessageIds = listOf(),
                shareEntities = listOf(),
                mentions = listOf(),
                todoId = null,
            )
        )
    }

    connection.close()
    return chatRecords
}
