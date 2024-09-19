import dayjs from 'dayjs';
import { IHkjTemplateStep } from 'app/shared/model/hkj-template-step.model';
import { IHkjEmployee } from 'app/shared/model/hkj-employee.model';
import { IHkjProject } from 'app/shared/model/hkj-project.model';
import { HkjOrderStatus } from 'app/shared/model/enumerations/hkj-order-status.model';
import { HkjPriority } from 'app/shared/model/enumerations/hkj-priority.model';

export interface IHkjTask {
  id?: number;
  name?: string;
  description?: string | null;
  assignedDate?: dayjs.Dayjs;
  expectDate?: dayjs.Dayjs;
  completedDate?: dayjs.Dayjs | null;
  status?: keyof typeof HkjOrderStatus;
  priority?: keyof typeof HkjPriority;
  point?: number | null;
  notes?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  templateStep?: IHkjTemplateStep | null;
  employee?: IHkjEmployee | null;
  hkjProject?: IHkjProject | null;
}

export const defaultValue: Readonly<IHkjTask> = {
  isDeleted: false,
};
