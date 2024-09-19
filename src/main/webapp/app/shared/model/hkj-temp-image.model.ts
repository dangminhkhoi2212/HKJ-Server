import dayjs from 'dayjs';

export interface IHkjTempImage {
  id?: number;
  url?: string | null;
  isUsed?: boolean;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IHkjTempImage> = {
  isUsed: false,
  isDeleted: false,
};
