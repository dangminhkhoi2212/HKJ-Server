import dayjs from 'dayjs';

export interface IHkjPosition {
  id?: number;
  name?: string;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IHkjPosition> = {
  isDeleted: false,
};
