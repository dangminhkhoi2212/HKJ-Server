import dayjs from 'dayjs';

export interface IHkjMaterial {
  id?: number;
  name?: string;
  quantity?: number;
  unit?: string;
  unitPrice?: number | null;
  supplier?: string | null;
  coverImage?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IHkjMaterial> = {
  isDeleted: false,
};
