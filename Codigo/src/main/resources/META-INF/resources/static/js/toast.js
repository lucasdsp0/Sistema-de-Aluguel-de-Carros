// Toast Notification System
class Toast {
    constructor() {
        this.toasts = [];
        this.createContainer();
    }

    createContainer() {
        if (!document.querySelector('.toast-container')) {
            const container = document.createElement('div');
            container.className = 'toast-container';
            document.body.appendChild(container);
        }
    }

    show(message, type = 'info', duration = 3000) {
        const container = document.querySelector('.toast-container');
        const toast = document.createElement('div');

        const icons = {
            success: '✓',
            error: '✕',
            warning: '⚠',
            info: 'ℹ'
        };

        toast.className = `toast ${type}`;
        toast.innerHTML = `
            <span class="icon">${icons[type]}</span>
            <span class="toast-message">${message}</span>
            <button class="toast-close" onclick="this.parentElement.remove()">✕</button>
        `;

        container.appendChild(toast);

        if (duration > 0) {
            setTimeout(() => {
                toast.classList.add('removing');
                setTimeout(() => toast.remove(), 300);
            }, duration);
        }

        return toast;
    }

    success(message, duration = 3000) {
        return this.show(message, 'success', duration);
    }

    error(message, duration = 4000) {
        return this.show(message, 'error', duration);
    }

    warning(message, duration = 3500) {
        return this.show(message, 'warning', duration);
    }

    info(message, duration = 3000) {
        return this.show(message, 'info', duration);
    }
}

// Instância global
const toast = new Toast();

// Substituir confirm() por toast + ação
function showConfirmation(message, onConfirm, onCancel) {
    const container = document.querySelector('.toast-container');
    const confirmation = document.createElement('div');

    confirmation.className = 'toast info';
    confirmation.style.minWidth = '400px';
    confirmation.innerHTML = `
        <span class="toast-message" style="flex: 1;">${message}</span>
        <button class="toast-confirm-btn" style="background: #10b981; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; margin-right: 8px;">Confirmar</button>
        <button class="toast-cancel-btn" style="background: #ef4444; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer;">Cancelar</button>
    `;

    container.appendChild(confirmation);

    const confirmBtn = confirmation.querySelector('.toast-confirm-btn');
    const cancelBtn = confirmation.querySelector('.toast-cancel-btn');

    confirmBtn.onclick = () => {
        confirmation.remove();
        if (onConfirm) onConfirm();
    };

    cancelBtn.onclick = () => {
        confirmation.classList.add('removing');
        setTimeout(() => confirmation.remove(), 300);
        if (onCancel) onCancel();
    };
}
