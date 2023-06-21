export default class Objective {
    /**
     * Creates a new object of type Objective.
     * 
     * @constructor
     * @param {int} id - The unique identifier of the objective.
     * @param {string} name - The name of the objective.
     * @param {string} [description] The description of the objective.
     * @param {date} [deadline] - The deadline of the objective.
     * @param {string} [note] - The notes for the objective.
     */
    constructor(id, name, description, deadline, note) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.note = note;
    }
}