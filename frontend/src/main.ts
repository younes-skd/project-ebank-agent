import {Component} from '@angular/core';import {bootstrapApplication} from '@angular/platform-browser';import {provideHttpClient} from '@angular/common/http';import {Project} from './app/project';
@Component({selector:'app-root',standalone:true,imports:[Project],template:`<nav class="navbar mb-4"><div class="container"><span class="navbar-brand text-white fw-bold">EMSI / EBANK</span></div></nav><main class="container pb-5"><ng-container><app-project/></ng-container></main>`})class App{}
bootstrapApplication(App,{providers:[provideHttpClient()]}).catch(console.error);
