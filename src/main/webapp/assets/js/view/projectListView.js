import ProjectService from '../service/projectService.js';
import Project from '../model/project.js';

class ProjectListView {
    
    /**
     * Initializes variables and adds an event listener to render projects when
     * the page is loaded.
     */
    static init() {
        this.projectList = document.querySelector('#projectsContainer');
        this.projectCardTemplate = document.querySelector('#projectCardTemplate');

        window.addEventListener('DOMContentLoaded', this.renderProjects.bind(this));
    }

    /**
     * The function fetches projects for a specific student, creates cards for
     * each of them and renders them onto the page.
     */
    static renderProjects() {
        const studentId = sessionStorage.getItem('userId');

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
        projectCard.querySelector('#url').href = `#project-${project.id}`

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
}

// Initialize the page when its loaded.
ProjectListView.init();