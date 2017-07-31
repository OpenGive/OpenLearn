export class ItemLinkModel {
  public id: Number
  public name: string
  public description: string
  public thumbnailImageUrl: string;
  public itemUrl: string;

  constructor(
    id: Number,
    name: string,
    description: string,
    thumbnailImageUrl: string,
    itemUrl: string
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.thumbnailImageUrl = thumbnailImageUrl;
    this.itemUrl = itemUrl;
  }
}
