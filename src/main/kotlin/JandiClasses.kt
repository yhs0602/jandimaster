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
    val height:  Long,
    val width: Long,
    val largeThumbnailUrl: String,
    val mediumThumbnailUrl: String,
)
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
data class Attachment(
    val id: Long,
    val contentType: String,
    val content: AttachmentContent,
)

data class Mention(
    val id: Long,
    val type: String,
    val offset: Int,
    val length: Int,
)

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
    val feedbackType: String?,
    val feedbackId: Long?,
    val status: String?,
    val messageId: Long,
    val time: Long,
    val toEntity: List<Long>,
    val message: Message?
)

data class JsonData(
    val entityId: Long,
    val globalLastLinkId: Long,
    val firstLinkId: Long,
    val lastLinkId: Long,
    val isLimited: Boolean,
    val records: List<ChatRecord>
)