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
                throw new Error(res.status);
            }
            return res.json();
        }).then(data => {
            return data.map(project => new Project(
                project.id,
                project.name,
                project.description,
                project.startDate,
                project.endDate
            ))
        });
    }

    static getProject(projectId) {
        return fetch(`${API_URL}/projects/${projectId}`, {
            method: 'GET',
        }).then(res => {
            if (!res.ok) {
                throw new Error(res.status);
            }
            return res.json();
        }).then(data => new Project(
            data.id,
            data.name,
            data.description,
            data.startDate,
            data.endDate
        ));
    }

    static addProject(studentId, project) {
        return fetch(`${API_URL}/students/${studentId}/projects`, {
            method: 'POST',
            body: JSON.stringify(project),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status !== 201) {
                throw new Error(res.status);
            }
            return res.json();
        }).then(data => new Project(
            data.id,
            data.name,
            data.description,
            data.startDate,
            data.endDate
        ));
    }

    static updateProject(projectId, project) {
        return fetch(`${API_URL}/projects/${projectId}`, {
            method: 'PUT',
            body: JSON.stringify(project),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status !== 200) {
                throw new Error(res.status);
            }
            return res.json();
        }).then(data => new Project(
            data.id,
            data.name,
            data.description,
            data.startDate,
            data.endDate
        ));
    }

    static deleteProject(projectId) {
        return fetch(`${API_URL}/projects/${projectId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            if (res.status !== 200) {
                throw new Error(res.status);
            }
            return res;
        });
    }
}