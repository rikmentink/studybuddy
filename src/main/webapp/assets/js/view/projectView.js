import AuthService from '../service/authService.js';
import ObjectiveService from '../service/objectiveService.js';
import Objective from '../model/objective.js';
import { formDataToJson } from '../utils/utils.js';

class ProjectView {
    
    /**
     * Initializes variables and adds an event listener to render projects when
     * the page is loaded.
     */
    static init() {
        this.objectiveList = document.querySelector('#objectivesContainer');
        this.objectiveRowTemplate = document.querySelector('#objectiveRowTemplate');

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
    }

    /**
     * The function fetches objectives for a specific projects, creates row 
     * entries for each of them and renders them onto the page.
     */
    static renderObjectives() {
        const projectId = 1 // TODO: Find the saved project id, maybe from query param?

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
                    message.textContent = 'Je hebt nog geen taken, maak er een aan!';
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
        // objectiveRow.querySelector('#completed').textContent = objective.completed;

        if (objective.deadline) {
            objectiveRow.querySelector('#deadline').textContent = objective.deadline;
        }
        // TODO: Als opdracht taak is, vul checkbox in.

        return objectiveRow;
    }

   /**
    * Clears the objective list by removing all its child elements.
    */
    static clearObjectiveList() {
        while (this.objectiveList.firstChild) {
            this.objectiveList.firstChild.remove();
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