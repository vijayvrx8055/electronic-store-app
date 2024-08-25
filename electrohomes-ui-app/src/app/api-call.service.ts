import { SocialUser } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiCallService {



  constructor(private http: HttpClient) {
  }

  loginWithGoogle(user: SocialUser) {
    return this.http.post(`http://localhost:9090/auth/google`, user);
  }

  getUsers(jwtToken: any) {
    return this.http.get(`http://localhost:9090/users`, {
      headers: { "Authorization": "Bearer " + jwtToken }
    });
  }

}
