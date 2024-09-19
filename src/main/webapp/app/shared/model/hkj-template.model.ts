import dayjs from 'dayjs';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';

export interface IHkjTemplate {
  id?: number;
  name?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  category?: IHkjCategory | null;
  creater?: IHkjEmployee | null;
}

export const defaultValue: Readonly<IHkjTemplate> = {
  isDeleted: false,
};
