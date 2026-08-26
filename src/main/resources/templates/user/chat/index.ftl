<!DOCTYPE html>
<html lang="ru">
<#include "../../fragments/httpHeader.ftl">
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
        height: calc(100vh - 115px); /* минус высота хедера, если он есть */
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
        height: 100px;
        overflow: hidden;
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
        height: calc(100vh - 250px);
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
        background-color: #ffffffba;
        color: #3f3232;
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
        align-items: center;
        gap: 8px;
        position: absolute;
        bottom: 0px;
        left: 0px;
        width: 100%;
    }

    .chat-input {
        flex: 1;
        padding: 10px 12px;
        border-radius: 24px;
        border: 1px solid var(--border-color);
        outline: none;
        width: 80%;
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
        background-color: #445752;
        border-radius: 3px;
    }
</style>
<body>

<!-- Header (если нужен) -->
<!-- Header -->
<#include "../../fragments/header.ftl">

<div class="container mt-5">
    <input type="hidden"
           name="current_conversation_id"
           value="${currentConversation.id}"
           id="currentConversationId"
           class="js-current-conversation"/>
    <div class="chat-container">
        <!-- Левая панель с чатами -->
        <aside class="chat-sidebar">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <span class="fw-bold">Недавние чаты</span>
                <i class="fas fa-ellipsis-v text-muted"></i>
            </div>

            <div class="js-conversation-container" id="conversationContainer">
                <#include "components/conversation.ftl">
            </div>
        </aside>

        <main class="chat-area">
            <ul class="nav nav-tabs mb-3" id="chatTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="all-messages-tab" data-bs-toggle="tab"
                            data-bs-target="#all-messages" type="button" role="tab">Все сообщения
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="favorites-tab" data-bs-toggle="tab"
                            data-bs-target="#favorites" type="button" role="tab">Избранное
                    </button>
                </li>
            </ul>
            <div class="tab-content" id="chatTabsContent">
                <div class="tab-pane fade show active" id="all-messages" style="background-color: #a5d6a8"
                     role="tabpanel">

                    <div class="chat-messages" id="chatMessages">
                        <#include "components/message.ftl">
                    </div>
                </div>
                <div class="tab-pane fade" id="favorites" role="tabpanel">
                    <div> тут будут избранные сообщения
                    </div>
                </div>
                <!-- Поле ввода внизу -->
                <div class="chat-input-area">
                    <input type="text" class="chat-input" id="messageInput" placeholder="Напишите сообщение..."/>
                    <input type="hidden" id="fromId" value="${(currentUser.id)}"/>
                    <input type="hidden" id="toId" value="${(userTo.id)}"/>
                    <button class="send-btn js-send-message">
                        <i class="fas fa-paper-plane"></i>
                    </button>
                </div>
            </div>
        </main>
    </div>
</div>

<#include "../../fragments/jschat.ftl">
</body>
</html>