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
        if (this.projectId == null) window.location.href = 'projects.html';

        ProjectService.getProject(this.projectId).then(async project => {
            document.title = document.title.replace('Project', project.name);
            document.querySelector('.js-project-description').textContent = project.description;
            document.querySelector('.js-project-date').textContent = project.startDate + ' t/m ' + project.endDate;

            const tasks = await this.getAmountOfTasks();
            const objectives = await this.getAmountOfObjectives();
            const progress = await this.computeProgress();
            const formattedProgress = progress % 1 === 0 ? progress.toFixed(0) : progress.toFixed(1);

            if (tasks == null && tasks.length == 0) {
                document.querySelector('.js-project-tasks-total').parentElement.style.display = 'none';
            } else {
                document.querySelector('.js-project-tasks-completed').textContent = await this.getAmountOfTasksCompleted();
                document.querySelector('.js-project-tasks-total').textContent = tasks;
            }

            if (objectives == null && objectives.length == 0) {
                document.querySelector('.js-project-objectives-total').parentElement.style.display = 'none';
            } else {
                document.querySelector('.js-project-objectives-passed').textContent = await this.getAmountOfObjectivesPassed();
                document.querySelector('.js-project-objectives-total').textContent = objectives;
            }

            document.querySelector('.js-project-progress').value = progress;
            document.querySelector('.js-project-progress-value').textContent = `${formattedProgress} %`;
            document.querySelector('.js-project-progress-value').style.left = `${progress}%`;
            document.querySelector('.js-project-time-remaining').textContent = await this.getRemainingTime() + ' uur';
        }).catch(err => {
            console.error(err);
            window.location.href = 'projects.html';
        });

        this.objectiveList = document.querySelector('#objectiveList');
        this.objectiveRowTemplate = document.querySelector('#objectiveRowTemplate');
        this.taskList = document.querySelector('#taskList');
        this.taskRowTemplate = document.querySelector('#taskRowTemplate');

        window.addEventListener('DOMContentLoaded', this.renderData.bind(this));

        // Event listeners for project actions
        document.querySelector('#updateProjectFormSubmit').addEventListener('click', () => {
            const form = document.querySelector('#updateProjectForm');
            this.updateProjectFormSubmit(form);
        });
        document.querySelector('.js-update-project').addEventListener('click', () => {
            this.initProjectForm();
            document.querySelector('#updateProjectFormDialog').showModal();
        });
        document.querySelector('#closeUpdateProjectFormDialog').addEventListener('click', () => {
            document.querySelector('#updateProjectFormDialog').close();
        });
        document.querySelector('.js-delete-project').addEventListener('click', () => {
            this.deleteProject();
            window.location.href = 'projects.html'
        })

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
            this.updateTaskFormSubmit(form);
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

                        const taskRowElement = document.querySelector(`.task-row[data-id='${task.id}']`);

                        taskRowElement.querySelector('.js-complete-task').addEventListener('change', this.handleCompleteTask(task));
                        taskRowElement.querySelector('.js-update-task').addEventListener('click', this.handleUpdateTask(task.id).bind(this));
                        taskRowElement.querySelector('.js-delete-task').addEventListener('click', this.handleDeleteTask(task.id).bind(this));
                        taskRowElement.querySelector('.js-view-task').addEventListener('click', this.handleViewTask(task.id).bind(this));
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

                        const objectiveRowElement = document.querySelector(`.objective-row[data-id='${objective.id}']`);

                        objectiveRowElement.querySelector('.js-update-objective').addEventListener('click', this.handleUpdateObjective(objective.id).bind(this));
                        objectiveRowElement.querySelector('.js-delete-objective').addEventListener('click', this.handleDeleteObjective(objective.id).bind(this));
                        objectiveRowElement.querySelector('.js-view-objective').addEventListener('click', this.handleViewObjective(objective.id).bind(this));
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

        if (task.expectedTime) {
            taskRow.querySelector('#expectedTime').textContent = task.expectedTime + ' uur';
        } else {
            taskRow.querySelector('#expectedTime').textContent = "-";
        }
        if (task.deadline) {
            taskRow.querySelector('#deadline').textContent = task.deadline;
        } else {
            taskRow.querySelector('#deadline').textContent = "-";
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

        if (objective.expectedTime) {
            objectiveRow.querySelector('#expectedTime').textContent = objective.expectedTime + ' uur';
        } else {
            objectiveRow.querySelector('#expectedTime').textContent = "-";
        }

        if (objective.deadline) {
            objectiveRow.querySelector('#deadline').textContent = objective.deadline;
        } else {
            objectiveRow.querySelector('#deadline').textContent = "-";
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
            data.completed = false;
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            TaskService.addTask(this.projectId, data)
                .then(() => {
                    document.querySelector('#addTaskFormDialog').close();
                    this.renderData();
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
                    document.querySelector('#addObjectiveFormDialog').close();
                    this.renderData();
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

        TaskService.getTask(taskId)
            .then(task => {
                form.dataset.id = task.id;
                form.querySelector('#name').value = task.name;

                if (task.description) form.querySelector('#description').value = task.description;
                if (task.expectedTime) form.querySelector('#expectedTime').value = task.expectedTime;
                if (task.deadline) form.querySelector('#deadline').value = task.deadline;
            });
    }

    /**
     * Initializes the task details by filling in all the data.
     * 
     * @param taskId The identifier of the task to be updated.
     */
    static initTaskDetails(taskId) {
        const form = document.querySelector('#taskAndObjectiveDetails');

        TaskService.getTask(taskId)
            .then(task => {
                form.querySelector('#name').textContent = task.name;

                if (task.description) {
                    form.querySelector('#description').textContent = task.description;
                } else {
                    form.querySelector('#description').style.display = 'none';
                }

                if (task.expectedTime) {
                    form.querySelector('#expectedTime').textContent = task.expectedTime;
                } else {
                    form.querySelector('#expectedTime').style.display = 'none';
                }

                if (task.deadline) {
                    form.querySelector('#deadline').textContent = task.deadline;
                } else {
                    form.querySelector('#deadline').parentElement.style.display = 'none';
                }

                if (task.completed != null) {
                    form.querySelector('#status').textContent = task.completed ? 'Voltooid' : 'Niet voltooid';
                } else {
                    form.querySelector('#status').parentElement.style.display = 'none';
                }

                form.style.display = 'block';
            });
    }

    /**
     * Initializes the update objective form by filling in all the existing 
     * data.
     * 
     * @param objectiveId The identifier of the objective to be updated.
     */
    static initObjectiveForm(objectiveId) {
        const form = document.querySelector('#updateObjectiveForm');

        ObjectiveService.getObjective(objectiveId)
            .then(objective => {
                form.dataset.id = objective.id;
                form.querySelector('#name').value = objective.name;

                if (objective.description) form.querySelector('#description').value = objective.description;
                if (objective.expectedTime) form.querySelector('#expectedTime').value = objective.expectedTime;
                if (objective.deadline) form.querySelector('#deadline').value = objective.deadline;
            });
    }

    /**
     * Initializes the objective details by filling in all the data.
     * 
     * @param objectiveId The identifier of the objective to be updated.
     */
    static initObjectiveDetails(objectiveId) {
        const form = document.querySelector('#taskAndObjectiveDetails');

        ObjectiveService.getObjective(objectiveId)
            .then(objective => {
                form.querySelector('#name').textContent = objective.name;

                if (objective.description) {
                    form.querySelector('#description').textContent = objective.description;
                } else {
                    form.querySelector('#description').style.display = 'none';
                }

                if (objective.expectedTime) {
                    form.querySelector('#expectedTime').textContent = objective.expectedTime;
                } else {
                    form.querySelector('#expectedTime').style.display = 'none';
                }

                if (objective.deadline) {
                    form.querySelector('#deadline').textContent = objective.deadline;
                } else {
                    form.querySelector('#deadline').parentElement.style.display = 'none';
                }

                form.querySelector('#status').parentElement.style.display = 'none';
                form.style.display = 'block';
            });
    }

    /**
     * Updates an existing task when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static updateTaskFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#updateTaskFormMessage');
        let taskId = document.querySelector('#updateTaskForm').dataset.id;

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            TaskService.updateTask(taskId, data)
                .then(() => {
                    document.querySelector('#updateTaskFormDialog').close();
                    this.renderData();
                })
                .catch(err => {
                    console.log(err)
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
        let objectiveId = document.querySelector('#updateObjectiveForm').dataset.id;

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            ObjectiveService.updateObjective(objectiveId, data)
                .then(() => {
                    document.querySelector('#updateObjectiveFormDialog').close();
                    this.renderData();
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
     * Initializes the update project form by filling in all the existing data.
     */
    static initProjectForm() {
        const form = document.querySelector('#updateProjectForm');

        ProjectService.getProject(this.projectId)
            .then(project => {
                form.dataset.id = project.id;
                form.querySelector('#name').value = project.name;

                if (project.description) form.querySelector('#description').value = project.description;
                if (project.startDate) form.querySelector('#startDate').value = project.startDate;
                if (project.endDate) form.querySelector('#endDate').value = project.endDate;
            });
    }

    /**
     * Updates an existing project when the form is submitted.
     * 
     * @param event The triggered event when the form was submitted.
     */
    static updateProjectFormSubmit(form) {
        const data = formDataToJson(form);
        let message = document.querySelector('#updateProjectFormMessage');
        let projectId = document.querySelector('#updateProjectForm').dataset.id;

        if (data.name) {
            if (data.deadline) {
                data.deadline = data.deadline.split('T').join(' ');
            }
            ProjectService.updateProject(projectId, data)
                .then(() => {
                    document.querySelector('#updateProjectFormDialog').close();
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

    /**
     * Deletes an existing project when the button is pressed.
     */
    static deleteProject() {
        ProjectService.deleteProject(this.projectId);
    }

    static handleCompleteTask(task) {
        return function (e) {
            task.completed = e.currentTarget.checked;
            TaskService.updateTask(task.id, task);
        };
    }

    static handleUpdateTask(taskId) {
        return function () {
            this.initTaskForm(taskId);
            document.querySelector('#updateTaskFormDialog').showModal();
        };
    }

    static handleDeleteTask(taskId) {
        return function () {
            TaskService.deleteTask(taskId)
                .catch(err => {
                    console.error("Error while deleting task:", err);
                }).finally(() => {
                    this.renderData();
                });
        };
    }

    static handleViewTask(taskId) {
        return function () {
            this.initTaskDetails(taskId);
        }
    }

    static handleUpdateObjective(objectiveId) {
        return function () {
            this.initObjectiveForm(objectiveId);
            document.querySelector('#updateObjectiveFormDialog').showModal();
        };
    }

    static handleDeleteObjective(objectiveId) {
        return function () {
            ObjectiveService.deleteObjective(objectiveId)
                .catch(err => {
                    console.error("Error while deleting objective:", err);
                }).finally(() => {
                    this.renderData();
                });
        };
    }

    static handleViewObjective(objectiveId) {
        return function () {
            this.initObjectiveDetails(objectiveId);
        }
    }

    static async computeProgress() {
        try {
            const [totalTasks, completedTasks, totalObjectives, passedObjectives] = await Promise.all([
                this.getAmountOfTasks(),
                this.getAmountOfTasksCompleted(),
                this.getAmountOfObjectives(),
                this.getAmountOfObjectivesPassed()
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

    static async getRemainingTime() {
        try {
            const [totalTasks, completedTasks, totalObjectives, passedObjectives] = await Promise.all([
                this.getAmountOfTasks(),
                this.getAmountOfTasksCompleted(),
                this.getAmountOfObjectives(),
                this.getAmountOfObjectivesPassed()
            ]);

            const uncompletedTasks = totalTasks - completedTasks;
            const futureObjectives = totalObjectives - passedObjectives;

            let remainingTime = 0;

            const tasks = await TaskService.getTasks(this.projectId);
            const objectives = await ObjectiveService.getObjectives(this.projectId);

            for (const task of tasks.slice(completedTasks, completedTasks + uncompletedTasks)) {
                remainingTime += task.expectedTime || 0;
            }

            for (const objective of objectives.slice(passedObjectives, passedObjectives + futureObjectives)) {
                remainingTime += objective.expectedTime || 0;
            }

            return remainingTime;
        } catch (error) {
            console.error("Error while calculating total expected time:", error);
            return 0;
        }
    }

    static getAmountOfTasks() {
        return TaskService.getTasks(this.projectId).then(data => data.length);
    }

    static getAmountOfTasksCompleted() {
        return TaskService.getTasks(this.projectId)
            .then(data => {
                const completedTasks = data.filter(task => task.completed);
                return completedTasks.length;
            });
    }

    static getAmountOfObjectives() {
        return ObjectiveService.getObjectives(this.projectId).then(data => data.length);
    }

    static getAmountOfObjectivesPassed() {
        return ObjectiveService.getObjectives(this.projectId)
            .then(data => {
                const today = new Date();
                const passedObjectives = data.filter(objective => new Date(objective.deadline) < today)
                return passedObjectives.length;
            });
    }
}

// Initialize the page when its loaded.
ProjectView.init();