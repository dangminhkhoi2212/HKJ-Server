import dayjs from 'dayjs';
import { IHkjMaterial } from 'app/shared/model/hkj-material.model';
import { IHkjTask } from 'app/shared/model/hkj-task.model';

export interface IHkjMaterialUsage {
  id?: number;
  quantity?: number;
  lossQuantity?: number | null;
  usageDate?: dayjs.Dayjs;
  notes?: string | null;
  weight?: number | null;
  price?: number | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  material?: IHkjMaterial | null;
  hkjTask?: IHkjTask | null;
}

export const defaultValue: Readonly<IHkjMaterialUsage> = {};
