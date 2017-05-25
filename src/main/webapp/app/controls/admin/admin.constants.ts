export const AdminModel = {
  User: {
    title: 'Users',
    columns: [
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'login', display: 'Username'},
      {property: 'authorities', display: 'Roles'},
      {property: 'activated', display: 'Active'}
    ],
    details: [
      {property: 'id', display: 'ID'},
      {property: 'imageUrl', display: 'Photo'},
      {property: 'email', display: 'Email'},
      {property: 'phoneNumber', display: 'Phone #'},
      {property: 'address', display: 'Address'},
      {property: 'is14Plus', display: '14+'},
      {property: 'createdBy', display: 'Created By'},
      {property: 'createdDate', display: 'Created Date'},
      {property: 'lastModifiedBy', display: 'Last Modified By'},
      {property: 'lastModifiedDate', display: 'Last Modified Date'}
    ]
  }
};
