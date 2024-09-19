import dayjs from 'dayjs';
import { IHkjOrder } from 'app/shared/model/hkj-order.model';

export interface IHkjOrderImage {
  id?: number;
  url?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  hkjOrder?: IHkjOrder | null;
}

export const defaultValue: Readonly<IHkjOrderImage> = {
  isDeleted: false,
};
