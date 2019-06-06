import {User} from "./user";
import {Task} from "./task";

export class Project {
  projectId: number;
  project: string;
  startDate: string;
  endDate: string;
  setStartAndEndDate: boolean;
  priority: number;
  managerId: number;
  tasks: Task[];
  manager: User;
  completed: boolean;
  noOfTasks: number;
  noOfCompletedTasks: number;
}
