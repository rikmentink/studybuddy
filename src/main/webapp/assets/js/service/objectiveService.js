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
                return new Error(res.status);
            }

            return res.json();
        }).catch(err => {
            return new Error(err);
        });
    }

    static addObjective(projectId, objective) {
        return fetch(`${API_URL}/projects/${projectId}/objectives`, {
            method: 'POST',
            body: JSON.stringify(objective),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status != 201) {
                return new Error(res.status);
            }

            return res.json();
        }).catch(err => {
            return new Error(err);
        });
    }
}