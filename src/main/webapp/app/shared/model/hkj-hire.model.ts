import dayjs from 'dayjs';
import { IHkjPosition } from 'app/shared/model/hkj-position.model';

export interface IHkjHire {
  id?: number;
  hireDate?: dayjs.Dayjs;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs;
  position?: IHkjPosition | null;
}

export const defaultValue: Readonly<IHkjHire> = {};
