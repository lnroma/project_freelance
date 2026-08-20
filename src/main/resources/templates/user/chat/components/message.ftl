<#if messages??>
<#list messages as msg>
    <div class="message
<#--${msg.senderId == currentUserId?then('outgoing','incoming')}-->
outgoing
">
        ${msg.message}
        <div class="message-meta">
            ${msg.createdAt}
        </div>
    </div>
</#list>
<#else>
    Сообшений пока нет
</#if>