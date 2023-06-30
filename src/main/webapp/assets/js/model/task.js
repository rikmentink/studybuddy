export default class Objective {
    /**
     * Creates a new object of type Task.
     * 
     * @constructor
     * @param {int} id - The unique identifier of the task.
     * @param {string} name - The name of the task.
     * @param {string} [description] The description of the task.
     * @param {date} [deadline] - The deadline of the task.
     * @param {boolean} [completed] - Whether the task is completed.
     */
    constructor(id, name, description, deadline, completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
    }
}