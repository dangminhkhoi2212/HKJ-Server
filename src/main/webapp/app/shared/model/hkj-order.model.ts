import dayjs from 'dayjs';
import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';

export interface IHkjOrder {
  id?: number;
  orderDate?: dayjs.Dayjs;
  expectedDeliveryDate?: dayjs.Dayjs | null;
  actualDeliveryDate?: dayjs.Dayjs | null;
  specialRequests?: string | null;
  status?: keyof typeof HkjOrderStatus;
  customerRating?: number | null;
  totalPrice?: number | null;
  depositAmount?: number | null;
  notes?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  project?: IHkjProject | null;
  customer?: IUserExtra | null;
  jewelry?: IHkjJewelryModel | null;
}

export const defaultValue: Readonly<IHkjOrder> = {
  isDeleted: false,
};
