const API_URL = 'http://localhost:8080/studybuddy-0.1.0/api';

document.addEventListener('DOMContentLoaded', function() {
    // Stores a user in the storage to demonstrate, if none is set.
    setCurrentUser(1);

    // Load all projects
    switch (this.location.pathname) {
        case '/projects.html':
            fetchProjects();
            break;
    }
});

function setCurrentUser(userId) {
    if (!getCurrentUser()) {
        sessionStorage.setItem('userId', userId.toString());
    }
}

function getCurrentUser() {
    return sessionStorage.getItem('userId');
}

// Fetches project data and places it in the projects container.
async function fetchProjects() {
    const res = await fetch(`${API_URL}/projects/${getCurrentUser()}`);
    const data = await res.json();

    if (res.ok) {
        if (data.length > 0) {
            for (let project in projects) {
                const projectCard = document.importNode(projectCardTemplate.textContent, true);
                
                // projectCard.querySelector("#image").src = 
                projectCard.querySelector("#title").textContent = project.name;
                projectCard.querySelector("#description").textContent = project.description;
                projectCard.querySelector("#date").textContent = project.startDate + " tot " + project.endDate;
                // projectCard.querySelector("#url").href = 
        
                document.querySelector('#projectsContainer').appendChild(projectBlock);
            }
        } else {
            const message = 'Je hebt nog geen projecten. Maak er hieronder een aan!';
            document.querySelector('#projectsContainer').textContent = message;
        }
    } else {
        console.error('API error: ' + data.error);

        const message = 'Er is een fout opgetreden, probeer het eens opnieuw.';
        document.querySelector('#projectsContainer').textContent = message;
    }
}

// Adds a new project linked to the current user
async function addProject(jsonBody) {
    const res = await fetch(`${API_URL}/projects`, { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: { 
            "studentId": parseInt(getCurrentUser()),
            "name": name, 
            "description": description, 
            "startDate": startDate, 
            "endDate": endDate 
        }
    });
    const data = await res.json();

    if (res.ok) {
        console.log(data);
    } else {
        console.error('API error: ' + data.error);
    }
}

// Forms
let newProjectForm = document.querySelector('#newProjectForm');
newProjectForm.addEventListener('submit', function (e) {
    let jsonBody = {};

    if (document.querySelector('input[name="name"').value == "") {
        alert("Vul een naam voor je project in!");
    } else {
        new FormData(document.querySelector('#newProjectForm')).forEach((value, key) => jsonBody[key] = value);
        addProject(jsonBody);
    }
});