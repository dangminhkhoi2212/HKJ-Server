import dayjs from 'dayjs';
import { IHkjProject } from 'app/shared/model/hkj-project.model';

export interface IHkjJewelryModel {
  id?: number;
  name?: string;
  description?: string | null;
  coverImage?: string | null;
  isCustom?: boolean | null;
  weight?: number | null;
  price?: number | null;
  color?: string | null;
  notes?: string | null;
  isDeleted?: boolean | null;
  active?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  project?: IHkjProject | null;
}

export const defaultValue: Readonly<IHkjJewelryModel> = {
  isCustom: false,
  isDeleted: false,
  active: false,
};
