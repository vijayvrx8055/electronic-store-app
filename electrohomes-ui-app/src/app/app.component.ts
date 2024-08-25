import { SocialAuthService } from '@abacritt/angularx-social-login';
import { Component, OnInit } from '@angular/core';
import { ApiCallService } from './api-call.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'electrohomes-ui-app';
  jwtToken: any;

  constructor(private authService: SocialAuthService, private apiCall: ApiCallService) { }

  ngOnInit(): void {
    this.authService.authState.subscribe({
      next: (user) => {
        alert("Login Success");
        console.log(user);
        this.apiCall.loginWithGoogle(user).subscribe({
          next: (data: any) => {
            console.log("Data from backend:", data);
            this.jwtToken = data['jwtToken'];
          },
          error: (error) => {
            console.log("Error:", error);
          }
        })
      },
      error: (error) => {
        alert("Login Error");
      },
      complete: () => {
        console.log("request completed")
      }
    })
    this.getUsers();
  }

  getUsers() {
    console.log('JWTToken:', this.jwtToken);
    this.apiCall.getUsers(this.jwtToken).subscribe({
      next: (data) => {
        console.log('User Data:', data);
      },
      error: error => {
        console.log('Error:', error);
      }
    })
  }

}
