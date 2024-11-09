import dayjs from 'dayjs';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';
import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { IHkjMaterial } from 'app/shared/model/hkj-material.model';
import { IHkjCart } from 'app/shared/model/hkj-cart.model';

export interface IHkjJewelryModel {
  id?: number;
  sku?: string;
  name?: string;
  description?: string | null;
  coverImage?: string | null;
  price?: number | null;
  isDeleted?: boolean | null;
  isCoverSearch?: boolean | null;
  active?: boolean | null;
  daysCompleted?: number | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  category?: IHkjCategory | null;
  project?: IHkjProject | null;
  material?: IHkjMaterial | null;
  hkjCart?: IHkjCart | null;
}

export const defaultValue: Readonly<IHkjJewelryModel> = {
  isDeleted: false,
  isCoverSearch: false,
  active: false,
};
