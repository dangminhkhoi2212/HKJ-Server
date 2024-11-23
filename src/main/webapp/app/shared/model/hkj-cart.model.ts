import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';

export interface IHkjCart {
  id?: number;
  quantity?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  customer?: IUserExtra | null;
  product?: IHkjJewelryModel | null;
}

export const defaultValue: Readonly<IHkjCart> = {
  isDeleted: false,
};
