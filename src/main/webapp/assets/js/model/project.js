export default class Project {
    /**
     * Creates a new object of type Project.
     * 
     * @constructor
     * @param {int} id - The unique identifier of the project.
     * @param {string} name - The name of the project.
     * @param {string} [description] The description of the project.
     * @param {date} [startDate] - The start date of the project.
     * @param {date} [endDate] - The end date of the project.
     */
    constructor(id, name, description, startDate, endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}