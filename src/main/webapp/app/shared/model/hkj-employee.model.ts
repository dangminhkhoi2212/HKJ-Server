import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjHire } from 'app/shared/model/hkj-hire.model';

export interface IHkjEmployee {
  id?: number;
  notes?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  userExtra?: IUserExtra | null;
  hkjHire?: IHkjHire | null;
}

export const defaultValue: Readonly<IHkjEmployee> = {};
