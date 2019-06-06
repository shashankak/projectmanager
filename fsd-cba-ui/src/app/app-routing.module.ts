import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UsersComponent } from './users/users.component';
import {ProjectsComponent} from "./projects/projects.component";
import {TasksComponent} from "./tasks/tasks.component";
import {ViewTasksComponent} from "./view-tasks/view-tasks.component";

const routes: Routes = [
  { path: 'adduser', component: UsersComponent },
  { path: 'addproject', component: ProjectsComponent},
  { path: 'addtask', component: TasksComponent},
  { path: 'edittask/:id', component: TasksComponent},
  { path: 'viewtask', component: ViewTasksComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
