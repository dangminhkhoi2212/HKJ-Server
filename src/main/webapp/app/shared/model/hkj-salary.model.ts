import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';

export interface IHkjSalary {
  id?: number;
  salary?: number | null;
  notes?: string | null;
  payDate?: dayjs.Dayjs | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  employee?: IUserExtra | null;
}

export const defaultValue: Readonly<IHkjSalary> = {
  isDeleted: false,
};
