export const AdminModel = {
  Organization: {
    title: 'Organizations',
    route: 'organizations',
    defaultSort: 'name',
    columns: [
      {property: 'name', display: 'Name'}
    ]
  },
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
    ]
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
    ]
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
    ]
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
    ]
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
    ]
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
    ]
  },
  School: {
    route: 'schools'
  }
};
