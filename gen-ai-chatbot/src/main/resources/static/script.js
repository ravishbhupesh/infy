document.addEventListener('DOMContentLoaded', function() {
    const chatForm = document.getElementById('chatForm');
    const chatContainer = document.getElementById('chatContainer');
    displayMessage('Welcome!!! How may I help you?', 'Bot');

    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const messageInput = document.getElementById('message');
        const message = messageInput.value.trim();
        if (message === '') return;
        messageInput.value = '';

        displayMessage(message, 'User');
        const response = await sendMessage(message);
        displayMessage(response, 'Bot');
    });

    async function sendMessage(message) {
        try {
            const response = await fetch('/sendMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message })
            });
            const data = await response.json();
            return data.message;
        } catch (error) {
            console.error('Error sending message:', error);
            return 'An error occurred while processing your message.';
        }
    }

    function displayMessage(message, sender) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', sender);
        //messageElement.innerText = sender + ' : ' + message;
        messageElement.innerText = message;
        chatContainer.appendChild(messageElement);
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }
});
