import { API_URL } from '../config.js';
import Task from '../model/task.js';

export default class TaskService {

    /**
     * Retrieves a list of tasks of the project with a given ID.
     * 
     * @param {int} projectId - The unique identifier of the project.
     * @return {Promise<array<Task>>} - A list of tasks owned by the projectId.
     */
    static getTasks(projectId) {
        return fetch(`${API_URL}/projects/${projectId}/tasks`, {
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
            return data.map(taskData => new Task(
                taskData.id,
                taskData.name,
                taskData.description,
                taskData.expectedTime,
                taskData.deadline,
                taskData.completed
            ));
        }).catch(err => {
            return err;
        });
    }

    /**
     * Retrieves a certain task based on the given identifier.
     * 
     * @param {int} taskId - The unique identifier of the task.
     * @return {Promise<Task>} - The task with the given ID.
     */
    static getTask(taskId) {
        return fetch(`${API_URL}/tasks/${taskId}`, {
            method: 'GET',
        }).then(res => {
            if (!res.ok) {
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => new Task(
                data.id,
                data.name,
                data.description,
                data.expectedTime,
                data.deadline,
                data.completed
        )).catch(err => {
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
                return res.json().then(err => {
                    err.status = res.status;
                    throw err;
                });
            }
            return res.json();
        }).then(data => new Task(
            data.id,
            data.name,
            data.description,
            data.expectedTime,
            data.deadline,
            data.completed
        )).catch(err => {
            return err;
        });
    }

    static updateTask(taskId, task) {
        return fetch(`${API_URL}/tasks/${taskId}`, {
            method: 'PUT',
            body: JSON.stringify(task),
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
        }).then(data => new Task(
            data.id,
            data.name,
            data.description,
            data.expectedTime,
            data.deadline,
            data.completed
        )).catch(err => {
            return err;
        });
    }

    static deleteTask(taskId) {
        return fetch(`${API_URL}/tasks/${taskId}`, {
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
        }).catch(err => {
            return err;
        });
    }
}