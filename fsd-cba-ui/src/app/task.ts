export class Task {
  taskId: number;
  projectId: number;
  projectName: string;
  task: string;
  thisIsParent: boolean;
  priority: number;
  startDate: string;
  endDate: string;
  userId: number;
  userFirstName: string;
  userLastName: string;
  status: string;
  parentTaskId: number;
  parentTaskName: string;
}
