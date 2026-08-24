<h5>Вопросы и ответы по заказу</h5>
<#if order.orderFaqEntities?has_content && order.orderFaqEntities?size gt 0 >
    <#list order.orderFaqEntities as faq >
        <div class="app-card primary-border mb-3">
            <div class="row">
                <div class="col-md-6 right-border">
                    ${faq.question}
                </div>
                <div class="col-md-6">
                    ${faq.response! "На данный вопрос еще не было ответа"}
                    <#if !faq.response??>
                        <#if isAuthenticated && user.id == order.creator.id >
                        <form action="/catalog/order/${order.id}/response/question" method="post">
                            <textarea class="form-control" rows="2" name="response"></textarea>
                            <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}" />
                            <input type="hidden" name="id" value="${faq.id}" />
                            <button type="submit" class="btn mt-2 btn-outline-primary">Ответить</button>
                        </form>
                        </#if>
                    </#if>
                </div>
            </div>
        </div>
    </#list>
<#else>
    <div class="app-card primary-border">
        На данный момент по заказу нету вопросов
    </div>
</#if>