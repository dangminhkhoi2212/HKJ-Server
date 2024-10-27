import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';
import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';
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
  budget?: number | null;
  depositAmount?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  customer?: IUserExtra | null;
  jewelry?: IHkjJewelryModel | null;
  project?: IHkjProject | null;
  category?: IHkjCategory | null;
}

export const defaultValue: Readonly<IHkjOrder> = {
  isDeleted: false,
};
