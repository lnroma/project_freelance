<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Чат</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js" integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js" integrity="sha384-cuYeSxntonz0PPNlHhBs68uyIAVpIIOZZ5JqeqvYYIcEL727kskC66kF92t6Xl2V" crossorigin="anonymous"></script>
    <!-- Font Awesome для иконок (если понадобятся позже) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        :root {
            --chat-bg: #f8f9fa;
            --sidebar-bg: #ffffff;
            --border-color: #dee2e6;
            --accent-color: #ff7e5f; /* оранжево‑красный акцент */
            --accent-hover: #e55e4e;
        }

        body {
            background-color: var(--chat-bg);
            height: 100vh;
            margin: 0;
            display: flex;
            flex-direction: column;
        }

        .chat-container {
            display: flex;
            height: calc(100vh - 56px); /* минус высота хедера, если он есть */
            overflow: hidden;
        }

        /* Левая панель: список чатов */
        .chat-sidebar {
            width: 320px;
            border-right: 1px solid var(--border-color);
            background: var(--sidebar-bg);
            display: flex;
            flex-direction: column;
            padding: 8px;
        }

        .chat-sidebar .chat-item {
            padding: 12px 16px;
            border-radius: 8px;
            cursor: pointer;
            transition: background 0.2s;
            border: 1px transparent solid;
        }

        .chat-sidebar .chat-item:hover {
            background-color: #f1f3f5;
        }

        .chat-sidebar .chat-item.active {
            background-color: rgba(255, 126, 95, 0.1);
            border-color: var(--accent-color);
        }

        .chat-item-name {
            font-weight: 600;
            color: #212529;
        }

        .chat-item-last {
            font-size: 0.85rem;
            color: #6c757d;
        }

        /* Правая часть: область сообщений */
        .chat-area {
            flex: 1;
            position: relative;
            display: flex;
            flex-direction: column;
            background: #fff;
        }

        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 20px;
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .message {
            max-width: 70%;
            padding: 10px 14px;
            border-radius: 12px;
            font-size: 0.95rem;
            line-height: 1.4;
            word-wrap: break-word;
        }

        .message.incoming {
            align-self: flex-start;
            background-color: #e9ecef;
            color: #212529;
            border-bottom-left-radius: 4px;
        }

        .message.outgoing {
            align-self: flex-end;
            background-color: var(--accent-color);
            color: #ffffff;
            border-bottom-right-radius: 4px;
        }

        .message-meta {
            font-size: 0.75rem;
            opacity: 0.8;
            margin-top: 4px;
            text-align: right;
        }

        /* Нижняя панель: поле ввода */
        .chat-input-area {
            padding: 12px;
            background: #fff;
            border-top: 1px solid var(--border-color);
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .chat-input {
            flex: 1;
            padding: 10px 12px;
            border-radius: 24px;
            border: 1px solid var(--border-color);
            outline: none;
        }

        .chat-input:focus {
            border-color: var(--accent-color);
            box-shadow: 0 0 0 3px rgba(255, 126, 95, 0.15);
        }

        .send-btn {
            background-color: var(--accent-color);
            color: white;
            border: none;
            padding: 0 18px;
            border-radius: 24px;
            cursor: pointer;
        }

        .send-btn:hover {
            background-color: var(--accent-hover);
        }

        /* Скроллбар */
        .chat-messages::-webkit-scrollbar {
            width: 6px;
        }

        .chat-messages::-webkit-scrollbar-thumb {
            background-color: #bdc3c7;
            border-radius: 3px;
        }
    </style>
</head>
<body>

<!-- Header (если нужен) -->
<!-- Header -->
<#include "../../fragments/header.ftl">

<div class="container mt-5">
    <div class="chat-container">
        <!-- Левая панель с чатами -->
        <aside class="chat-sidebar">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <span class="fw-bold">Недавние чаты</span>
                <i class="fas fa-ellipsis-v text-muted"></i>
            </div>

            <!-- Пример чата (в FreeMarker будет цикл) -->
            <#if (is_present_chats)??>
            <#list conversations as chat>
                <div class="chat-item ${chat.active?then('active','')} js-conversation"
                     data-conversation-id="${chat.conversationId}"
                     data-recipient-id="${chat.recipientId!"-1"}" >
                    <div class="d-flex flex-column">
                        <span class="chat-item-name">${(chat.recipientName)! "no-present"}</span>
                        <span class="chat-item-last">${chat.lastMessage!"test"}</span>
                    </div>
                </div>
            </#list>
            <#else>
                Воспользуйтесь поиском пользователей что бы связаться с кем либо
            </#if>
        </aside>

        <!-- Правая часть — сообщения -->
        <main class="chat-area">
            <ul class="nav nav-tabs mb-3" id="chatTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="all-messages-tab" data-bs-toggle="tab"
                                data-bs-target="#all-messages" type="button" role="tab">Все сообщения</button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="favorites-tab" data-bs-toggle="tab"
                                data-bs-target="#favorites" type="button" role="tab">Избранное</button>
                    </li>
                </ul>
             <div class="tab-content" id="chatTabsContent">
             <div class="tab-pane fade show active" id="all-messages" role="tabpanel">

            <div class="chat-messages" id="chatMessages">
                <#if (is_present_chats)??>
                    <#include "components/message.ftl">
                <#else>
                    У вас нет активных чатов
                </#if>
                </div>
             </div>
             <div class="tab-pane fade" id="favorites" role="tabpanel">
                <div> тут будут избранные сообщения
                </div>
            </div>

            <!-- Поле ввода внизу -->
            <div class="chat-input-area">
                <input type="text" class="chat-input" id="messageInput" placeholder="Напишите сообщение..."/>
                <input type="hidden" id="fromId" value="${currentUser.getId()}"/>
                <input type="hidden" id="toId" value="${userTo.getId()}" />
                <button class="send-btn js-send-message">
                    <i class="fas fa-paper-plane"></i>
                </button>
            </div>
        </main>
    </div>
</div>

<#include "../../fragments/jschat.ftl">
</body>
</html>