import dayjs from 'dayjs';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';

export interface IHkjTemplate {
  id?: number;
  name?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  category?: IHkjCategory | null;
}

export const defaultValue: Readonly<IHkjTemplate> = {};
