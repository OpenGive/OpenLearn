import {ColumnModel} from "./column.model";
export class GridModel {
  constructor(
    public title: string,
    public fields: ColumnModel[],
    public entries: any[]
  ) { }
}
