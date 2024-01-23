import com.google.gson.Gson
import java.io.File
import java.sql.Connection
import java.sql.DriverManager


fun prepareSchema(
    connection: Connection,
) {
    val statement = connection.createStatement()
    statement.executeUpdate(
        """
        CREATE TABLE IF NOT EXISTS Message (
            id INTEGER PRIMARY KEY,
            `from` TEXT,
            deleterId INTEGER,
            updateTime INTEGER,
            linkPreviewId INTEGER,
            status TEXT,
            isFormatted BOOLEAN,
            formatKey TEXT,
            commentCount INTEGER,
            isEdited BOOLEAN,
            isThreaded BOOLEAN,
            updatedAt TEXT,
            teamId INTEGER,
            writerId INTEGER,
            contentType TEXT,
            permission INTEGER,
            createdAt TEXT,
            likedCount INTEGER,
            createTime INTEGER,
            isStarred BOOLEAN,
            body TEXT,
            connectInfo TEXT
        );
    """.trimIndent()
    )
    statement.executeUpdate(
        """
    CREATE TABLE IF NOT EXISTS Metadata(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        key TEXT UNIQUE,
        value TEXT
    );
    """.trimIndent()
    )
}

fun insertOrUpdateMetadata(
    connection: Connection,
    key: String,
    value: String
) {
    val statement = connection.prepareStatement(
        """
        INSERT INTO Metadata (key, value)
        VALUES (?, ?)
        ON CONFLICT(key) 
        DO UPDATE SET value = excluded.value;
        """.trimIndent()
    )
    statement.setString(1, key)
    statement.setString(2, value)
    statement.executeUpdate()
}


fun convertFile(
    absFilePath: String,
    outFilePath: String,
) {
    println("convertFile: $absFilePath")
    // read the json and convert it to a list of objects
    val gson = Gson()
    // read the json file
    val text = File(absFilePath).readText(Charsets.UTF_8)
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
    // Prepare the output file
    val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$outFilePath")
    prepareSchema(connection)
    // Insert the metadata
    insertOrUpdateMetadata(connection, "entityId", json.entityId.toString())
    insertOrUpdateMetadata(connection, "globalLastLinkId", json.globalLastLinkId.toString())
    insertOrUpdateMetadata(connection, "firstLinkId", json.firstLinkId.toString())
    insertOrUpdateMetadata(connection, "lastLinkId", json.lastLinkId.toString())
    insertOrUpdateMetadata(connection, "isLimited", json.isLimited.toString())
    // Insert the records, using prepared statement
    val insertStatement = connection.prepareStatement(
        """
        INSERT INTO Message (
            id,
            `from`,
            deleterId,
            updateTime,
            linkPreviewId,
            status,
            isFormatted,
            formatKey,
            commentCount,
            isEdited,
            isThreaded,
            updatedAt,
            teamId,
            writerId,
            contentType,
            permission,
            createdAt,
            likedCount,
            createTime,
            isStarred,
            body,
            connectInfo
        ) VALUES
        (
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?
        );
    """.trimIndent()
    )
    for (record in json.records) {
        insertStatement.setLong(1, record.message.id)
        insertStatement.setString(2, record.message.from)
        insertStatement.setLong(3, record.message.deleterId ?: -1)
        insertStatement.setLong(4, record.message.updateTime ?: -1)
        insertStatement.setLong(5, record.message.linkPreviewId ?: -1)
        insertStatement.setString(6, record.message.status ?: "")
        insertStatement.setBoolean(7, record.message.isFormatted)
        insertStatement.setString(8, record.message.formatKey ?: "")
        insertStatement.setInt(9, record.message.commentCount)
        insertStatement.setBoolean(10, record.message.isEdited)
        insertStatement.setBoolean(11, record.message.isThreaded)
        insertStatement.setString(12, record.message.updatedAt ?: "")
        insertStatement.setLong(13, record.message.teamId)
        insertStatement.setLong(14, record.message.writerId)
        insertStatement.setString(15, record.message.contentType)
        insertStatement.setInt(16, record.message.permission)
        insertStatement.setString(17, record.message.createdAt)
        insertStatement.setInt(18, record.message.likedCount)
        insertStatement.setLong(19, record.message.createTime)
        insertStatement.setBoolean(20, record.message.isStarred)
        insertStatement.setString(21, record.message.content.body)
        insertStatement.setString(22, record.message.content.connectInfo.toString())
        insertStatement.addBatch()
    }
    insertStatement.executeBatch()
    connection.close()
    println("convertFile: $absFilePath -> $outFilePath")
}