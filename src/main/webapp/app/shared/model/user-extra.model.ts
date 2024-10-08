import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IUserExtra {
  id?: number;
  phone?: string;
  address?: string | null;
  isDeleted?: boolean | null;
  active?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserExtra> = {
  isDeleted: false,
  active: false,
};
