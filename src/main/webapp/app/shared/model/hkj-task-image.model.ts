import dayjs from 'dayjs';
import { IHkjTask } from 'app/shared/model/hkj-task.model';

export interface IHkjTaskImage {
  id?: number;
  url?: string;
  description?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  task?: IHkjTask | null;
}

export const defaultValue: Readonly<IHkjTaskImage> = {
  isDeleted: false,
};
