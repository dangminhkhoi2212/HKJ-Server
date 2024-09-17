import dayjs from 'dayjs';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';

export interface IHkjJewelryImage {
  id?: number;
  url?: string;
  isSearchImage?: boolean;
  description?: string | null;
  tags?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  uploadedBy?: IHkjEmployee | null;
  jewelryModel?: IHkjJewelryModel | null;
}

export const defaultValue: Readonly<IHkjJewelryImage> = {
  isSearchImage: false,
};
