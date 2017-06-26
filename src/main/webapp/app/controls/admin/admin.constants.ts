import {AppConstants} from "../../app.constants";

export const AdminModel = {
  Administrator: {
    title: 'Administrators',
    route: 'administrators',
    defaultSort: 'login',
    columns: [
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'login', display: 'Username'},
      {property: 'authorities', display: 'Roles'},
      {property: 'activated', display: 'Active'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Instructor: {
    title: 'Instructors',
    route: 'instructors',
    defaultSort: 'login',
    columns: [
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'login', display: 'Username'},
      {property: 'authorities', display: 'Roles'},
      {property: 'activated', display: 'Active'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Student: {
    title: 'Students',
    route: 'students',
    defaultSort: 'login',
    columns: [
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'login', display: 'Username'},
      {property: 'authorities', display: 'Roles'},
      {property: 'activated', display: 'Active'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Organization: {
    title: 'Organizations',
    route: 'organizations',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'}
    ],
    authorities: [AppConstants.Role.Admin]
  },
  Session: {
    title: 'Sessions',
    route: 'sessions',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'organization', display: 'Organization'},
      {property: 'startDate', display: 'Start Date'},
      {property: 'endDate', display: 'End Date'},
      {property: 'active', display: 'Active'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Program: {
    title: 'Programs',
    route: 'programs',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'session', display: 'Session'},
      {property: 'school', display: 'School'},
      {property: 'active', display: 'Active'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Course: {
    title: 'Courses',
    route: 'courses',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'organization', display: 'Organization'},
      {property: 'program', display: 'Program'},
      {property: 'instructor', display: 'Instructor'},
      {property: 'startDate', display: 'Start Date'},
      {property: 'endDate', display: 'End Date'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Milestone: {
    title: 'Milestones',
    route: 'milestones',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'course', display: 'Course'},
      {property: 'points', display: 'Points'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Achievement: {
    title: 'Achievements',
    route: 'achievements',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'milestone', display: 'Milestone'},
      {property: 'achievedBy', display: 'Achieved By'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  School: {
    route: 'schools'
  }
};
