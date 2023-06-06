const API_URL = 'http://localhost:8080/studybuddy-0.1.0/api';
const URL_PREFIX = '/studybuddy-0.1.0';

// Saves the current user id in the client's session storage.
function setCurrentUser(userId) {
    if (!getCurrentUser()) {
        sessionStorage.setItem('userId', userId.toString());
    }
}

// Reads the current user id from the client's session storage.
function getCurrentUser() {
    return sessionStorage.getItem('userId');
}

// Translates data of a given form into clean json.
function getJsonData(form) {
    let formData = new FormData(form);
    let jsonData = {};

    formData.forEach((value, key) => (jsonData[key] = value));
    return jsonData;
}

// Initializes app on DOM load.
if (document.readyState !== 'loading') {
    initializeApp();
} else {
    document.addEventListener('DOMContentLoaded', function () {
        initializeApp();
    });
}

// Lazy loads certain data on different pages.
function initializeApp() {
    // Stores a user in the storage to demonstrate, if none is set.
    setCurrentUser(1);
 
    switch (this.location.pathname) {
        case `${URL_PREFIX}/projects.html`:
            getUserProjects();
            break;
    }

    console.log('INFO: App has been initialized.');
}

// Fetches project data and places it in the projects container.
function getUserProjects() {
    fetch(`${API_URL}/students/${getCurrentUser()}/projects`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
    })
        .then((res) => {
            return res.json();
        })
        .then((data) => {
            console.log(data);
            if (data.length > 0) {
                const projectCardTemplate = document.querySelector('#projectCardTemplate');

                data.forEach((project) => {
                    console.log(project);
                    const projectCard = document.importNode(
                        projectCardTemplate.content,
                        true
                    );

                    // projectCard.querySelector('#image').src =
                    projectCard.querySelector('#title').textContent = project.name;
                    projectCard.querySelector('#description').textContent =
                        project.description;
                    projectCard.querySelector('#date').textContent =
                        project.startDate + ' tot ' + project.endDate;
                    // projectCard.querySelector('#url').href =

                    document
                        .querySelector('#projectsContainer')
                        .appendChild(projectCard);
                });
            } else {
                const message =
                    'Je hebt nog geen projecten. Maak er hieronder een aan!';
                document.querySelector('#projectsContainer').textContent = message;
            }
        })
        .catch((err) => {
            document.querySelector('#projectsContainer').textContent =
                'Something went wrong!';
            console.error(err);
        });
}

// Forms
document.querySelector('#newProjectForm').addEventListener('submit', (e) => {
    e.preventDefault();

    fetch(`${API_URL}/students/${getCurrentUser()}/projects`, {
        method: 'POST',
        body: getJsonData(e.target),
        headers: { 'Content-Type': 'application/json' },
    })
        .then((res) => {
            return res.json();
        })
        .then((result) => {
            console.log(result);
            document.querySelector('#newProjectMessage').textContent =
                'Project successfully added!';
            e.target.reset();
        })
        .catch((err) => {
            document.querySelector('#newProjectMessage').textContent =
                'Something went wrong!';
            console.error(err);
        });
});