import dayjs from 'dayjs';

export interface IHkjCategory {
  id?: number;
  name?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IHkjCategory> = {};
