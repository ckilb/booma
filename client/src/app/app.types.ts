export interface Entry {
  id: number,
  bookmark: Bookmark | null;
  folder: Folder | null;
}
export interface Bookmark {
  id?: number,
  title: string,
  url: string,
  folder?: string,
}

export interface Folder {
  id?: number,
  title: string,
  path?: string
}
