<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
        integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<script>

    function loadConversation(conversationId) {
        console.log("load conversation id")
    }

    function sendMessage(text, fromId, toId) {
        if (!stompClient || !stompClient.connected) {
            console.error('WebSocket not connected');
            return;
        }

        const payload = {
            conversationId: 0, // из шаблона
            senderId: parseInt(fromId),
            recipientId: parseInt(toId),
            text: text,
            createdAt: new Date().toISOString()
        };

        // ВАЖНО: destination должен совпадать с @MessageMapping в контроллере
        stompClient.send('/app/send-message', {}, JSON.stringify(payload));
    }

    function sendTriggerGetMessages(cId) {
        const payload = {
            conversationId: cId
        }

        stompClient.send('/app/load-message', {}, JSON.stringify(payload));
    }

    function renderMessage(msg) {
        const container = document.getElementById('chatMessages');
        if (!container) return;

        // Простая защита от XSS: экранируй текст сообщения вручную
        // const safeText = msg.template
        //     .replace(/&/g, '&amp;')
        //     .replace(/</g, '&lt;')
        //     .replace(/>/g, '&gt;')
        //     .replace(/"/g, '&quot;')
        //     .replace(/'/g, '&#39;');

        <#--const isMe = msg.senderId === parseInt('${currentUserId}'); // из шаблона FreeMarker-->


        container.innerHTML = msg.template;
        container.scrollTop = container.scrollHeight; // автоскролл вниз
    }

    function listenerSocket() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/user/queue/message', function (message) {
                try {
                    const payload = JSON.parse(message.body);
                    renderMessage(payload);
                } catch (e) {
                    console.log(e);
                }
                // console.log(message.body);
            });
        });
    }

    function eventListening() {
        document.querySelectorAll('.js-conversation').forEach(el => {
            el.addEventListener('click', function () {
                const conversationId = this.getAttribute('data-conversation-id');
                const recipientId = this.getAttribute('data-recipient-id');

                console.log(recipientId + " this is a recipient id ");
                localStorage.setItem("currentRecipientId", conversationId)
                // или: const conversationId = this.dataset.conversationId;
                document.querySelectorAll('.js-conversation-id').item(0)
                    .setAttribute("data-conversation-id", conversationId);

                // document.querySelector("#toId")

                // Тут логика: открыть чат, отправить запрос на бэкенд и т.п.
                sendTriggerGetMessages(conversationId);
            });
        });

        document.querySelectorAll('.js-send-message').forEach(el => {
            el.addEventListener('click', function () {
                const recipientId = localStorage.getItem("currentRecipientId");

                const text = document.getElementById('messageInput').value;
                const fId = document.getElementById('fromId').value;
                const tId = document.getElementById('toId').value;
                console.log("current recipient id " + recipientId);
                sendMessage(text, fId, recipientId);
            });
        });
    }

    document.addEventListener('DOMContentLoaded', (e) => {
        listenerSocket();
        eventListening();

        if (localStorage.getItem("currentRecipientId") == null) {
            const fId = document.getElementById('fromId').value;
            localStorage.setItem("currentRecipientId", fId);
        }

        cId = document.querySelectorAll('.js-conversation-id').item(0)
            .getAttribute("data-conversation-id");
        setTimeout(() => {
            console.log("Executed after 2 seconds");
            sendTriggerGetMessages(cId);
        }, 2000);
    });
</script>