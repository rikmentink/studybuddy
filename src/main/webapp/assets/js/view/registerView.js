import AuthService from '../service/authService.js';
import { formDataToJson } from '../utils/utils.js';
import { URL_PREFIX } from '../config.js';

class RegisterView {

    static init() {
        document.querySelector('#registerFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#registerForm')
            this.registerFormSubmit(form);
        })
    }

    static registerFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#registerFormMessage');

        if (data.firstname && data.lastname && data.email && data.password) {
            AuthService.register(data)
                .then(res => {
                    console.log(`Student with id ${res.userId} was registered.`);
                    window.location.href = `${URL_PREFIX}/`;
                })
                .catch(err => {
                    console.error(err);
                    message.classList.add('error');
                    message.textContent = err.message;
                })
        } else {
            message.classList.add('error');
            message.textContent = 'Please fill all fields correctly!';
        }
    }
}

// Initialize the page when its loaded.
RegisterView.init();