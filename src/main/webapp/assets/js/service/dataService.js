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