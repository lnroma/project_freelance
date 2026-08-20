<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<script>
    let stompClient = null;

    function connect() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            console.log('🟢 Connected:', frame);

            stompClient.subscribe('/topic/notification', function(message) {
                console.log('📩 Received:', message);
                try {
                    const data = JSON.parse(message.body);
                    console.log('���:', data);

                    // Простой тест: вывести в alert, если это наш DEBUG_MESSAGE
                    if (data.status === 'DEBUG_MESSAGE') {
                        alert('✅ ДОШЛО: ' + data.text);
                    }
                } catch (e) {
                    console.error('💥 Parse error:', e, 'body:', message.body);
                }
            });


            stompClient.subscribe('/user/queue/n', function(message) {
                console.log('📩 Received:', message);
                try {
                    const data = JSON.parse(message.body);
                    console.log('���:', data);

                    // Простой тест: вывести в alert, если это наш DEBUG_MESSAGE
                    if (data.status === 'DEBUG_MESSAGE') {
                        alert('✅ ДОШЛО: ' + data.text);
                    }
                } catch (e) {
                    console.error('💥 Parse error:', e, 'body:', message.body);
                }
            });
        }, function(error) {
            console.error('🔴 Connection error:', error);
        });
    }

    document.addEventListener('DOMContentLoaded', (e) => {
        connect()
    });
</script>