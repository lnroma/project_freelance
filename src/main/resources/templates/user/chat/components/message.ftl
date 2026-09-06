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
        <hr/>
        <div class="message-meta">
            <#if !isFavorite >
            <a href="/user/message/${msg.id}/add/to/favorite">Добавить в избранное</a>
            </#if>
            ${msg.createdAt?datetime("yyyy-MM-dd'T'HH:mm:ss.SSS")?string("dd-MM-yy HH:mm")}
            <#if isFavorite >
                | Добавлено в избранное - ${msg.favoritedAt?datetime("yyyy-MM-dd'T'HH:mm:ss.SSS")?string("dd-MM-yy HH:mm")}
            </#if>
        </div>
    </div>
</#list>
<#else>
    Сообшений пока нет
</#if>