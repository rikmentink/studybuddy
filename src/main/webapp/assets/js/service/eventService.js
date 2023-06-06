// Projects page
document.querySelector('#showNewProjectDialog').addEventListener('click', () => {
    document.querySelector('#newProjectDialog').showModal();
});

document.querySelector('#closeNewProjectDialog').addEventListener('click', () => {
    document.querySelector('#newProjectDialog').close();
});

document.querySelector('#newProjectForm').addEventListener('submit', (e) => {
    e.preventDefault();
    handleNewProjectFormSubmit(e);
});