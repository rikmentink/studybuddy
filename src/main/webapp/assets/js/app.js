import AuthService from './service/authService.js';

function initializeApp() {
    AuthService.performAuthenticationCheck();
}

// Initializes app on DOM load.
if (document.readyState !== 'loading') {
    initializeApp();
} else {
    document.addEventListener('DOMContentLoaded', function () {
        initializeApp();
    });
}