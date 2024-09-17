import dayjs from 'dayjs';
import { IHkjCategory } from 'app/shared/model/hkj-category.model';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { HkjPriority } from 'app/shared/model/enumerations/hkj-priority.model';

export interface IHkjProject {
  id?: number;
  name?: string;
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
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  category?: IHkjCategory | null;
  manager?: IHkjEmployee | null;
}

export const defaultValue: Readonly<IHkjProject> = {
  qualityCheck: false,
};
