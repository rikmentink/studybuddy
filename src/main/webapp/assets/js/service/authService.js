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