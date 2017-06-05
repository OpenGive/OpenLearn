export const AdminModel = {
  Organization: {
    title: 'Organizations',
    route: 'organizations',
    columns: []
  },
  Administrator: {
    title: 'Administrators',
    route: 'administrators',
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
    columns: []
  },
  Student: {
    title: 'Students',
    route: 'students',
    columns: []
  },
  Session: {
    title: 'Sessions',
    route: 'sessions',
    columns: []
  },
  Program: {
    title: 'Programs',
    route: 'programs',
    columns: []
  },
  Course: {
    title: 'Courses',
    route: 'courses',
    columns: []
  },
  Portfolio: {
    title: 'Portfolios',
    route: 'portfolios',
    columns: []
  },
  Achievement: {
    title: 'Achievements',
    route: 'achievements',
    columns: []
  }
};
