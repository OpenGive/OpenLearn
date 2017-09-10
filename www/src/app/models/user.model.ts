export class User {
  public id?: any;
  public login?: string;
  public firstName?: string;
  public lastName?: string;
  public email?: string;
  public phoneNumber?: string;
  public address?: any;
  public activated?: Boolean;
  public authorities?: any[];
  public organizations?: any[];
  public biography?: string;
  public createdBy?: string;
  public createdDate?: Date;
  public lastModifiedBy?: string;
  public lastModifiedDate?: Date;
  public password?: string;
  public is14Plus: Boolean;

  constructor(id?: any,
              login?: string,
              firstName?: string,
              lastName?: string,
              email?: string,
              phoneNumber?: string,
              address?: any,
              activated?: Boolean,
              authorities?: any[],
              createdBy?: string,
              createdDate?: Date,
              lastModifiedBy?: string,
              lastModifiedDate?: Date,
              password?: string,
              organizations?: any[],
              biography?: string) {
    this.id = id ? id : null;
    this.login = login ? login : null;
    this.firstName = firstName ? firstName : null;
    this.lastName = lastName ? lastName : null;
    this.email = email ? email : null;
    this.phoneNumber = phoneNumber ? phoneNumber : null;
    this.address = address ? address : null;
    this.activated = activated ? activated : false;
    this.authorities = authorities ? authorities : null;
    this.createdBy = createdBy ? createdBy : null;
    this.createdDate = createdDate ? createdDate : null;
    this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
    this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
    this.password = password ? password : null;
    this.organizations = organizations;
    this.biography = biography;
  }
}
