import dayjs from 'dayjs';
import { IHkjOrderItem } from 'app/shared/model/hkj-order-item.model';

export interface IHkjOrderImage {
  id?: number;
  url?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  orderItem?: IHkjOrderItem | null;
}

export const defaultValue: Readonly<IHkjOrderImage> = {
  isDeleted: false,
};
