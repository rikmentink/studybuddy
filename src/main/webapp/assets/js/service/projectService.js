import { API_URL } from '../config.js';
import Project from '../model/project.js';

export default class ProjectService {

    /**
     * Retrieves a list of projects of the student with a given ID.
     * 
     * @param {int} studentId - The unique identifier of the student.
     * @return {Promise<array<Project>>} - A list of projects owned by the student.
     */
    static getProjects(studentId) {
        return fetch(`${API_URL}/students/${studentId}/projects`, {
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

    static addProject(studentId, project) {
        return fetch(`${API_URL}/students/${studentId}/projects`, {
            method: 'POST',
            body: JSON.stringify(project),
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