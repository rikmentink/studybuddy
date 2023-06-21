import ObjectiveService from '../service/objectiveService.js';
import Objective from '../model/objective.js';
import { formDataToJson } from '../utils/utils.js';

class ProjectView {
    
    /**
     * Initializes variables and adds an event listener to render projects when
     * the page is loaded.
     */
    static init() {
        this.objectiveList = document.querySelector('#objectiveList');
        this.objectiveRowTemplate = document.querySelector('#objectiveRowTemplate');
        this.taskList = document.querySelector('#taskList');
        this.taskRowTemplate = document.querySelector('#taskRowTemplate');

        window.addEventListener('DOMContentLoaded', this.renderObjectives.bind(this));
        
        document.querySelector('#addObjectiveFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#addObjectiveForm')
            this.addObjectiveFormSubmit(form);
        });
        document.querySelector('#showAddObjectiveFormDialog').addEventListener('click', () => {
            document.querySelector('#addObjectiveFormDialog').showModal();
        });
        document.querySelector('#closeAddObjectiveFormDialog').addEventListener('click', () => {
            document.querySelector('#addObjectiveFormDialog').close();
        });

        document.querySelector('#addTaskFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#addTaskForm')
            this.addTaskFormSubmit(form);
        });
        document.querySelector('#showAddTaskFormDialog').addEventListener('click', () => {
            document.querySelector('#addTaskFormDialog').showModal();
        });
        document.querySelector('#closeAddTaskFormDialog').addEventListener('click', () => {
            document.querySelector('#addTaskFormDialog').close();
        });
    }

    /**
     * The function fetches objectives for a specific projects, creates row 
     * entries for each of them and renders them onto the page.
     */
    static renderObjectives() {
        const projectId = 1 // TODO: Find the saved project id, maybe from query param?

        TaskService.getTasks(projectId)
            .then(tasks => {
                if (tasks.length > 0) {
                    this.clearObjectiveList();

                    tasks.forEach(task => {
                        const taskRow = this.createObjectiveRow(task);
                        this.taskList.appendChild(taskRow);
                    });
                } else {
                    const message = document.createElement('p');
                    message.textContent = 'Je hebt nog geen taken, maak er een aan!';
                    this.taskList.appendChild(message);
                }
            })
            .catch(err => {
                console.error("Error while fetching tasks:", err);
            });

        ObjectiveService.getObjectives(projectId)
            .then(objectives => {
                if (objectives.length > 0) {
                    this.clearObjectiveList();

                    objectives.forEach(objective => {
                        const objectiveRow = this.createObjectiveRow(objective);
                        this.objectiveList.appendChild(objectiveRow);
                    });
                } else {
                    const message = document.createElement('p');
                    message.textContent = 'Je hebt nog geen deadlines, maak er een aan!';
                    this.objectiveList.appendChild(message);
                }
            })
            .catch(err => {
                console.error("Error while fetching objectives:", err);
            });
    }

    /**
     * The function creates a row for an objective by cloning a template and 
     * populating it with objective information.
     * 
     * @param {Objective} objective - Contains the objective object to create a row for.
     * @returns {Node} The code for the row representing the objective.
     */
    static createObjectiveRow(objective) {
        const objectiveRow = this.objectiveRowTemplate.content.cloneNode(true);

        objectiveRow.querySelector('#name').textContent = objective.name;
        objectiveRow.querySelector('#description').textContent = objective.description;

        if (objective.deadline) {
            objectiveRow.querySelector('#deadline').textContent = objective.deadline;
        }
        if (objective.completed) {
            objectiveRow.querySelector('#completed').checked = objective.completed;
        }

        return objectiveRow;
    }

   /**
    * Clears the objective and task lists by removing all their child elements.
    */
    static clearObjectiveList() {
        while (this.objectiveList.firstChild) {
            this.objectiveList.firstChild.remove();
        }
        while (this.taskList.firstChild) {
            this.taskList.firstChild.remove();
        }
    }

    /**
     * Adds a new task when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static addTaskFormSubmit(form) {
        const projectId = 1 // TODO: (dubbel) project id vinden
        const data = formDataToJson(form);
        let message = document.querySelector('#addTaskFormMessage');

        if (data.name) {
            TaskService.addObjective(projectId, data)
            .then(() => {
                message.classList.add('success');
                message.textContent = 'Task successfully added!';
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

    /**
     * Adds a new objective when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static addObjectiveFormSubmit(form) {
        const projectId = 1 // TODO: (dubbel) project id vinden
        const data = formDataToJson(form);
        let message = document.querySelector('#addObjectiveFormMessage');

        if (data.name) {
            ObjectiveService.addObjective(projectId, data)
            .then(() => {
                message.classList.add('success');
                message.textContent = 'Objective successfully added!';
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
ProjectView.init();