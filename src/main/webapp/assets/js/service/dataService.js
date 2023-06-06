// Translates data of a given form into clean json.
function getJsonData(form) {
    let formData = new FormData(form);
    let jsonData = {};

    formData.forEach((value, key) => {
        jsonData[key] = value
    });
    
    return jsonData;
}

// Fetches API data from a given url
function fetchJson(url, options) {
    return fetch(url, options)
        .then((res) => {
            if (res.status === 204) {
                return null;
            } else if (res.status === 200) {
                return res.json();
            } else {
                throw new Error(`Request failed with status ${res.status}`);
            }
        })
        .catch((err) => {
            console.error(err);
        });
}

// Fetches project data and places it in the projects container.
function getUserProjects() {
    fetchJson(`${API_URL}/students/${getCurrentUser()}/projects`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
    })
        .then((data) => {
            const projectsContainer = document.querySelector('#projectsContainer');

            if (data.length > 0) {
                const projectCardTemplate = document.querySelector('#projectCardTemplate');

                data.forEach((project) => {
                    const projectCard = document.importNode(projectCardTemplate.content, true);

                    // projectCard.querySelector('#image').src =
                    projectCard.querySelector('#title').textContent       = project.name;
                    projectCard.querySelector('#description').textContent = project.description;
                    projectCard.querySelector('#date').textContent        = project.startDate + ' tot ' + project.endDate;
                    // projectCard.querySelector('#url').href =

                    projectsContainer.appendChild(projectCard);
                });
            } else {
                projectsContainer.textContent = 'Je hebt nog geen projecten. Maak er hieronder een aan!';
            }
        })
        .catch(() => {
            document.querySelector('#projectsContainer').textContent = 'Something went wrong!';
        });
}

// Creates a new project when the form is submitted
function handleNewProjectFormSubmit(e) {
    fetchJson(`${API_URL}/students/${getCurrentUser()}/projects`, {
        method: 'POST',
        body: JSON.stringify(getJsonData(e.target)),
        headers: { 'Content-Type': 'application/json' },
    })
        .then(() => {
            document.querySelector('#newProjectMessage').textContent = 'Project successfully added!';
            e.target.reset();
        })
        .catch(() => {
            document.querySelector('#newProjectMessage').textContent = 'Something went wrong!';
        });
}