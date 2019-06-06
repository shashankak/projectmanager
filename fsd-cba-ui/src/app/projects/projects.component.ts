import {Component, OnInit} from '@angular/core';
import {Project} from "../project";
import {FsdServiceService} from "../fsd-service.service";
import {User} from "../user";
import {UserResponse} from "../user-response";
import {ProjectResponse} from "../project-response";
import {DatePipe} from "@angular/common";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projectInput: Project = <Project>{};
  project: Project = <Project>{};
  projects: Project[];
  infoMessage: string;
  sliderMin: number;
  sliderMax: number;
  searchManager: string;
  searchManagerText: string;
  managers: User[];
  editing: boolean;
  filterProjectText: string;
  errorMessages: string[];

  constructor(private route: ActivatedRoute, private router: Router, private fsdService: FsdServiceService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.resetPage();
    this.getProjects();
  }

  resetPage(): void {
    this.sliderMin = 0;
    this.sliderMax = 30;
    this.projectInput.setStartAndEndDate = true;
    this.projectInput.priority = 0;
    this.infoMessage = '';
    this.resetErrorMessage();
    this.dateCheckChange();
  }

  resetErrorMessage(): void {
    this.errorMessages = [];
  }

  dateCheckChange(): void {
    var date = new Date();
    this.projectInput.startDate = this.transformDate(date);
    date.setDate(date.getDate() + 1);
    this.projectInput.endDate = this.transformDate(date);
  }

  transformDate(date) {
    return this.datePipe.transform(date, 'yyyy-MM-dd');
  }

  getManagers(): void {
    this.fsdService.getManagers()
      .subscribe((userResponse) => {
        if (userResponse.result === 'success') {
          this.managers = (userResponse as UserResponse).dataList;
        } else {
          // this.errorMessage = 'There was error while loading managers';
          this.errorMessages.push('There was error while loading managers');
        }
      });
  }

  onSliderChange(sliderValue): void {
    this.projectInput.priority = sliderValue;
  }

  managerSelected(selectedUser: User): void {
    this.projectInput.managerId = selectedUser.userId;
    this.searchManagerText = selectedUser.firstName + ' ' + selectedUser.lastName;
    this.searchManager = '';
  }

  searchManagers(): void {
    this.getManagers();
  }

  onClickSubmit(dataToSave) {
    this.saveProject(dataToSave);
  }

  saveProject(project: Project) {
    if (this.editing) {
      project.projectId = this.projectInput.projectId;
    }
    project.priority = this.projectInput.priority;
    project.managerId = this.projectInput.managerId;
    project.startDate = this.projectInput.startDate;
    project.endDate = this.projectInput.endDate;

    if (!this.validate(project)) {
      return;
    }

    var saveProjectObj: Project = <Project>{};
    saveProjectObj.projectId = project.projectId;
    saveProjectObj.project = project.project;
    saveProjectObj.startDate = project.startDate;
    saveProjectObj.endDate = project.endDate;
    saveProjectObj.setStartAndEndDate = project.setStartAndEndDate;
    saveProjectObj.priority = project.priority;
    saveProjectObj.managerId = project.managerId;
    saveProjectObj.completed = project.completed;
    saveProjectObj.noOfTasks = project.noOfTasks;
    saveProjectObj.noOfCompletedTasks = project.noOfCompletedTasks;

    this.fsdService.addProject(saveProjectObj)
      .subscribe(projectResponse => {
        if (projectResponse.result === 'success' && projectResponse.data.projectId > 0) {
          this.project = projectResponse.data;
          if (!this.editing) {
            this.infoMessage = 'Project saved successfully'
          } else if (this.editing) {
            this.infoMessage = 'Project updated successfully'
          }
          this.getProjects();
        } else {
          this.errorMessages.push('There was error while saving Project ' + saveProjectObj.project);
        }
      });
  }

  getProjects(): void {
    this.fsdService.getProjects()
      .subscribe((projectResponse) => {
        if (projectResponse.result === 'success') {
          this.projects = (projectResponse as ProjectResponse).dataList;
        } else {
          this.errorMessages.push('There was error while loading projects');
        }
      });
  }

  onReset(): void {
    this.resetPage();
  }

  sortByStartDate(): void {
    this.projects.sort((a, b) => a.startDate.localeCompare(b.endDate));
  }

  sortByEndDate(): void {
    this.projects.sort((a, b) => a.endDate.localeCompare(b.endDate));
  }

  sortByPriority(): void {
    this.projects.sort((a, b) => a.priority - b.priority);
  }

  sortByCompleted(): void {
    this.projects.sort((a, b) => a.noOfCompletedTasks - b.noOfCompletedTasks);
  }

  editProject(project: Project): void {
    let copy = Object.assign(this.projectInput, project);
    this.projectInput.setStartAndEndDate = true;
    this.searchManagerText = this.projectInput.manager.firstName;
    this.editing = true;
  }

  deleteUser(projectId: number): void {
    this.fsdService.deleteProject(projectId)
      .subscribe((projectResponse) => {
        if (projectResponse.result === 'success') {
          this.infoMessage = 'Project deleted successfully';
          this.getProjects();
        } else {
          this.errorMessages.push('There was error while deleting project');
        }
      })
  }

  validate(project: Project): boolean {
    this.resetErrorMessage();
    if (this.editing) {
      if (project.projectId === undefined || project.projectId === null || project.projectId === 0) {
        this.errorMessages.push('There was error. Please refresh the page.');
      }
    }
    if (project.project === undefined || project.project === null || project.project.trim() === '') {
      this.errorMessages.push('Please enter Project name.');
    } else {
      let projectEntry = this.projects.find(ob => (ob['project'].trim() === project.project.trim()));
      if (isNotNullOrUndefined(projectEntry)) {
        this.errorMessages.push('Project already exists.');
      }
    }
    if (project.startDate === undefined || project.startDate === null || project.startDate.trim() === '') {
      this.errorMessages.push('Please enter Start date.');
    }
    if (project.endDate === undefined || project.endDate === null || project.endDate.trim() === '') {
      this.errorMessages.push('Please enter End date.');
    }
    if (project.priority === undefined || project.priority === null) {
      this.errorMessages.push('Please select priority.');
    }
    if (project.managerId === undefined || project.managerId === null) {
      this.errorMessages.push('Please select a manager.');
    }
    if (!(project.startDate === undefined || project.startDate === null || project.startDate.trim() === ''
        || project.endDate === undefined || project.endDate === null || project.endDate.trim() === '')
      && (project.startDate.localeCompare(project.endDate)) !== -1) {
      this.errorMessages.push('Please select end date greater than start date.');
    }
    if (this.errorMessages.length > 0) {
      return false;
    }
    return true;
  }
}
