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

document.querySelector('.js-user-dropdown-toggle').addEventListener('click', (e) => {
    const dropdown = document.querySelector('.js-user-dropdown');

    if (dropdown.classList.contains('show')) {
        dropdown.classList.remove('show');
    } else {
        dropdown.classList.add('show');
    }
});