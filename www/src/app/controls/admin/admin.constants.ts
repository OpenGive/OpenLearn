import {AppConstants} from "../../app.constants";

export const AdminTabs = {
  Administrator: {
    title: 'Administrators',
    route: 'administrators',
    defaultSort: 'login',
    columns: [
      {property: 'login', display: 'Username'},
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'email', display: 'Email'}
    ],
    authorities: [AppConstants.Role.Admin]
  },
  OrgAdministrator: {
    title: 'Org Administrators',
    route: 'org-administrators',
    defaultSort: 'login',
    columns: [
      {property: 'login', display: 'Username'},
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'organizationId', display: 'Organization'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Instructor: {
    title: 'Instructors',
    route: 'instructors',
    defaultSort: 'login',
    columns: [
      {property: 'login', display: 'Username'},
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'organizationId', display: 'Organization'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor]
  },
  Student: {
    title: 'Students',
    route: 'students',
    defaultSort: 'login',
    columns: [
      {property: 'login', display: 'Username'},
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'organizationId', display: 'Organization'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor]
  },
  Organization: {
    title: 'Organizations',
    route: 'organizations',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'primaryContactName', display: 'Primary Contact Name'},
      {property: 'primaryContactInfo', display: 'Primary Contact Info'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin]
  },
  Program: {
    title: 'Programs',
    route: 'programs',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'organizationId', display: 'Organization'},
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor]
  },
  Session: {
    title: 'Sessions',
    route: 'sessions',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'startDate', display: 'Start Date'},
      {property: 'endDate', display: 'End Date'},
      {property: 'programId', display: 'Program'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor]
  },
  Course: {
    title: 'Courses',
    route: 'courses',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'startDate', display: 'Start Date'},
      {property: 'endDate', display: 'End Date'},
      {property: 'sessionId', display: 'Session'},
      {property: 'instructorId', display: 'Instructor'}
    ],
    authorities: [AppConstants.Role.Admin, AppConstants.Role.OrgAdmin, AppConstants.Role.Instructor]
  }
};
