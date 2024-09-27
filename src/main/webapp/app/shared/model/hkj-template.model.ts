import dayjs from 'dayjs';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';
import { IUserExtra } from 'app/shared/model/user-extra.model';

export interface IHkjTemplate {
  id?: number;
  name?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  category?: IHkjCategory | null;
  creater?: IUserExtra | null;
}

export const defaultValue: Readonly<IHkjTemplate> = {
  isDeleted: false,
};
