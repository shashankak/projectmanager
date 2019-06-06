import {Component, OnInit} from '@angular/core';
import {User} from '../user';
import {FsdServiceService} from '../fsd-service.service';
import {UserResponse} from "../user-response";
import {isNull, isUndefined} from "util";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  user: User
  userInput: User
  users: User[];
  infoMessage: string;
  errorMessages: string[];
  searchText: string;
  editing: boolean;

  constructor(private userService: FsdServiceService) {
  }

  ngOnInit() {
    this.resetUser();
    this.getUsers();
  }

  onClickSubmit(dataToSave) {
    this.saveUser(dataToSave);
  }

  resetErrorMessage(): void {
    this.errorMessages = [];
  }

  resetUser(): void {
    this.userInput = <User>{};
    this.user = <User>{};
    this.editing = false;
  }

  saveUser(user: User) {
    if (this.editing) {
      user.userId = this.userInput.userId;
    }
    if (!this.validate(user)) {
      return;
    }
    this.userService.addUser(user)
      .subscribe(userResponse => {
        if (userResponse.result === 'success' && userResponse.data.userId > 0) {
          this.user = userResponse.data;
          if (!this.editing) {
            this.infoMessage = 'User saved successfully'
          } else if (this.editing) {
            this.infoMessage = 'User updated successfully'
          }
          this.getUsers();
        } else {
          this.errorMessages.push('There was error while saving User ' + user.firstName);
        }
      });
  }

  getUsers(): void {
    this.userService.getUsers()
      .subscribe((userResponse) => {
        if (userResponse.result === 'success') {
          this.users = (userResponse as UserResponse).dataList;
        } else {
          this.errorMessages.push('There was error while loading Users');
        }
      });
  }

  deleteUser(userId: number): void {
    this.userService.deleteUser(userId)
      .subscribe((userResponse) => {
        if (userResponse.result === 'success') {
          this.infoMessage = 'User deleted successfully';
          this.getUsers();
        } else {
          this.errorMessages.push('There was error while deleting user');
        }
      })
  }

  sortByFirstName(): void {
    this.users.sort((a, b) => a.firstName.localeCompare(b.firstName));
  }

  sortByLastName(): void {
    this.users.sort((a, b) => a.lastName.localeCompare(b.lastName));
  }

  sortByEmployeeID(): void {
    this.users.sort((a, b) => a.employeeId.localeCompare(b.employeeId));
  }

  editUser(user: User): void {
    let copy = Object.assign(this.userInput, user);
    this.editing = true;
  }

  onReset(): void {
    this.infoMessage = '';
    this.resetErrorMessage();
    this.resetUser();
  }

  validate(user: User): boolean {
    this.resetErrorMessage();

    if (isUndefined(user.firstName) || isNull(user.firstName) || user.firstName.trim() === '') {
      this.errorMessages.push('Please enter First name');
    }
    if (isUndefined(user.lastName) || isNull(user.lastName) || user.lastName.trim() === '') {
      this.errorMessages.push('Please enter Last name');
    }
    if (isUndefined(user.employeeId) || isNull(user.employeeId) || user.employeeId.trim() === '') {
      this.errorMessages.push('Please enter Employee ID');
    }
    if (this.errorMessages.length === 0) {
      let dataFirstName = this.users.find(ob => (ob['firstName'].trim() === user.firstName.trim()) && (ob['lastName'].trim() === user.lastName.trim()) && (ob['employeeId'].trim() === user.employeeId.trim()));
      if (isNotNullOrUndefined(dataFirstName)) {
        this.errorMessages.push('User already exists.');
      }
    }
    if (this.errorMessages.length > 0) {
      return false;
    }
    return true;
  }

}
