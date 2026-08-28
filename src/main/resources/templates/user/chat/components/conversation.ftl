<#list conversations as chat>
    <a href="/user/chat/${chat.conversationId}" style="text-decoration: none">
        <div class="chat-item mb-1
                    <#if chat.conversationId == currentConversation.id>
                        active
                    </#if>
                    ">
            <div class="d-flex flex-column">
                <span class="chat-item-name">${(chat.recipientName)! "no-present"}
                    <#if chat.countUnreadMessages gt 0>
                    <span class="badge bg-primary">${chat.countUnreadMessages}</span>
                    </#if>
                </span>
                <span class="chat-item-last">${chat.lastMessage!"test"}</span>
            </div>
        </div>
    </a>
</#list>