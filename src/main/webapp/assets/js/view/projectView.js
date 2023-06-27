import ObjectiveService from '../service/objectiveService.js';
import ProjectService from '../service/projectService.js';
import TaskService from '../service/taskService.js';
import { formDataToJson } from '../utils/utils.js';

class ProjectView {
    
    /**
     * Initializes variables and adds an event listener to render projects when
     * the page is loaded.
     */
    static init() {
        this.projectId = (new URL(document.location)).searchParams.get('project');

        if (this.projectId == null || ProjectService.getProject(this.projectId) == null) {
            window.location.href = '/projects.html'
        }

        this.objectiveList = document.querySelector('#objectiveList');
        this.objectiveRowTemplate = document.querySelector('#objectiveRowTemplate');
        this.taskList = document.querySelector('#taskList');
        this.taskRowTemplate = document.querySelector('#taskRowTemplate');

        window.addEventListener('DOMContentLoaded', this.renderData.bind(this));
        
        // Event listeners for add objective form
        document.querySelector('#addObjectiveFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#addObjectiveForm');
            this.addObjectiveFormSubmit(form);
        });
        document.querySelector('#showAddObjectiveFormDialog').addEventListener('click', () => {
            document.querySelector('#addObjectiveFormDialog').showModal();
        });
        document.querySelector('#closeAddObjectiveFormDialog').addEventListener('click', () => {
            document.querySelector('#addObjectiveFormDialog').close();
        });

        // Event listeners for add task form
        document.querySelector('#addTaskFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#addTaskForm');
            this.addTaskFormSubmit(form);
        });
        document.querySelector('#showAddTaskFormDialog').addEventListener('click', () => {
            document.querySelector('#addTaskFormDialog').showModal();
        });
        document.querySelector('#closeAddTaskFormDialog').addEventListener('click', () => {
            document.querySelector('#addTaskFormDialog').close();
        });

        // Event listeners for update objective form
        document.querySelector('#updateObjectiveFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#updateObjectiveForm');
            this.updateObjectiveFormSubmit(form);
        });
        document.querySelector('#closeUpdateObjectiveFormDialog').addEventListener('click', () => {
            document.querySelector('#updateObjectiveFormDialog').close();
        });

        // Event listeners for update task form
        document.querySelector('#updateTaskFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#updateTaskForm');
            this.addTaskFormSubmit(form);
        });
        document.querySelector('#closeUpdateTaskFormDialog').addEventListener('click', () => {
            document.querySelector('#updateTaskFormDialog').close();
        });
    }

    /**
     * The function fetches objectives for a specific projects, creates row 
     * entries for each of them and renders them onto the page.
     */
    static renderData() {
        ProjectService.getProject(this.projectId)
            .then(project => {
                document.querySelector('.js-project-name').textContent = project.name;
            })

        TaskService.getTasks(this.projectId)
            .then(tasks => {
                if (tasks.length > 0) {
                    this.clearTaskList();

                    tasks.forEach(task => {
                        const taskRow = this.createTaskRow(task);
                        this.taskList.appendChild(taskRow);

                        // taskRow.querySelector('.js-update-task').addEventListener('click', (e) => {
                        //     const taskId = taskRow.querySelector('.task-row').getAttribute('data-id');
                
                        //     this.initTaskForm(taskId);
                        //     document.querySelector('#updateTaskFormDialog').showModal();
                        // });

                        // taskRow.querySelector('.js-delete-task').addEventListener('click', () => {
                        //     const taskId = taskRow.querySelector('.task-row').getAttribute('data-id');

                        //     TaskService.deleteTask(taskId)
                        //         .catch(err => {
                        //             console.error("Error while deleting task:", err);
                        //         });

                        //     window.location.reload();
                        // });
                    });
                } else {
                    const message = document.createElement('p');
                    message.textContent = 'Je hebt nog geen taken, maak er een aan!';
                    document.querySelector('#taskContainer').replaceWith(message);
                }
            })
            .catch(err => {
                console.error("Error while fetching tasks:", err);
            });

        ObjectiveService.getObjectives(this.projectId)
            .then(objectives => {
                if (objectives.length > 0) {
                    this.clearObjectiveList();

                    objectives.forEach(objective => {
                        const objectiveRow = this.createObjectiveRow(objective);
                        this.objectiveList.appendChild(objectiveRow);

                        // objectiveRow.querySelector('.js-update-objective').addEventListener('click', () => {
                        //     const objectiveId = objectiveRow.querySelector('.objective-row').getAttribute('data-id');
                
                        //     this.initObjectiveForm(objectiveId);
                        //     document.querySelector('#updateObjectiveFormDialog').showModal();
                        // });

                        // objectiveRow.querySelector('.js-delete-objective').addEventListener('click', () => {
                        //     const objectiveId = objectiveRow.querySelector('.objective-row').getAttribute('data-id');

                        //     ObjectiveService.deleteObjective(objectiveId)
                        //         .catch(err => {
                        //             console.error("Error while deleting objective:", err);
                        //         });

                        //     window.location.reload();
                        // });
                    });
                } else {
                    const message = document.createElement('p');
                    message.textContent = 'Je hebt nog geen deadlines, maak er een aan!';
                    document.querySelector('#objectiveContainer').replaceWith(message);
                }
            })
            .catch(err => {
                console.error("Error while fetching objectives:", err);
            });
    }

    /**
     * The function creates a row for an task by cloning a template and 
     * populating it with task information.
     * 
     * @param {Task} task - Contains the task object to create a row for.
     * @returns {Node} The code for the row representing the task.
     */
    static createTaskRow(task) {
        const taskRow = this.taskRowTemplate.content.cloneNode(true);

        taskRow.querySelector('.task-row').setAttribute('data-id', task.id)
        taskRow.querySelector('#name').textContent = task.name;

        if (task.description) {
            taskRow.querySelector('#description').textContent = task.description;
        }
        if (task.deadline) {
            taskRow.querySelector('#deadline').textContent = task.deadline;
        }
        if (task.completed) {
            taskRow.querySelector('#completed').checked = task.completed;
        }

        return taskRow;
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

        objectiveRow.querySelector('.objective-row').setAttribute('data-id', objective.id)
        objectiveRow.querySelector('#name').textContent = objective.name;

        if (objective.description) {
            objectiveRow.querySelector('#description').textContent = objective.description;
        }
        if (objective.deadline) {
            objectiveRow.querySelector('#deadline').textContent = objective.deadline;
        }

        return objectiveRow;
    }

    /**
    * Clears the task list by removing all its child elements.
    */
    static clearTaskList() {
        while (this.taskList.firstChild) {
            this.taskList.firstChild.remove();
        }
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
     * Adds a new task when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static addTaskFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#addTaskFormMessage');

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            TaskService.addTask(this.projectId, data)
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
        const data = formDataToJson(form);
        let message = document.querySelector('#addObjectiveFormMessage');

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            ObjectiveService.addObjective(this.projectId, data)
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

    /**
     * Initializes the update task form by filling in all the existing data.
     * 
     * @param taskId The identifier of the task to be updated.
     */
    static initTaskForm(taskId) {
        const form = document.querySelector('#updateTaskForm');
        const task = TaskService.getTask(taskId);

        form.querySelector('#name').value = task.name;
        form.querySelector('#completed').checked = task.completed;

        if (task.description) form.querySelector('#description').value = task.description;
        if (task.deadline) form.querySelector('#deadline').value = task.deadline;
    }

    /**
     * Initializes the update objective form by filling in all the existing 
     * data.
     * 
     * @param objectiveId The identifier of the objective to be updated.
     */
    static initObjectiveForm(objectiveId) {
        const form = document.querySelector('#updateObjectiveForm');
        const objective = ObjectiveService.getObjective(objectiveId);

        form.querySelector('#name').value = objective.name;

        if (objective.description) form.querySelector('#description').value = objective.description;
        if (objective.deadline) form.querySelector('#deadline').value = objective.deadline;
    }

    /**
     * Updates an existing task when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static updateTaskFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#updateTaskFormMessage');

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            TaskService.updateTask(data.id, data)
            .then(() => {
                message.classList.add('success');
                message.textContent = 'Task successfully updated!';
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
     * Updates an existing objective when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static updateObjectiveFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#updateObjectiveFormMessage');

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            ObjectiveService.updateObjective(data.id, data)
            .then(() => {
                message.classList.add('success');
                message.textContent = 'Objective successfully updated!';
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