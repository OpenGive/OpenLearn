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
    authorities: [AppConstants.Role.Admin.name]
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
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name]
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
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name, AppConstants.Role.Instructor.name]
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
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name, AppConstants.Role.Instructor.name]
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
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name]
  },
  Program: {
    title: 'Programs',
    route: 'programs',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'organizationId', display: 'Organization'},
    ],
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name, AppConstants.Role.Instructor.name]
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
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name, AppConstants.Role.Instructor.name]
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
    authorities: [AppConstants.Role.Admin.name, AppConstants.Role.OrgAdmin.name, AppConstants.Role.Instructor.name]
  },
  Assignment: {
    title: 'Assignment',
    route: 'assignments',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'},
      {property: 'description', display: 'Description'}
    ]
  },
  StudentAssignment: {
    title: 'StudentAssignment',
    route: 'student-assignments'
  },
  Portfolio: {
    route: 'portfolio-items'
  }
};
