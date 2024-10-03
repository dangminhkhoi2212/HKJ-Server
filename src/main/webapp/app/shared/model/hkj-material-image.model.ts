import dayjs from 'dayjs';
import { IHkjMaterial } from 'app/shared/model/hkj-material.model';

export interface IHkjMaterialImage {
  id?: number;
  url?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  material?: IHkjMaterial | null;
}

export const defaultValue: Readonly<IHkjMaterialImage> = {
  isDeleted: false,
};
