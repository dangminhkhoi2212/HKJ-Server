import dayjs from 'dayjs';
import { IHkjMaterial } from 'app/shared/model/hkj-material.model';
import { IHkjJewelryModel } from 'app/shared/model/hkj-jewelry-model.model';
import { IHkjTask } from 'app/shared/model/hkj-task.model';

export interface IHkjMaterialUsage {
  id?: number;
  usage?: number | null;
  notes?: string | null;
  price?: number | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  material?: IHkjMaterial | null;
  jewelry?: IHkjJewelryModel | null;
  task?: IHkjTask | null;
}

export const defaultValue: Readonly<IHkjMaterialUsage> = {
  isDeleted: false,
};
