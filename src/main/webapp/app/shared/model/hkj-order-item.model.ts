import dayjs from 'dayjs';
import { IHkjMaterial } from 'app/shared/model/hkj-material.model';
import { IHkjOrder } from 'app/shared/model/hkj-order.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';

export interface IHkjOrderItem {
  id?: number;
  quantity?: number | null;
  specialRequests?: string | null;
  price?: number | null;
  isDeleted?: boolean | null;
  notes?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  material?: IHkjMaterial | null;
  order?: IHkjOrder | null;
  product?: IHkjJewelryModel | null;
  category?: IHkjCategory | null;
}

export const defaultValue: Readonly<IHkjOrderItem> = {
  isDeleted: false,
};
