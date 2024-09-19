import dayjs from 'dayjs';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';

export interface IHkjJewelryImage {
  id?: number;
  url?: string;
  isSearchImage?: boolean;
  description?: string | null;
  tags?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  jewelryModel?: IHkjJewelryModel | null;
}

export const defaultValue: Readonly<IHkjJewelryImage> = {
  isSearchImage: false,
  isDeleted: false,
};
