export class AccountModel {
  constructor(
    public authority: string,
    public login: string,
    public firstName: string,
    public lastName: string,
    public id: number
  ) {}
}
