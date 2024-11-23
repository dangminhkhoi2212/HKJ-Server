import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';

export interface IHkjOrder {
  id?: number;
  orderDate?: dayjs.Dayjs;
  expectedDeliveryDate?: dayjs.Dayjs | null;
  actualDeliveryDate?: dayjs.Dayjs | null;
  status?: keyof typeof HkjOrderStatus;
  totalPrice?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  customer?: IUserExtra | null;
  project?: IHkjProject | null;
}

export const defaultValue: Readonly<IHkjOrder> = {
  isDeleted: false,
};
