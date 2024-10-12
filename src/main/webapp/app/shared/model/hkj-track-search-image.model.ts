import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';

export interface IHkjTrackSearchImage {
  id?: number;
  searchOrder?: number | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  user?: IUserExtra | null;
  jewelry?: IHkjJewelryModel | null;
}

export const defaultValue: Readonly<IHkjTrackSearchImage> = {};
