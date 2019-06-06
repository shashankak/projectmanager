import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;
    searchText = searchText.toString().toLowerCase();
    return items.filter(it => {
      return (it.firstName !== undefined && it.firstName.toString().toLowerCase().includes(searchText))
        || (it.lastName !== undefined && it.lastName.toString().toLowerCase().includes(searchText))
        || (it.employeeId !== undefined && it.employeeId.toString().toLowerCase().includes(searchText))
        || (it.project !== undefined && it.project.toString().toLowerCase().includes(searchText))
        || (it.startDate !== undefined && it.startDate.toString().toLowerCase().includes(searchText))
        || (it.endDate !== undefined && it.endDate.toString().toLowerCase().includes(searchText))
        || (it.project !== undefined && it.project.toString().toLowerCase().includes(searchText))
        || (it.task !== undefined && it.task.toString().toLowerCase().includes(searchText));
    });
  }
}
