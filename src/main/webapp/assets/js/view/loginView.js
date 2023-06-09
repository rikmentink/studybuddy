import AuthService from '../service/authService.js';
import { formDataToJson } from '../utils/utils.js';
import { URL_PREFIX } from '../config.js';

class LoginView {

    static init() {
        document.querySelector('#loginFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#loginForm')
            this.loginFormSubmit(form);
        })
    }

    static loginFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#loginFormMessage');

        if (data.email && data.password) {
            AuthService.login(data)
                .then(res => {
                    console.log(`Student with id ${res.userId} logged in.`);
                    window.location.href = `${URL_PREFIX}/`;
                })
                .catch(err => {
                    console.error(`Student login failed: ${err}`)
                    message.classList.add('error');
                    message.textContent = err.message;
                })
        } else {
            message.classList.add('error');
            message.textContent = 'Please enter a valid e-mail and password!';
        }
    }
}

// Initialize the page when its loaded.
LoginView.init();