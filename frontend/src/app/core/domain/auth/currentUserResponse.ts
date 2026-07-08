export interface CurrentUserResponse{
    id: number,
    firstName: string,
    lastName: string,
    email: string,
    enabled: boolean,
    roles: string[],
}