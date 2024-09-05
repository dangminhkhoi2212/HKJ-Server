import { IUser } from 'app/shared/model/user.model';

export interface IUserExtra {
  id?: number;
  phone?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserExtra> = {};
