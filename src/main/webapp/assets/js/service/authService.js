import { API_URL, URL_PREFIX } from '../config.js';

export default class AuthService {

    static login(data) {
        return fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => {
            this.saveUserToStorage(data.token, data.userId);
            return data;
        })
    }

    static register(data) {
        return fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => {
            this.saveUserToStorage(data.token, data.userId);
            return data;
        })
    }

    static logout() {
        this.removeUserFromStorage();
        window.location.href = `${URL_PREFIX}/auth/login.html`;
    }

    static saveUserToStorage(token, userId) {
        sessionStorage.setItem('token', token);
        sessionStorage.setItem('user', userId);
    }

    static removeUserFromStorage() {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('user');
    } 

    static requiresAuthentication() {
        const restrictedPages = [
            `${URL_PREFIX}/auth/login.html`, 
            `${URL_PREFIX}/auth/register.html`, 
            `${URL_PREFIX}/auth/forgot-password.html`
        ];
        const currentPage = window.location.pathname;
        return !restrictedPages.includes(currentPage);
    }

    static isAuthenticated() {
        const token = sessionStorage.getItem('token');
        return token !== null && token !== 'undefined';
    }

    static performAuthenticationCheck() {
        if (this.requiresAuthentication() && !this.isAuthenticated()) {
            window.location.href = `${URL_PREFIX}/auth/login.html`;
        } else if (!this.requiresAuthentication() && this.isAuthenticated()) {
            window.location.href = `${URL_PREFIX}`;
        }
    }

    static getCurrentUser() {
        return sessionStorage.getItem('user');
    }
}