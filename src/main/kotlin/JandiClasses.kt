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