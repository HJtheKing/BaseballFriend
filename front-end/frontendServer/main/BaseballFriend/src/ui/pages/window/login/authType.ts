export interface AuthMessage {
  isSuccess: boolean;
  message: string;
}

export interface UserRegistration {
  email: string;
  pw: string;
  checkPw: string;
  name: string;
  favoriteTeamName: string;
  isValidEmail: boolean;
}

export interface UserPreferences {
  name: string;
  isBriefingAllowed: boolean;
  isBroadcastAllowed: boolean;
  favoriteTeam: string;
}

export interface CodeCheck {
  email: string;
  code: string;
}
