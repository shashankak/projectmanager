import {Component, OnInit} from '@angular/core';
import {Task} from "../task";
import {FsdServiceService} from "../fsd-service.service";
import {Project} from "../project";
import {ProjectResponse} from "../project-response";
import {TaskResponse} from "../task-response";
import {DatePipe} from "@angular/common";
import {User} from "../user";
import {UserResponse} from "../user-response";
import {isNullOrUndefined} from "util";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  taskInput: Task = <Task>{};
  task: Task = <Task>{};
  tasks: Task[];
  taskIdToEdit: number;
  infoMessage: string;
  sliderMin: number;
  sliderMax: number;
  editing: boolean;
  errorMessages: string[];
  searchProjectText: string;
  filterProjectText: string;
  projects: Project[];
  searchParentTaskText: string;
  filterParentTaskText: string;
  searchUserText: string;
  filterUserText: string;
  users: User[];

  constructor(private fsdService: FsdServiceService, private datePipe: DatePipe, private route: ActivatedRoute) {
    this.route.params.subscribe(params => this.taskIdToEdit = params['id']);
  }

  ngOnInit() {
    this.resetPage();
    this.getTasks();
    if (isNotNullOrUndefined(this.taskIdToEdit)) {
      this.getTaskByTaskId(this.taskIdToEdit);
      this.editing = true;
    }
  }

  resetPage(): void {
    this.task = <Task>{};
    this.taskInput = <Task>{};
    this.sliderMin = 0;
    this.sliderMax = 30;
    this.dateCheckChange();
    this.taskInput.priority = 0;
    this.taskInput.thisIsParent = true;
    this.errorMessages = [];
    this.infoMessage = '';
  }

  dateCheckChange(): void {
    var date = new Date();
    this.taskInput.startDate = this.transformDate(date);
    date.setDate(date.getDate() + 1);
    this.taskInput.endDate = this.transformDate(date);
  }

  transformDate(date) {
    return this.datePipe.transform(date, 'yyyy-MM-dd'); //whatever format you need.
  }

  searchProjects(): void {
    this.getProjects();
  }

  searchParentTasks(): void {
    this.getTasks();
  }

  searchUsers(): void {
    this.getUsers();
  }

  onReset(): void {
    this.resetPage();
  }

  resetErrorMessage(): void {
    this.errorMessages = [];
  }

  onClickSubmit(dataToSave) {
    this.saveTask(dataToSave);
  }

  onSliderChange(sliderValue): void {
    this.taskInput.priority = sliderValue;
  }

  projectSelected(selectedProject: Project): void {
    this.taskInput.projectId = selectedProject.projectId;
    this.taskInput.projectName = selectedProject.project;
    this.searchProjectText = selectedProject.project;
    this.filterProjectText = '';
  }

  parentTaskSelected(selectedParentTask: Task): void {
    this.taskInput.parentTaskId = selectedParentTask.taskId;
    this.taskInput.parentTaskName = selectedParentTask.task;
    this.searchParentTaskText = selectedParentTask.task;
    this.filterParentTaskText = '';
  }

  userSelected(selectedUser: User): void {
    this.taskInput.userId = selectedUser.userId;
    this.taskInput.userFirstName = selectedUser.firstName;
    this.taskInput.userLastName = selectedUser.lastName;
    this.searchUserText = selectedUser.firstName + ' ' + selectedUser.lastName;
    this.filterUserText = '';
  }

  getProjects(): void {
    this.fsdService.getProjects()
      .subscribe((projectResponse) => {
        if (projectResponse.result === 'success') {
          this.projects = (projectResponse as ProjectResponse).dataList;
        } else {
          // this.errorMessage = 'There was error while loading projects';
          this.errorMessages.push('There was error while loading projects');
        }
      });
  }

  getTasks(): void {
    this.fsdService.getTasks()
      .subscribe((taskResponse) => {
        if (taskResponse.result === 'success') {
          this.tasks = (taskResponse as TaskResponse).dataList;
        } else {
          // this.errorMessage = 'There was error while loading projects';
          this.errorMessages.push('There was error while loading tasks');
        }
      });
  }

  getUsers(): void {
    this.fsdService.getTaskUsers()
      .subscribe((userResponse) => {
        if (userResponse.result === 'success') {
          this.users = (userResponse as UserResponse).dataList;
        } else {
          // this.errorMessage = 'There was error while deleting user';
          this.errorMessages.push('There was error while loading Users');
        }
      });
  }

  saveTask(task: Task) {
    if (this.editing) {
      task.taskId = this.taskInput.taskId;
    }
    task.priority = this.taskInput.priority;
    task.projectId = this.taskInput.projectId;
    task.parentTaskId = this.taskInput.parentTaskId;
    task.userId = this.taskInput.userId;
    task.startDate = this.taskInput.startDate;
    task.endDate = this.taskInput.endDate;

    if (!this.validate(task)) {
      return;
    }

    var saveTaskObj: Task = <Task>{};
    saveTaskObj.taskId = task.taskId;
    saveTaskObj.task = task.task;
    saveTaskObj.projectId = task.projectId;
    saveTaskObj.thisIsParent = task.thisIsParent;
    saveTaskObj.priority = task.priority;
    saveTaskObj.parentTaskId = task.parentTaskId;
    saveTaskObj.startDate = task.startDate;
    saveTaskObj.endDate = task.endDate;
    saveTaskObj.userId = task.userId;


    this.fsdService.addTask(saveTaskObj)
      .subscribe(taskResponse => {
        if (taskResponse.result === 'success' && taskResponse.data.taskId > 0) {
          this.task = taskResponse.data;
          if (!this.editing) {
            this.infoMessage = 'Task saved successfully'
          } else if (this.editing) {
            this.infoMessage = 'Task updated successfully'
          }
          this.getProjects();
        } else {
          // this.errorMessage = 'There was error while saving Project ' + project.project;
          this.errorMessages.push('There was error while saving Task ' + saveTaskObj.task);
        }
      });
  }

  getTaskByTaskId(taskId: number): void {
    this.fsdService.getTaskByTaskId(taskId)
      .subscribe((taskResponse) => {
        if (taskResponse.result === 'success') {
          this.taskInput = (taskResponse as TaskResponse).data;
          this.searchProjectText = this.taskInput.projectName;
          if (isNotNullOrUndefined(this.taskInput.parentTaskName)) {
            this.searchParentTaskText = this.taskInput.parentTaskName;
          }
          if (isNotNullOrUndefined(this.taskInput.userFirstName)) {
            this.searchUserText = this.taskInput.userFirstName + ' ' + this.taskInput.userLastName;
          }
          console.log(this.taskInput);
        } else {
          this.errorMessages.push('There was error while loading task');
        }
      });
  }

  removeCurrentParentTask(): void {
    this.taskInput.parentTaskName = null;
    this.taskInput.parentTaskId = null;
    this.searchParentTaskText = '';
    this.filterParentTaskText = '';
  }

  validate(task: Task): boolean {
    this.resetErrorMessage();
    if (this.editing) {
      if (task.taskId === undefined || task.taskId === null || task.taskId === 0) {
        this.errorMessages.push('There was error. Please refresh the page.');
      }
    }
    if (isNullOrUndefined(task.task) || task.task.trim() === '') {
      this.errorMessages.push('Please enter Task name.');
    } else {
      let taskEntry = this.tasks.find(ob => (ob['task'].trim() === task.task.trim()));
      if (isNotNullOrUndefined(taskEntry)) {
        this.errorMessages.push('Task already exists.');
      }
    }
    if (isNullOrUndefined(task.projectId)) {
      this.errorMessages.push('Please select a Project.');
    }
    if (!task.thisIsParent) {
      if (isNullOrUndefined(task.priority)) {
        this.errorMessages.push('Please select priority.');
      }
      if (isNullOrUndefined(task.startDate) || task.startDate.trim() === '') {
        this.errorMessages.push('Please enter Start date.');
      }
      if (isNullOrUndefined(task.endDate) || task.endDate.trim() === '') {
        this.errorMessages.push('Please enter End date.');
      }
      if (isNullOrUndefined(task.userId)) {
        this.errorMessages.push('Please select an User.');
      }

      if (!(isNullOrUndefined(task.startDate) || task.startDate.trim() === ''
          || isNullOrUndefined(task.endDate) || task.endDate.trim() === '')
        && (task.startDate.localeCompare(task.endDate)) !== -1) {
        this.errorMessages.push('Please select end date greater than start date.');
      }
    }
    if (this.errorMessages.length > 0) {
      return false;
    }
    return true;
  }

}
