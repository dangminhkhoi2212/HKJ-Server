import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';

export interface IHkjEmployee {
  id?: number;
  notes?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  userExtra?: IUserExtra | null;
}

export const defaultValue: Readonly<IHkjEmployee> = {
  isDeleted: false,
};
