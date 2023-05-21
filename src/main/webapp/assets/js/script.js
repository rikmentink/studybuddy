document.addEventListener('DOMContentLoaded', function() {
    if (window.location.pathname === '/projects.html') {
        // fetch certain project data
    }
});


// Fetches data from the api and returns the fetched json-data.
function fetchData(url) {
    fetch(`https://localhost:8080/studybuddy-0.1.0/api/${url}`)
        .then(response => response.json())
        .then(data => { 
            return data;
        })
        .catch(err => {
            console.err('An error occured while fetching data: ' + err)
        });
}