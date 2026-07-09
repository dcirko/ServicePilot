import { HttpContextToken } from '@angular/common/http';

export const SKIP_SESSION_REFRESH = new HttpContextToken<boolean>(() => false);
export const SESSION_RETRY_ATTEMPT = new HttpContextToken<boolean>(() => false);
