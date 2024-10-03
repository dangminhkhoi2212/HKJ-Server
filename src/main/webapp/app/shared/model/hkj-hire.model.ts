import dayjs from 'dayjs';
import { IHkjPosition } from 'app/shared/model/hkj-position.model';
import { IUserExtra } from 'app/shared/model/user-extra.model';

export interface IHkjHire {
  id?: number;
  beginDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  beginSalary?: number | null;
  notes?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  position?: IHkjPosition | null;
  employee?: IUserExtra | null;
}

export const defaultValue: Readonly<IHkjHire> = {
  isDeleted: false,
};
