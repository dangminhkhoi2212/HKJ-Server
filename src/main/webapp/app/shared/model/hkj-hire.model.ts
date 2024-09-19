import dayjs from 'dayjs';
import { IHkjPosition } from 'app/shared/model/hkj-position.model';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';

export interface IHkjHire {
  id?: number;
  beginDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  beginSalary?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  position?: IHkjPosition | null;
  employee?: IHkjEmployee | null;
}

export const defaultValue: Readonly<IHkjHire> = {
  isDeleted: false,
};
