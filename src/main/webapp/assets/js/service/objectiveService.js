import { API_URL } from '../config.js';
import Objective from '../model/objective.js';

export default class ObjectiveService {

    /**
     * Retrieves a list of objectives of the project with a given ID.
     * 
     * @param {int} projectId - The unique identifier of the project.
     * @return {Promise<array<Objective>>} - A list of objectives owned by the projectId.
     */
    static getObjectives(projectId) {
        return fetch(`${API_URL}/projects/${projectId}/objectives`, {
            method: 'GET',
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => {
            return data.map(objectiveData => new Objective(
                objectiveData.id,
                objectiveData.name,
                objectiveData.description,
                objectiveData.expectedTime,
                objectiveData.deadline
            ));
        });
    }

    /**
     * Retrieves a certain objective based on the given identifier.
     * 
     * @param {int} objectiveId - The unique identifier of the objective.
     * @return {Promise<Objective>} - The objective with the given ID.
     */
    static getObjective(objectiveId) {
        return fetch(`${API_URL}/objectives/${objectiveId}`, {
            method: 'GET',
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => new Objective(
            data.id,
            data.name,
            data.description,
            data.expectedTime,
            data.deadline
        ));
    }

    static addObjective(projectId, objective) {
        return fetch(`${API_URL}/projects/${projectId}/objectives`, {
            method: 'POST',
            body: JSON.stringify(objective),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status !== 201) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => new Objective(
            data.id,
            data.name,
            data.description,
            data.expectedTime,
            data.deadline
        ));
    }

    static updateObjective(objectiveId, objective) {
        return fetch(`${API_URL}/objectives/${objectiveId}`, {
            method: 'PUT',
            body: JSON.stringify(objective),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => new Objective(
            data.id,
            data.name,
            data.description,
            data.expectedTime,
            data.deadline
        ));
    }

    static deleteObjective(objectiveId) {
        return fetch(`${API_URL}/objectives/${objectiveId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }

            return res;
        });
    }
}