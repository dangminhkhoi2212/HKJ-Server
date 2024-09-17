import dayjs from 'dayjs';

export interface IHkjJewelryModel {
  id?: number;
  name?: string;
  description?: string | null;
  isCustom?: boolean;
  weight?: number | null;
  price?: number | null;
  color?: string | null;
  notes?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IHkjJewelryModel> = {
  isCustom: false,
};
