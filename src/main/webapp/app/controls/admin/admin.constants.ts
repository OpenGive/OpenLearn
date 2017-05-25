export const AdminModel = {
  User: {
    title: 'Users',
    columns: [
      {property: 'imageUrl', display: 'Photo'},
      {property: 'login', display: 'Username'},
      {property: 'firstName', display: 'First Name'},
      {property: 'lastName', display: 'Last Name'},
      {property: 'activated', display: 'Active'},
      {property: 'authorities', display: 'Roles'},
      {property: 'is14Plus', display: '14+'}
    ],
    details: [
      {property: 'id', display: 'ID'},
      {property: 'email', display: 'Email'},
      {property: 'phoneNumber', display: 'Phone #'},
      {property: 'address', display: 'Address'},
      {property: 'createdBy', display: 'Created By'},
      {property: 'createdDate', display: 'Created Date'},
      {property: 'lastModifiedBy', display: 'Last Modified By'},
      {property: 'lastModifiedDate', display: 'Last Modified Date'}
    ]
  }
};
