import dayjs from 'dayjs';
import { IHkjTemplate } from 'app/shared/model/hkj-template.model';

export interface IHkjTemplateStep {
  id?: number;
  name?: string | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  hkjTemplate?: IHkjTemplate | null;
}

export const defaultValue: Readonly<IHkjTemplateStep> = {};
