import AuthService from '../service/authService.js';
import { formDataToJson } from '../utils/utils.js';

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
                // window.location.href = `${URL_PREFIX}`;
            })
            .catch(e => {
                console.error(`Student login failed: ${e}`)
                message.classList.add('error');
                message.textContent = 'Something went wrong!';
            })
        } else {
            message.classList.add('error');
            message.textContent = 'Please fill all fields correctly!';
        }
    }
}

// Initialize the page when its loaded.
RegisterView.init();