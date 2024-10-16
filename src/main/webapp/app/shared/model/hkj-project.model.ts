import dayjs from 'dayjs';
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { HkjPriority } from 'app/shared/model/enumerations/hkj-priority.model';

export interface IHkjProject {
  id?: number;
  name?: string;
  coverImage?: string | null;
  description?: string | null;
  startDate?: dayjs.Dayjs;
  expectDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof HkjOrderStatus;
  priority?: keyof typeof HkjPriority;
  budget?: number | null;
  actualCost?: number | null;
  qualityCheck?: boolean | null;
  notes?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  manager?: IUserExtra | null;
  category?: IHkjCategory | null;
}

export const defaultValue: Readonly<IHkjProject> = {
  qualityCheck: false,
  isDeleted: false,
};
