import AuthService from '../service/authService.js';
import ProjectService from '../service/projectService.js';
import Project from '../model/project.js';
import { formDataToJson } from '../utils/utils.js';
import { URL_PREFIX } from '../config.js';

class ProjectListView {
    
    /**
     * Initializes variables and adds an event listener to render projects when
     * the page is loaded.
     */
    static init() {
        this.projectList = document.querySelector('#projectsContainer');
        this.projectCardTemplate = document.querySelector('#projectCardTemplate');

        window.addEventListener('DOMContentLoaded', this.renderProjects.bind(this));
        
        document.querySelector('#addProjectFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#addProjectForm')
            this.addProjectFormSubmit(form);
        });
        document.querySelector('#showAddProjectFormDialog').addEventListener('click', () => {
            document.querySelector('#addProjectFormDialog').showModal();
        });
        
        document.querySelector('#closeAddProjectFormDialog').addEventListener('click', () => {
            document.querySelector('#addProjectFormDialog').close();
        });
    }

    /**
     * The function fetches projects for a specific student, creates cards for
     * each of them and renders them onto the page.
     */
    static renderProjects() {
        const studentId = AuthService.getCurrentUser();

        ProjectService.getProjects(studentId)
            .then(projects => {
                if (projects.length > 0) {
                    this.clearProjectList();

                    projects.forEach(project => {
                        const projectCard = this.createProjectCard(project);
                        this.projectList.appendChild(projectCard);
                    });
                } else {
                    const message = document.createElement('p');
                    message.textContent = 'Je hebt nog geen projecten, maak er een aan!';
                    this.projectList.appendChild(message);
                }
            })
            .catch(err => {
                console.error("Error while fetching projects:", err);
            });
    }

    /**
     * The function creates a project card by cloning a template and populating
     * it with project information.
     * 
     * @param {Project} project - Contains the project object to create a card for.
     * @returns {Node} The code for the card representing the project.
     */
    static createProjectCard(project) {
        const projectCard = this.projectCardTemplate.content.cloneNode(true);

        // projectCard.querySelector('#image').src = 
        projectCard.querySelector('#title').textContent       = project.name;
        projectCard.querySelector('#url').href = `${URL_PREFIX}/project.html?project=${project.id}`

        if (project.description) {
            projectCard.querySelector('#description').textContent = project.description;
        }
        if (project.startDate && project.endDate) {
            projectCard.querySelector('#date').textContent = `${project.startDate} tot ${project.endDate}`;
        } else if (!project.startDate && project.endDate) {
            projectCard.querySelector('#date').textContent = `Tot ${project.endDate}`;
        } else if (project.startDate && !project.endDate) {
            projectCard.querySelector('#date').textContent = `Sinds ${project.startDate}`;
        }

        return projectCard;
    }

   /**
    * Clears the project list by removing all its child elements.
    */
    static clearProjectList() {
        while (this.projectList.firstChild) {
            this.projectList.firstChild.remove();
        }
    }

    /**
     * Adds a new project when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static addProjectFormSubmit(form) {
        const studentId = AuthService.getCurrentUser();
        const data = formDataToJson(form);
        let message = document.querySelector('#addProjectFormMessage');

        if (data.name) {
            ProjectService.addProject(studentId, data)
            .then(() => {
                message.classList.add('success');
                message.textContent = 'Project successfully added!';
                e.target.reset();
            })
            .catch(() => {
                message.classList.add('error');
                message.textContent = 'Something went wrong!';
            });
        } else {
            message.classList.add('error');
            message = 'Please enter a valid name!';
        }
    }
}

// Initialize the page when its loaded.
ProjectListView.init();