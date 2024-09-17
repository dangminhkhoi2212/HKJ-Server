import dayjs from 'dayjs';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';

export interface IHkjSalary {
  id?: number;
  salary?: number | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  hkjEmployee?: IHkjEmployee | null;
}

export const defaultValue: Readonly<IHkjSalary> = {};
