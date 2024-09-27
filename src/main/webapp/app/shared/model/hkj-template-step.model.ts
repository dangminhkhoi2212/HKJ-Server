import dayjs from 'dayjs';
import { IHkjTemplate } from 'app/shared/model/hkj-template.model';

export interface IHkjTemplateStep {
  id?: number;
  name?: string | null;
  isDeleted?: boolean | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  template?: IHkjTemplate | null;
}

export const defaultValue: Readonly<IHkjTemplateStep> = {
  isDeleted: false,
};
