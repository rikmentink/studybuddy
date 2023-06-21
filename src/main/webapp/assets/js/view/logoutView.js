import AuthService from '../service/authService.js';

class LoginView {
    
    static init() {
        AuthService.logout();
    }
}

// Initialize the page when its loaded.
LoginView.init();