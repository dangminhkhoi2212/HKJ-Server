import { IUser } from 'app/shared/model/user.model';

export interface IApplicationUser {
  id?: string;
  phoneNumber?: string | null;
  internalUser?: IUser | null;
}

export const defaultValue: Readonly<IApplicationUser> = {};
