import { URL_PREFIX } from './config.js';

// Lazy loads certain data on different pages.
function initializeApp() {
    setCurrentUser(1); // Stores a user in the storage to demonstrate, if none is set.
 
    switch (location.pathname) {
        case `${URL_PREFIX}/projects.html`:
            getUserProjects();
            break;
    }

    document.querySelector('#copyrightYear').textContent = new Date().getFullYear();

    console.log('INFO: App has been initialized.');
}

// Initializes app on DOM load.
if (document.readyState !== 'loading') {
    initializeApp();
} else {
    document.addEventListener('DOMContentLoaded', function () {
        initializeApp();
    });
}

// Adds event listeners to the forms.
document.querySelector('#newProjectForm').addEventListener('submit', (e) => handleNewProjectFormSubmit(e));