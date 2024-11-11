import kotlinx.serialization.Serializable

@Serializable
data class KickEvents(
    val kickedIds: List<Long> = listOf(),
)

@Serializable
data class CreateInfo(
    val members: List<Long> = listOf(),
    val pg_members: List<Long> = listOf(),
)

@Serializable
data class ChatInfo(
    val eventInfo: KickEvents? = null,
    val inviteUsers: List<Long> = listOf(),
    val createInfo: CreateInfo? = null,
)

@Serializable
data class MessageContent(
    val body: String = "",
    val connectInfo: List<Long?>,
)

@Serializable
data class MessageInfo(
    val mention: List<Long?>
)

@Serializable
class LinkPreview(
    val canonicalUrl: String? = null,
    val domain: String? = null,
    val imageUrl: String? = null,
    val _id: String? = null,
    val linkUrl: String? = null,
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
)

data class AttachmentInfo(
    val height: Long,
    val width: Long,
    val largeThumbnailUrl: String,
    val mediumThumbnailUrl: String,
)

@Serializable
data class AttachmentContent(
    val title: String,
    val name: String,
    val filename: String,
    val type: String,
    val icon: String,
    val size: Long,
    val ext: String,
    val serverUrl: String,
    val fileUrl: String,
    val externalCode: String?,
    val externalUrl: String?,
    val externalShared: Boolean,
    val filterType: String,
//    val extraInfo: AttachmentInfo,
)

@Serializable
data class Attachment(
    val id: Long,
    val contentType: String,
    val content: AttachmentContent,
)

@Serializable
data class Mention(
    val id: Long,
    val type: String,
    val offset: Int,
    val length: Int,
)

@Serializable
data class Message(
    val content: MessageContent,
    val info: MessageInfo,
    val from: String?,
    val deleterId: Long?,
    val updateTime: Long?,
    val linkPreviewId: String?,
    val status: String?,
    val shareEntities: List<Long?>,
    val feedbackId: Long?,
    val pollId: Long?,
    val postId: Long?,
    val todoId: Long?,
    val isFormatted: Boolean,
    val formatKey: String?,
    val commentCount: Int,
    val attachments: List<Attachment>,
    val sharedMessageIds: List<Long?>,
    val isEdited: Boolean,
    val isThreaded: Boolean,
    val updatedAt: String?,
    val id: Long,
    val teamId: Long,
    val writerId: Long,
    val contentType: String,
    val permission: Int,
    val createdAt: String?,
    val mentions: List<Mention>,
    val likedCount: Int = 0,
    val createTime: Long,
    val linkPreview: LinkPreview,
    val isStarred: Boolean = false,
)

@Serializable
data class ChatRecord(
    val id: Long,
    val fromEntity: Long,
    val teamId: Long,
    val info: ChatInfo?,
    val pollId: Long?,
    val feedbackType: String?,
    val feedbackId: Long?,
    val status: String?,
    val messageId: Long,
    val time: Long,
    val toEntity: List<Long>,
    val message: Message?
)

@Serializable
data class JsonData(
    val entityId: Long,
    val globalLastLinkId: Long,
    val firstLinkId: Long,
    val lastLinkId: Long,
    val isLimited: Boolean,
    val records: List<ChatRecord>
)