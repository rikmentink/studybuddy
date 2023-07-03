import AuthService from '../service/authService.js';
import ProjectService from '../service/projectService.js';
import TaskService from '../service/taskService.js';
import ObjectiveService from '../service/objectiveService.js';
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

                    projects.forEach(async project => {
                        const projectCard = await this.createProjectCard(project);
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
    static async createProjectCard(project) {
        const projectCard = this.projectCardTemplate.content.cloneNode(true);
        const projectProgress = await this.computeProjectProgress(project.id);
        const projectProgressValue = projectProgress % 1 === 0 ? projectProgress.toFixed(0) : projectProgress.toFixed(1);

        // projectCard.querySelector('#image').src = 
        projectCard.querySelector('#title').textContent = project.name;
        projectCard.querySelector('#url').href = `${URL_PREFIX}/project.html?project=${project.id}`
        projectCard.querySelector('#progress').value = projectProgress;
        projectCard.querySelector('#progressValue').textContent = `${projectProgressValue} %`;
        projectCard.querySelector('#progressValue').style.left = `${projectProgress}%`;

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
                    window.location.reload();
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

    static async computeProjectProgress(projectId) {
        try {
            const [totalTasks, completedTasks, totalObjectives, passedObjectives] = await Promise.all([
                TaskService.getTasks(projectId).then(data => data.length),
                TaskService.getTasks(projectId)
                    .then(data => {
                        const completedTasks = data.filter(task => task.completed);
                        return completedTasks.length;
                    }),
                ObjectiveService.getObjectives(projectId).then(data => data.length),
                ObjectiveService.getObjectives(projectId)
                    .then(data => {
                        const today = new Date();
                        const passedObjectives = data.filter(objective => new Date(objective.deadline) < today)
                        return passedObjectives.length;
                    })
            ]);

            const totalItems = totalTasks + totalObjectives;
            const completedItems = completedTasks + passedObjectives;

            const progressPercentage = (completedItems / totalItems) * 100;

            return progressPercentage;
        } catch (error) {
            console.error("Error while calculating project progress:", error);
            return 0;
        }
    }
}

// Initialize the page when its loaded.
ProjectListView.init();