export class AdminGridModel {
  constructor(
    public title: string,
    public route: string,
    public defaultSort: string,
    public columns: any[],
    public rows: any[]
  ) { }
}
