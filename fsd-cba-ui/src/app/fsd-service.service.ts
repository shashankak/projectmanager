import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {User} from './user';
import {UserResponse} from './user-response';
import {Project} from "./project";
import {ProjectResponse} from "./project-response";
import {TaskResponse} from "./task-response";
import {Task} from "./task";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};
const saveUserUrl = 'http://localhost:8082/projectmanager/saveUser';
const getAllUserUrl = 'http://localhost:8082/projectmanager/getAllUsers';
const deleteUserUrl = 'http://localhost:8082/projectmanager/deleteUserById/';
const saveProjectUrl = 'http://localhost:8082/projectmanager/saveProject';
const getAllProjectUrl = 'http://localhost:8082/projectmanager/getAllProjects';
const getManagersUrl = 'http://localhost:8082/projectmanager/getAvailableManagers';
const getUsersForTaskUrl = 'http://localhost:8082/projectmanager/getAvailableUsersForTask';
const deleteProjectUrl = 'http://localhost:8082/projectmanager/deleteProjectById/';
const getAllTaskUrl = 'http://localhost:8082/projectmanager/getAllTasks';
const saveTaskUrl = 'http://localhost:8082/projectmanager/saveTask';
const getTaskUsersUrl = 'http://localhost:8082/projectmanager/getAvailableUsersForTask';
const getTaskByIdUrl = 'http://localhost:8082/projectmanager/getTaskById/';

@Injectable({
  providedIn: 'root'
})
export class FsdServiceService {
  constructor(private http: HttpClient) {
  }

  addUser(user: User): Observable<UserResponse> {
    return this.http.post<UserResponse>(saveUserUrl, user, httpOptions).pipe(
      catchError(this.handleError<UserResponse>('addUser'))
    );
  }

  addProject(project: Project): Observable<ProjectResponse> {
    return this.http.post<ProjectResponse>(saveProjectUrl, project, httpOptions).pipe(
      catchError(this.handleError<ProjectResponse>('add Project'))
    );
  }

  addTask(task: Task): Observable<TaskResponse> {
    return this.http.post<TaskResponse>(saveTaskUrl, task, httpOptions).pipe(
      catchError(this.handleError<TaskResponse>('add Task'))
    );
  }

  getUsers(): Observable<UserResponse> {
    return this.http.get<UserResponse>(getAllUserUrl)
      .pipe(
        catchError(this.handleError<UserResponse>('getAllUsers'))
      );
  }

  getTaskUsers(): Observable<UserResponse> {
    return this.http.get<UserResponse>(getTaskUsersUrl)
      .pipe(
        catchError(this.handleError<UserResponse>('getAllUsers'))
      );
  }

  getTaskByTaskId(taskId: number): Observable<TaskResponse> {
    return this.http.get<TaskResponse>(getTaskByIdUrl + taskId)
      .pipe(
        catchError(this.handleError<UserResponse>('getTaskByTaskId'))
      );
  }

  getProjects(): Observable<ProjectResponse> {
    return this.http.get<ProjectResponse>(getAllProjectUrl)
      .pipe(
        catchError(this.handleError<ProjectResponse>('getAllProjects'))
      );
  }

  getTasks(): Observable<TaskResponse> {
    return this.http.get<TaskResponse>(getAllTaskUrl)
      .pipe(
        catchError(this.handleError<TaskResponse>('getTasks'))
      );
  }


  getManagers(): Observable<UserResponse> {
    return this.http.get<UserResponse>(getManagersUrl, httpOptions).pipe(
      catchError(this.handleError<UserResponse>('getManagers'))
    );
  }

  getUsersForTask(): Observable<UserResponse> {
    return this.http.get<UserResponse>(getUsersForTaskUrl, httpOptions).pipe(
      catchError(this.handleError<UserResponse>('getUsersForTask'))
    );
  }

  deleteUser(userId: number): Observable<UserResponse> {
    return this.http.delete<UserResponse>(deleteUserUrl + userId, httpOptions)
      .pipe(
        catchError(this.handleError<UserResponse>('delete User'))
      );
  }

  deleteProject(project: number): Observable<ProjectResponse> {
    return this.http.delete<ProjectResponse>(deleteProjectUrl + project, httpOptions)
      .pipe(
        catchError(this.handleError<ProjectResponse>('delete Project'))
      );
  }


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      return of(result as T);
    };
  }
}
