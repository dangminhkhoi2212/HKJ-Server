import dayjs from 'dayjs';

export interface IHkjPosition {
  id?: number;
  name?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IHkjPosition> = {};
