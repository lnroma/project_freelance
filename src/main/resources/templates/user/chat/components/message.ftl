<#if messages??>
<#list messages as msg>
    <div class="message
    <#if currentUser.id == msg.sender.id>
        incoming
        <#else>
        outgoing
    </#if>
">
        ${msg.message}
        <div class="message-meta">
            ${msg.createdAt?datetime("yyyy-MM-dd'T'HH:mm:ss.SSS")?string("dd-MM-yy HH:mm")}
        </div>
    </div>
</#list>
<#else>
    Сообшений пока нет
</#if>