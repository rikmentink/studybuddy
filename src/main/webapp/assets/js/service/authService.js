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
                return new Error(res.status);
            }
            return res.json();
        }).then(data => {
            console.log(data);
            this.saveUserToStorage(data.token, data.userId);
            return data;
        }) .catch(err => {
            return new Error(err);
        });
    }

    static saveUserToStorage(token, userId) {
        sessionStorage.setItem('token', token);
        sessionStorage.setItem('user', userId);
    }

    static requiresAuthentication() {
        const restrictedPages = [
            `/${URL_PREFIX}/auth/login.html`, 
            `/${URL_PREFIX}/auth/register.html`, 
            `/${URL_PREFIX}/auth/forgot-password.html`
        ];
        const currentPage = window.location.pathname;
        return !restrictedPages.includes(currentPage);
    }

    static isAuthenticated() {
        const token = sessionStorage.getItem('token');
        return token !== null && token !== undefined;
    }

    static performAuthenticationCheck() {
        if (this.requiresAuthentication() && !this.isAuthenticated()) {
            window.location.href = `/${URL_PREFIX}/auth/login.html`;
        } else if (!this.requiresAuthentication() && this.isAuthenticated()) {
            window.location.href = `/${URL_PREFIX}/`;
        }
    }
}