import { API_URL } from '../config.js';
import Task from '../model/task.js';

export default class TaskService {

    /**
     * Retrieves a list of tasks of the project with a given ID.
     * 
     * @param {int} projectId - The unique identifier of the project.
     * @return {Promise<array<Task>>} - A list of objectives owned by the projectId.
     */
    static getTasks(projectId) {
        return fetch(`${API_URL}/projects/${projectId}/tasks`, {
            method: 'GET',
        }).then(res => {
            if (!res.ok) {
                throw new Error(res.status);
            }

            return res.json();
        }).catch(err => {
            return err;
        });
    }

    static addTask(projectId, task) {
        return fetch(`${API_URL}/projects/${projectId}/tasks`, {
            method: 'POST',
            body: JSON.stringify(task),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status !== 201) {
                throw new Error(res.status);
            }

            return res.json();
        }).catch(err => {
            return err;
        });
    }
}