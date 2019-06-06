import {Component, OnInit} from '@angular/core';
import {FsdServiceService} from "../fsd-service.service";
import {Project} from "../project";
import {ProjectResponse} from "../project-response";
import {Task} from "../task";
import {Router} from "@angular/router";

@Component({
  selector: 'app-view-tasks',
  templateUrl: './view-tasks.component.html',
  styleUrls: ['./view-tasks.component.css']
})
export class ViewTasksComponent implements OnInit {
  projects: Project[];
  tasks: Task[];
  searchProjectText: string;
  filterProjectText: string;

  constructor(private fsdService: FsdServiceService, private router: Router) {
  }

  ngOnInit() {
    this.getProjects();
  }

  searchProjects(): void {
    this.getProjects();
  }

  getProjects(): void {
    this.fsdService.getProjects()
      .subscribe((projectResponse) => {
        if (projectResponse.result === 'success') {
          this.projects = (projectResponse as ProjectResponse).dataList;
        }
      });
  }

  projectSelected(selectedProject: Project): void {
    this.tasks = selectedProject.tasks;
  }

  sortByStartDate(): void {
    this.tasks.sort((a, b) => a.startDate.localeCompare(b.endDate));
  }

  sortByEndDate(): void {
    this.tasks.sort((a, b) => a.endDate.localeCompare(b.endDate));
  }

  sortByPriority(): void {
    this.tasks.sort((a, b) => a.priority - b.priority);
  }

  sortByCompleted(): void {
    this.tasks.sort((a, b) => a.status.localeCompare(b.status));
  }

  editTask(taskId: number): void {
    this.router.navigate(['/edittask/' + taskId]);
  }

  endTask(task: Task) {
    task.status = 'completed';

    this.fsdService.addTask(task)
      .subscribe(taskResponse => {
        if (taskResponse.result === 'success' && taskResponse.data.taskId > 0) {

        }
      });
  }
}
